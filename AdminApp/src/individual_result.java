import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;


public class individual_result {

	protected Shell shell;
	private Table individualResultTable;
	private Label label;
	private TableColumn tblclmnresultid,tblclmpatientid,tblclmfilelocation, tblclmnQuestionnairename,tblclmnPatientName;
	private TableColumn tblclmnStatus;
	private TableColumn tblclmnQuestionnaireId;

	protected individual_result(Display display) {
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(554, 381);
		shell.setText("SWT Application");
		String patientname=patients.txt_fname.getText()+" "+patients.txt_lname.getText();
		Group grpAttemptedQuestionnairesBy = new Group(shell, SWT.NONE);
		grpAttemptedQuestionnairesBy.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		grpAttemptedQuestionnairesBy.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpAttemptedQuestionnairesBy.setText("Attempted Questionnaires by "+patientname);
		grpAttemptedQuestionnairesBy.setBounds(10, 20, 518, 311);

		individualResultTable = new Table(grpAttemptedQuestionnairesBy, SWT.BORDER | SWT.FULL_SELECTION);
		individualResultTable.setBounds(10, 26, 498, 275);
		individualResultTable.setHeaderVisible(true);
		individualResultTable.setLinesVisible(true);

		String individual_log = "SELECT * FROM Results WHERE Patient_ID="+patients.txt_patid.getText()+" ";
		sql_queries queries = new sql_queries();
		
		
		tblclmnresultid= new TableColumn(individualResultTable, SWT.NONE);
		tblclmnresultid.setText("Result_ID");
		
		tblclmnQuestionnairename = new TableColumn(individualResultTable, SWT.NONE);
		tblclmnQuestionnairename.setText("Questionnaire Name");
		
		tblclmnPatientName = new TableColumn(individualResultTable, SWT.NONE);
		tblclmnPatientName.setText("Patient Name");
		
		tblclmnStatus = new TableColumn(individualResultTable, SWT.NONE);
		tblclmnStatus.setText("Status");
		
		tblclmnQuestionnaireId = new TableColumn(individualResultTable, SWT.NONE);
		tblclmnQuestionnaireId.setText("Questionnaire ID");
		
		tblclmpatientid = new TableColumn(individualResultTable, SWT.NONE);
		tblclmpatientid.setText("Patient ID");
		
		tblclmfilelocation = new TableColumn(individualResultTable, SWT.NONE);
		tblclmfilelocation.setText("File Location");
		
		queries.db_table(individualResultTable, individual_log);
	
		
		for (int i=0; i<individualResultTable.getColumnCount(); i++) {
	    	 individualResultTable.getColumn (i).pack ();
		        }
		
		label = new Label(shell, SWT.NONE);
		label.setText("09/03/2014 11:06:57 PM");
		label.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		label.setBackground(SWTResourceManager.getColor(211, 211, 211));
		label.setBounds(374, 0, 164, 14);
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			
		}
		shell.dispose();
	}
}
