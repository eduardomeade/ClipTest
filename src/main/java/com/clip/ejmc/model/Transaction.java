package com.clip.ejmc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

/**
 * Transaction POJO
 * @author mjmeade
 *
 */

@JsonPropertyOrder({ "transaction_id", "amount","description","date","user_id" })
public class Transaction {

	@JsonProperty("transaction_id") 
	private String transactionId;
	
	@JsonProperty("amount") 
	private float amount;
	
	@JsonProperty("description") 
	private String description;
	
	@JsonProperty("date") 
	private Date date;
	
	@JsonProperty("user_id")
	private int userId;
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString(){
		return getTransactionId() + ", "+ getAmount() + ", "+ getDescription() + ", "+
				getDate() + ", "+ getUserId();
	}
}
