package com.rugobal.dt.repository.trade;

import java.util.Date;
import java.util.List;

import com.rugobal.dt.entities.model.Trade;

/**
 * 
 * Custom repository for {@link Trade} entity.
 * 
 * @author Ruben Gomez
 *
 */
public interface TradeJpaRepositoryCustom {
	
	/**
	 * Gets all the {@link Trade} for which their start date is in the day of the given date.
	 * 
	 * @param userId
	 * @param date
	 * @return list of trade objects for which their start date is in the day of the given date.
	 */
	List<Trade> getTradesForDay(int userId, Date date);
	
	/**
	 * 
	 * Gets all the {@link Trade} for which their start date is between the period of time given.
	 * 
	 * @param userId
	 * @param startDate the start date of the period
	 * @param endDate the end date of the period
	 * @return list of trades for which their start date is between the period of time given
	 */
	List<Trade> getTradesForPeriod(int userId, Date startDate, Date endDate);
	
}
