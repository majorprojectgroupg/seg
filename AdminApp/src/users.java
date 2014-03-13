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
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class users {

	protected static Shell shell;
	private static Table user_table;
	public static Text txt_Filter, txt_role, txt_fname, txt_lname, txt_username, txt_password , txt_UserId;
	private static String executestaement = "SELECT empid AS Employee_id, fname AS First_Name, lname AS Last_Name,role AS Role, username AS Username,password AS Password,userlevel AS UserLevel, active AS Active FROM users"; // used by other methods
	static sql_queries queries = new sql_queries();
	private static String fnme, lnme;
	private static Connection sqlconnection = null;
	private static ResultSet sqlresult=null;
	private static Statement sqlstatement=null;
	static Combo cmbo_ulevel ;
	private static Button btnAdd ,btnUpdate ,btnDelete ,btnDisable, btnEnable;
	public static Boolean individual_log= false;
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected users(final Display display) {
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(635, 494);
		shell.setText("Group G");
		
		Group grpUsers = new Group(shell, SWT.NONE);
		grpUsers.setText("User Records");
		grpUsers.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpUsers.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpUsers.setBounds(10, 20, 599, 219);
		
		Composite composite = new Composite(grpUsers, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite.setBounds(10, 22, 579, 194);
		
		user_table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		user_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				tableselection();
				enablebuttons();
			}
		});
		user_table.setToolTipText("Double Click to Select");
		user_table.setLinesVisible(true);
		user_table.setHeaderVisible(true);
		user_table.setBounds(0, 24, 579, 170);
		
		Label lblSearchForUser = new Label(composite, SWT.NONE);
		lblSearchForUser.setText("Quick Search");
		lblSearchForUser.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblSearchForUser.setBounds(134, 3, 79, 15);
		
		txt_Filter = new Text(composite, SWT.BORDER);
		txt_Filter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String filtertxt =txt_Filter.getText().toString();

				if(filtertxt.length()==0){// if filter search is cleared dbtable method is called to display all results
					user_table.removeAll();
					queries.db_table(user_table,executestaement);

				}else{
					user_table.removeAll();
					String filterquery= "SELECT empid AS Employee_id, fname AS First_Name, lname AS Last_Name , role AS Role, username AS Username,password AS Password,userlevel AS UserLevel,active AS Active FROM users where fname like '%"+filtertxt+"%' or lname like '%"+filtertxt+"%' or role like '%"+filtertxt+"%' or userlevel like '%"+filtertxt+"%'";
					System.out.println(filterquery);// debugging
					queries.db_table(user_table,filterquery);
					
				}
			}
		
		});
		txt_Filter.setBounds(219, 0, 145, 21);
		
		Group grpUserInfo = new Group(shell, SWT.NONE);
		grpUserInfo.setText("User Info");
		grpUserInfo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpUserInfo.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpUserInfo.setBounds(10, 245, 452, 185);
		
		Label lblFirstName = new Label(grpUserInfo, SWT.NONE);
		lblFirstName.setText("First Name ");
		lblFirstName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblFirstName.setBounds(10, 42, 66, 15);
		
		Label lblLastName = new Label(grpUserInfo, SWT.NONE);
		lblLastName.setText("Last Name ");
		lblLastName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblLastName.setBounds(10, 69, 66, 15);
		
		Label lblUsername = new Label(grpUserInfo, SWT.NONE);
		lblUsername.setText("Username ");
		lblUsername.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblUsername.setBounds(10, 101, 74, 15);
		
		Label lblPassword = new Label(grpUserInfo, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPassword.setBounds(10, 134, 74, 15);
		
		Label lblRole = new Label(grpUserInfo, SWT.NONE);
		lblRole.setText("Role");
		lblRole.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblRole.setBounds(245, 42, 66, 15);
		
		Label lblUserLevel = new Label(grpUserInfo, SWT.NONE);
		lblUserLevel.setText("User Level");
		lblUserLevel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblUserLevel.setBounds(245, 72, 66, 15);
		
		Label lblUserId = new Label(grpUserInfo, SWT.NONE);
		lblUserId.setText("User ID");
		lblUserId.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblUserId.setBounds(245, 101, 66, 15);
		
		cmbo_ulevel = new Combo(grpUserInfo,  SWT.READ_ONLY);
		cmbo_ulevel.setItems(new String[] {"Please Select", "Admin", "Doctor", "Nurse", "Receptionist"});
		cmbo_ulevel.setBounds(317, 66, 125, 23);
	
		cmbo_ulevel.select(0);
		
		txt_role = new Text(grpUserInfo, SWT.BORDER);
		txt_role.setBounds(317, 36, 125, 21);
		
		txt_fname = new Text(grpUserInfo, SWT.BORDER);
		txt_fname.setBounds(99, 36, 125, 21);
		
		txt_lname = new Text(grpUserInfo, SWT.BORDER);
		txt_lname.setBounds(99, 66, 125, 21);
		
		txt_username = new Text(grpUserInfo, SWT.BORDER);
		txt_username.setBounds(99, 95, 125, 21);
		
		txt_password = new Text(grpUserInfo, SWT.BORDER);
		txt_password.setBounds(99, 128, 125, 21);
		
		txt_UserId = new Text(grpUserInfo, SWT.READ_ONLY);
		txt_UserId.setEnabled(false);
		txt_UserId.setEditable(false);
		txt_UserId.setBounds(317, 98, 125, 21);
		
		Group group_2 = new Group(shell, SWT.NONE);
		group_2.setText("Quick Tools");
		group_2.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		group_2.setBackground(SWTResourceManager.getColor(211, 211, 211));
		group_2.setBounds(488, 245, 121, 185);
		
		btnAdd = new Button(group_2, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fnme= txt_fname.getText().toString();
				lnme= txt_lname.getText().toString();
				
				if(txt_fname.getText().length()==0 ||txt_lname.getText().length()==0||txt_username.getText().length()==0|| txt_password.getText().length()==0 ||txt_role.getText().length()==0||cmbo_ulevel.getSelectionIndex()==0){
					System.out.println("fill in all the fields");
					JOptionPane.showMessageDialog(null, "Please Fill in all the fields under UserInfo","Warning", JOptionPane.WARNING_MESSAGE);
					
				}else{
					
					int confirmantion = JOptionPane.showConfirmDialog(null, "Are you sure you would like to add "+txt_fname.getText()+" "+txt_lname.getText()+"to the user database","Confrimation", JOptionPane.YES_NO_OPTION);
					if(confirmantion==JOptionPane.YES_OPTION){

					String addstatement="INSERT INTO users(fname,lname,role,username,password,userlevel,active)VALUES("+"'"+txt_fname.getText()+"'"+","+"'"+
							txt_lname.getText()+"'"+","+"'"+txt_role.getText()+"'"+","+"'"+txt_username.getText()+"'"+","+"'"+txt_password.getText()+"'"+","+"'"+cmbo_ulevel.getSelection().toString()+"'"+","+"'y'"+");";

					
					queries.delete_add_update(addstatement);
					user_table.removeAll();
					queries.db_table(user_table,executestaement);
				
					
					String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','The user added"+" "+fnme+" "+lnme+" as a new user to the system')";
					queries.delete_add_update(updatelog); // updating the log table
					JOptionPane.showMessageDialog(null, "You have successfully added a new user to the system","Success", JOptionPane.PLAIN_MESSAGE);
					cleartextfields();
					}
					
					else{
						
					}
				}

			}
		});
		btnAdd.setText("Add New User");
		btnAdd.setBounds(10, 28, 100, 25);
		
		btnUpdate = new Button(group_2, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fnme= txt_fname.getText().toString();
				lnme= txt_lname.getText().toString();
				String combo_select=null;
				
				if(cmbo_ulevel.getSelectionIndex()==1){
				combo_select="Admin";
				}
				else if(cmbo_ulevel.getSelectionIndex()==2) {
					
					combo_select="Doctor";
				}
				else if(cmbo_ulevel.getSelectionIndex()==3) {
				
					combo_select="Nurse";
				}
				else if(cmbo_ulevel.getSelectionIndex()==4) {
					
					combo_select="Receptionist";
				}
				else{
					
				}
				

				if(txt_fname.getText().length()==0 ||txt_lname.getText().length()==0||txt_username.getText().length()==0|| txt_password.getText().length()==0 ||txt_role.getText().length()==0||cmbo_ulevel.getSelectionIndex()==0){
					System.out.println("fill in all the fields");
					JOptionPane.showMessageDialog(null, "Please Fill in all the fields under UserInfo","Warning", JOptionPane.WARNING_MESSAGE);
					
				}else{
					

					int confirmantion = JOptionPane.showConfirmDialog(null, "Are you sure you would like to update the user record "+txt_fname.getText()+" "+txt_lname.getText()+"","Confrimation", JOptionPane.YES_NO_OPTION);
					if(confirmantion==JOptionPane.YES_OPTION){
						enablebuttons();
						String updatequery="UPDATE users SET fname='"+txt_fname.getText()+"' , lname='"+txt_lname.getText()+"' ,role='"+txt_role.getText()+"' ,password='"+txt_password.getText()+"' , userlevel='"+combo_select+"' WHERE empid="+txt_UserId.getText();
						
						queries.delete_add_update(updatequery);
						user_table.removeAll();
						queries.db_table(user_table,executestaement);//	if filtered use filter query to refresh table 
						String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','the user updated the logon credentials of "+" "+fnme+" "+lnme+"')";
						queries.delete_add_update(updatelog);
						JOptionPane.showMessageDialog(null, "You have successfully updated the user record","Success", JOptionPane.PLAIN_MESSAGE);
						cleartextfields();
					}
					else{
						
					}
					
			
			
				}
			}
		});
		btnUpdate.setText("Update User");
		btnUpdate.setEnabled(false);
		btnUpdate.setBounds(10, 59, 100, 25);
		
		btnDelete = new Button(group_2, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fnme= txt_fname.getText().toString();
				lnme= txt_lname.getText().toString();
				
				int confirmantion = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the user "+txt_fname.getText()+" "+txt_lname.getText()+"","Confrimation", JOptionPane.YES_NO_OPTION);
				if(confirmantion==JOptionPane.YES_OPTION){
				
				String deletequery="DELETE FROM users WHERE empid="+txt_UserId.getText();
				queries.delete_add_update(deletequery);
				user_table.removeAll();
				queries.db_table(user_table,executestaement);//	if filtered use filter query to refresh table 
				//disablebtns_ncleartxt();
				String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','the user deleted the logon credentials of "+" "+fnme+" "+lnme+" from the database')";
				queries.delete_add_update(updatelog);
				JOptionPane.showMessageDialog(null, "You have successfully deleted the user from the database","Success", JOptionPane.PLAIN_MESSAGE);
				cleartextfields();
			}else{
				
			}
			}
		});
		btnDelete.setText("Delete User");
		btnDelete.setEnabled(false);
		btnDelete.setBounds(10, 90, 100, 25);
		
		btnDisable = new Button(group_2, SWT.NONE);
		btnDisable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fnme= txt_fname.getText().toString();
				lnme= txt_lname.getText().toString();
				
				// validation maybe joption pane confirm dialogue.
				int confirmantion = JOptionPane.showConfirmDialog(null, "Are you sure you would like to disable the user "+txt_fname.getText()+" "+txt_lname.getText()+"","Confrimation", JOptionPane.YES_NO_OPTION);
				if(confirmantion==JOptionPane.YES_OPTION){
				
				String disablequery="UPDATE users SET active='n' WHERE empid="+txt_UserId.getText();
				queries.delete_add_update(disablequery);
				user_table.removeAll();
				queries.db_table(user_table,executestaement);
				//disablebtns_ncleartxt();
				
				String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','the user disabled "+" "+fnme+" "+lnme+" logon credentials from the system')";
				queries.delete_add_update(updatelog);
				
				JOptionPane.showMessageDialog(null, "You have successfully disabled the user","Success", JOptionPane.PLAIN_MESSAGE);
				cleartextfields();
				}else{
					
				}
			}
		});
		btnDisable.setText("Disable User");
		btnDisable.setEnabled(false);
		btnDisable.setBounds(10, 121, 100, 25);
		
		 btnEnable = new Button(group_2, SWT.NONE);
		btnEnable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fnme= txt_fname.getText().toString();
				lnme= txt_lname.getText().toString();
				
				int confirmantion = JOptionPane.showConfirmDialog(null, "Are you sure you would like to enable the user "+txt_fname.getText()+" "+txt_lname.getText()+"","Confrimation", JOptionPane.YES_NO_OPTION);
				if(confirmantion==JOptionPane.YES_OPTION){
					
				String enablequery="UPDATE users SET active='y' WHERE empid="+txt_UserId.getText();
				queries.delete_add_update(enablequery);
				user_table.removeAll();
				queries.db_table(user_table,executestaement);
			
				String updatelog = "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','the user enabled "+" "+fnme+" "+lnme+" logon credentials')";
				queries.delete_add_update(updatelog);

				JOptionPane.showMessageDialog(null, "You have successfully enabled the user","Success", JOptionPane.PLAIN_MESSAGE);
				cleartextfields();
				}
				else{
					
				}
			}
		});
		btnEnable.setText("Enable User");
		btnEnable.setEnabled(false);
		btnEnable.setBounds(10, 150, 100, 25);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem menu_additional = new MenuItem(menu, SWT.CASCADE);
		menu_additional.setText("Addtional Tools");
		
		Menu menu_1 = new Menu(menu_additional);
		menu_additional.setMenu(menu_1);
		
		MenuItem mntmClearAllFields = new MenuItem(menu_1, SWT.NONE);
		mntmClearAllFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleartextfields();
			}
		});
		mntmClearAllFields.setText("Clear All Fields");
		
		MenuItem menu_item_view_log = new MenuItem(menu_1, SWT.NONE);
		menu_item_view_log.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				individual_log=false;
				String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user viewed the user log table')";
				queries.delete_add_update(updatelog);
			new activity_log(display);
			}
		});
		menu_item_view_log.setText("View User Log");
		
		MenuItem menu_item_individual_log = new MenuItem(menu_1, SWT.NONE);
		menu_item_individual_log.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(txt_UserId.getText().length()==0){
					JOptionPane.showMessageDialog(null, "Please select a user to view their individual log","Warning", JOptionPane.WARNING_MESSAGE);
					
				}
				else{
				individual_log=true;
				if(users.txt_username.getText().equals(login.username)){
					String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user viewed their own log')";
					queries.delete_add_update(updatelog);
					new activity_log(display);
					
				}else{
					String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user view the user log for "+users.txt_username.getText()+"')";
					queries.delete_add_update(updatelog);
					new activity_log(display);
				}
			
				}
			}
		});
		menu_item_individual_log.setText("View Individual User Log");
		
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
				login loginframe= new login();
				loginframe.open();
				
			}
		});
		mntmLogout.setText("Logout");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		queries.db_table(user_table,executestaement);
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("09/03/2014 11:06:57 PM");
		label.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		label.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label.setBounds(455, 0, 164, 14);
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			if(shell.isDisposed()){
				new main_menu(display);
			}
		}
	}
	
	public void tableselection(){
		try {
			System.out.println("i am called");
			//enablebtn(); // enabling buttons and textfields

			//btnAddUser.setEnabled(false);

			Class.forName("org.sqlite.JDBC");
			sqlconnection= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

			int selectedindex = user_table.getSelectionIndex();
			TableItem[] tableRowData = user_table.getItems();
			String user_id=tableRowData[selectedindex].getText(0).toString();

			System.out.println(txt_UserId.getText());
			sqlstatement=sqlconnection.createStatement();

			sqlresult=sqlstatement.executeQuery("SELECT * FROM users WHERE empid='"+user_id+"'");

			if(sqlresult.next()){

				txt_fname.setText(sqlresult.getString("fname").toString());
				txt_lname.setText(sqlresult.getString("lname").toString());
				txt_role.setText(sqlresult.getString("role").toString());
				txt_password.setText(sqlresult.getString("password").toString());
				txt_username.setText(sqlresult.getString("username").toString());
				txt_UserId.setText(sqlresult.getString("empid").toString());

				String level =sqlresult.getString("userlevel").toString();
				if(level.contains("Admin")){
					cmbo_ulevel.select(1);
				}
				else if(level.contains("Doctor")) {
					cmbo_ulevel.select(2);
				}
				else if(level.contains("Nurse")) {
					cmbo_ulevel.select(3);
				}
				else if(level.contains("Receptionist")) {
					cmbo_ulevel.select(4);
				}
				else{
					cmbo_ulevel.select(0);
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
	public void enablebuttons(){// when user double clicks on a record certain buttons are enabled.. Add user button will be disabled
		btnAdd.setEnabled(false);
		btnUpdate.setEnabled(true);
		btnDelete.setEnabled(true);
	    btnDisable.setEnabled(true);
	    btnEnable.setEnabled(true);
	    txt_username.setEditable(false);
	    txt_username.setEnabled(false);
	}
	
	public void cleartextfields(){
		 txt_Filter.setText("");
		 txt_role.setText("");
		 txt_fname.setText("");
		 txt_lname.setText("");
		 txt_username.setText("");
		 txt_password.setText("");
		 txt_UserId.setText("");
		 cmbo_ulevel.select(0);
		 txt_username.setEditable(true);
		 txt_username.setEnabled(true);
			btnAdd.setEnabled(true);
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
		    btnDisable.setEnabled(false);
		    btnEnable.setEnabled(false);
		}


}
