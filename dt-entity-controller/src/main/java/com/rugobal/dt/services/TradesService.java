package com.rugobal.dt.services;

import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rugobal.dt.business.model.DayTrade;
import com.rugobal.dt.entities.model.Trade;
import com.rugobal.dt.repository.trade.TradeJpaRepository;
import com.rugobal.dt.services.ninja.NinjaTradeParser;
import com.rugobal.dt.util.DateUtils;


@Component
@Transactional
public class TradesService {
	
	@Inject
	private TradeJpaRepository tradeRepository;
	
	private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
	
	/**
	 * Reads the lines of a file and converts them to Trade objects.
	 * 
	 * @param absPathToFile absolute path to the file to read.
	 * @return a list of {@link Trade} objects.
	 */
	public List<Trade> readTradesFromFile(String absPathToFile)  {
		
		NinjaTradeParser ninjaTradeParser = new NinjaTradeParser();
		return ninjaTradeParser.parseTrades(absPathToFile);
		
	}
	
	/**
	 * Reads the lines of a file that lives in the system temp directory and converts them to Trade objects.
	 * 
	 * @param fileName fileName inside the system temp folder
	 * @return a list of {@link Trade} objects.
	 */
	public List<Trade> readTradesFromTempFile(String fileName)  {
		
		String separator = FileSystems.getDefault().getSeparator();
        String targetDir = System.getProperty(JAVA_IO_TMPDIR);
        String absPathToFile = targetDir + separator + fileName;
		 
		NinjaTradeParser ninjaTradeParser = new NinjaTradeParser();
		return ninjaTradeParser.parseTrades(absPathToFile);
		
	}
	
	/**
	 * Find by id. 
	 * 
	 * @param id
	 * @return a trade
	 */
	public Trade findById(int id) {
		return this.tradeRepository.findOne(id);
	}
	
	
	/**
	 * Saves a list of {@link Trade} objects to the DB.
	 * 
	 * @param trades
	 */
	public void saveTradesToDB(List<Trade> trades) {
		
		for (Trade trade : trades) {
			saveTradeToDB(trade);
		}
		
		this.tradeRepository.save(trades);
		
	}
	
	/**
	 * Saves a {@link Trade} object to the DB.
	 * 
	 * @param trade
	 */
	public void saveTradeToDB(Trade trade) {
		validateTrade(trade);

		// set crated date
		trade.setCreatedDate(new Date());


		this.tradeRepository.save(trade);

	}
	
	/**
	 * Finds if there are trades on the DB for a given day and user.
	 * 
	 * @param userId
	 * @param date
	 * @return if there are trades on the DB for a given day and user
	 */
	public boolean tradesExistForDay(int userId, Date date) {
		List<Trade> trades = this.tradeRepository.getTradesForDay(userId, date);
		return !trades.isEmpty();
	}
	
	/**
	 * Deletes trades on the DB for the given day and user.
	 * 
	 * @param userId
	 * @param date
	 */
	public void deleteTradesForDay(int userId, Date date) {
		List<Trade> trades = this.tradeRepository.getTradesForDay(userId, date);
		this.tradeRepository.delete(trades);
	}
	
	/**
	 * Calculates the day trade totals given a list of trades.
	 * <br>
	 * All trades must be from the same day.
	 * 
	 * @param trades
	 * @return the day trade totals
	 */
	public DayTrade calculateDayTrade(List<Trade> trades) {
		
		if (CollectionUtils.isEmpty(trades)) {
			return null;
		}
		
		Trade firstTrade = trades.iterator().next();
		long startOfDay = DateUtils.startOfDay(firstTrade.getStartDate()).getTime();
		long endOfDay = DateUtils.endOfDay(firstTrade.getStartDate()).getTime();
		
		for (Trade trade: trades) {
			long tradeStartTime = trade.getStartDate().getTime();
			long tradeEndTime = trade.getStartDate().getTime();
			if (tradeStartTime < startOfDay  || tradeEndTime > endOfDay) {
				throw new IllegalStateException("All trades must begin and end on the same day");
			}
		}
		
		DayTrade result = new DayTrade();
		result.setDate(new Date(startOfDay));
		
		for (Trade trade: trades) {
			
			if ("T1".equals(trade.getType())) {
				
				if (trade.getProfitLoss() >= 0) {
					result.setT1PossitiveOperations( result.getT1PossitiveOperations() +1 );
					result.setT1PossitivePoints( result.getT1PossitivePoints() + trade.getProfitLoss() );
				} else {
					result.setT1NegativeOperations( result.getT1NegativeOperations() +1 );
					result.setT1NegativePoints( result.getT1NegativePoints() + Math.abs(trade.getProfitLoss()) );
				}
			}
			
			if ("T2".equals(trade.getType())) {
				
				if (trade.getProfitLoss() >= 0) {
					result.setT2PossitiveOperations( result.getT2PossitiveOperations() +1 );
					result.setT2PossitivePoints( result.getT2PossitivePoints() + trade.getProfitLoss() );
				} else {
					result.setT2NegativeOperations( result.getT2NegativeOperations() +1 );
					result.setT2NegativePoints( result.getT2NegativePoints() + Math.abs(trade.getProfitLoss()) );
				}
			}
			
			if ("T3".equals(trade.getType())) {
				
				if (trade.getProfitLoss() >= 0) {
					result.setT3PossitiveOperations( result.getT3PossitiveOperations() +1 );
					result.setT3PossitivePoints( result.getT3PossitivePoints() + trade.getProfitLoss() );
				} else {
					result.setT3NegativeOperations( result.getT3NegativeOperations() +1 );
					result.setT3NegativePoints( result.getT3NegativePoints() + Math.abs(trade.getProfitLoss()) );
				}
			}
			
			// Total values
			if (trade.getProfitLoss() >= 0) {
				result.setTotalPositiveOperations(result.getTotalPositiveOperations() + 1);
			} else {
				result.setTotalNegativeOperations(result.getTotalNegativeOperations() + 1);
			}
			result.setTotalProfitLoss(result.getTotalProfitLoss() + trade.getProfitLoss());
			
			
		}
		
		
		return result;
	}
	
	/**
	 * Calculates the day trades totals given a list of trades.
	 * 
	 * @param trades
	 * @return the day trade totals
	 */
	public List<DayTrade> calculateDayTrades(List<Trade> trades) {
		
		Map<Date, List<Trade>> mappedByDate = mapTradesByDate(trades);
		
		List<DayTrade> result = new ArrayList<>();
		for (List<Trade> list : mappedByDate.values()) {
			result.add(calculateDayTrade(list));
		}
		
		return result;
	}
	
	
	private Map<Date, List<Trade>> mapTradesByDate(List<Trade> trades) {
		
	    Map<Date, List<Trade>> result = new LinkedHashMap<>();
	    
		for (Trade trade : trades) {
			Date key = DateUtils.startOfDay(trade.getStartDate());
			
			List<Trade> existing = result.get(key);
			if (existing == null) {
				List<Trade> list = new ArrayList<>(Arrays.asList(trade));
				result.put(key, list);
			} else {
				existing.add(trade);
			}
			
		}
		
		return result;
		
    }


	private void validateTrade(Trade trade) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Trade> loadAllTrades() {
		return this.tradeRepository.findAll();
	}



}
