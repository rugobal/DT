package com.rugobal.dt.services.ninja;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Order {
	private String instrument;
	private ActionType action;
	private OrderType type;
	private Integer quantity;
	private OrderState state;
	private Integer filledQuantity;
	private Double avgPrice;
	private String name;
	private Date time;
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
//	static {
//		TimeZone timeZone = TimeZone.getTimeZone("UTC");
//        sdf.setTimeZone(timeZone);
//	}
	
	
	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public ActionType getAction() {
		return action;
	}
	
	public void setAction(String action) throws Exception {
		if ("Buy".equalsIgnoreCase(action)) {
			this.action = ActionType.BUY;
		} else if ("Sell".equalsIgnoreCase(action)) {
			this.action = ActionType.SELL;
		} else {
			throw new Exception("Unknown action: " + action);
		}
	}
	
	public OrderType getType() {
		return type;
	}
	
	public void setType(String type) throws Exception {
		if ("Market".equalsIgnoreCase(type)) {
			this.type = OrderType.MARKET;
		} else if (type.contains("Limit")) {
			this.type = OrderType.LIMIT;
		} else if ("Stop".equalsIgnoreCase(type)) {
			this.type = OrderType.STOP;
		} else {
			throw new Exception("Unknown type: " + type);
		}
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public OrderState getState() {
		return state;
	}
	
	public void setState(String state) throws Exception {
		if ("Filled".equalsIgnoreCase(state)) {
			this.state = OrderState.FILLED;
		} else if ("Cancelled".equalsIgnoreCase(state)) {
			this.state = OrderState.CANCELED;
		} else {
			throw new Exception("Unknown state: " + state);
		}
	}
	
	public Integer getFilledQuantity() {
		return filledQuantity;
	}
	
	public void setFilledQuantity(Integer filledQuantity) {
		this.filledQuantity = filledQuantity;
	}
	
	public Double getAvgPrice() {
		return avgPrice;
	}
	
	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		
		this.time = time;
	}
	
	public void setTime(String time) throws ParseException {
		this.time = sdf.parse(time);
	}
	
	/**
	 * If the order quantity of contracts is more than one it create as many
	 * identical orders as number of contracts with quantity = 1.
	 * @return
	 */
	public List<Order> expand() {
		if (this.quantity == 1) {
			return Arrays.asList(this);
		} else {
			List<Order> result = new ArrayList<>();
			for (int i=0; i<this.quantity; i++) {
				Order copy = this.copy();
				copy.quantity = 1;
				result.add(copy);
			}
			return result;
		}
	}

	private Order copy() {
		Order copy = new Order();
		copy.instrument = this.instrument;     
		copy.action = this.action;     
		copy.type = this.type;        
		copy.quantity = this.quantity;      
		copy.state = this.state;      
		copy.filledQuantity = this.filledQuantity;
		copy.avgPrice = this.avgPrice;       
		copy.name = this.name;           
		copy.time = this.time;   
		return copy;
	}

	@Override
	public String toString() {
		return "Order [instrument=" + instrument + ", action=" + action
				+ ", type=" + type + ", quantity=" + quantity + ", state="
				+ state + ", filledQuantity=" + filledQuantity + ", avgPrice="
				+ avgPrice + ", name=" + name + ", time=" + time + "]";
	}
	
	
	
}
