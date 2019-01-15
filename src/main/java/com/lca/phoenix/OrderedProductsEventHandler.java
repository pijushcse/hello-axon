package com.lca.phoenix;

import com.lca.phoenix.entities.Product;
import com.lca.phoenix.entities.ProductRepository;
import com.lca.phoenix.events.OrderConfirmedEvent;
import com.lca.phoenix.events.OrderPlacedEvent;
import com.lca.phoenix.events.OrderShippedEvent;
import com.lca.phoenix.queries.FindAllOrderedProductsQuery;
import com.lca.phoenix.queries.OrderStatus;
import com.lca.phoenix.queries.OrderedProduct;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderedProductsEventHandler {

    private final Map<String, OrderedProduct> orderedProducts = new HashMap<>();

    @Autowired
    ProductRepository repository;

    @EventHandler
    public void on(OrderPlacedEvent event, ReplayStatus status) {
        String orderId = event.getOrderId();
        orderedProducts.put(orderId, new OrderedProduct(orderId, event.getProduct()));
        Product p = new Product(orderId, event.getProduct());
        repository.save(p);
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            Optional<Product> product = repository.findById(orderId);
            if (product.isPresent()) {
                Product p = product.get();
                p.setOrderStatus(OrderStatus.CONFIRMED);
                repository.save(p);
            }
            return orderedProduct;
        });


    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            Optional<Product> product = repository.findById(orderId);
            if (product.isPresent()) {
                Product p = product.get();
                p.setOrderStatus(OrderStatus.SHIPPED);
                repository.save(p);
            }
            return orderedProduct;
        });
    }

    @QueryHandler
    public List<OrderedProduct> handle(FindAllOrderedProductsQuery query) {

        List<Product> resultList = repository.findAll();

        return resultList.stream().map(e -> {
            OrderedProduct p = new OrderedProduct(e.getOrderId(), e.getProduct());
            p.setOrderStatus(e.getOrderStatus());

            return p;
        }).collect(Collectors.toList());
    }

}