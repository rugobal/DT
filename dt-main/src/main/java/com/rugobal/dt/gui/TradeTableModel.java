package com.rugobal.dt.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.rugobal.dt.entities.model.Trade;

public class TradeTableModel extends AbstractTableModel {
	
	
	private static final long serialVersionUID = 1584346469927256832L;
	
	SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
	SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

	// Table panel code
	String[] columnNames = {"Date", "Start Time", "Contracts", "Start Price", "End Price", "End Time", "Type", "Entry", "Zone", "P&L"};
	
	
	private List<Trade> trades = new ArrayList<>();
	
	public TradeTableModel(List<Trade> trades) {
		if (trades != null) {
			this.trades = trades;
		}
	}

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return trades.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	Trade trade = this.trades.get(row);
    	switch (col) {
    		case 0:
    			return sdfDate.format(trade.getStartDate());
    		case 1:
    			return sdfTime.format(trade.getStartDate());
    		case 2:
    			return trade.getNoOfContracts();
    		case 3:
    			return trade.getStartPrice();
    		case 4:
    			return trade.getEndPrice();
    		case 5:
    			return sdfTime.format(trade.getEndDate());
    		case 6:
    			return trade.getType() == null ? "" : trade.getType();
    		case 7:
    			return trade.getEntry() == null ? "" : trade.getEntry();
    		case 8:
    			return trade.getZone() == null ? "" : trade.getZone();
    		case 9:
    			return trade.getProfitLoss();
    		default:
    			throw new IllegalStateException("Unknown column index:" + col);
    				
    	}
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 6 || col == 7 || col == 8) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {

    	Trade trade = this.trades.get(row);
    	if (col == 6) {
    		
    		trade.setType((String) value);
    		
    	} else if (col == 7) {

    		trade.setEntry((String) value);
    		
    	} else if (col == 8) {

    		trade.setZone((String) value);
    		
    	} else  {
    		
    		throw new IllegalStateException("Column not upldateable:" + col);

    	}
    	
        fireTableCellUpdated(row, col);
        
    }
    
    public List<Trade> getTrades() {
    	return this.trades;
    }
}
