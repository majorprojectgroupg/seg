import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class patients {

	protected static Shell shell;
	private Table patient_table;
	public static Text txt_filter, txt_mobile, txt_hmeno,txt_fname,txt_lname,txt_dob,txt_email,txt_patid,txt_county, txt_city,txt_address1,txt_address2,txt_address3,txt_pstcde;
	private String executestaement = "SELECT patid , fname , lname ,dob , sex ,homenumber , mobile, email , address1  ,address2  , address3 ,city AS City, county,postcode FROM patients"; // used by other methods
	private static sql_queries queries = new sql_queries();
	private static Connection sqlconnection = null;
	private static ResultSet sqlresult=null;
	private static Statement sqlstatement=null;
	private static Combo cmbo_sex;
	private static Button btnAddPatient, btnupdate, btndelete;
	static Boolean add= false , delete=false , update =false;
	private static 	MessageBox message;
	
	protected patients( final Display display) {
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(615, 636);
		shell.setText("Group G");

		Group group = new Group(shell, SWT.NONE);
		group.setText("Patients");
		group.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		group.setBackground(SWTResourceManager.getColor(211, 211, 211));
		group.setBounds(10, 20, 585, 219);

		Composite composite = new Composite(group, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite.setBounds(10, 22, 567, 194);

		patient_table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		patient_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				tableselection();
			}
		});
		
		message = new MessageBox(shell,SWT.ICON_WARNING |SWT.OK);
		
		patient_table.setToolTipText("Double Click to Select");
		patient_table.setLinesVisible(true);
		patient_table.setHeaderVisible(true);
		patient_table.setBounds(0, 24, 567, 170);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Search for Patient");
		label.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label.setBounds(101, 3, 112, 15);

		txt_filter = new Text(composite, SWT.BORDER);
		txt_filter.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {

				String filtertxt =txt_filter.getText().toString();

				if(filtertxt.length()==0){// if filter search is cleared dbtable method is called to display all results
					patient_table.removeAll();
					queries.db_table(patient_table, executestaement);

				}else{

					String filterquery= executestaement +" WHERE fname like '%"+filtertxt+"%' or lname like '%"+filtertxt+"%' or dob like '%"+filtertxt+"%' or email like '%"+filtertxt+"%' or homenumber like '%"+filtertxt+"%' or mobile like '%"+filtertxt+"%' or sex like '%"+filtertxt+"%' or address1 like '%"+filtertxt+"%' or address2 like '%"+filtertxt+"%' or address3 like '%"+filtertxt+"%' or city like '%"+filtertxt+"%' or county like '%"+filtertxt+"%' or postcode like '%"+filtertxt+"%'";
					System.out.println(filterquery);// debugging
					patient_table.removeAll();
					queries.db_table(patient_table, filterquery);

				}

			}
		});
		txt_filter.setBounds(219, 0, 145, 21);

		Group grpBasicInfo = new Group(shell, SWT.NONE);
		grpBasicInfo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpBasicInfo.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpBasicInfo.setText("Basic Info");
		grpBasicInfo.setBounds(10, 245, 452, 183);

		Label lblFirstName = new Label(grpBasicInfo, SWT.NONE);
		lblFirstName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblFirstName.setBounds(10, 42, 66, 15);
		lblFirstName.setText("First Name *");

		Label lblLastName = new Label(grpBasicInfo, SWT.NONE);
		lblLastName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblLastName.setText("Last Name *");
		lblLastName.setBounds(10, 69, 66, 15);

		Label lblDateOfBirth = new Label(grpBasicInfo, SWT.NONE);
		lblDateOfBirth.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblDateOfBirth.setText("Date of Birth *");
		lblDateOfBirth.setBounds(10, 101, 82, 15);

		Label lblEmailAddress = new Label(grpBasicInfo, SWT.NONE);
		lblEmailAddress.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblEmailAddress.setText("Email Address");
		lblEmailAddress.setBounds(10, 134, 82, 15);

		Label lblHomeTel = new Label(grpBasicInfo, SWT.NONE);
		lblHomeTel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblHomeTel.setText("Home Tel *");
		lblHomeTel.setBounds(245, 42, 66, 15);

		Label lblMobileNo = new Label(grpBasicInfo, SWT.NONE);
		lblMobileNo.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblMobileNo.setText("Mobile No");
		lblMobileNo.setBounds(245, 69, 66, 15);

		Label lblSex = new Label(grpBasicInfo, SWT.NONE);
		lblSex.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblSex.setText("Sex *");
		lblSex.setBounds(245, 101, 66, 15);

		Label lblPatientId = new Label(grpBasicInfo, SWT.NONE);
		lblPatientId.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPatientId.setText("Patient ID");
		lblPatientId.setBounds(245, 134, 66, 15);

		cmbo_sex = new Combo(grpBasicInfo, SWT.READ_ONLY);
		cmbo_sex.setItems(new String[] {"Please select", "Male", "Female"});
		cmbo_sex.setBounds(317, 98, 125, 23);

		txt_mobile = new Text(grpBasicInfo, SWT.BORDER);
		txt_mobile.setBounds(317, 66, 125, 21);

		txt_hmeno = new Text(grpBasicInfo, SWT.BORDER);
		txt_hmeno.setBounds(317, 36, 125, 21);

		txt_fname = new Text(grpBasicInfo, SWT.BORDER);
		txt_fname.setBounds(99, 36, 125, 21);

		txt_lname = new Text(grpBasicInfo, SWT.BORDER);
		txt_lname.setBounds(99, 66, 125, 21);

		txt_dob = new Text(grpBasicInfo, SWT.BORDER);
		txt_dob.setBounds(99, 95, 125, 21);

		txt_email = new Text(grpBasicInfo, SWT.BORDER);
		txt_email.setBounds(99, 128, 125, 21);

		txt_patid = new Text(grpBasicInfo, SWT.BORDER);
		txt_patid.setEnabled(false);
		txt_patid.setEditable(false);
		txt_patid.setBounds(317, 128, 125, 21);

		Group grpAddressInfo = new Group(shell, SWT.NONE);
		grpAddressInfo.setText("Address Info");
		grpAddressInfo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpAddressInfo.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpAddressInfo.setBounds(10, 437, 452, 135);

		Label lblAddressLine = new Label(grpAddressInfo, SWT.NONE);
		lblAddressLine.setText("Address Line 1 *");
		lblAddressLine.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblAddressLine.setBounds(10, 42, 84, 15);

		Label lblAddressLine_1 = new Label(grpAddressInfo, SWT.NONE);
		lblAddressLine_1.setText("Address Line 2 *");
		lblAddressLine_1.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblAddressLine_1.setBounds(10, 69, 84, 15);

		Label lblAddressLine_2 = new Label(grpAddressInfo, SWT.NONE);
		lblAddressLine_2.setText("Address Line 3");
		lblAddressLine_2.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblAddressLine_2.setBounds(10, 101, 84, 15);

		Label lblCitl = new Label(grpAddressInfo, SWT.NONE);
		lblCitl.setText("City *");
		lblCitl.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblCitl.setBounds(245, 42, 66, 15);

		Label lblCounty = new Label(grpAddressInfo, SWT.NONE);
		lblCounty.setText("County");
		lblCounty.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblCounty.setBounds(245, 69, 66, 15);

		Label lblPostcode = new Label(grpAddressInfo, SWT.NONE);
		lblPostcode.setText("PostCode *");
		lblPostcode.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPostcode.setBounds(245, 101, 66, 15);

		txt_county = new Text(grpAddressInfo, SWT.BORDER);
		txt_county.setBounds(317, 66, 125, 21);

		txt_city = new Text(grpAddressInfo, SWT.BORDER);
		txt_city.setBounds(317, 36, 125, 21);

		txt_address1 = new Text(grpAddressInfo, SWT.BORDER);
		txt_address1.setBounds(99, 36, 125, 21);

		txt_address2 = new Text(grpAddressInfo, SWT.BORDER);
		txt_address2.setBounds(100, 66, 125, 21);

		txt_address3 = new Text(grpAddressInfo, SWT.BORDER);
		txt_address3.setBounds(99, 95, 125, 21);

		txt_pstcde = new Text(grpAddressInfo, SWT.BORDER);
		txt_pstcde.setBounds(317, 98, 125, 21);

		Group grpQuickTools = new Group(shell, SWT.NONE);
		grpQuickTools.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpQuickTools.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpQuickTools.setText("Quick Tools");
		System.out.println(login.ulevel.toString());
		if(login.ulevel.contains("Doctor")){
			grpQuickTools.setBounds(468, 360, 121, 110);
		}else{
			grpQuickTools.setBounds(468, 360, 121, 150);
		}
		btnAddPatient = new Button(grpQuickTools, SWT.NONE);
		btnAddPatient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				add = true;
				update = false;
				delete = false;
				validationcheck();
				
			}
		});
		btnAddPatient.setBounds(10, 28, 100, 25);
		btnAddPatient.setText("Add Patient");

		 btnupdate = new Button(grpQuickTools, SWT.NONE);
		btnupdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				update = true;
				add=false;
				delete = false;
				validationcheck();
			
			}
		});
		btnupdate.setEnabled(false);
		btnupdate.setBounds(10, 68, 100, 25);
		btnupdate.setText("Update Patient");

		 btndelete = new Button(grpQuickTools, SWT.NONE);
		 btndelete.addSelectionListener(new SelectionAdapter() {
		 	@Override
		 	public void widgetSelected(SelectionEvent e) {
		 		delete = true;
		 		update = false;
		 		add=false;
				validationcheck();
		 	
		 	}
		 });
		btndelete.setEnabled(false);
		btndelete.setBounds(10, 110, 100, 25);
		btndelete.setText("Delete Patient");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmTools = new MenuItem(menu, SWT.CASCADE);
		mntmTools.setText("Tools");

		Menu menu_1 = new Menu(mntmTools);
		mntmTools.setMenu(menu_1);

		MenuItem mntmClearAllFields = new MenuItem(menu_1, SWT.NONE);
		mntmClearAllFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { // clearing all text fields
				clear_txtfields();
				btnAddPatient.setEnabled(true);// everytime all fields are clear the gui will revert to default.. btnupdate & btndelete will be disabled
				
			}
		});
		mntmClearAllFields.setText("Clear All Fields");

		MenuItem mntmViewAttemptedQuestionnaires = new MenuItem(menu_1, SWT.NONE);
		mntmViewAttemptedQuestionnaires.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(txt_patid.getText().length()==0){
					message = new MessageBox(shell,SWT.ICON_ERROR|SWT.OK);
					message.setText("Alert!");
					message.setMessage("Please select a patient then try again");
					message.open();
						}else{
				new individual_result(display);
			}}
		});
		mntmViewAttemptedQuestionnaires.setText("View Attempted Questionnaires by Patient");

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
				System.out.println("did press logout");//debugging
			}
		});
		mntmLogout.setText("Logout");

		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");

		TableColumn colm_patid= new TableColumn(patient_table, SWT.NONE);
		colm_patid.setText("Patient ID");
		
		TableColumn colm_fname = new TableColumn(patient_table, SWT.NONE);
		colm_fname.setText("First Name");
		
		TableColumn colm_lname = new TableColumn(patient_table, SWT.NONE);
		colm_lname.setText("Last Name");
		
		TableColumn colm_dob = new TableColumn(patient_table, SWT.NONE);
		colm_dob.setText("DOB");
		
		TableColumn colm_gender = new TableColumn(patient_table, SWT.NONE);
		colm_gender.setText("Gender");
		
		TableColumn colm_homeno = new TableColumn(patient_table, SWT.NONE);
		colm_homeno.setText("Home Telepone");
		
		
		TableColumn colm_mobile= new TableColumn(patient_table, SWT.NONE);
		colm_mobile.setText("Mobile Number");
		
		TableColumn colm_email = new TableColumn(patient_table, SWT.NONE);
		colm_email.setText("Email Address");
		
		TableColumn colm_add1 = new TableColumn(patient_table, SWT.NONE);
		colm_add1.setText("Address 1");
		
		TableColumn colm_add2 = new TableColumn(patient_table, SWT.NONE);
		colm_add2.setText("Address 2");
		
		TableColumn colm_add3 = new TableColumn(patient_table, SWT.NONE);
		colm_add3.setText("Address 3");
		
		TableColumn colm_city = new TableColumn(patient_table, SWT.NONE);
		colm_city.setText("City");
		
		TableColumn colm_county = new TableColumn(patient_table, SWT.NONE);
		colm_county.setText("County");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("09/03/2014 11:06:57 PM");
		label_1.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		label_1.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label_1.setBounds(431, 0, 164, 14);
		
		if (login.ulevel.contains("Admin")){// a non admin account delete records
			
			btndelete.setVisible(true);
		}else{
			btndelete.setEnabled(false);
			btndelete.setVisible(false);
			grpQuickTools.setBounds(468, 360, 121, 110);
			
		}
		
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		      shell.dispose();
		      System.out.println("i am using exit button patient");
		      new main_menu(display);
		    
		      }
		    });


		queries.db_table(patient_table,executestaement );
		for (int i=0; i<patient_table.getColumnCount(); i++) {
	    	 patient_table.getColumn (i).pack ();
		        }
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}

		}
	}
	public void tableselection(){// selecting record from the table
		try {
			enablebtn();
			
			Class.forName("org.sqlite.JDBC");
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
			int selectedindex = patient_table.getSelectionIndex();
			TableItem[] tableRowData = patient_table.getItems();
			String patid=tableRowData[selectedindex].getText(0).toString();
			System.out.println(patid);
			sqlstatement=sqlconnection.createStatement();

			sqlresult=sqlstatement.executeQuery("SELECT * FROM patients WHERE patid='"+patid+"'");

			if(sqlresult.next()){

				txt_fname.setText(sqlresult.getString("fname").toString());
				txt_lname.setText(sqlresult.getString("lname").toString());
				txt_address1.setText(sqlresult.getString("address1").toString());
				txt_address2.setText(sqlresult.getString("address2").toString());
				txt_address3.setText(sqlresult.getString("address3").toString());
				txt_city.setText(sqlresult.getString("city").toString());
				txt_county.setText(sqlresult.getString("county").toString());
				txt_pstcde.setText(sqlresult.getString("postcode").toString());
				txt_email.setText(sqlresult.getString("email").toString());
				txt_hmeno.setText("0"+sqlresult.getString("homenumber").toString());
				
			String mob= sqlresult.getString("mobile").toString();
				if(mob.length()!=0){
					txt_mobile.setText("0"+mob);
				}else{
					txt_mobile.setText(sqlresult.getString("mobile").toString());
				}
				txt_patid.setText(sqlresult.getString("patid").toString());
				txt_dob.setText(sqlresult.getString("dob").toString());


				String sex =sqlresult.getString("sex").toString();
				if(sex.contains("Male")){
					cmbo_sex.select(1);
				}
				else if(sex.contains("Female")) {
					cmbo_sex.select(2);
				}

				else{
					cmbo_sex.select(0);
				}


			}

			sqlstatement.close();// closing statement

			sqlconnection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void validationcheck(){
		int fname = txt_fname.getText().length();
		int lname = txt_lname.getText().length();
		int address1 = txt_address1.getText().length();
		int city = txt_city.getText().length();
		int postcde= txt_pstcde.getText().length();
		int homenumber=txt_hmeno.getText().length();
		int dob = txt_dob.getText().length();
		int cmboselect= cmbo_sex.getSelectionIndex();
		message = new MessageBox(shell,SWT.ICON_WARNING |SWT.OK);

		if(fname==0|| lname==0|| address1==0|| city==0|| postcde==0|| dob==0|| homenumber==0){
			message.setText("Alert!");
			message.setMessage("Please fill in all mandatory fileds marked with the symbol '*' ");
			message.open();
			
		}
		else if (cmboselect==0){
			
			message.setText("Alert!");
			message.setMessage("Please select a sex for the patient");
			message.open();
			}
		else{
			regex();
		}


	}
	public void regex(){// for validating mandatory textfields
		String namereg= "([A-Za-z]{2,40})";
		String telmobreg="([0-9]{11})";
		String emailreg="(\\w(\\.?[\\w\\-]+)*@\\w+(\\.[\\w\\-]+)+)";
		String postcodereg="(([A-Za-z]){1,2})([0-9]{1,2})([a-zA-Z]{0,1})(\\s?){1}([0-9]{1})(([a-z]|[A-Z]){1,2})";
		String dobreg="(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(([0-9]{4}))";
		String cityreg="([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)";

		String fname = txt_fname.getText();
		String lname = txt_lname.getText();
		String city = txt_city.getText();
		String postcde= txt_pstcde.getText();
		String emailad= txt_email.getText();
		String telphone=txt_hmeno.getText();
		String moblie=txt_mobile.getText();
		String dob = txt_dob.getText();
		

		System.out.println(fname.matches(namereg)+"firstname");
		System.out.println(dob.matches(dobreg)+"dob");
		System.out.println(emailad.matches(emailreg)+"email");
		System.out.println(postcde.matches(postcodereg)+"postcode");
		System.out.println(telphone.matches(telmobreg)+"home");
		System.out.println(moblie.matches(telmobreg)+"mobile");
		System.out.println(city.matches(cityreg)+"city");
		message = new MessageBox(shell,SWT.ICON_WARNING |SWT.OK);
		
		if(fname.matches(namereg)==false){
			
			message.setText("Alert!");
			message.setMessage("Invalid First Name, field cannot contain special/numeric characters");
			message.open();
		}

		else if(lname.matches(namereg)==false ){
			message.setText("Alert!");
			message.setMessage("Invalid Last Name , field cannot contain special/numeric characters");
			message.open();
			
		}
		else if(postcde.matches(postcodereg)==false){
			message.setText("Alert!");
			message.setMessage("Invalid Postcode field cannot contain special characters");
			message.open();

		}
		else if(emailad.length()!=0 && emailad.matches(emailreg)==false){
			message.setText("Alert!");
			message.setMessage("Invalid Email address");
			message.open();
			
			}
		else if(dob.matches(dobreg)==false){
			message.setText("Alert!");
			message.setMessage("Invalid Date of Birth, valid format DD/MM/YYYY");
			message.open();
			

		}

		else if(city.matches(cityreg)==false){
			
			message.setText("Alert!");
			message.setMessage("Invalid City , this field cannot contain numeric / special characters");
			message.open();


		}
		else if(telphone.matches(telmobreg)==false){
			
			message.setText("Alert!");
			message.setMessage("Invalid Home Telephone number");
			message.open();

		}

		else if(moblie.length()!=0 && moblie.matches(telmobreg)==false){
			
			message.setText("Alert!");
			message.setMessage("Invalid Mobile Number");
			message.open();
			
		}


		else if(fname.matches(namereg)==false && lname.matches(namereg)==false &&postcde.matches(postcodereg)==false && emailad.matches(emailreg)==false
				&& dob.matches(dobreg)==false && city.matches(cityreg)==false && telphone.matches(telmobreg)==false && moblie.matches(telmobreg)==false){

			message.setText("Alert!");
			message.setMessage("Invalid entries Please fill in valid data");
			message.open();

		} 

		else if(fname.matches(namereg) && lname.matches(namereg) &&postcde.matches(postcodereg) && (emailad.matches(emailreg) || emailad.length()==0)
				&& dob.matches(dobreg) && city.matches(cityreg) && telphone.matches(telmobreg) && (moblie.matches(telmobreg) || moblie.length()==0) && cmbo_sex.getSelectionIndex()!=0) {
			statementexecute();
		}
		else{
			message.setText("Alert!");
			message.setMessage("Make sure all mandatory fileds have valid data");
			message.open();
			
		}
	}

	public void clear_txtfields(){
		txt_address1.setText("");
		txt_address2.setText("");
		txt_address3.setText("");
		txt_city.setText("");
		txt_county.setText("");
		txt_pstcde.setText("");
		txt_hmeno.setText("");
		txt_mobile.setText("");
		txt_dob.setText("");
		txt_fname.setText("");
		txt_lname.setText("");
		txt_patid.setText("");
		txt_email.setText("");
		cmbo_sex.select(0);
		add=false;
		delete=false;
		update=false;
		btndelete.setEnabled(false);
		btnupdate.setEnabled(false);
		
	}
	public static void enablebtn(){ //by default update and delete buttons are disabled
		btnAddPatient.setEnabled(false);
		if (login.ulevel.contains("Admin")){
			btndelete.setEnabled(true);
			
		}else{
			btndelete.setEnabled(false);
		}
		btnupdate.setEnabled(true);


	}
	public void statementexecute(){ // select the correct option depending if the users is updating or deleting or adding new patient
		String sex = null ;
		message = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
		if(cmbo_sex.getSelectionIndex()==1){
			sex="Male";
		}else if (cmbo_sex.getSelectionIndex()==2){
			sex="Female";
		}
		
		
		if(add==true){
				message.setText("Warning!");
			message.setMessage("Are you sure you would like to add the patient "+txt_fname.getText()+" "+txt_lname.getText()+" to the database");
		int confirmation= message.open();
			
			if(confirmation==SWT.OK){

			String addstatement="INSERT INTO patients(fname,lname,dob,sex,homenumber,mobile,email,address1,address2,address3,city,county,postcode)VALUES("+"'"+txt_fname.getText()+"'"+","+"'"+
					txt_lname.getText()+"'"+","+"'"+txt_dob.getText()+"'"+","+"'"+sex+"'"+","+"'"+txt_hmeno.getText().toString()+"'"+","+"'"+txt_mobile.getText()+"'"+","+"'"+txt_email.getText()+"'"+","+"'"+txt_address1.getText()+"'"+","+"'"+txt_address2.getText()+"'"+","+"'"+txt_address3.getText()+"'"+","+"'"+txt_city.getText()+"'"+","+"'"+txt_county.getText()+"'"+","+"'"+txt_pstcde.getText()+"');";
			queries.delete_add_update(addstatement);

			// adding to the log what the user did
			String addlogstatement = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','user added the patient "+" "+txt_fname.getText()+" "+txt_lname.getText()+" to the database')";
			queries.delete_add_update(addlogstatement);
			
			//re freshing table
			patient_table.removeAll();
			queries.db_table(patient_table, executestaement);
			
			
			message.setText("Success");
			message.setMessage("You have successfully added the patient to the database");
			message.open();
			clear_txtfields();
		}
		}
		else if (delete==true){
			
			
			message.setText("Warning!");
			message.setMessage("Are you sure you would like to delete the patient "+txt_fname.getText()+" "+txt_lname.getText()+" from the database");
			
			
			int confirmation_delete =message.open();
			
			if(confirmation_delete==SWT.YES){
				
	 		String deletequery = "DELETE FROM patients WHERE patid="+txt_patid.getText();
			queries.delete_add_update(deletequery);
			

			//query below is to insert new record on to the log table of what the user did
			String deletelogstatement = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','user delete the Patient"+" "+txt_fname.getText()+" "+txt_lname.getText()+" from the database')";
			queries.delete_add_update(deletelogstatement);
			
			// re freshing the table after delete
			patient_table.removeAll();
			queries.db_table(patient_table,executestaement);
			
			// message to indicate the update has gone through
			
			message = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
			message.setText("Success");
			message.setMessage("You have successfully deleted the patient from the database");
			message.open();
			
			clear_txtfields();// clearing all textfields
			btnAddPatient.setEnabled(true);
			btnupdate.setEnabled(false);
			btndelete.setEnabled(false);
			}
			else{
			System.out.println("noo");
			}
		}
			else if (update==true){
				
				message.setText("Warning!");
				message.setMessage("Are you sure you would like to update the patient record "+txt_fname.getText()+" "+txt_lname.getText());
			int confirmation_update = message.open();
				
				if(confirmation_update==SWT.YES){
				String updatequery="UPDATE patients SET fname='"+txt_fname.getText()+"'"+","+"lname='"+txt_lname.getText()+"'"+","+"dob='"+txt_dob.getText()+"'"+","+"sex='"+sex+"'"+","+"homenumber='"+txt_hmeno.getText()+"'"+","+"mobile='"+txt_mobile.getText()+"'"+","+"email='"+txt_email.getText()+"'"+","+"address1='"+txt_address1.getText()+"'"+","+"address2='"+txt_address2.getText()+"'"+","+"address3='"+txt_address3.getText()+"'"+","+"city='"+txt_city.getText()+"'"+","+"county='"+txt_county.getText()+"'"+","+"postcode='"+txt_pstcde.getText()+"' WHERE patid="+txt_patid.getText();

				queries.delete_add_update(updatequery);// sending the query built above to the necessary method to execute
				 

				//query below inserts new record to the log table of what the user did 
				String updatelogstatement = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"', 'user updated the details of the Patient "+ txt_fname.getText()+" "+txt_lname.getText()+"')";
				queries.delete_add_update(updatelogstatement);
				
				
				// re freshing the table after delete
				patient_table.removeAll();
				queries.db_table(patient_table,executestaement);
				
				message = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				message.setText("Success");
				message.setMessage("You have successfully updated the patient record");
				message.open();
	
				clear_txtfields();// clearing all textfields
				btnAddPatient.setEnabled(true);
				btnupdate.setEnabled(false);
				btndelete.setEnabled(false);
			}
				else{
					System.out.println("nope");
				}
			
		}
	}
}
