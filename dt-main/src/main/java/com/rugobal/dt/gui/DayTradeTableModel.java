package com.rugobal.dt.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.rugobal.dt.business.model.DayTrade;

public class DayTradeTableModel extends AbstractTableModel {
	
 
    private static final long serialVersionUID = -4668758750073122695L;

	SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

	
	String[] columnNames = {
			"Date", 
			"Oper (+)", "Puntos (+)", "Oper (-)", "Puntos (-)",
			"Oper (+)", "Puntos (+)", "Oper (-)", "Puntos (-)", 
			"Oper (+)", "Puntos (+)", "Oper (-)", "Puntos (-)"
	};
	
	
	private List<DayTrade> dayTrades = new ArrayList<>();
	
	public DayTradeTableModel(List<DayTrade> dayTrades) {
		if (dayTrades != null) {
			this.dayTrades = dayTrades;
		}
	}
	

	public List<DayTrade> getDayTrades() {
		return dayTrades;
	}

	@Override
    public int getRowCount() {
	    return dayTrades.size();
    }

	@Override
    public int getColumnCount() {
	    return columnNames.length;
    }
	
	public String getColumnName(int col) {
        return columnNames[col];
    }

	public Object getValueAt(int row, int col) {
    	DayTrade dayTrade = this.dayTrades.get(row);
    	switch (col) {
    		case 0:
    			return sdfDate.format(dayTrade.getDate());
    		case 1:
    			return dayTrade.getT1PossitiveOperations();
    		case 2:
    			return dayTrade.getT1PossitivePoints();
    		case 3:
    			return dayTrade.getT1NegativeOperations();
    		case 4:
    			return dayTrade.getT1NegativePoints();
    		case 5:
    			return dayTrade.getT2PossitiveOperations();
    		case 6:
    			return dayTrade.getT2PossitivePoints();
    		case 7:
    			return dayTrade.getT2NegativeOperations();
    		case 8:
    			return dayTrade.getT2NegativePoints();
    		case 9:
    			return dayTrade.getT3PossitiveOperations();
    		case 10:
    			return dayTrade.getT3PossitivePoints();
    		case 11:
    			return dayTrade.getT3NegativeOperations();
    		case 12:
    			return dayTrade.getT3NegativePoints();
    		
    		default:
    			throw new IllegalStateException("Unknown column index:" + col);
    				
    	}
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


}
