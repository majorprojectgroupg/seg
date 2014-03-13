import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;


public class activity_log {

	public static Shell shell;
	public static String date_timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy h:mm:ss a").format(new Date());
	static String executequery = "SELECT * FROM log";
	private static sql_queries queries = new sql_queries();
	private Table logtable;

	public activity_log(final Display display) {
		// TODO Auto-generated constructor stub
	
		shell = new Shell(display);
		shell.setBackground(SWTResourceManager.getColor(211, 211, 211));
		  shell.setSize(559, 446);

		  shell.setText("Group G");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));
		lblNewLabel.setBackground(SWTResourceManager.getColor(211, 211, 211));
		lblNewLabel.setBounds(368, 0, 164, 23);
		lblNewLabel.setText(date_timestamp);
		
		Group grpIndividualLogFor = new Group(shell, SWT.NONE);
		grpIndividualLogFor.setBackground(SWTResourceManager.getColor(211, 211, 211));
		grpIndividualLogFor.setText("User Log");
		grpIndividualLogFor.setBounds(10, 22, 522, 376);
		
		logtable = new Table(grpIndividualLogFor, SWT.BORDER | SWT.FULL_SELECTION);
		logtable.setLinesVisible(true);
		logtable.setHeaderVisible(true);
		logtable.setBounds(0, 17, 522, 349);
		
		
		
		if(users.individual_log==true){
			String individual_log = "SELECT * FROM log WHERE username='"+users.txt_username.getText()+"'";
			grpIndividualLogFor.setText("User Log for the username "+users.txt_username.getText());
			queries.db_table(logtable, individual_log);
		}
		else{
			queries.db_table(logtable, executequery);// calling the db_table_log method so the table gets populated
			
		}
		
	
		
		shell.open();

		

				// While the shell (Window) is opened do the following.
				while (!shell.isDisposed()) {
						// Listens for events.
						if (!display.readAndDispatch())
								display.sleep();
				}
				// Gets rid of the display events, etc.
			shell.dispose();
			if(shell.isDisposed()){
				//users.individual_log.
			}
				
	}

	
}
