package com.rugobal.dt.services.ninja;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.rugobal.dt.entities.model.Trade;

public class NinjaTradeParser {
	
	private Stack<Order> ordersStack = new Stack<Order>();
    private Stack<Order> buyStack = new Stack<Order>();
    private Stack<Order> sellStack = new Stack<Order>();
    

    public List<Trade> parseTrades(String ordersFilePath) {
		parseOrdersFile(ordersFilePath);
        List<Trade> tradesList = generateTrades();

        return tradesList;
	}
    
    private void parseOrdersFile(String absPathToFile)  {
        
        try(BufferedReader input = new BufferedReader(new FileReader(absPathToFile))) {
			
			List<Order> ordersList = new ArrayList<Order>();

			for(String line; (line = input.readLine()) != null; ) {
				
				if (line.lastIndexOf("Instrument") != -1) {
					continue;
				}
				
				Order order = decodeOrder(line);
				ordersList.addAll(order.expand());
//                ordersStack.push(order); 
			}
			
			for (int i=ordersList.size()-1; i >= 0; i--) {
				ordersStack.push(ordersList.get(i));  
			}
			
        }catch (Exception ex) {
			// convert to runtime
			throw new IllegalStateException(ex);
		}
              
		
    }
    
    private List<Trade> generateTrades() {
    	List<Trade> tradesList = new ArrayList<Trade>();
    	
    	while (!ordersStack.isEmpty()) {
    		Order order = ordersStack.pop();
    		Trade lastTrade = tradesList.size() == 0 ? null : tradesList.get(tradesList.size()-1);
    		
    		if (!order.getState().equals(OrderState.FILLED)) {
    			continue;
    		}
    		
    		if (order.getAction().equals(ActionType.BUY)) {
    			if (!sellStack.isEmpty()) {
    				Trade shortTrade = createShortTrade(order);
    				if (isSameAsLastTrade(shortTrade, lastTrade)) {
    					if (!lastTrade.getEndPrice().equals(shortTrade.getEndPrice())) {
    						lastTrade.setComment(String.format("%.2f + %.2f", calculateProfitLoss(lastTrade), calculateProfitLoss(shortTrade)));
    					}
    					lastTrade.setProfitLoss(calculateProfitLoss(lastTrade) + calculateProfitLoss(shortTrade));
    					lastTrade.setNoOfContracts(lastTrade.getNoOfContracts() - 1);
    					lastTrade.setEndPrice(Math.min(lastTrade.getEndPrice(), shortTrade.getEndPrice()));
    					lastTrade.setEndDate(new Date(Math.max(lastTrade.getEndDate().getTime(), shortTrade.getEndDate().getTime())));
    				} else {
    					tradesList.add(shortTrade);
    				}
    			} else {
    				buyStack.push(order);
    			}
    		} else { // action == SELL
    			if (!buyStack.isEmpty()) {
    				Trade longTrade = createLongTrade(order);
    				if (isSameAsLastTrade(longTrade, lastTrade)) {
    					if (!lastTrade.getEndPrice().equals(longTrade.getEndPrice())) {
    						lastTrade.setComment(String.format("%.2f + %.2f", calculateProfitLoss(lastTrade), calculateProfitLoss(longTrade)));
    					}
    					lastTrade.setProfitLoss(calculateProfitLoss(lastTrade) + calculateProfitLoss(longTrade));
    					lastTrade.setNoOfContracts(lastTrade.getNoOfContracts() + 1);
    					lastTrade.setEndPrice(Math.max(lastTrade.getEndPrice(), longTrade.getEndPrice()));
    					lastTrade.setEndDate(new Date(Math.max(lastTrade.getEndDate().getTime(), longTrade.getEndDate().getTime())));
    				} else {
    					tradesList.add(longTrade);
    				}
    			} else {
    				sellStack.push(order);
    			}
    		}
    		
    		
    	}
    	
    	return tradesList;
    }
    
    private boolean isSameAsLastTrade(Trade trade, Trade lastTrade) {
    	return lastTrade == null ? false :
    		trade.getInstrument().equals(lastTrade.getInstrument())
				&& trade.getStartDate().getTime() == lastTrade.getStartDate().getTime()
				&& trade.getStartPrice().equals(lastTrade.getStartPrice());
	}

	private Trade createLongTrade(Order sellOrder) {
		Order buyOrder = buyStack.pop();
		Trade trade = new Trade();
		trade.setInstrument(buyOrder.getInstrument());
		trade.setStartDate(buyOrder.getTime());
		trade.setEndDate(sellOrder.getTime());
		trade.setNoOfContracts(1);
		trade.setStartPrice(buyOrder.getAvgPrice());
		trade.setEndPrice(sellOrder.getAvgPrice());
		trade.setProfitLoss(calculateProfitLoss(trade));
		return trade;
	}

	
	private Trade createShortTrade(Order buyOrder) {
		Order sellOrder = sellStack.pop();
		Trade trade = new Trade();
		trade.setInstrument(sellOrder.getInstrument());
		trade.setStartDate(sellOrder.getTime());
		trade.setEndDate(buyOrder.getTime());
		trade.setNoOfContracts(-1);
		trade.setStartPrice(sellOrder.getAvgPrice());
		trade.setEndPrice(buyOrder.getAvgPrice());
		trade.setProfitLoss(calculateProfitLoss(trade));
		return trade;
	}
	
	private Double calculateProfitLoss(Trade trade) {
		if (trade.getStartPrice().equals(trade.getEndPrice())) {
			return 0D;
		}
		
		if (trade.getNoOfContracts() > 0) {
			// long trade
			return Math.abs(trade.getNoOfContracts()) * (trade.getEndPrice() - trade.getStartPrice());
		} else {
			// short trade
			return Math.abs(trade.getNoOfContracts()) * (- (trade.getEndPrice() - trade.getStartPrice()));
		}
	}


    private Order decodeOrder(String str) throws Exception {
        Order order = new Order();
        
        List<String> tokens = parseLine(str);
        Iterator<String> it = tokens.iterator();
        
        order.setInstrument(it.next()); // Instrument
        order.setAction(it.next());    
        order.setType(it.next());
        order.setQuantity(Integer.parseInt(it.next()));
        it.next(); // Limit
        it.next(); // Stop
        order.setState(it.next());
        order.setFilledQuantity(Integer.parseInt(it.next()));
        order.setAvgPrice(NumberFormat.getInstance().parse(it.next().replace(',', '.')).doubleValue());
        it.next(); // Remaining
        order.setName(it.next());
        it.next(); // OCO
        it.next(); // TIF
        it.next(); // GTD
        it.next(); // Account
        it.next(); // Connection
        it.next(); // ID
        it.next(); // Strategy ID
        it.next(); // Token
        order.setTime(it.next());
        
        return order;
    }
    


    // Break an individual line into tokens. 
    private List<String> parseLine(String curLine)  {
        List<String> result = new ArrayList<String>();
        String firstToken = null;
        String remainderOfLine = curLine;
        
        while (remainderOfLine != null && !remainderOfLine.equals("")) {
            int commaIndex = locateFirstDelimiter(remainderOfLine);
            if (commaIndex > -1) {
                firstToken = remainderOfLine.substring(0, commaIndex).trim();
                remainderOfLine = remainderOfLine.substring(commaIndex+1).trim();
            } else {
                // no commas, so the entire line is the token
                firstToken = remainderOfLine;
                remainderOfLine = null;
            }

            // remove redundant quotes
            firstToken = cleanupQuotes(firstToken);
            
            result.add(firstToken);
        }
        
        return result;

    }
    
    // locate the position of the comma, taking into account that
    // a quoted token may contain ignorable commas.
    private int locateFirstDelimiter(String curLine) {
        char delimiter = ','; // the delimiter of the tokens
        
		if (curLine.startsWith("\"")) {
            boolean inQuote = true;
            int numChars = curLine.length();
            for (int i=1; i<numChars; i++) {
                char curChar = curLine.charAt(i);
                if (curChar == '"') {
                    inQuote = !inQuote;
                } else if (curChar == delimiter && !inQuote) {
                    return i;
                }
            }
            return -1;
        } else {
            return curLine.indexOf(delimiter);
        }
    }
    
    // remove quotes around a token, as well as pairs of quotes
    // within a token.
    private String cleanupQuotes(String token) {
        StringBuffer buf = new StringBuffer();
        int length = token.length();
        int curIndex = 0;

        if (token.startsWith("\"") && token.endsWith("\"")) {
            curIndex = 1;
            length--;
        }

        boolean oneQuoteFound = false;
        boolean twoQuotesFound = false;

        while (curIndex < length) {
            char curChar = token.charAt(curIndex);
            if (curChar == '"') {
                twoQuotesFound = (oneQuoteFound) ? true : false;
                oneQuoteFound = true;
            } else {
                oneQuoteFound = false;
                twoQuotesFound = false;
            }

            if (twoQuotesFound) {
                twoQuotesFound = false;
                oneQuoteFound = false;
                curIndex++;
                continue;
            }

            buf.append(curChar);
            curIndex++;
        }

        return buf.toString();
    }
    


//    private void writeToXLS(List<Trade> tradesList, String xlsPath) {
//    	try
//        {
//            //Excel document to be imported
////            WorkbookSettings ws = new WorkbookSettings();
////            ws.setLocale(new Locale("en", "EN"));
////            Workbook wRead = Workbook.getWorkbook(new File(xlsPath),ws);
//            WritableWorkbook w = Workbook.createWorkbook(new File(xlsPath));
//            // Gets the sheets from workbook
////            WritableSheet s = w.getSheet(1);
////            int startLine = -1;
////            Cell[] row = null;
////            for (int i = 3 ; i < s.getRows() ; i++) {
////            	row = s.getRow(i);
////            	if (row[3].getContents() == null || "".equals(row[3].getContents())) {
////            		startLine = i;
////            		break;
////            	}
////            }
//            
//            WritableSheet s = w.createSheet("1", 0);
//            // Lets create a times font
//    		WritableFont arial12pt = new WritableFont(WritableFont.ARIAL, 12);
//    		// Define the cell format
//    		WritableCellFormat  cellFormat = new WritableCellFormat(arial12pt);
//    		// Lets automatically wrap the cells
//    		cellFormat.setWrap(true);
//    		
//    		
//            // write trades to excel
//            DateTime dateTime = null;
//            Number number = null;
//            int rowIndex = 0;
//            for (Trade trade : tradesList) {
//            	// date
//            	dateTime = new DateTime(0, rowIndex, trade.getDate().getDate());
//            	s.addCell(dateTime);
////            	dateTime.setCellFormat(cellFormat);
//            	
//            	// start time
//            	dateTime = new DateTime(1, rowIndex, trade.getStartTime().getDate());
//            	s.addCell(dateTime);
////            	dateTime.setCellFormat(cellFormat);
//            	
//            	// no of contracts
//            	number = new Number(2, rowIndex, trade.getNoOfContracts());
//            	s.addCell(number);
////            	number.setCellFormat(cellFormat);
//            	
//            	// start price
//            	number = new Number(3, rowIndex, trade.getStartPrice());
//            	s.addCell(number);
////            	number.setCellFormat(cellFormat);
//            	
//            	// end price
//            	number = new Number(4, rowIndex, trade.getEndPrice());
//            	s.addCell(number);
////            	number.setCellFormat(cellFormat);
//            	
//            	// end time
//            	dateTime = new DateTime(5, rowIndex, trade.getEndTime().getDate());
//            	s.addCell(dateTime);
////            	dateTime.setCellFormat(cellFormat);
//            	
//            	// profit loss
//            	number = new Number(6, rowIndex, trade.getProfitLoss());
//            	s.addCell(number);
////            	number.setCellFormat(cellFormat);
//            	
//            	
//            	rowIndex++;            	
//            }
//            
//            w.write();
//    		w.close();
//            
//
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            System.err.println(e.toString());
//        }
//        catch (IOException e)
//        {
//            System.err.println(e.toString());
//        }
//        catch (Exception e)
//        {
//            System.err.println(e.toString());
//        }
//    }

//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        
//    	final String DEFAULT_ORDERS_DIR = "C:\\Users\\Ruben\\Downloads\\Ninja Trader\\";
//    	final String DEFAULT_ORDERS_FILE = "Orders.csv";
//    	final String DEFAULT_RESULTS_DIR = "C:\\Users\\Ruben\\Desktop\\";
//    	final String DEFAULT_RESULTS_FILE = "Trades.xls";
//    	
//    	String ordersFilePath = DEFAULT_ORDERS_DIR + DEFAULT_ORDERS_FILE;
//    	if (args.length > 0 && args[0] != null) {
//    		String arg0 = args[0];
//    		if (arg0.lastIndexOf("c:") != -1) {
//    			ordersFilePath = arg0;
//    		} else {
//    			ordersFilePath = DEFAULT_ORDERS_DIR + "\\" + arg0;
//    		}
//    	}
//    	
//    	String resultsFilePath = DEFAULT_RESULTS_DIR + DEFAULT_RESULTS_FILE;
//    	if (args.length > 1 && args[1] != null) {
//    		String arg1 = args[1];
//    		if (arg1.lastIndexOf("c:") != -1) {
//    			resultsFilePath = arg1;
//    		} else {
//    			resultsFilePath = DEFAULT_RESULTS_DIR + "\\" + arg1;
//    		}
//    	}
//    	
//        try {
//            parseOrdersFile(ordersFilePath);
//            List<Trade> tradesList = generateTrades();
//            writeToXLS(tradesList, resultsFilePath);
//        }
//        catch (Exception e) {
//            System.out.println("Exception: ");
//            e.printStackTrace();
//        }
//    
//    }

}

