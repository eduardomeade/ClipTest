package com.clip.ejmc.app;

import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.clip.ejmc.datastore.TransactionDAO;
import com.clip.ejmc.datastore.TransactionFileDAOImpl;
import com.clip.ejmc.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TransactionApp {

	private static String printTransaction(Transaction t) {
		String s = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			objectMapper.setDateFormat(df);
			s = objectMapper.writeValueAsString(t);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return s;
	}

	public static void main(String[] args) throws IOException {

		int userId = 0;
		String operation = "";
		String transactionArg = "";
		
		try {	
			

			userId = Integer.parseInt(args[0]);
			operation = args[1];
			if (args.length == 3)
			{
				transactionArg = args[2];
			}


		}
		catch (ArrayIndexOutOfBoundsException e){
			System.err.println("Incorrect number of arguments");
			System.exit(1);
		}catch (NumberFormatException e) {
			System.err.println("First argument (user_id) must be a number");
			System.exit(1);
		}



		try {
			TransactionDAO ds = new TransactionFileDAOImpl();
			
			if (operation.equals("add")) {

				ObjectMapper objectMapper = new ObjectMapper();
				final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				objectMapper.setDateFormat(df);
				
				Transaction transaction = objectMapper.readValue(transactionArg,Transaction.class);

				if (transaction.getUserId() != userId){

					System.err.println("User id ("+ userId +")argument does not match with the one in the transaction("+transaction.getUserId()+")");
					System.exit(1);
				}

				ds.saveTransaction(transaction);

				System.out.println(printTransaction(transaction));
				
			} else if (operation.equals("list")) {

				List<Transaction> transactionList = ds
						.getTransactionsByUser(userId);

				if (transactionList != null && !transactionList.isEmpty()) {

					ObjectMapper objectMapper = new ObjectMapper();
					final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
					objectMapper.setDateFormat(df);
					String s = objectMapper.writeValueAsString(transactionList.toArray());
					System.out.println(s);

				} else {

					System.out.println("No transactions found for user: "
							+ userId);

				}

			} else if (operation.equals("sum")) {

				float sum = ds.getTransactionSumByUser(userId);
				
				if (Float.isNaN(sum)) {
					System.out.println("No transactions found for user: "
							+ userId);
				} else {
					System.out.println( "{\"user_id\":" + userId + ", \"sum\":" + sum +"}");

				}

			} else {
				
				/*in this case the specification for the second argument is that
				 * it can be a transaction ID, so we use a new variable for clarity*/
				
				String transactionId = operation;

				Transaction t = ds.getTransaction(userId, transactionId);
				if (t != null) {
					System.out.println(printTransaction(t));
				} else {
					System.out.println("Transaction: " + transactionId
							+ " for user: " + userId + " not found");
				}

			}
		} catch (JsonMappingException e) {
			System.err.println("Transaction being added is incorrect");
			System.exit(1);
		}

	}

}
