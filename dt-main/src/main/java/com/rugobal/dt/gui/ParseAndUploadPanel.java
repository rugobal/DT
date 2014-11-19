package com.rugobal.dt.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.springframework.stereotype.Component;

import com.rugobal.dt.business.model.DayTrade;
import com.rugobal.dt.entities.model.Trade;
import com.rugobal.dt.gui.thirdparty.groupabletableheader.ColumnGroup;
import com.rugobal.dt.gui.thirdparty.groupabletableheader.GroupableTableHeader;
import com.rugobal.dt.services.TradesService;

@Component
public class ParseAndUploadPanel extends JPanel implements ActionListener {
	
    private static final long serialVersionUID = 6531762366364280819L;
    
	private JPanel cardsPanel;
	private JButton selectFileButton;
	private JButton saveTradesButton;
	private JButton parseTradesButton;
	private JTextField fileSelectedTextField;
	private JTextArea textArea;
	private JScrollPane scrollPaneWithTradesTable;
	private JScrollPane scrollPaneWithStackTrace;
	private JScrollPane scrollPaneWithDayTradeTable;
	private JTable tradesTable;
	private JTable dayTradeTable;
	private JButton calculateDayTradeButton;
	private JLabel totalSummaryLabel;
	
	final static String TABLESPANEL = "Card with table";
	final static String STACKTRACEPANEL = "Card with stacktrace";
	
	private static final String TEN_SPACES = "          ";
	
	@Inject
	private TradesService tradesService;
	
	@Inject
	private StatisticsPanel statisticsPanel;
	
	public ParseAndUploadPanel() {
 
		initComponents();
	}

	private void initComponents() {
		
		setLayout(new BorderLayout());
		
		//The first panel contains a JLabel and JCombobox 
		final JPanel topPanel = new JPanel(new BorderLayout(5,5)); 
		JLabel comboLbl = new JLabel("Select file:"); 
		fileSelectedTextField = new JTextField(30);
		fileSelectedTextField.setText("C:\\Users\\ballesterr\\Dropbox\\Trading\\Ninja Trader\\Orders.csv");
		selectFileButton = new JButton("Select File");
		topPanel.add(comboLbl, BorderLayout.LINE_START); 
		topPanel.add(fileSelectedTextField, BorderLayout.CENTER);
		topPanel.add(selectFileButton, BorderLayout.LINE_END);
		parseTradesButton = new JButton("Parse Trades");
		topPanel.add(parseTradesButton, BorderLayout.SOUTH);
		
		selectFileButton.addActionListener(this);
		parseTradesButton.addActionListener(this);
		
		// Tables panel
		JPanel tablesPanel = tablesPanelSetup();
		
		// Text area code
		this.textArea = new JTextArea();
		this.scrollPaneWithStackTrace = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		saveTradesButton = new JButton("Save Trades");
		saveTradesButton.addActionListener(this);
		
		//Create the panel that contains the "cards".
		cardsPanel = new JPanel(new CardLayout());
		cardsPanel.add(tablesPanel, TABLESPANEL);
		cardsPanel.add(this.scrollPaneWithStackTrace, STACKTRACEPANEL);
		
		
		
		add(topPanel, BorderLayout.NORTH); 
		add(cardsPanel, BorderLayout.CENTER); 
		add(saveTradesButton, BorderLayout.SOUTH); 
		
		
		CardLayout cl = (CardLayout)(cardsPanel.getLayout());
	    cl.show(cardsPanel, TABLESPANEL);
		
	}

	private JPanel tablesPanelSetup() {
		// Trades Table setup
		{
			tradesTable = new JTable(new TradeTableModel(null));
			scrollPaneWithTradesTable = new JScrollPane(tradesTable);
//			tradesTable.setFillsViewportHeight(true);


			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			tradesTable.setDefaultRenderer(String.class, centerRenderer);
			tradesTable.setDefaultRenderer(Double.class, centerRenderer);
			tradesTable.setDefaultRenderer(Integer.class, centerRenderer);

			TableColumn typeColumn = tradesTable.getColumnModel().getColumn(6);
			JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.addItem("");
			comboBox.addItem("T1");
			comboBox.addItem("T2");
			comboBox.addItem("T3");
			typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

			TableColumn entryColumn = tradesTable.getColumnModel().getColumn(7);
			comboBox = new JComboBox<String>();
			comboBox.addItem("");
			comboBox.addItem("RET");
			comboBox.addItem("R2VFC");
			comboBox.addItem("F62");
			comboBox.addItem("HOR");
			comboBox.addItem("RR");
			comboBox.addItem("ZD-B");
			entryColumn.setCellEditor(new DefaultCellEditor(comboBox));

			TableColumn zoneColumn = tradesTable.getColumnModel().getColumn(8);
			comboBox = new JComboBox<String>();
			comboBox.addItem("");
			comboBox.addItem("BKJ-1");
			comboBox.addItem("Z1");
			comboBox.addItem("Z2");
			comboBox.addItem("Z3");
			comboBox.addItem("BKJ-2");
			zoneColumn.setCellEditor(new DefaultCellEditor(comboBox));
			
			TableColumn contractsColumn = tradesTable.getColumnModel().getColumn(2);
			contractsColumn.setCellRenderer(new ContractsColumnCellRenderer());
		}
		
		// Calculate day trade button
		{
			calculateDayTradeButton = new JButton("Calculate Day Totals");
			calculateDayTradeButton.addActionListener(this);
		}
		
		// Total summary label
		{
			totalSummaryLabel = new JLabel();
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
			
//			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
//			this.dayTradeTable.setDefaultRenderer(String.class, centerRenderer);
//			this.dayTradeTable.setDefaultRenderer(Double.class, centerRenderer);
//			this.dayTradeTable.setDefaultRenderer(Integer.class, centerRenderer);
			
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
		    
			scrollPaneWithDayTradeTable = new JScrollPane(dayTradeTable);
			scrollPaneWithDayTradeTable.setPreferredSize(new Dimension(dayTradeTable.getPreferredSize().width, 200));
//			dayTradeTable.setFillsViewportHeight(true);
		}
		
		
		JPanel result = new JPanel();
		result.setLayout((new BoxLayout(result, BoxLayout.Y_AXIS)));
		
		result.add(this.scrollPaneWithTradesTable);
		
		JPanel dayTradesPanel = new JPanel(new BorderLayout(3, 3));
		dayTradesPanel.add(this.totalSummaryLabel, BorderLayout.NORTH);
		dayTradesPanel.add(this.calculateDayTradeButton, BorderLayout.SOUTH);
		result.add(dayTradesPanel);
		
		result.add(this.scrollPaneWithDayTradeTable);
		
		return result;
    }
	
	private Throwable getRootCause(Throwable t) {
		if (t.getCause() == null) {
			return t;
		} else {
			if (t.getCause() == t) {
				return t;
			} else {
				return getRootCause(t.getCause());
			}
		}
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.selectFileButton) {
			
			JFileChooser fileChooser = new JFileChooser("C:\\Users\\Ruben\\Downloads\\Ninja Trader");
        	
            int returnVal = fileChooser.showOpenDialog(ParseAndUploadPanel.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                this.fileSelectedTextField.setText(file.getAbsolutePath());
                
            } 
            
		} else if (e.getSource() == this.saveTradesButton) {
			
			try {
				TradeTableModel model = (TradeTableModel) this.tradesTable.getModel();
				if (model.getTrades().isEmpty())  {
					return;
				}
				
				// If there are trades ask if we want to override them
				if ( tradesService.tradesExistForDay(1, model.getTrades().iterator().next().getStartDate()) ) {
					
					int n = JOptionPane.showConfirmDialog(
						    this,
						    "Trades exist for this day on the database.\n"
								    + "Do you want to override them?",
						    "Trades exist on DB",
						    JOptionPane.YES_NO_OPTION);
					
					if (n == 0) { // YES. Delete existing and save new
						
						tradesService.deleteTradesForDay(1, model.getTrades().iterator().next().getStartDate());
						saveTrades(model);
						
					} else { // NO. Just exit
						return;
					}
					
				} else {
					
					saveTrades(model);
				}
			
				
				
				
			} catch (Exception ex) {
				
				Throwable rootEx = getRootCause(ex);
				
				StringBuilder sb = new StringBuilder(rootEx.getMessage())
					.append("\n\nStacktrace:\n").append(rootEx.getClass().getName())
					.append(rootEx.getMessage()).append("\n");
				
				StackTraceElement[] stackTraceElements = rootEx.getStackTrace();
				for (StackTraceElement stackElement : stackTraceElements) {
					sb.append(TEN_SPACES).append("at ").append(stackElement.toString()).append("\n");
				}
				textArea.setText(sb.toString());
				
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						scrollPaneWithStackTrace.getVerticalScrollBar().setValue(0);
						CardLayout cl = (CardLayout)(cardsPanel.getLayout());
					    cl.show(cardsPanel, STACKTRACEPANEL);
					}
					
				});
				
				
			}
			
		} else if (e.getSource() == this.parseTradesButton) {
			
			if (this.scrollPaneWithStackTrace.isVisible()) { // if the visble panel is the stacktrace, just show the table again
				
				CardLayout cl = (CardLayout)(cardsPanel.getLayout());
			    cl.show(cardsPanel, TABLESPANEL);
			    
			} else {
			
				// Fill trades table
				{
					TradeTableModel model = (TradeTableModel) this.tradesTable.getModel();//setModel(new TradeTableModel(tradesList()));
					model.getTrades().clear();
					List<Trade> trades = tradesService.readTradesFromFile(fileSelectedTextField.getText());
					// Set user to Ruben
					for (Trade trade : trades) {
						trade.setUserId(1);
					}
					model.getTrades().addAll(trades);
					model.fireTableChanged(new TableModelEvent(model));
					
					// Fill total summary label
					{
						fillTotalSummaryLabel(tradesService.calculateDayTrade(trades));
					}
				}
				
				
			}
	
		    
		} else if (e.getSource() == this.calculateDayTradeButton) {
			
			TradeTableModel model = (TradeTableModel) this.tradesTable.getModel();
			if (model.getTrades().isEmpty())  {
				return;
			}
	    	
	    	List<Trade> trades = new ArrayList<>(model.getTrades());
	    	DayTrade dayTrade = tradesService.calculateDayTrade(trades);
			
			// Fill day trade table 
		    {
		    	
				DayTradeTableModel dayTradeTableModel = (DayTradeTableModel) this.dayTradeTable.getModel();
				dayTradeTableModel.getDayTrades().clear();
				dayTradeTableModel.getDayTrades().add(dayTrade);
				dayTradeTableModel.fireTableChanged(new TableModelEvent(dayTradeTableModel));
				
		    }
		    
			
		} else {
			
			throw new IllegalStateException();
			
		}
		
	}

	private void fillTotalSummaryLabel(DayTrade dayTrade) {
	    this.totalSummaryLabel.setText(String.format(
	    		"  Total Operations: %d  |  Possitive: %d  |  Negative: %d  | Total P&L: %.2f", 
	    		dayTrade.getTotalNegativeOperations() + dayTrade.getTotalPositiveOperations(),
	    		dayTrade.getTotalPositiveOperations(),
	    		dayTrade.getTotalNegativeOperations(),
	    		dayTrade.getTotalProfitLoss())
	    );
    }

	private void saveTrades(TradeTableModel tradesTableModel) {
		
	    List<Trade> trades = new ArrayList<>(tradesTableModel.getTrades());
		tradesService.saveTradesToDB(trades);
	    
		tradesTableModel.getTrades().clear();
	    tradesTableModel.fireTableChanged(new TableModelEvent(tradesTableModel));
	    
	    // Clear totals label
	    this.totalSummaryLabel.setText("");
	    
	    // Clear the day trade table 
	    DayTradeTableModel dayTradeTableModel = (DayTradeTableModel) this.dayTradeTable.getModel();
	    dayTradeTableModel.getDayTrades().clear();
	    dayTradeTableModel.fireTableChanged(new TableModelEvent(tradesTableModel));
	    
	    // Update the statistics panel
	    this.statisticsPanel.loadDayTrades();
	    
    }
	
	
	public DayTrade newDayTrade() {
		DayTrade dayTrade = new DayTrade();
		
		dayTrade.setDate(new Date());
		
		dayTrade.setT1PossitiveOperations(2);
		dayTrade.setT1PossitivePoints(3.25D);
		dayTrade.setT1NegativeOperations(1);
		dayTrade.setT1NegativePoints(1.5);
		
		dayTrade.setT2PossitiveOperations(1);
		dayTrade.setT2PossitivePoints(1.5D);
		dayTrade.setT2NegativeOperations(2);
		dayTrade.setT2NegativePoints(3);
		
		dayTrade.setT3PossitiveOperations(1);
		dayTrade.setT3PossitivePoints(1.25);
		dayTrade.setT3NegativeOperations(0);
		dayTrade.setT3NegativePoints(0);
		
		return dayTrade;
		
		
	}
	
//	public List<Trade> tradesList() {
//		
//		Trade trade1 = new Trade();
//		trade1.setUserId(1);
//		trade1.setInstrument("ES"); 
//		trade1.setStartDate(new Date());
//		trade1.setEndDate(new Date()); 
//		trade1.setNoOfContracts(1);
//		trade1.setStartPrice(100.75D);
//		trade1.setEndPrice(100D);
//		trade1.setType("T1");
//		trade1.setEntry("F62");
//		trade1.setZone("Z1");
//		trade1.setProfitLoss(-1.5D);
//		trade1.setCreatedDate(new Date());
//		
//		Trade trade2 = new Trade();
//		trade2.setUserId(1);
//		trade2.setInstrument("ES"); 
//		trade2.setStartDate(new Date());
//		trade2.setEndDate(new Date()); 
//		trade2.setNoOfContracts(-1);
//		trade2.setStartPrice(100D);
//		trade2.setEndPrice(100D);
//		trade2.setType("T2");
//		trade2.setEntry("RET");
//		trade2.setZone("Z2");
//		trade2.setProfitLoss(2D);
//		trade2.setCreatedDate(new Date());
//		
//		Trade trade3 = new Trade();
//		trade3.setUserId(1);
//		trade3.setInstrument("ES"); 
//		trade3.setStartDate(new Date());
//		trade3.setEndDate(new Date()); 
//		trade3.setNoOfContracts(1);
//		trade3.setStartPrice(100D);
//		trade3.setEndPrice(100D);
//		trade3.setType("T3");
//		trade3.setEntry("RM");
//		trade3.setZone("BKJ-1");
//		trade3.setProfitLoss(4D);
//		trade3.setCreatedDate(new Date());
//		
//		return Arrays.asList(trade1, trade2, trade3);
//	}
	
	private class ContractsColumnCellRenderer extends DefaultTableCellRenderer {
	
        private static final long serialVersionUID = 3013396567703545390L;

		public ContractsColumnCellRenderer() {
			setHorizontalAlignment( JLabel.CENTER );
		}

		@Override
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

			//Cells are by default rendered as a JLabel.
			JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

			//Get the status for the current row.
			TradeTableModel tableModel = (TradeTableModel) table.getModel();
			if (((int)tableModel.getValueAt(row, col)) > 0) {
				l.setForeground(new Color(23, 156, 63)); // nice green
				// also set the + sign before
				l.setText("+" + l.getText());
			} else {
				l.setForeground(Color.RED);
			}
			
			// Set bold
			Font f = l.getFont();
			// bold
			l.setFont(f.deriveFont(f.getStyle() | Font.BOLD));


			//Return the JLabel which renders the cell.
			return l;

		}
	}


}
