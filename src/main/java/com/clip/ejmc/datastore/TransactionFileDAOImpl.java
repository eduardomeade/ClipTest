package com.clip.ejmc.datastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;

import com.clip.ejmc.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TransactionDAO implementation for using files.
 * 
 * This class will handle opening and closing the transactions file
 * and internally will add the transactions to a mapping structure.
 * 
 * The mapping structure will to have a top level map with userId as
 * key, and its values will be another map with transactionId as key
 * 
 * @author mjmeade
 *
 */
public class TransactionFileDAOImpl implements TransactionDAO {

	private HashMap<Integer, HashMap<String, Transaction>> transactions;
	private static final String FILENAME = "transactions.txt";

	private boolean ReadTransactionsFile() {

		boolean retVal = false;
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
		
			File file = new File(FILENAME);
			if (file.exists()) {
				
				fr = new FileReader(file.getAbsoluteFile());
				br = new BufferedReader(fr);
				transactions = new HashMap<Integer, HashMap<String, Transaction>>();
				
				String line;
				while ((line = br.readLine()) != null) {
					
					ObjectMapper objectMapper = new ObjectMapper();
					final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					objectMapper.setDateFormat(df);

					Transaction t = objectMapper.readValue(line, Transaction.class);
					int userId = t.getUserId();

					HashMap<String, Transaction> mt = null;
					if (!transactions.containsKey(userId)) {
			
						mt = new HashMap<String, Transaction>();
					
					} else {
					
						mt = transactions.get(userId);
					
					}
					
					mt.put(t.getTransactionId(), t);
					transactions.put(userId, mt);

				}

				br.close();
				retVal = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try {
	
				if (br != null)
					br.close();
	
				if (fr != null)
					fr.close();
	
			} catch (IOException ex) {
	
				ex.printStackTrace();
			}
		}
		return retVal;

	}
	
	private List<Transaction> GetUserTransactionsHelper(int userId) {

		if (transactions.containsKey(userId)) {
			
			HashMap<String, Transaction> userTransactions = transactions.get(userId);
			
			List<Transaction> transactionList = new ArrayList<Transaction>();
			
			for (Transaction t : userTransactions.values()) {
				transactionList.add(t);
			}
			
			return transactionList;
		}

		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.clip.ejmc.datastore.TransactionDAO#saveTransaction(com.clip.ejmc.model.Transaction)
	 */
	public void saveTransaction(Transaction t) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			String uniqueID = UUID.randomUUID().toString();
			t.setTransactionId(uniqueID);

			ObjectMapper objectMapper = new ObjectMapper();
			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			objectMapper.setDateFormat(df);
			
			String data = objectMapper.writeValueAsString(t);

			File file = new File(FILENAME);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(data + "\n");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}



	/* (non-Javadoc)
	 * @see com.clip.ejmc.datastore.TransactionDAO#getTransaction(int, java.lang.String)
	 */
	public Transaction getTransaction(int userId, String transactionId) {

		Transaction t = null;
		if (ReadTransactionsFile()) {
			if (transactions.containsKey(userId)) {
				HashMap<String, Transaction> userTransactions = transactions
						.get(userId);

				if (userTransactions.containsKey(transactionId)) {
					t = userTransactions.get(transactionId);
				}
			}
		}
		return t;
	}

	/* (non-Javadoc)
	 * @see com.clip.ejmc.datastore.TransactionDAO#getTransactionsByUser(int)
	 */
	public List<Transaction> getTransactionsByUser(int userId) {

		List<Transaction> transactions = null;
		
		if (ReadTransactionsFile())
		{
			transactions = GetUserTransactionsHelper(userId);

			if (transactions != null)
			{
				Collections.sort(transactions, new Comparator<Transaction>() {
				    @Override
				    public int compare(Transaction o1, Transaction o2) {
				        return o1.getDate().compareTo(o2.getDate());
				    }
				});

			}

			
		}

		return transactions;

	}

	/* (non-Javadoc)
	 * @see com.clip.ejmc.datastore.TransactionDAO#getTransactionSumByUser(int)
	 */
	public float getTransactionSumByUser(int userId) {

		float sum = 0.0f;
		if (ReadTransactionsFile()) {
			List<Transaction> userTransactions = GetUserTransactionsHelper(userId);

			if (userTransactions != null) {

				for (Transaction t : userTransactions) {
					sum += t.getAmount();
				}
			}
		} else {
			sum = Float.NaN;
		}
		return sum;
	}

	

}
