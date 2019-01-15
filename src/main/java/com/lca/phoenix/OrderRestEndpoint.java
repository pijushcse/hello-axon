package com.lca.phoenix;

import com.lca.phoenix.command.ConfirmOrderCommand;
import com.lca.phoenix.command.PlaceOrderCommand;
import com.lca.phoenix.command.ShipOrderCommand;
import com.lca.phoenix.queries.FindAllOrderedProductsQuery;
import com.lca.phoenix.queries.OrderedProduct;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderRestEndpoint {

	Logger LOGGER = LoggerFactory.getLogger(OrderRestEndpoint.class);

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	private EventHandlingConfiguration configuration;

	@Autowired
	TrackingProcessorService service;

	@Autowired
	private EventHandlingConfiguration eventHandlingConfiguration;



	public OrderRestEndpoint(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping("/ship-order")
	public void shipOrder() {
		String orderId = UUID.randomUUID().toString();
		LOGGER.info("Generated orderID- {} ", orderId);
		commandGateway.send(new PlaceOrderCommand(orderId, "Deluxe Chair"));
		commandGateway.send(new ConfirmOrderCommand(orderId));
		commandGateway.send(new ShipOrderCommand(orderId));
	}

	@PostMapping("/ship-unconfirmed-order")
	public void shipUnconfirmedOrder() {
		String orderId = UUID.randomUUID().toString();
		commandGateway.send(new PlaceOrderCommand(orderId, "Deluxe Chair"));
		commandGateway.send(new ShipOrderCommand(orderId));
	}

	@PostMapping("/replay-order")
	public void replayOrder() {

		List<EventProcessor> processors = eventHandlingConfiguration.getProcessors();

		service.startReplay("com.lca.phoenix");
	}

	@GetMapping("/all-orders")
	public List<OrderedProduct> findAllOrderedProducts() {
		return queryGateway
				.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(OrderedProduct.class))
				.join();
	}

}
