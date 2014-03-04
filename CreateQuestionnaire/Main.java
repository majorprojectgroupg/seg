import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class Main {

		public static CreateQ createQForm;

    public static void main(String[] args) {
        // Each SWT Application must have only one Display instance. No more than that!
				Display display = new Display();
				// Each SWT Application must pass in a Display instance into the constructor of a Shell.
        createQForm = new CreateQ(display);
				
				// the requests have been implemented, and they work fine - even when I had some answers to the answerList' through enable the single choice button, and suddenly switching
				// to user input mode, with the answers still being in the answerList, if I were to click addQtnToList, it doesn't add the answers... yay!
				
				// I just need to work on the previous questions section, and maybe grouping.
				// I also need to get this to input data into the 'Questionnaire' table too.
    }
} 