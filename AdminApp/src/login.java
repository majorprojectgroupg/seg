import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;


public class login {

	protected Shell shell;
	private Text txtusername;
	private Text txtpassword;
	Connection sqlitecon= null;
	ResultSet sqlresult =null;
	public static String username;
	public static String date_timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy h:mm:ss a").format(new Date());
	PreparedStatement sqlstatement=null;
	Statement logstate;
	public static String ulevel; // to store the userlevel globally so other class can access the value
	private JPanel panel;
	
public static Display display = Display.getDefault();
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			login window = new login();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			
		}
		if(shell.isDisposed()){
			display.dispose();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(544, 353);
		shell.setText("SWT Application");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite.setBounds(56, 55, 387, 212);
		
		Label lblUsername = new Label(composite, SWT.NONE);
		lblUsername.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblUsername.setBounds(78, 58, 81, 15);
		lblUsername.setText("UserName");
		
		txtusername = new Text(composite, SWT.BORDER);
		txtusername.setBounds(170, 55, 134, 21);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel.setBounds(78, 98, 81, 15);
		lblNewLabel.setText("Password");
		
		txtpassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtpassword.setBounds(170, 95, 134, 21);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String statement1= "select * FROM users WHERE username=? and password=?";
				try {
					Class.forName("org.sqlite.JDBC");
					sqlitecon= DriverManager.getConnection("jdbc:sqlite:./database.sqlite");

					sqlstatement =sqlitecon.prepareStatement(statement1);
					sqlstatement.setString(1, txtusername.getText());// stores username
					sqlstatement.setString(2, txtpassword.getText());// stores password
					sqlresult=sqlstatement.executeQuery();// executes the sql query

					if (sqlresult.next()){

						String user_level=sqlresult.getString("userlevel").toString();// to check account type..
						String active_status=sqlresult.getString("active").toString();// to see if the account is disabled
						
						ulevel=user_level;// assigning the value to ulevel
						
						System.out.println(active_status);

						if(active_status.contains("n")){// if active = n then the account has been disabled 

							JOptionPane.showMessageDialog(null, "Your account has been Disabled Please contact administrator","Warning", JOptionPane.WARNING_MESSAGE);
							sqlresult.close();
							sqlitecon.close();
						}

						else{// if everything provided is correct the main menu frame will be displayed and login frame will be disposed
									username=txtusername.getText();
										logstate=sqlitecon.createStatement();
									
									logstate.executeUpdate("INSERT INTO log VALUES ('"+username+"','"+date_timestamp+"','user logged on to the system')");
									logstate.close();
									sqlitecon.close();
									sqlresult.close();
									
									shell.setVisible(false);
									main_menu main_menu = new main_menu(display);
									System.out.println("hello");

						
						}
					}

					else{// when the username and password are incorrect
						JOptionPane.showMessageDialog(null, "Make sure your username and password are correct","Alert", JOptionPane.WARNING_MESSAGE);
				
					}



				} catch (SQLException ex) {

					ex.printStackTrace();
				} catch (ClassNotFoundException ex1) {// error message incase the program fails to connect to the database
					JOptionPane.showMessageDialog(null, "connection to the database failed","Warning", JOptionPane.ERROR_MESSAGE);
					ex1.printStackTrace();

				}


			}
		});

				
				

		
		btnNewButton.setBounds(180, 140, 91, 25);
		btnNewButton.setText("Login");

	}
}
