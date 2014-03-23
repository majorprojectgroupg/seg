import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;


public class transfer {

	protected static Shell shell;
	private static Table table_questionnaire;
	private static Table table_patient;
	static Text filter_patient;
	static Text txt_quest_name;
	static Text txt_questID;
	static Text txt_pname;
	static Text txt_pid;

	private static Connection sqlconnection = null;
	private static ResultSet sqlresult=null;
	private static Statement sqlstatement=null;
	 static String questionnaire_location;
	static String dob;
	static String fname;
	static String lname;
	private String executepatient = "SELECT patid , fname , lname ,dob , sex ,homenumber , mobile, email , address1  ,address2  , address3 ,city AS City, county,postcode FROM patients"; // used by other methods

	private static String executequestionnaire = "SELECT Questionnaire_id, Questionnaire_name,Questionnaire_location FROM Questionnaires";
	static sql_queries queries = new sql_queries();
	private static Text txt_filterquest;
	private static 	MessageBox messages;
	


	/**
	 * @wbp.parser.entryPoint
	 */
	protected void tranfer(final Display display) {
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(785, 637);
		shell.setText("SWT Application");

		Group grpQuestionnaireTable = new Group(shell, SWT.NONE);
		grpQuestionnaireTable.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpQuestionnaireTable.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpQuestionnaireTable.setText("Questionnaires");
		grpQuestionnaireTable.setBounds(10, 20, 478, 272);

		Composite composite = new Composite(grpQuestionnaireTable, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite.setBounds(10, 22, 458, 240);

		table_questionnaire = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_questionnaire.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				int selectedindex = table_questionnaire.getSelectionIndex();
				TableItem[] tableRowData = table_questionnaire.getItems();
				String questionnaire_id=tableRowData[selectedindex].getText(0).toString();

				try {
					Class.forName("org.sqlite.JDBC");
					sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

					sqlstatement=sqlconnection.createStatement();

					sqlresult=sqlstatement.executeQuery("SELECT * FROM Questionnaires WHERE Questionnaire_ID='"+questionnaire_id+"'");

					if(sqlresult.next()){

						txt_quest_name.setText(sqlresult.getString("Questionnaire_Name").toString());

						txt_questID.setText(sqlresult.getString("Questionnaire_ID").toString());

						questionnaire_location= sqlresult.getString("Questionnaire_Location").toString();


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


			}
		});




		table_questionnaire.setToolTipText("Double Click to Select");
		table_questionnaire.setBounds(0, 33, 448, 207);
		table_questionnaire.setHeaderVisible(true);
		table_questionnaire.setLinesVisible(true);

		Label lblFilterQuestionnaire = new Label(composite, SWT.NONE);
		lblFilterQuestionnaire.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblFilterQuestionnaire.setBounds(65, 13, 158, 15);
		lblFilterQuestionnaire.setText("Search for Questionnaire");

		Group grpPatients = new Group(shell, SWT.NONE);
		grpPatients.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpPatients.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpPatients.setText("Patients");
		grpPatients.setBounds(10, 298, 478, 272);

		final Composite composite_1 = new Composite(grpPatients, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite_1.setBounds(10, 22, 458, 240);

		table_patient = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table_patient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {

				int selectedindex = table_patient.getSelectionIndex();
				TableItem[] tableRowData = table_patient.getItems();
				String patid=tableRowData[selectedindex].getText(0).toString();

				try {
					Class.forName("org.sqlite.JDBC");
					sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

					System.out.println(patid);
					sqlstatement=sqlconnection.createStatement();

					sqlresult=sqlstatement.executeQuery("SELECT * FROM patients WHERE patid='"+patid+"'");

					if(sqlresult.next()){

						txt_pname.setText(sqlresult.getString("fname").toString()+" "+ sqlresult.getString("lname"));

						txt_pid.setText(sqlresult.getString("patid").toString());
						dob=sqlresult.getString("dob").toString();
						fname =sqlresult.getString("fname").toString();
						lname =sqlresult.getString("lname").toString();
					}

					sqlstatement.close();// closing statement

					sqlconnection.close();

				} catch (SQLException sqle) {
					// TODO Auto-generated catch block
					sqle.printStackTrace();
				} catch (ClassNotFoundException catche) {
					// TODO Auto-generated catch block
					catche.printStackTrace();
				}
			}

		});
		table_patient.setToolTipText("Double Click to Select");
		table_patient.setLinesVisible(true);
		table_patient.setHeaderVisible(true);
		table_patient.setBounds(0, 33, 448, 207);

		Label lblSearchForPatient = new Label(composite_1, SWT.NONE);
		lblSearchForPatient.setText("Search for Patient");
		lblSearchForPatient.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblSearchForPatient.setBounds(70, 13, 128, 15);

		filter_patient = new Text(composite_1, SWT.BORDER);
		filter_patient.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				String filtertxt =filter_patient.getText().toString();

				if(filtertxt.length()==0){// if filter search is cleared dbtable method is called to display all results

					table_patient.removeAll();
					queries.db_table(table_patient, executepatient);

				}else{

					String filterquery= executepatient +" WHERE fname like '%"+filtertxt+"%' or lname like '%"+filtertxt+"%' or dob like '%"+filtertxt+"%' or email like '%"+filtertxt+"%' or homenumber like '%"+filtertxt+"%' or mobile like '%"+filtertxt+"%' or sex like '%"+filtertxt+"%' or address1 like '%"+filtertxt+"%' or address2 like '%"+filtertxt+"%' or address3 like '%"+filtertxt+"%' or city like '%"+filtertxt+"%' or county like '%"+filtertxt+"%' or postcode like '%"+filtertxt+"%'";
					System.out.println(filterquery);// debugging
					table_patient.removeAll();
					queries.db_table(table_patient, filterquery);
				}
			}
		});
	
		filter_patient.setBounds(219, 10, 145, 21);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmTools_1 = new MenuItem(menu, SWT.CASCADE);
		mntmTools_1.setText("Tools");

		Menu menu_1 = new Menu(mntmTools_1);
		mntmTools_1.setMenu(menu_1);

		MenuItem mntmClear = new MenuItem(menu_1, SWT.NONE);
		mntmClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleartxtfields();
			}
		});
		mntmClear.setText("Clear Selection");

		MenuItem mntmMainMenu = new MenuItem(menu, SWT.NONE);
		mntmMainMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
		 new main_menu(display);
			}
		});
		mntmMainMenu.setText("Main Menu");

		MenuItem mntmLogout = new MenuItem(menu, SWT.NONE);
		mntmLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user logged off the system')";
				queries.delete_add_update(updatelog);
				shell.dispose();
				login.shell.setVisible(true);
				login.shell.forceActive();
				
				
			}
		});
		mntmLogout.setText("Logout");

		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");

		Label label = new Label(shell, SWT.NONE);
		label.setText("09/03/2014 11:06:57 PM");
		label.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		label.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label.setBounds(600, 0, 164, 23);

		Group grpSelectionForTransfer = new Group(shell, SWT.NONE);
		grpSelectionForTransfer.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpSelectionForTransfer.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpSelectionForTransfer.setText("Selection for Transfer");
		grpSelectionForTransfer.setBounds(494, 136, 270, 301);

		Label lblQuestionnaireName = new Label(grpSelectionForTransfer, SWT.NONE);
		lblQuestionnaireName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblQuestionnaireName.setBounds(10, 33, 122, 15);
		lblQuestionnaireName.setText("Questionnaire Name");

		txt_quest_name = new Text(grpSelectionForTransfer, SWT.BORDER);
		txt_quest_name.setEditable(false);
		txt_quest_name.setBounds(139, 30, 121, 21);

		txt_questID = new Text(grpSelectionForTransfer, SWT.BORDER);
		txt_questID.setEditable(false);
		txt_questID.setBounds(139, 78, 121, 21);

		txt_pname = new Text(grpSelectionForTransfer, SWT.BORDER);
		txt_pname.setEditable(false);
		txt_pname.setBounds(139, 129, 121, 21);

		txt_pid = new Text(grpSelectionForTransfer, SWT.BORDER);
		txt_pid.setEditable(false);
		txt_pid.setBounds(139, 185, 121, 21);

		Label lblQuestionnaireId = new Label(grpSelectionForTransfer, SWT.NONE);
		lblQuestionnaireId.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblQuestionnaireId.setText("Questionnaire ID");
		lblQuestionnaireId.setBounds(10, 84, 122, 15);

		Label lblPatientName = new Label(grpSelectionForTransfer, SWT.NONE);
		lblPatientName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPatientName.setText("Patient Name");
		lblPatientName.setBounds(10, 135, 122, 15);

		Label lblPatientId = new Label(grpSelectionForTransfer, SWT.NONE);
		lblPatientId.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPatientId.setText("Patient ID");
		lblPatientId.setBounds(10, 191, 122, 15);

		messages = new MessageBox(shell, SWT.ICON_WARNING|SWT.OK);
		
		Button btn_transfer = new Button(grpSelectionForTransfer, SWT.NONE);
		btn_transfer.addSelectionListener(new SelectionAdapter() {
			
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				
				if(txt_questID.getText().length()==0 && txt_pid.getText().length()==0 ){
					messages.setMessage("Please select a patient and a Questionnaire");
					messages.setText("Alert!");
					messages.open();
					
				}else if (txt_questID.getText().length()==0){
					messages.setMessage("Please select a Questionnaire");
					messages.setText("Alert!");
					messages.open();
				}
				else if (txt_pid.getText().length()==0){
					messages.setMessage("Please select a Patient");
					messages.setText("Alert!");
					messages.open();
				}
			
				else  {
					
					queries.tranfersql();
				}
				
			}
		});
		btn_transfer.setImage(SWTResourceManager.getImage("./btn images/pctodroid.gif"));
		btn_transfer.setBounds(87, 226, 136, 50);

		Label lblTransfre = new Label(grpSelectionForTransfer, SWT.NONE);
		lblTransfre.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblTransfre.setBounds(130, 275, 55, 15);
		lblTransfre.setText("Transfer");

		txt_filterquest = new Text(composite, SWT.BORDER);
		txt_filterquest.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				String filterquest=txt_filterquest.getText().toString();

				if(filterquest.length()==0){// if filter search is cleared dbtable method is called to display all results
					table_questionnaire.removeAll();
					queries.db_table(table_questionnaire, executequestionnaire);

				}else{

					String filterquery= executequestionnaire +" WHERE Questionnaire_ID like '%"+filterquest+"%' or Questionnaire_Name like '%"+filterquest+"%'"; 
					System.out.println(filterquery);// debugging
					table_questionnaire.removeAll();
					queries.db_table(table_questionnaire, filterquery);


				}
			}
		});
		
		

		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		          shell.dispose();
		          System.out.println("i am using exit button transfer");
			    	new main_menu(display);
			    
		      }
		    });
		
		txt_filterquest.setBounds(228, 10, 145, 21);
		
		// Patient table columns
		
		TableColumn colm_patid= new TableColumn(table_patient, SWT.NONE);
		colm_patid.setText("Patient ID");
		
		TableColumn colm_fname = new TableColumn(table_patient, SWT.NONE);
		colm_fname.setText("First Name");
		
		TableColumn colm_lname = new TableColumn(table_patient, SWT.NONE);
		colm_lname.setText("Last Name");
		
		TableColumn colm_dob = new TableColumn(table_patient, SWT.NONE);
		colm_dob.setText("DOB");
		
		TableColumn colm_gender = new TableColumn(table_patient, SWT.NONE);
		colm_gender.setText("Gender");
		
		TableColumn colm_homeno = new TableColumn(table_patient, SWT.NONE);
		colm_homeno.setText("Home Telepone");
		
		
		TableColumn colm_mobile= new TableColumn(table_patient, SWT.NONE);
		colm_mobile.setText("Mobile Number");
		
		TableColumn colm_email = new TableColumn(table_patient, SWT.NONE);
		colm_email.setText("Email Address");
		
		TableColumn colm_add1 = new TableColumn(table_patient, SWT.NONE);
		colm_add1.setText("Address 1");
		
		TableColumn colm_add2 = new TableColumn(table_patient, SWT.NONE);
		colm_add2.setText("Address 2");
		
		TableColumn colm_add3 = new TableColumn(table_patient, SWT.NONE);
		colm_add3.setText("Address 3");
		
		TableColumn colm_city = new TableColumn(table_patient, SWT.NONE);
		colm_city.setText("City");
		
		TableColumn colm_county = new TableColumn(table_patient, SWT.NONE);
		colm_county.setText("County");
		// end of patient table columns
		
		

		TableColumn tblclmnQuestionnaireId = new TableColumn(table_questionnaire, SWT.NONE);
		tblclmnQuestionnaireId.setText("Questionnaire ID");
		
		TableColumn tblclmnQuestionnaireName = new TableColumn(table_questionnaire, SWT.NONE);
		tblclmnQuestionnaireName.setText("Questionnaire Name");
		
		TableColumn tblclmnQuestionnaireLocation = new TableColumn(table_questionnaire, SWT.NONE);
		tblclmnQuestionnaireLocation.setText("Questionnaire Location");
		
		queries.db_table(table_questionnaire, executequestionnaire);
		
		queries.db_table(table_patient, executepatient);
		
		for (int i=0; i<table_questionnaire.getColumnCount(); i++) {
	    	 table_questionnaire.getColumn (i).pack ();
		        }
		
		for (int i=0; i<table_patient.getColumnCount(); i++) {
	    	 table_patient.getColumn (i).pack ();
		        }
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		
		}	    	
	
		
		  
			}

	public static void cleartxtfields(){// clearing all text fields
		txt_quest_name.setText("");
		txt_questID.setText("");
		txt_pid.setText("");
		txt_pname.setText("");
		questionnaire_location="";
		dob="";
		filter_patient.setText("");
		txt_filterquest.setText("");


	}





}
