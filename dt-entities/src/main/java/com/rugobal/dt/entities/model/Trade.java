package com.rugobal.dt.entities.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the as_user database table.
 *
 */
@Entity
@Table(name = "trade")
public class Trade implements Serializable {
	
	private static final long serialVersionUID = 3943062817544400192L;
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
	
//	@Version
//    @Column(name = "version")
//    private int version;
//	
    @Column(name = "dt_user_id")
    private int userId;
    
    @Column(name = "instrument")
    private String instrument;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;
    
    @Column(name = "no_of_contracts")
    private int noOfContracts;
    
    @Column(name = "start_price")
    private Double startPrice;
    
    @Column(name = "end_price")
    private Double endPrice;
    
    @Column(name = "zone")
    private String zone;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "entry")
    private String entry;
    
    @Column(name = "profit_loss")
    private Double profitLoss;
    
    @Column(name = "comment")
    private String comment;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date createdDate;
    
    /**
     * Default Constructor.
     */
    public Trade() {
    }
    
    

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
//	public int getVersion() {
//		return version;
//	}
//	
//	public void setVersion(int id) {
//		this.version = id;
//	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNoOfContracts() {
		return noOfContracts;
	}

	public void setNoOfContracts(int noOfContracts) {
		this.noOfContracts = noOfContracts;
	}

	public Double getStartPrice() {
		return startPrice;
	}
	
	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}
	
	public Double getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(Double endPrice) {
		this.endPrice = endPrice;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Double getProfitLoss() {
		return profitLoss;
	}

	public void setProfitLoss(Double profitLoss) {
		this.profitLoss = profitLoss;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



	@Override
	public String toString() {
		return "Trade [id=" + id + ", userId=" + userId + ", instrument="
				+ instrument + ", startDate=" + startDate + ", endDate="
				+ endDate + ", noOfContracts=" + noOfContracts
				+ ", startPrice=" + startPrice + ", endPrice=" + endPrice
				+ ", zone=" + zone + ", type=" + type + ", entry=" + entry
				+ ", profitLoss=" + profitLoss + ", comment=" + comment
				+ ", createdDate=" + createdDate + "]";
	}
	
	


}
