/**
 * Copyright 2012 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.rugobal.dt.client.application.home;

import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.rugobal.dt.client.application.ApplicationPresenter;
import com.rugobal.dt.client.place.NameTokens;
import com.rugobal.dt.client.request.MyRequestFactory;
import com.rugobal.dt.client.request.TradesServiceRequest;
import com.rugobal.dt.client.request.proxy.MyEntityProxy;
import com.rugobal.dt.client.request.proxy.TradeProxy;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> implements
        HomeUiHandlers {
	
    public interface MyView extends View, HasUiHandlers<HomeUiHandlers> {
        void editUser(MyEntityProxy myEntity);

        void setData(List<TradeProxy> data);
        
        List<TradeProxy> getData();
    }

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    private final MyRequestFactory requestFactory;

//    private MyServiceRequest currentContext;
    private TradesServiceRequest currentContext;
    private String searchToken;

    @Inject
    public HomePagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
            final MyRequestFactory requestFactory) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.requestFactory = requestFactory;

        getView().setUiHandlers(this);
    }

    @Override
    public void saveEntity(MyEntityProxy myEntity) {
    	
    	TradeProxy newTrade = currentContext.create(TradeProxy.class);
    	newTrade.setUserId(1);
		newTrade.setInstrument("ES"); // not necessary
		newTrade.setStartDate(new Date());
		newTrade.setEndDate(new Date()); // not necessary
		newTrade.setStartPrice(100D);
		newTrade.setEndPrice(103D);
		newTrade.setNoOfContracts(1);
		newTrade.setZone("Z1");
		newTrade.setType("T1");
		newTrade.setEntry("RET");
		newTrade.setProfitLoss(3D);
		newTrade.setComment("any comment"); // not necessary
		newTrade.setCreatedDate(new Date());
		
		currentContext.saveTradeToDB(newTrade).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				loadTrades();
				initializeContext();
			}
			
		});
		
//		TradeProxy lastTradeOnTable = getView().getData().get(getView().getData().size()-1);
//		TradeProxy editableTrade = currentContext.edit(lastTradeOnTable);
//		editableTrade.setStartPrice(255.25D);
//		currentContext.saveTradeToDB(editableTrade).fire();
		
		
    	
//        currentContext.create(myEntity).fire(new Receiver<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
////                loadEntities();
//                initializeContext();
//            }
//        });
		
    }

    @Override
    protected void onReveal() {
        searchToken = "";
        initializeContext();
//        loadEntities();
        loadTrades();
    }

	private void initializeContext() {
		currentContext = requestFactory.tradesService();
//        currentContext = requestFactory.myService();
//        MyEntityProxy newEntity = currentContext.create(MyEntityProxy.class);
//        getView().editUser(newEntity);
    }

	
	private void loadTrades() {
		requestFactory.tradesService().loadAllTrades().fire(new Receiver<List<TradeProxy>>() {
			@Override
			public void onSuccess(List<TradeProxy> data) {
				getView().setData(data);
			}
		});
	}
	
//    private void loadEntities() {
//        requestFactory.myService().loadAll(searchToken).fire(new Receiver<List<MyEntityProxy>>() {
//            @Override
//            public void onSuccess(List<MyEntityProxy> data) {
//                getView().setData(data);
//            }
//        });
//    }
}
