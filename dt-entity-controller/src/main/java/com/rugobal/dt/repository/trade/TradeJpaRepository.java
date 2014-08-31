package com.rugobal.dt.repository.trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rugobal.dt.entities.model.Trade;

/**
* 
* JPA Repository for {@link Trade}.
* 
* @author Ruben Gomez
*
*/
@Repository
public interface TradeJpaRepository extends JpaRepository<Trade, Integer>, TradeJpaRepositoryCustom, 
                                               JpaSpecificationExecutor <Trade> {
}
