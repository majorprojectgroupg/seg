import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Main {

		public static EditQ editQForm;

    public static void main(String[] args) {
        // Each SWT Application must have only one Display instance. No more than that!
				Display display = new Display();
				// Each SWT Application must pass in a Display instance into the constructor of a Shell.
        editQForm = new EditQ(display);
				
				// Copy and paste completed... lol.
				
				// Todo List:
				// 1. Change the 'exportBtn' to UPDATE AN EXISTING json file, and existing rows in the table - DONE.

    }
} 