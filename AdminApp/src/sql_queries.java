import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class sql_queries {
	private static Connection sqlconnection = null;
	private static ResultSet sqlresult=null;
	private static Statement sqlstatement=null;
	
	public void db_table(Table table , String Query){ // queries are sent to the database and displayed on the jtable.
		try {
			
			Class.forName("org.sqlite.JDBC");
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
			sqlstatement=sqlconnection.createStatement();

			sqlresult=sqlstatement.executeQuery(Query);

			ResultSetMetaData tabledata = sqlresult.getMetaData();// stores the number of columns in the table
			int cols = tabledata.getColumnCount();
	    ArrayList<String>colname = new ArrayList<String>();
	  
			 for (int i = 1; i <=cols; i++) {// adding the colnames to the list
			colname.add(tabledata.getColumnName(i));
		
		        }
		     
		     for(int k = 0; k<colname.size();k++){ // adding the colnames from the list to the table
		    		TableColumn column = new TableColumn(table, SWT.NONE);
		            column.setText(colname.get(k));
		            table.getColumn(k).pack();
		     }
		     
		     
		     while(sqlresult.next()){// adding the row data to the table

		    	 TableItem tblrows = new TableItem(table, SWT.NONE);// creating new row
		    	  
		    	 for (int j = 1 ; j<=cols ; j++){// adding the data to the rows
		            tblrows.setText(j-1,sqlresult.getString(j));// row data under col 1
		          
		    	 
		     }
		  
		     
		     for (int i=0; i<colname.size(); i++) {
		    	 table.getColumn (i).pack ();
		        }    
			     
		     }


			sqlstatement.close();
			sqlconnection.close();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete_add_update(String execute){
		try {
			Class.forName("org.sqlite.JDBC");

			
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
			sqlconnection.setAutoCommit(false);


			sqlstatement= sqlconnection.createStatement();

			sqlstatement.executeUpdate(execute);// executing query

			System.out.println(execute);// debugg ; testing to see if the query is correct

			sqlstatement.close();// closing statement
			sqlconnection.commit();
			sqlconnection.close(); //closing connection to database


		}

		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

}
