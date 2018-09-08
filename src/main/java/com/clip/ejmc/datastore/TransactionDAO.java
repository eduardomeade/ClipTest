package com.clip.ejmc.datastore;

import java.util.List;

import com.clip.ejmc.model.Transaction;

/**
 * Data Access Object Interface for transactions.
 * @author mjmeade
 *
 */
public interface TransactionDAO {

	/**
	 * Save a transaction to the data store
	 * 
	 * @param t transaction to be saved
	 */
	public void saveTransaction( Transaction t );
	
	/**
	 * Retrieve a transaction based on usedId and transactionId
	 * 
	 * @param userId
	 * @param transactionId
	 * @return the requested transaction or null if not found
	 */
	public Transaction getTransaction(int userId, String transactionId);
	
	
	/**
	 * Get all transactions by specified userID
	 * 
	 * @param userId
	 * @return Transaction list if found, null otherwise
	 */
	public List<Transaction> getTransactionsByUser( int userId);
	
	
	/**
	 * Get the sum of the amounts of all transactions by userdId
	 * 
	 * @param userId
	 * @return the amount of the sum or NaN if the user was not found.
	 */
	public float getTransactionSumByUser( int userId );
}
