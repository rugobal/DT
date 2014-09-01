package com.rugobal.dt.client.request.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rugobal.dt.entities.model.Trade;

@ProxyFor(value = Trade.class)
public interface TradeProxy extends ValueProxy {

	int getId();

	int getUserId();

	void setUserId(int userId);

	String getInstrument();

	void setInstrument(String instrument);

	Date getStartDate();

	void setStartDate(Date startDate);

	Date getEndDate();

	void setEndDate(Date endDate);

	int getNoOfContracts();

	void setNoOfContracts(int noOfContracts);

	Double getStartPrice();

	void setStartPrice(Double startPrice);

	Double getEndPrice();

	void setEndPrice(Double endPrice);

	String getZone();

	void setZone(String zone);

	String getType();

	void setType(String type);

	String getEntry();

	void setEntry(String entry);

	Double getProfitLoss();

	void setProfitLoss(Double profitLoss);

	String getComment();

	void setComment(String comment);

	Date getCreatedDate();

	void setCreatedDate(Date createdDate);
	
}
