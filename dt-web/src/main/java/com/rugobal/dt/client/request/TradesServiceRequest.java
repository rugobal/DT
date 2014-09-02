package com.rugobal.dt.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.rugobal.dt.client.request.proxy.TradeProxy;
import com.rugobal.dt.server.util.SpringServiceLocator;
import com.rugobal.dt.services.TradesService;

@Service(value = TradesService.class, locator = SpringServiceLocator.class)
public interface TradesServiceRequest extends RequestContext {
	
	Request<List<TradeProxy>> loadAllTrades();
	
	Request<Void> saveTradeToDB(TradeProxy trade);
	
	Request<TradeProxy> findById(int id);
	
	Request<List<TradeProxy>> readTradesFromTempFile(String absPathToFile);

}
