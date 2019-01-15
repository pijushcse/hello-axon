package com.lca.phoenix;

import com.lca.phoenix.entities.TokenJpaRepository;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
public class TrackingProcessorService {
 
    private final TokenJpaRepository repository;
    private final EventHandlingConfiguration eventHandlingConfiguration;
 
    public TrackingProcessorService(EventHandlingConfiguration eventHandlingConfiguration, TokenJpaRepository repository) {
        this.eventHandlingConfiguration = eventHandlingConfiguration.usingTrackingProcessors();
        this.eventHandlingConfiguration.registerTrackingProcessor("com.lca.phoenix") ;
        this.repository = repository;
    }
 
    public void startReplay(String name) {
        final TokenEntry.PK id = new TokenEntry.PK(name, 0);
        final TokenEntry one = this.repository.getOne(id);
        final Supplier<? extends RuntimeException> notFoundSupplier = () -> new IllegalArgumentException("Processor " + name + " not registered.");
        if (one == null) {
             throw notFoundSupplier.get();
        }
        this.eventHandlingConfiguration.getProcessor(name).orElseThrow(notFoundSupplier).shutDown();
        this.repository.deleteById(id);
        this.eventHandlingConfiguration.getProcessor(name).orElseThrow(notFoundSupplier).start();   
    }
}