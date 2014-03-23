import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
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


public class view_edit_result {

	protected Shell shell;
	private Table Resultstable;
	private Text txt_search;
	private Text txt_questionniare_name;
	private Text txt_qestn_name;
	private Text text_3;
	private Text txt_pname;
	private static String query= "SELECT Result_ID , Patient_ID , Questionnaire_Name , Patient_Name ,Status FROM Results WHERE Status='Complete' OR Status='Incomplete'";

	public view_edit_result(final Display display) {
		// TODO Auto-generated constructor stub
	 
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(604, 571);
		shell.setText("SWT Application");
		
		Group grpResultTable = new Group(shell, SWT.NONE);
		grpResultTable.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpResultTable.setText("Result Table");
		grpResultTable.setBounds(10, 25, 568, 227);
		
		Resultstable = new Table(grpResultTable, SWT.BORDER | SWT.FULL_SELECTION);
		Resultstable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				
				// once doubled clicked on a record the patient name , questionnaire name and list of questions will be loaded
				// questions with missing answers will be highlighted in red ( or text colour can be changed to red)
				// the table will only show completed or incomplete questionnaires
				
				//
			
				
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
		Timestamp.setBounds(424, 0, 164, 23);
		Timestamp.setText(login.date_timestamp.toString());
		
		Group grpListOfQuestions = new Group(shell, SWT.NONE);
		grpListOfQuestions.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpListOfQuestions.setText("List of Questions");
		grpListOfQuestions.setBounds(339, 258, 239, 217);
		
		List list = new List(grpListOfQuestions, SWT.BORDER);
		list.setBounds(10, 21, 219, 186);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel_1.setBounds(10, 266, 122, 15);
		lblNewLabel_1.setText("Questionnaire Name");
		
		txt_questionniare_name = new Text(shell, SWT.BORDER);
		txt_questionniare_name.setEnabled(false);
		txt_questionniare_name.setEditable(false);
		txt_questionniare_name.setBounds(138, 263, 176, 21);
		
		Label lblQuestionName = new Label(shell, SWT.NONE);
		lblQuestionName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblQuestionName.setText("Question Name");
		lblQuestionName.setBounds(10, 319, 122, 15);
		
		txt_qestn_name = new Text(shell, SWT.BORDER);
		txt_qestn_name.setEnabled(false);
		txt_qestn_name.setEditable(false);
		txt_qestn_name.setBounds(138, 313, 176, 21);
		
		Label lblEditAnswer = new Label(shell, SWT.NONE);
		lblEditAnswer.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblEditAnswer.setBounds(149, 340, 75, 15);
		lblEditAnswer.setText("Edit Answer");
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(10, 361, 315, 114);
		
		Button btnApplyChanges = new Button(shell, SWT.NONE);
		btnApplyChanges.setBounds(125, 481, 99, 25);
		btnApplyChanges.setText("Apply Changes");
		
		
		
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
		new sql_queries().db_table(Resultstable,query);

	
	
		
		txt_pname = new Text(shell, SWT.BORDER);
		txt_pname.setEnabled(false);
		txt_pname.setEditable(false);
		txt_pname.setBounds(138, 287, 176, 21);
		
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
}
