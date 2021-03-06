import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;




public class LoadQtns 
{
	private Table table;
	private Text questionnaireInput;
	private Button loadQtnBtn;


	public Connection connectToDB;
	public Statement statement;
	public String query;
	
	protected static Shell shell;


	public EditQ editQ;


	
	
	public LoadQtns(final Display display) {
		shell = new Shell(display);
		
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));



	

		table = new Table(shell, SWT.BORDER  | SWT.FULL_SELECTION);
		table.setBounds(10, 86, 609, 175);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);


		// -- Add a MouseListener to 'table' -- //


		table.addMouseListener(new MouseListener(){


			@Override
			public void mouseDoubleClick(MouseEvent me) {
				// TODO Auto-generated method stub


				// The method will go here or something.
				// Assigns the values depending on what row was selected.
				//retrieveInfo();
			}


			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				// Assigns the values depending on what row was selected.
				retrieveInfo();
			}


			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				// Assigns the values depending on what row was selected.
				//retrieveInfo();
			}});


		// -- End of MouseListener event -- //


		TableColumn colQtnID = new TableColumn(table, SWT.CENTER);
		colQtnID.setWidth(154);
		colQtnID.setText("Questionnaire ID");


		TableColumn colQtnName = new TableColumn(table, SWT.CENTER);
		colQtnName.setWidth(451);
		colQtnName.setText("Name of the Questionnaire");


		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);


		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setEnabled(false);
		mntmFile.setText("File");


		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);


		MenuItem mntmMainMenu = new MenuItem(menu_1, SWT.NONE);
		mntmMainMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				new main_menu(display);
			}
		});
		mntmMainMenu.setText("Main Menu");


		Label lblSearchQuestionnaires = new Label(shell, SWT.NONE);
		lblSearchQuestionnaires.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblSearchQuestionnaires.setBounds(56, 43, 134, 26);
		lblSearchQuestionnaires.setText("Search Questionnaires:");


		questionnaireInput = new Text(shell, SWT.BORDER);
		questionnaireInput.setBounds(196, 40, 321, 26);
		questionnaireInput.addModifyListener(new ModifyListener(){


			@Override
			public void modifyText(ModifyEvent me)
			{
				String strQuestionnaire = questionnaireInput.getText();
				// If there has been text inputed...
				if(strQuestionnaire.length() > 0)
				{
					query = "SELECT Questionnaire_id, Questionnaire_name FROM Questionnaires WHERE Questionnaire_name LIKE '%" 
							+ strQuestionnaire + "%';";
					// Filters the table depending on the input.
					loadQuestionnaires(query);
				}else
				{
					query = "SELECT Questionnaire_id, Questionnaire_name FROM Questionnaires;";
					// If no text is in the text field, restore the table to default.
					loadQuestionnaires(query);
					//
				}


			}});




		loadQtnBtn = new Button(shell, SWT.NONE);
		loadQtnBtn.setBounds(248, 278, 144, 28);
		loadQtnBtn.setText("Load Question");
		loadQtnBtn.setEnabled(false);


		//loadQtnBtn.addSelectionListener();


		createContents();


		// I shall come back to this in a minute.
		// Populate the tables when the window is initially opened.
		query = "SELECT Questionnaire_id, Questionnaire_name FROM Questionnaires;";
		loadQuestionnaires(query);


		// Load event lulz.
		loadQtnBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent se)
			{
				// This part I shall finish off another time !
				//dispose();


				// Automatically selected the first item in the table 
				retrieveInfo();


				if(EditQ.questionnaireName.isEmpty())
				{
					// Do nothing.
					
					System.out.println("No questionnaire has been selected");
					
					MessageBox sql_message = new MessageBox(shell,SWT.ICON_WARNING |SWT.OK);
					sql_message.setText("Alert!");
					sql_message.setMessage("No questionnaire has been selected");
					sql_message.open();
					
					
				}else
				{


					System.out.println("Selected a Questionnaire");
					// Editing the loaded questionnaire.


					editQ = new EditQ(display);


					//EditQ.qtnInput.setText("I made it");






				}




				// -- Pasted code goes here aha -- //




			}
		});


		// Call main lol




		// ----
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		      shell.dispose();
		      System.out.println("i am using exit button patient");
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


	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		shell.setText("SWT Application");
	shell.setSize(648, 398);


	}


	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	public void loadQuestionnaires(String query)
	{
		// Load the questionnaire names - I will turn it into a method eventually.
				try
				{
					// Clear the table.
					table.removeAll();


					//String query = "";




					// This is needed to set up the SQLite Driver, and also
					// WE MUST ALSO PROVIDE THE 'sqlite-jdbc.jar' file before we submit it.
					Class.forName("org.sqlite.JDBC");
					// Connect to the Database.
					connectToDB = DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
					// Queries will be executed from this instance.
					statement = connectToDB.createStatement();


					ResultSet rs = statement.executeQuery(query);


					int count = 0;


					while(rs.next())
					{
						TableItem tableRow = new TableItem(table, SWT.NONE, count++);
						String columnValues[] = {rs.getString("Questionnaire_id"), rs.getString("Questionnaire_name")};
						tableRow.setText(columnValues);
						//colQtnID.setText(count++, rs.getString("Questionnaire_id"));			
						//colQtnName.setText(count++, rs.getString("Questionnaire_name"));


					}


					// Automatically selected the first item in the table - I need to come back to this section.
					if(table.getItemCount() <= 0)
					{
						// If the LIKE clause does not return any results, do nothing. Otherwise...
						loadQtnBtn.setEnabled(false);


					}else
					{
						// Automatically selected the first item in the table 
						table.setSelection(0);
						// Enable the load button.
						loadQtnBtn.setEnabled(true);
					}






				}catch(SQLException sqle)
				{
					sqle.printStackTrace();
				}
				catch(ClassNotFoundException cnfe)
				{
					cnfe.printStackTrace();
				}


	}
	public void retrieveInfo()
	{
		// Enable the button.
		loadQtnBtn.setEnabled(true);


		int indexOfRow = table.getSelectionIndex();


		System.out.println("TableItem Selected: ");


		// Debug purposes.
		System.out.println(table.getItems()[indexOfRow].getText(0) + " and the name of the question is  " + table.getItems()[indexOfRow].getText(1));


		// Store the ID of the Questionnaire
		EditQ.questionnaireID = Integer.parseInt(table.getItems()[indexOfRow].getText(0));
		// Store the Questionnaire Name
		EditQ.questionnaireName = table.getItems()[indexOfRow].getText(1);


		// Store the Location of the selected (json) Questionnaire 
		try
		{
			// This is needed to set up the SQLite Driver, and also
			// WE MUST ALSO PROVIDE THE 'sqlite-jdbc.jar' file before we submit it.
			Class.forName("org.sqlite.JDBC");
			// Connect to the Database.
			connectToDB = DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
			// Queries will be executed from this instance.
			statement = connectToDB.createStatement();
			// Query that we will run.
			query = "SELECT Questionnaire_location FROM Questionnaires WHERE Questionnaire_id=" + EditQ.questionnaireID + ";";


			ResultSet rs = statement.executeQuery(query);


			while(rs.next())
			{
				// Grab the location and store it for later use.
				EditQ.questionnaireLocation = rs.getString("Questionnaire_location");
			}


			// Debug purposes. So far so good. :)
			System.out.println("Location of selected questionnaire: " + EditQ.questionnaireLocation);








		}catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}
}

