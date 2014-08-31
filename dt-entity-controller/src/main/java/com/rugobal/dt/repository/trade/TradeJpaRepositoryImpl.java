package com.rugobal.dt.repository.trade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.rugobal.dt.entities.model.Trade;

/**
 * Implementation of Custom Repository for {@link AuthToken} entity.
 * 
 * @author Ruben Gomez
 *
 */
public class TradeJpaRepositoryImpl implements TradeJpaRepositoryCustom { 
    
//    private static final Logger LOGGER = LoggerFactory.getLogger(TradeJpaRepositoryImpl.class);
    
    @Inject
    private EntityManager entityManager;

	@Override
	public List<Trade> getTradesForDay(int userId, Date date) {
		
		Query query = entityManager.createQuery("from Trade where userId = :userId and startDate >= :startDate and startDate <= :endDate");
		
		query.setParameter("userId", userId);
	    query.setParameter("startDate", startOfDay(date));
	    query.setParameter("endDate", endOfDay(date));

	    @SuppressWarnings("unchecked")
	    List<Trade> result = query.getResultList();

	    return result;
		
	}
	
	@Override
	public List<Trade> getTradesForPeriod(int userId, Date startDate, Date endDate) {
		
		Query query = entityManager.createQuery("from Trade where userId = :userId and startDate >= :startDate and startDate <= :endDate");
		
		query.setParameter("userId", userId);
		query.setParameter("startDate", startOfDay(startDate));
		query.setParameter("endDate", endOfDay(endDate));
		
		@SuppressWarnings("unchecked")
		List<Trade> result = query.getResultList();
		
		return result;
		
	}
	
	
	private Date startOfDay(Date date)  {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    private Date endOfDay(Date date)  {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
        
    }
    
    
}