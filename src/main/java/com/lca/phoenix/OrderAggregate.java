package com.lca.phoenix;

 
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import com.lca.phoenix.command.ConfirmOrderCommand;
import com.lca.phoenix.command.PlaceOrderCommand;
import com.lca.phoenix.command.ShipOrderCommand;
import com.lca.phoenix.events.OrderConfirmedEvent;
import com.lca.phoenix.events.OrderPlacedEvent;
import com.lca.phoenix.events.OrderShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    @CommandHandler
    public OrderAggregate(PlaceOrderCommand command) {
        apply(new OrderPlacedEvent(command.getOrderId(), command.getProduct()));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        apply(new OrderConfirmedEvent(orderId));
    }

    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (!orderConfirmed) {
            throw new IllegalStateException("Cannot ship an order which has not been confirmed yet.");
        }

        apply(new OrderShippedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderPlacedEvent event) {
        this.orderId = event.getOrderId();
        orderConfirmed = false;
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        orderConfirmed = true;
    }

    protected OrderAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }

}