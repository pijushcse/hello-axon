package com.lca.phoenix.entities;

import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenJpaRepository extends JpaRepository<TokenEntry, TokenEntry.PK> {
 
}