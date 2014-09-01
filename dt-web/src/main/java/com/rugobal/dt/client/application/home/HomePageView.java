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

import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.rugobal.dt.client.application.home.ui.MyEntityEditor;
import com.rugobal.dt.client.css.MyCellTableResources;
import com.rugobal.dt.client.request.proxy.MyEntityProxy;
import com.rugobal.dt.client.request.proxy.TradeProxy;

public class HomePageView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePagePresenter.MyView {
    public interface Binder extends UiBinder<Widget, HomePageView> {
    }
    

    @UiField(provided = true)
    MyEntityEditor myEntityEditor;
    @UiField(provided = true)
    CellTable<TradeProxy> myTable;

    private final ListDataProvider<TradeProxy> dataProvider;
    
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
    private DateTimeFormat timeFormat = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
    NumberFormat numberFormat = NumberFormat.getDecimalFormat();

    @Inject
    public HomePageView(final Binder uiBinder, final MyEntityEditor myEntityEditor,
            final ListDataProvider<TradeProxy> dataProvider, MyCellTableResources myCellTableResources) {
    	
        this.myEntityEditor = myEntityEditor;
        this.dataProvider = dataProvider;
        this.myTable = new CellTable<TradeProxy>(50, myCellTableResources);

        initWidget(uiBinder.createAndBindUi(this));

        initCellTable();
        dataProvider.addDataDisplay(myTable);
    }

    @Override
    public void editUser(MyEntityProxy myEntity) {
//        myEntityEditor.edit(myEntity);
    }

    @Override
    public void setData(List<TradeProxy> data) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(data);
        dataProvider.refresh();
    }

    @UiHandler("submit")
    void onSubmitClicked(ClickEvent event) {
        getUiHandlers().saveEntity(null/*myEntityEditor.get()*/);
    }

    private void initCellTable() {
    	
        TextColumn<TradeProxy> dateColumn = new TextColumn<TradeProxy>() {
            @Override
            public String getValue(TradeProxy object) {
            	return dateFormat.format(object.getStartDate());
            }
        };
        
        TextColumn<TradeProxy> startTimeColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return timeFormat.format(object.getStartDate());
        	}
        };
        
        TextColumn<TradeProxy> contractsColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return object.getNoOfContracts() > 0 ? "+" + object.getNoOfContracts() : String.valueOf(object.getNoOfContracts());
        	}
        	
        	@Override
        	public String getCellStyleNames(Context context, TradeProxy object) {
        		if (object.getNoOfContracts() > 0 ) {
        			return "greenText";
        		} else {
        			return "redText";
        		}
        		
         		
        	}
        };
        
        TextColumn<TradeProxy> startPriceColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return numberFormat.format(object.getStartPrice());
        	}
        };
        
        TextColumn<TradeProxy> endPriceColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return numberFormat.format(object.getEndPrice());
        	}
        };
        
        TextColumn<TradeProxy> endTimeColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return timeFormat.format(object.getEndDate());
        	}
        };
        
        TextColumn<TradeProxy> typeColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return object.getType();
        	}
        };
        
        TextColumn<TradeProxy> entryColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return object.getEntry();
        	}
        };
        

        TextColumn<TradeProxy> zoneColumn = new TextColumn<TradeProxy>() {
            @Override
            public String getValue(TradeProxy object) {
                return object.getZone();
            }
        };
        
        TextColumn<TradeProxy> profitLossColumn = new TextColumn<TradeProxy>() {
        	@Override
        	public String getValue(TradeProxy object) {
        		return numberFormat.format(object.getProfitLoss());
        	}
        };
        
        contractsColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        
        myTable.addColumn(dateColumn, "Date");
        myTable.addColumn(startTimeColumn, "Start Time");
        myTable.addColumn(contractsColumn, "Contracts");
        myTable.addColumn(startPriceColumn, "Start Price");
        myTable.addColumn(endPriceColumn, "End Price");
        myTable.addColumn(endTimeColumn, "End Time");
        myTable.addColumn(typeColumn, "Type");
        myTable.addColumn(entryColumn, "Entry");
        myTable.addColumn(zoneColumn, "Zone");
        myTable.addColumn(profitLossColumn, "P&L");
    }
}
