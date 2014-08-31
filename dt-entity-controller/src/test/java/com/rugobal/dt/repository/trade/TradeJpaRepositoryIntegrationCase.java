package com.rugobal.dt.repository.trade;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.Test;

import com.rugobal.dt.entities.model.Trade;
import com.rugobal.dt.entities.model.Trade_;
import com.rugobal.dt.repository.AbstractRepositoryTest;

import static com.rugobal.dt.repository.Specifications.byColAndValue;
import static org.junit.Assert.*;

public class TradeJpaRepositoryIntegrationCase extends AbstractRepositoryTest {

	@Inject
	private TradeJpaRepository tradeRepository;

	
	@Test
	public void shouldFindByUserId() {
	    
		final Trade trade = new Trade();
		trade.setUserId(1);
		trade.setInstrument("ES"); // not necessary
		trade.setStartDate(new Date());
		trade.setEndDate(new Date()); // not necessary
		trade.setStartPrice(100D);
		trade.setEndPrice(103D);
		trade.setNoOfContracts(1);
		trade.setZone("Z1");
		trade.setType("T1");
		trade.setEntry("RET");
		trade.setProfitLoss(3D);
		trade.setComment("any comment"); // not necessary
		trade.setCreatedDate(new Date());
		
		tradeRepository.save(trade);
		
		
		List<Trade> lookupTrade = tradeRepository.findAll(byColAndValue(Trade_.userId, 1));
		assertNotNull(lookupTrade);
		assertTrue(lookupTrade.size() == 1);
		
		lookupTrade = tradeRepository.findAll(byColAndValue(Trade_.userId, 2));
		assertNotNull(lookupTrade);
		assertTrue(lookupTrade.size() == 0);
	}
	
	@Test
	public void testGetTradesForDay() {
		
		final Trade trade1 = new Trade();
		trade1.setUserId(1);
		trade1.setInstrument("ES"); // not necessary
		trade1.setStartDate(new Date());
		trade1.setEndDate(new Date()); // not necessary
		trade1.setNoOfContracts(1);
		trade1.setStartPrice(100D);
		trade1.setEndPrice(103D);
		trade1.setZone("Z1");
		trade1.setType("T1");
		trade1.setEntry("RET");
		trade1.setProfitLoss(3D);
		trade1.setComment("comment 1"); // not necessary
		trade1.setCreatedDate(new Date());
		
		final Trade trade2 = new Trade();
		trade2.setUserId(1);
		trade2.setInstrument("ES"); // not necessary
		trade2.setStartDate(new Date(new Date().getTime() + TimeUnit.DAYS.toMillis(1))); //next day
		trade2.setEndDate(new Date()); // not necessary
		trade2.setStartPrice(200D);
		trade2.setEndPrice(201D);
		trade2.setNoOfContracts(-1);
		trade2.setZone("Z2");
		trade2.setType("T2");
		trade2.setEntry("F62");
		trade2.setProfitLoss(-1D);
		trade2.setComment("comment 2");
		trade2.setCreatedDate(new Date());
		
		tradeRepository.save(trade1);
		tradeRepository.save(trade2);
		
		List<Trade> lookupTrade = tradeRepository.getTradesForDay(1, new Date());
		assertNotNull(lookupTrade);
		assertEquals(1, lookupTrade.size());
		
		Trade trade = lookupTrade.iterator().next();
		
		assertEquals("Z1", trade.getZone());
		
		lookupTrade = tradeRepository.getTradesForDay(2, new Date());
		assertNotNull(lookupTrade);
		assertEquals(0, lookupTrade.size());
		
	}
	
}
