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

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.ModalUploadStatus;
import gwtupload.client.SingleUploaderModal;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
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
    
    interface MyUploadButtonStyle extends CssResource {
    	
        String htmlButton();
        
        String htmlButton_over();
    }
    

//    @UiField(provided = true)
//    MyEntityEditor myEntityEditor;
    @UiField(provided = true)
    CellTable<TradeProxy> myTable;
    
    @UiField(provided = true) 
    SingleUploaderModal fileUploader;
    
    @UiField
    MyUploadButtonStyle style;
    
    @UiField
    Button submit;

    private final ListDataProvider<TradeProxy> dataProvider;
    
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
    private DateTimeFormat timeFormat = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
    NumberFormat numberFormat = NumberFormat.getDecimalFormat();

    @Inject
    public HomePageView(final Binder uiBinder, final MyEntityEditor myEntityEditor,
            final ListDataProvider<TradeProxy> dataProvider, MyCellTableResources myCellTableResources) {
    	
//        this.myEntityEditor = myEntityEditor;
        this.dataProvider = dataProvider;
        this.myTable = new CellTable<TradeProxy>(50, myCellTableResources);
//        this.myTable.setWidth("85%", true);
        
        ModalUploadStatus uploadStatus = new ModalUploadStatus() {
        	@Override
        	public void setVisible(boolean b) {
//        		box.hide();
        		super.setVisible(b);
        	}
        };
        
//        Button b = new Button("Upload Trade File");
//        b.setWidth("300px");
        
        Button cssButton = new Button("Upload file");
        
//        IFileInput anchor = new ButtonFileInput(new Anchor("This is my custom anchor"), false);
		cssButton.setStyleName("style.htmlButton");
		fileUploader = new SingleUploaderModal(FileInputType.CUSTOM.with(cssButton), uploadStatus, null);
        this.fileUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
        this.fileUploader.setAutoSubmit(true);
		this.fileUploader.addStatusBar(uploadStatus);
        
       

        initWidget(uiBinder.createAndBindUi(this));
        
        // Change style of the button now that styles are available
        cssButton.addStyleName(style.htmlButton());
        
        
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
    
    @Override
    public List<TradeProxy> getData() {
    	return dataProvider.getList();
    }

    @UiHandler("submit")
    void onSubmitClicked(ClickEvent event) {
        getUiHandlers().saveTrades(dataProvider.getList());
    }
    
    @UiHandler("submit") 
    void onMouseOver(MouseOverEvent event) {
    	submit.addStyleName(style.htmlButton_over());
    }
    
    @UiHandler("submit") 
    void onMouseOut(MouseOutEvent event) {
    	submit.removeStyleName(style.htmlButton_over());
    }

   
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
    	public void onFinish(IUploader uploader) {
    		if (uploader.getStatus() == Status.SUCCESS) {
    			String filename = uploader.getServerInfo().getField();
    			getUiHandlers().loadTradesFromFile(filename);
    		}
    	}


    };
    
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
        
        Column<TradeProxy, String> typeColumn = new Column<TradeProxy, String>(new SelectionCell(getTypes())) {

			@Override
			public String getValue(TradeProxy object) {
				return object.getType();
			}
        	
        };
        
        Column<TradeProxy, String> entryColumn = new Column<TradeProxy, String>(new SelectionCell(getEntries())) {
        	@Override
        	public String getValue(TradeProxy object) {
        		return object.getEntry();
        	}
        };
        

        Column<TradeProxy, String> zoneColumn = new Column<TradeProxy, String>(new SelectionCell(getZones())) {
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
        
        // columns field updaters
        typeColumn.setFieldUpdater(new FieldUpdater<TradeProxy, String>() {
        	
        	@Override
        	public void update(int index, TradeProxy object, String value) {
        		object.setType(value);
        		
        	}
        	
        });
        
        entryColumn.setFieldUpdater(new FieldUpdater<TradeProxy, String>() {
        	
        	@Override
        	public void update(int index, TradeProxy object, String value) {
        		object.setEntry(value);
        		
        	}
        	
        });
        
        
        zoneColumn.setFieldUpdater(new FieldUpdater<TradeProxy, String>() {
        	
        	@Override
        	public void update(int index, TradeProxy object, String value) {
        		object.setZone(value);
        		
        	}
        	
        });
        
        
        // Add columns to table
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
        
//        myTable.setColumnWidth(typeColumn, 100D, Unit.PX);
    }

	private List<String> getTypes() {
		
		return Arrays.asList(EMPTY_STRING, "T1", "T2", "T3");
	}
    
	private List<String> getEntries() {
		
		return Arrays.asList(EMPTY_STRING, "RET", "F62", "HOR", "RM", "RR");
	}
	
	private List<String> getZones() {
		
		return Arrays.asList(EMPTY_STRING, "Z1", "Z2", "Z3");
	}
	
    private static final String EMPTY_STRING = "";
   

}
