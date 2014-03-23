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


public class view_edit_result {

	protected Shell shell;
	private Table Resultstable;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;


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
		Resultstable.setBounds(10, 44, 548, 173);
		Resultstable.setHeaderVisible(true);
		Resultstable.setLinesVisible(true);
		
		Label lblNewLabel = new Label(grpResultTable, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel.setBounds(183, 20, 80, 15);
		lblNewLabel.setText("Quick Search");
		
		text = new Text(grpResultTable, SWT.BORDER);
		text.setBounds(269, 17, 135, 21);
		
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
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("09/03/2014 11:06:57 PM");
		label.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		label.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label.setBounds(424, 0, 164, 23);
		
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
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setEnabled(false);
		text_1.setEditable(false);
		text_1.setBounds(138, 263, 176, 21);
		
		Label lblQuestionName = new Label(shell, SWT.NONE);
		lblQuestionName.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblQuestionName.setText("Question Name");
		lblQuestionName.setBounds(10, 300, 122, 15);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setEnabled(false);
		text_2.setEditable(false);
		text_2.setBounds(138, 294, 176, 21);
		
		Label lblEditAnswer = new Label(shell, SWT.NONE);
		lblEditAnswer.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblEditAnswer.setBounds(149, 340, 75, 15);
		lblEditAnswer.setText("Edit Answer");
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(10, 361, 315, 114);
		
		Button btnApplyChanges = new Button(shell, SWT.NONE);
		btnApplyChanges.setBounds(125, 481, 99, 25);
		btnApplyChanges.setText("Apply Changes");
		
		String results= "SELECT * FROM Results";
		sql_queries queries = new sql_queries();
		queries.db_table(Resultstable, results);
		
		TableColumn tblclmnResultId = new TableColumn(Resultstable, SWT.NONE);
		tblclmnResultId.setWidth(100);
		tblclmnResultId.setText("Result ID");
		
		TableColumn tblclmnPatientId = new TableColumn(Resultstable, SWT.NONE);
		tblclmnPatientId.setWidth(100);
		tblclmnPatientId.setText("Patient ID");
		
		TableColumn tblclmnQuestionnaireName = new TableColumn(Resultstable, SWT.NONE);
		tblclmnQuestionnaireName.setWidth(142);
		tblclmnQuestionnaireName.setText("Questionnaire Name");
		
		TableColumn tblclmnNewColumn = new TableColumn(Resultstable, SWT.NONE);
		tblclmnNewColumn.setWidth(130);
		tblclmnNewColumn.setText("Patient Name");
		
		TableColumn tblclmnStatus = new TableColumn(Resultstable, SWT.NONE);
		tblclmnStatus.setWidth(95);
		tblclmnStatus.setText("Status");

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
