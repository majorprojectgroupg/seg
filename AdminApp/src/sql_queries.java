import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
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


			System.out.println(tabledata.getColumnCount());



			while(sqlresult.next()){// adding the row data to the table

				TableItem tblrows = new TableItem(table, SWT.NONE);// creating new row

				for (int j = 0 ; j<table.getColumnCount() ; j++){// adding the data to the rows
					tblrows.setText(j,sqlresult.getString(j+1));// row data under col 1
					System.out.println(sqlresult.getString(j+1));

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

	public void tranfersql(){// this will allow us to check if the questionnaire that has been selected is already been done by the selected patient
		String patid= transfer.txt_pid.getText();
		String questn= transfer.txt_quest_name.getText();
	 	MessageBox messages;
		String compquestion = null;
		String status = null;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

			sqlstatement=sqlconnection.createStatement();


			sqlresult=sqlstatement.executeQuery("SELECT * FROM Results WHERE Patient_ID='"+patid+"' AND Questionnaire_Name='"+questn+"'");

			if(sqlresult.next()){

				compquestion=(sqlresult.getString("Questionnaire_Name").toString());

				status=(sqlresult.getString("Status").toString());

			}

			sqlstatement.close();// closing statement

			sqlconnection.close();
			
		} catch (SQLException sqlex) {
			// TODO Auto-generated catch block
			sqlex.printStackTrace();
		} catch (ClassNotFoundException catche) {
			// TODO Auto-generated catch block
			catche.printStackTrace();
		}
		if (questn.equals(compquestion) && status.equals("Completed")){
			messages= new MessageBox(transfer.shell,SWT.ICON_WARNING|SWT.OK);
			messages.setText("Alert!");
			messages.setMessage("Patient has Already completed this Questionnaire");
			messages.open();
		}
		else if(questn.equals(compquestion) && status.equals("In Progress")){
			messages= new MessageBox(transfer.shell,SWT.ICON_WARNING|SWT.OK);
			messages.setText("Alert!");
			messages.setMessage("Patient is currently attemping the questionnaire");
			messages.open();
			
		}
		else{
			
			// Decides which adb executable to run depending on the client's OS.
			String adbfile = "";
			String nameOfOS = System.getProperty("os.name");
			
			if(nameOfOS.contains("Mac") || nameOfOS.contains("Linux"))
			{
				adbfile = "./adb/adb";
				// Debug purposes.
				System.out.println("The Operating System is: " + System.getProperty("os.name"));
			}
			else if(nameOfOS.contains("Windows"))
			{
				adbfile="./adb/adb.exe";
				// Debug purposes.
				System.out.println("The Operating System is: " + System.getProperty("os.name"));
			}
			
			// All done ;)
			
			String outputfile="sdcard/Download/questionnaire.json";
			String devices = null;


			StringBuilder stringbuild = new StringBuilder();
			BufferedReader data;
			String readsymbols = null;



			try {
				FileReader in = new FileReader(transfer.questionnaire_location);

				data = new BufferedReader(in);

				while((readsymbols=data.readLine()) != null)
				{
					// Add the current line to the StringBuilder.
					stringbuild.append(readsymbols);

					// Debug purposes.
				}
				data.close();
			} catch (IOException catchexp) {
				// TODO Auto-generated catch block
				catchexp.printStackTrace();
			}

			System.out.println(stringbuild.toString());
			String x = stringbuild.toString();
			

			String additionaldata= ",\"details\": [{ \"patid\":\""+transfer.txt_pid.getText()+"\", \"fname\":\""+transfer.fname+"\", \"lname\":\""+transfer.lname+"\",  \"dob\":\""+transfer.dob+"\", \"questionnaire_name\":\""+transfer.txt_quest_name.getText().toString()+"\", \"questionmaire_id\":\""+transfer.txt_questID.getText().toString()+"\"}] }";

			if(x.endsWith("}")){
				x= x.substring(0, x.length()-1);


				try {
					String Transferdata = x+additionaldata;
					File tempfile = new File("./Temp/questionnaire.json");

					FileWriter writer = new FileWriter(tempfile);
					writer.write(Transferdata);
					writer.close();


					Process process = Runtime.getRuntime().exec(adbfile+" push "+ "./Temp/questionnaire.json"+" "+outputfile);// transfer the file

					BufferedReader breader = new BufferedReader(new InputStreamReader(process.getErrorStream()));  
					String line;

					while ((line = breader.readLine()) != null) {  
						devices =line;// to check for error messages
					}
					System.out.println(devices);

					if(devices.contains("error")){
						messages = new MessageBox(transfer.shell,SWT.ICON_ERROR|SWT.OK);
						messages.setMessage( devices);
						messages.setText("Error");
						messages.open();
					
					}

					else{
						// referencing edituser class to access the update delete method ..

						String InsertQuery ="INSERT INTO Results(Questionnaire_Name,Patient_Name,Status,Questionnaire_ID,Patient_ID, File_Location) VALUES('"+transfer.txt_quest_name.getText()+"','"+transfer.txt_pname.getText()+"','In Progress','"+transfer.txt_questID.getText()+"','"+transfer.txt_pid.getText()+"','./results');";
						delete_add_update(InsertQuery);// sending it to the update_delete method.to execute the query
						String query2= "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','The User Transfered "+" "+transfer.txt_quest_name.getText()+" Questionnaire for the patient "+transfer.txt_pname.getText()+" to complete')";
						delete_add_update(query2);
					
						
						messages = new MessageBox(transfer.shell,SWT.ICON_INFORMATION|SWT.OK);
						messages.setMessage("File was Sucessfully transfered");
						messages.setText("Success!");
						messages.open();
						transfer.cleartxtfields();
					}


				} catch (IOException catchexp) {
					//do stuff with exception
					catchexp.printStackTrace();
				}


			}
			}
		


	}
}
