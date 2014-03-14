import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class main_menu {

	protected Shell shell;
	static sql_queries queries = new sql_queries();
	static Button btnAdminTools,btnEditQuestn ,btnCreateQuestn;
	static Label lblAdminTools;

	/**
	 * Create contents of the window.
	 */
	protected main_menu(final Display display) {
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		shell.setSize(547, 438);
		shell.setText("SWT Application");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(211, 211, 211));
		composite.setBounds(19, 31, 492, 327);
		
		Label lblImportFromTablet = new Label(composite, SWT.NONE);
		lblImportFromTablet.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblImportFromTablet.setBounds(206, 199, 110, 15);
		lblImportFromTablet.setText("Import From Tablet");
		
		Button btnImport = new Button(composite, SWT.NONE);
	
		btnImport.setImage(SWTResourceManager.getImage("./btn images/android2pcgif.gif"));
		btnImport.setBounds(196, 133, 118, 60);
		
		Button btnTransfer = new Button(composite, SWT.NONE);
		btnTransfer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				transfer transfer2tab= new transfer();
				transfer2tab.tranfer(display);
				
			}
		});
	
		btnTransfer.setImage(SWTResourceManager.getImage("./btn images/pctodroid.gif"));
		btnTransfer.setBounds(28, 133, 118, 60);
		
		Label lblTransferToTablet = new Label(composite, SWT.NONE);
		lblTransferToTablet.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblTransferToTablet.setText("Transfer to Tablet");
		lblTransferToTablet.setBounds(38, 199, 110, 15);
		
		Label lblCreateQuestionnaire = new Label(composite, SWT.NONE);
		lblCreateQuestionnaire.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblCreateQuestionnaire.setText("Create Questionnaire");
		lblCreateQuestionnaire.setBounds(28, 90, 126, 15);
		
	 btnCreateQuestn = new Button(composite, SWT.NONE);
		btnCreateQuestn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				new CreateQ(display);
			}
		});
	
		btnCreateQuestn.setImage(SWTResourceManager.getImage("./btn images/create (2).gif"));
		btnCreateQuestn.setBounds(28, 24, 118, 60);
		
		Button btnEditQuestn = new Button(composite, SWT.NONE);
		btnEditQuestn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
	
							
				// --- Open the Load Questionnaire Window -- //
				try {
					Display display = Display.getDefault();
					LoadQtns shell = new LoadQtns(display);
					shell.open();
					shell.layout();
					shell.setText("Load a Questionnaire");
					while (!shell.isDisposed()) {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				// --- END -- //
				
				// New code starts here now... muwhaha.
				//new LoadQtns(display);
				
			}
		});
	
		btnEditQuestn.setImage(SWTResourceManager.getImage("./btn images/edit_quest.gif"));
		btnEditQuestn.setBounds(194, 24, 118, 60);
		
		Label lblEditQuestionnaire = new Label(composite, SWT.NONE);
		lblEditQuestionnaire.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblEditQuestionnaire.setText("Edit Questionnaire");
		lblEditQuestionnaire.setBounds(204, 90, 110, 15);
		
		btnAdminTools = new Button(composite, SWT.NONE);
		btnAdminTools.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				new users(display);
			}
		});
	
		btnAdminTools.setImage(SWTResourceManager.getImage("./btn images/admintools.gif"));
		btnAdminTools.setBounds(198, 231, 118, 60);
		
	 lblAdminTools = new Label(composite, SWT.NONE);
		lblAdminTools.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblAdminTools.setText("Admin Tools");
		lblAdminTools.setBounds(224, 297, 79, 15);
		
		Button btnViewResult = new Button(composite, SWT.NONE);
		btnViewResult.setImage(SWTResourceManager.getImage("./btn images/view-results.gif"));
		btnViewResult.setBounds(346, 24, 118, 60);
		
		Label lblVieweditResults = new Label(composite, SWT.NONE);
		lblVieweditResults.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblVieweditResults.setText("View/Edit Results");
		lblVieweditResults.setBounds(356, 90, 110, 15);
		
		Button btnPatient = new Button(composite, SWT.NONE);
		btnPatient.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
				new patients(display);
			}
		});
		btnPatient.setImage(SWTResourceManager.getImage("./btn images/Patients.gif"));
		btnPatient.setBounds(346, 133, 118, 60);
	
		Label lblPatientTable = new Label(composite, SWT.NONE);
		lblPatientTable.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblPatientTable.setText("Patient Table");
		lblPatientTable.setBounds(369, 199, 79, 15);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmLogOut = new MenuItem(menu, SWT.NONE);
		mntmLogOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user logged off the system')";
				queries.delete_add_update(updatelog);
				shell.dispose();
				login loginframe= new login();
				loginframe.open();
				
			}
		});
		mntmLogOut.setText("Log out");
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.NONE);
		mntmHelp.setText("Help");
		
		Label btnTimstamp = new Label(shell, SWT.NONE);
		btnTimstamp.setText("09/03/2014 11:06:57 PM");
		btnTimstamp.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		btnTimstamp.setBackground(SWTResourceManager.getColor(211, 211, 211));
		btnTimstamp.setBounds(357, 0, 164, 23);
	
		System.out.println(login.ulevel+"userlevel");
		if (login.ulevel.contains("Admin")){
			btnAdminTools.setVisible(true);
			
			btnEditQuestn.setEnabled(true);
			btnCreateQuestn.setEnabled(true);
		}
			else if(login.ulevel.contains("Doctor")){
				btnEditQuestn.setEnabled(true);
				btnCreateQuestn.setEnabled(true);
				btnAdminTools.setEnabled(false);
			
		}else{
			btnAdminTools.setEnabled(false);
			
			btnEditQuestn.setEnabled(false);
			btnCreateQuestn.setEnabled(false);
			
		}
	
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			if (shell.isDisposed()){
				display.dispose();
				String updatelog= "INSERT INTO log VALUES ('"+login.username+"','"+login.date_timestamp+"','user logged off the system')";
				queries.delete_add_update(updatelog);
			}
		}

	}
}
