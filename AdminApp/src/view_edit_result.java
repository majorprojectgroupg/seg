import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.json.simple.JSONObject;


public class view_edit_result {

	protected static Shell shell;
	static File file;
	private static Table Resultstable;
	private Text txt_search;
	private Text txt_questionniare_name;
	private Text txt_qestn_name;
	private Text txt_answer;
	private Text txt_pname;
	static String result_id;
	static List questn_list ;
	private static Connection sqlconnection = null;
	private static ResultSet sqlresult=null;
	private static Statement sqlstatement=null;
	private Label lblBlankLol;
	private static 	MessageBox wmessages, succ_message;
	private static String query= "SELECT Result_ID , Patient_ID , Questionnaire_Name , Patient_Name ,Status FROM Results WHERE Status='Complete' OR Status='Incomplete'";
	public view_edit_result(final Display display) {
		// TODO Auto-generated constructor stub
		
		
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(660, 580);
		shell.setText("SWT Application");
		
	
		
		Group grpResultTable = new Group(shell, SWT.NONE);
		grpResultTable.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpResultTable.setText("Result Table");
		grpResultTable.setBounds(38, 25, 568, 227);
		
		Resultstable = new Table(grpResultTable, SWT.BORDER | SWT.FULL_SELECTION);
		Resultstable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {// this is when the user double clicks on the record....he wishes to see the results of..
			
				questn_list.removeAll();
				Edit_result.hmap_answer=new HashMap<String, String>();
				Edit_result.hmap_status=new HashMap<String, String>();
				txt_answer.setText("");
			     txt_pname.setText("");
				txt_qestn_name.setText("");
				txt_questionniare_name.setText("");
				
				tbl_selection();
				
				new Edit_result().read(file);
			}
		});
		Resultstable.setBounds(10, 41, 548, 173);
		Resultstable.setHeaderVisible(true);
		Resultstable.setLinesVisible(true);
		
		Label lblNewLabel = new Label(grpResultTable, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel.setBounds(183, 20, 80, 15);
		lblNewLabel.setText("Quick Search");
		
		txt_search = new Text(grpResultTable, SWT.BORDER);
		txt_search.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String txt= txt_search.getText().toString();
				
				if(txt.length()==0){
					Resultstable.removeAll();
					new sql_queries().db_table(Resultstable, query);
				}
				else{
				// only selecting records which are complete or incomplete the search will not bring back records which have the status in progress or not attempted 
			String search = " SELECT Result_ID , Patient_ID , Questionnaire_Name , Patient_Name ,Status FROM Results WHERE Result_ID LIKE '%"+txt+"%' OR Patient_ID LIKE '%"+txt+"%' OR Questionnaire_Name LIKE '%"+txt+"%' OR Patient_Name LIKE '%"+txt+"%' EXCEPT SELECT Result_ID , Patient_ID , Questionnaire_Name , Patient_Name ,Status FROM Results WHERE Status ='In Progress';";
				System.out.println(search);
				Resultstable.removeAll();
				new sql_queries().db_table(Resultstable, search);
				}
			}
		});
		txt_search.setBounds(269, 17, 135, 21);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmTools_1 = new MenuItem(menu, SWT.CASCADE);
		mntmTools_1.setText("Tools");
		
		Menu menu_1 = new Menu(mntmTools_1);
		mntmTools_1.setMenu(menu_1);
		
		MenuItem mntmClearAllFields = new MenuItem(menu_1, SWT.NONE);
		mntmClearAllFields.setText("Clear All Fields");
		
		MenuItem mntmExportToA = new MenuItem(menu_1, SWT.NONE);
		mntmExportToA.setText("Export to CSV format");
		
		MenuItem mntmExportToDbm = new MenuItem(menu_1, SWT.NONE);
		mntmExportToDbm.setText("Export to DBM format");
		
		MenuItem mntmTools = new MenuItem(menu, SWT.NONE);
		mntmTools.setText("Main Menu");
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("Log out");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu, SWT.NONE);
		mntmNewItem_1.setText("Help");
		
		Label Timestamp = new Label(shell, SWT.NONE);
		Timestamp.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		Timestamp.setBackground(SWTResourceManager.getColor(211, 211, 211));
		Timestamp.setBounds(455, 0, 164, 23);
		Timestamp.setText(login.date_timestamp.toString());
		
		Group grpListOfQuestions = new Group(shell, SWT.NONE);
		grpListOfQuestions.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpListOfQuestions.setText("List of Questions");
		grpListOfQuestions.setBounds(339, 258, 295, 254);
		
		questn_list = new List(grpListOfQuestions, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		questn_list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { 
				String selectedQuestion = questn_list.getSelection()[0]; 
				int selectedIndex = questn_list.getSelectionIndex();
				
				
				System.out.println("I selected: " + selectedQuestion);
				
				txt_qestn_name.setText(selectedQuestion);
				String hmp_answer=Edit_result.hmap_answer.get(selectedQuestion.trim());
				
				System.out.println(hmp_answer + Edit_result.hmap_answer.size());
				
				txt_answer.setText(hmp_answer);
			
				String questn_status= Edit_result.hmap_status.get(selectedQuestion.trim()); // thanks man :)
				
		
				
				if(questn_status.contains("Incomplete")||questn_status.contains("Not Attempted"))
				{
					lblBlankLol.setText("Incomplete");
					lblBlankLol.setForeground(SWTResourceManager.getColor(220, 20, 60));
					
				}
				else{
					lblBlankLol.setText("Complete");
					lblBlankLol.setForeground(SWTResourceManager.getColor(0, 0, 0));
				}
				
				
			}
		});
		questn_list.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		questn_list.setBounds(10, 21, 275, 190);
		
		lblBlankLol = new Label(grpListOfQuestions, SWT.NONE);
		
		lblBlankLol.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblBlankLol.setAlignment(SWT.CENTER);
		lblBlankLol.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblBlankLol.setBounds(86, 217, 118, 27);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel_1.setBounds(10, 266, 122, 15);
		lblNewLabel_1.setText("Questionnaire Name");
		
		txt_questionniare_name = new Text(shell, SWT.BORDER);
		txt_questionniare_name.setEnabled(false);
		txt_questionniare_name.setEditable(false);
		txt_questionniare_name.setBounds(138, 263, 195, 21);
		
		Label lblQuestionName = new Label(shell, SWT.NONE);
		lblQuestionName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblQuestionName.setText("Question Title");
		lblQuestionName.setBounds(10, 319, 122, 15);
		
		txt_qestn_name = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.CANCEL);
		txt_qestn_name.setEnabled(false);
		txt_qestn_name.setEditable(false);
		txt_qestn_name.setBounds(138, 313, 195, 21);
		
		Label lblEditAnswer = new Label(shell, SWT.NONE);
		lblEditAnswer.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblEditAnswer.setBounds(149, 340, 75, 15);
		lblEditAnswer.setText("Edit Answer");
		
		txt_answer = new Text(shell, SWT.BORDER | SWT.V_SCROLL);
		txt_answer.setBounds(10, 361, 315, 114);
		
		wmessages = new MessageBox(shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(shell ,SWT.ICON_INFORMATION|SWT.OK);
		
		
		
		Button btnApplyChanges = new Button(shell, SWT.NONE);
		btnApplyChanges.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
		
				
				String strAnswer = txt_answer.getText().toString().trim();
				String strQuestion = txt_qestn_name.getText().toString().trim();// thanks man :)
		
				// okay done - thanks man ;)
				
				if(strAnswer.length()==0){
					wmessages.setMessage("Answer cannot be empty!");
					wmessages.setText("Alert!");
					wmessages.open();
				}else{
					
					MessageBox alert_message = new MessageBox(shell ,SWT.ICON_QUESTION|SWT.YES|SWT.NO);
					alert_message.setMessage("Are you sure you would like to update?");
					alert_message.setText("Confirmation");

					int confirmation = alert_message.open();

					if(confirmation==SWT.YES){

					
					
						Edit_result.hmap_answer.put(strQuestion,strAnswer);
						Edit_result.hmap_status.put(strQuestion,"Complete");
					
						StringBuilder buildJSON = new StringBuilder();
						for(int x = 0;x<Edit_result.questions.size();x++)
						{

							Edit_result.question= (JSONObject)Edit_result.questions.get(x);
						
							System.out.println( Edit_result.question.get("question").toString());
							buildJSON.append( "{\"question\":\"" + Edit_result.question.get("question").toString() +
									"\",\"answer\":\"" + Edit_result.hmap_answer.get(Edit_result.question.get("question").toString()) +
									"\", " + "\"status\":\"" + Edit_result.hmap_status.get(Edit_result.question.get("question").toString()) + "\"},"); 
						}
						
						String json= buildJSON.toString();
						if(json.endsWith(",")){// removing the comma and replacing it with the proper closing brackets.
							json= json.substring(0, json.length()-1);
							json=json+"]}";
						}
						//Edit_result.details.
						
						
						String jsonDetails = Edit_result.details.toString();
						
						if(Edit_result.noOfComplete == Edit_result.questions.size())
						{
							jsonDetails = jsonDetails.replace("Incomplete", "Complete");
							jsonDetails = jsonDetails.replace("Not Attempted", "Complete");
							
							
							String updatetable = "UPDATE Results SET Status='Complete' WHERE Result_ID="+result_id;
							new sql_queries().delete_add_update(updatetable);
							
						}
						// all good isi ttyaaayayayay now you were saying before about a label ? or should we leave it as it is now
						// which is the easier option ? well, the label thing shouldn't be too difficult. i'll try it now lolklkl
						
						// will that work i cant put the OR it should do, thats what we did with the other thing lol truee
						
						String update= "{\"details\":"+jsonDetails+",\"Results\":["+json;
						
						
						try {
							
							FileWriter writer = new FileWriter(file);
							
							writer.write(update);
							writer.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','the user the updated results of the following questionnaire "+ txt_questionniare_name.getText().toString() +" Orginally attempted by "+ txt_pname.getText().toString()+"')";
						new sql_queries().delete_add_update(updatelog);
						
						questn_list.removeAll();
						Edit_result.hmap_answer=new HashMap<String, String>();
						Edit_result.hmap_status=new HashMap<String, String>();
						new Edit_result().read(file);
						
						
						
					}
					else{
						System.out.println("user clicked no");
					}
				}
			
				
			}
		});
		btnApplyChanges.setBounds(125, 481, 99, 25);
		btnApplyChanges.setText("Apply Changes");
		
		// creating the columns here ....
		
		TableColumn tblclmnResultId = new TableColumn(Resultstable, SWT.NONE);
		
		tblclmnResultId.setText("Result ID");
		TableColumn tblclmnPatientId = new TableColumn(Resultstable, SWT.NONE);
		tblclmnResultId.setWidth(70);
		
		tblclmnPatientId.setText("Patient ID");
		TableColumn tblclmnQuestionnaireName = new TableColumn(Resultstable, SWT.NONE);
		tblclmnPatientId.setWidth(70);
		
		tblclmnQuestionnaireName.setText("Questionnaire Name");
		tblclmnQuestionnaireName.setWidth(170);
		
		TableColumn tblclmnfname = new TableColumn(Resultstable, SWT.NONE);
		tblclmnfname.setText("Patient Name");
		tblclmnfname.setWidth(170);
		
		TableColumn tblclmnStatus = new TableColumn(Resultstable, SWT.NONE);
		tblclmnStatus.setWidth(120);
		tblclmnStatus.setText("Status");
		// finish creating the columns...
		
		//populating the table...
		new sql_queries().db_table(Resultstable,query);
	
		
		txt_pname = new Text(shell, SWT.BORDER);
		txt_pname.setEnabled(false);
		txt_pname.setEditable(false);
		txt_pname.setBounds(138, 287, 195, 21);
		
		Label lblPatientName = new Label(shell, SWT.NONE);
		lblPatientName.setText("Patient Name");
		lblPatientName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPatientName.setBounds(10, 293, 122, 15);

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
				System.out.println("i am using exit button users");
				new main_menu(display);
			}
		});
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	public void tbl_selection(){
		int selectedindex = Resultstable.getSelectionIndex();
		TableItem[] tableRowData = Resultstable.getItems();
		String patid=tableRowData[selectedindex].getText(0).toString();

		try {
			Class.forName("org.sqlite.JDBC");
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

			System.out.println(patid);
			sqlstatement=sqlconnection.createStatement();

			sqlresult=sqlstatement.executeQuery("SELECT Result_ID , Patient_ID , Questionnaire_Name , Patient_Name ,Status ,File_Location FROM Results WHERE Status='Complete' OR Status='Incomplete'");// this was the error :P lol

			if(sqlresult.next()){

				txt_questionniare_name.setText(sqlresult.getString("Questionnaire_Name").toString());
				result_id=sqlresult.getString("Result_ID").toString();
				txt_pname.setText(sqlresult.getString("Patient_Name").toString());
				file=new File(sqlresult.getString("File_Location").toString());
			}

			sqlstatement.close();// closing statement // thanks all done :D sweeeeeeeeet well i think rohan is doing import..n i asked him if he could look into file convertion csv n dbm ooo, I seeeee :)
			

			sqlconnection.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} catch (ClassNotFoundException catche) {
			// TODO Auto-generated catch block
			catche.printStackTrace();
		}
	
	
	}
}
