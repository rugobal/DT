package com.rugobal.dt.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.springframework.stereotype.Component;

import com.michaelbaranov.microba.calendar.DatePicker;
import com.rugobal.dt.business.model.DayTrade;
import com.rugobal.dt.entities.model.Trade;
import com.rugobal.dt.gui.thirdparty.groupabletableheader.ColumnGroup;
import com.rugobal.dt.gui.thirdparty.groupabletableheader.GroupableTableHeader;
import com.rugobal.dt.repository.trade.TradeJpaRepository;
import com.rugobal.dt.services.TradesService;
import com.rugobal.dt.util.DateUtils;

@Component
public class StatisticsPanel extends JPanel implements ActionListener {
	
	
    private static final long serialVersionUID = -6387797098785007594L;
    private TradesService tradesService;
    private TradeJpaRepository tradeRepository;
    
    
	private JScrollPane scrollPaneWithDayTradeTable;
	private JTable dayTradeTable;
	private DatePicker datePickerFrom;
	private DatePicker datePickerTo;
	private JButton loadButton;
	private JLabel summaryLabel;
	private JPanel topPanel;
	
	@Inject
	public StatisticsPanel(TradesService tradesService, TradeJpaRepository tradeRepository) {
		
		this.tradesService = tradesService;
		this.tradeRepository = tradeRepository;
	    
		setLayout(new BorderLayout());

		// Top panel 
		{
			
			// Date picker
			this.topPanel = new JPanel();
	//		this.topPanel.setLayout((new BoxLayout(topPanel, BoxLayout.X_AXIS)));
			
			this.datePickerFrom = new DatePicker();
			this.datePickerFrom.setDateFormat(new SimpleDateFormat("dd-MMM-yyyy"));
			this.datePickerFrom.setEnabled(true);
			this.datePickerFrom.setKeepTime(false);
			this.datePickerFrom.setStripTime(true);
			this.datePickerFrom.setShowNumberOfWeek(false);
			
			this.datePickerTo = new DatePicker();
			this.datePickerTo.setDateFormat(new SimpleDateFormat("dd-MMM-yyyy"));
			this.datePickerTo.setEnabled(true);
			this.datePickerTo.setKeepTime(false);
			this.datePickerTo.setStripTime(true);
			this.datePickerTo.setShowNumberOfWeek(false);
			
			try {
				datePickerFrom.setDate(DateUtils.firstDayOfMonth(new Date()));
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
			
			this.loadButton = new JButton("Load");
			this.loadButton.addActionListener(this);
			
			topPanel.add(new JLabel("From:"));
			topPanel.add(datePickerFrom);
			topPanel.add(new JLabel("To:"));
			topPanel.add(datePickerTo);
			topPanel.add(this.loadButton);
		}
		
		// DayTrade table
		{
			dayTradeTable = new JTable(new DayTradeTableModel(null)) {

				private static final long serialVersionUID = 4602535802972787309L;

				@Override
				protected JTableHeader createDefaultTableHeader() {
					return new GroupableTableHeader(columnModel);
				}
			};

			//					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			//					centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			//					this.dayTradeTable.setDefaultRenderer(String.class, centerRenderer);
			//					this.dayTradeTable.setDefaultRenderer(Double.class, centerRenderer);
			//					this.dayTradeTable.setDefaultRenderer(Integer.class, centerRenderer);

			TableColumnModel cm = this.dayTradeTable.getColumnModel();

			ColumnGroup t1Group = new ColumnGroup("T1");
			t1Group.add(cm.getColumn(1));
			t1Group.add(cm.getColumn(2));
			t1Group.add(cm.getColumn(3));
			t1Group.add(cm.getColumn(4));

			ColumnGroup t2Group = new ColumnGroup("T2");
			t2Group.add(cm.getColumn(5));
			t2Group.add(cm.getColumn(6));
			t2Group.add(cm.getColumn(7));
			t2Group.add(cm.getColumn(8));

			ColumnGroup t3Group = new ColumnGroup("T3");
			t3Group.add(cm.getColumn(9));
			t3Group.add(cm.getColumn(10));
			t3Group.add(cm.getColumn(11));
			t3Group.add(cm.getColumn(12));

			GroupableTableHeader header = (GroupableTableHeader)this.dayTradeTable.getTableHeader();
			header.addColumnGroup(t1Group);
			header.addColumnGroup(t2Group);
			header.addColumnGroup(t3Group);

			this.scrollPaneWithDayTradeTable = new JScrollPane(dayTradeTable);
			//			dayTradeTable.setFillsViewportHeight(true);
			//					this.scrollPaneWithDayTradeTable.setPreferredSize(this.dayTradeTable.getPreferredSize());



		}
		
		this.summaryLabel = new JLabel();
		
		add(topPanel, BorderLayout.NORTH);
		add(this.scrollPaneWithDayTradeTable, BorderLayout.CENTER); 
		add(this.summaryLabel, BorderLayout.SOUTH);
		

		loadDayTrades();
		
    }

	void loadDayTrades() {
		
	    List<Trade> trades = this.tradeRepository.getTradesForPeriod(1, this.datePickerFrom.getDate(), this.datePickerTo.getDate());
	    List<DayTrade> dayTrades = this.tradesService.calculateDayTrades(trades);
	    
	    if (dayTrades != null) {

	    	DayTradeTableModel model = (DayTradeTableModel) this.dayTradeTable.getModel();
	    	model.getDayTrades().clear();
	    	model.getDayTrades().addAll(dayTrades);
	    	model.fireTableChanged(new TableModelEvent(model));
	    	
	    	printSummaryLabel(dayTrades);
	    	
	    }
    }

	private void printSummaryLabel(List<DayTrade> dayTrades) {

		// Get total P&L and total number of operations
		double totalProfitLoss = 0;
		int totalOperations = 0;
		for (DayTrade dayTrade : dayTrades) {
			totalProfitLoss += dayTrade.getTotalProfitLoss();
			totalOperations += (dayTrade.getTotalPositiveOperations() + dayTrade.getTotalNegativeOperations());
		}
		
		if (totalOperations == 0) {
			return;
		}
		
		this.summaryLabel.setText(String.format("Total Points: %.2f. Total P&L: %.2f$ - %d$ (Commission) = %.2f$", totalProfitLoss, totalProfitLoss * 50, totalOperations * 5, totalProfitLoss * 50 - (totalOperations * 5)));
	}

	@Override
    public void actionPerformed(ActionEvent e) {
		
	    if (this.loadButton == e.getSource()) {
	    	loadDayTrades();
	    }
	    
    }
	
}
