import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import org.eclipse.swt.SWT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EditQ{
	
	public static Shell shell;
	public static Composite leftPanel, leftMainPanel, leftBottomPanel, radioBtnGroup, rightPanel, rightTopPanel, rightBottomPanel, bottomPanel;
	public static Button singleChoiceBtn, multiChoiceBtn, userInputBtn, dateChoiceBtn, editAnsBtn, deleteAnsBtn, applyAnsBtn, addQtnToListBtn;
	
	public static Text searchInput;
	public static Button prevQtnAddBtn, editQtnBtn, applyQtnBtn, deleteQtnBtn, updateBtn;
	public static Label prevQtnTitleLbl, searchLbl, prevQuestionsLbl, prevAddQtnLbl;
	
	public static Label titleLabel, qtnLbl, qtnTypeLbl, answerLbl, answerListLbl, addQtnToListLbl, qtnListLbl, exportLbl;
	public static Text titleInput, qtnInput, answerInput;
	public static List answerList, prevQtnList, qtnList;
	
	public Connection connectToDB;
	public Statement statement;
	
	public static ArrayList<String> arrayPrevQtnNames;
	public static ArrayList<String> arrayPrevQtnType;
	public static ArrayList<String> arrayPrevAnswers;
	
	public static Menu menuBar, fileMenu, openMenu;
	public static MenuItem fileMenuHeader, openMenuItem, exitMenuItem;
	
	public static FileDialog loadQtnDialog;
	public static String questionnaireName, questionnaireTitle;
	public static int questionnaireID;
	
	
	// Constructor.
	public EditQ(Display display)
	{
		// Initialise the main ArrayLists.
		EditQtnEvents.arrayQuestionNames = new ArrayList<String>();
		EditQtnEvents.arrayQuestionType = new ArrayList<String>();
		EditQtnEvents.arrayAnswers = new ArrayList<String>();
		
		shell = new Shell(display);
		shell.setText("Edit a Questionnaire");
    shell.setSize(1024, 600);
		// Has two columns, and 'makeColumnsEqualWidth' = false.
		
		// MenuBar
		menuBar = new Menu(shell, SWT.BAR);
		menuBar.setVisible(true);
	
    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    fileMenuHeader.setText("&File");

    fileMenu = new Menu(shell, SWT.DROP_DOWN);
    fileMenuHeader.setMenu(fileMenu);
		
		openMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		openMenuItem.setText("&Open");
		// Adds a 'SelectionListener'.
		openMenuItem.addSelectionListener(new EditQtnEvents());
		
		
		exitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exitMenuItem.setText("&Return to Main Menu");
		
		// Set the menu bar.
		shell.setMenuBar(menuBar);
		
		// -- End of menu bar.
		
		// -- Open dialog -- //
		
		loadQtnDialog = new FileDialog(EditQ.shell, SWT.OPEN);
		// When the dialog is opened, it will open in the 'exports' folder
		loadQtnDialog.setFilterPath("./exports/");
		
		
		shell.setLayout(new GridLayout(2, true));
		
		
		
		leftPanel = new Composite(shell, SWT.BORDER);	
		GridLayout gridLayout1 = new GridLayout(1, false);
		//gridLayout1.marginLeft = 5;
		//gridLayout1.marginRight = 5;
		//gridLayout1.marginBottom = 5;
		leftPanel.setLayout(gridLayout1);
		leftPanel.setLayoutData(new GridData(500, 530));
		
		// -- Code for the leftPanel -- //
		
		titleLabel = new Label(leftPanel, SWT.HORIZONTAL);
		titleLabel.setText("Title of Questionnaire:");
		GridData gdLeftLbl = new GridData();
		gdLeftLbl.horizontalSpan = 2;
		titleLabel.setLayoutData(gdLeftLbl);
		
		titleInput = new Text(leftPanel, SWT.SINGLE | SWT.BORDER);
		GridData gdLeftText = new GridData(490, 20);
		gdLeftText.verticalAlignment = GridData.FILL;
		gdLeftText.horizontalAlignment = GridData.FILL;
		gdLeftText.horizontalSpan = 2;
		titleInput.setLayoutData(gdLeftText);
		
	  // -- End of leftPanel -- //
		
		// -- Code for leftMainPanel -- //
		
		leftMainPanel = new Composite(leftPanel, SWT.BORDER);
		// two columns and the columns do not have to have equal width.
		GridLayout gridLayout2 = new GridLayout(2, false);
		//gridLayout2.marginLeft = 5;
		//gridLayout2.marginRight = 5;
		//gridLayout2.marginBottom = 45;
		leftMainPanel.setLayout(gridLayout2);
		leftMainPanel.setLayoutData(new GridData(500, 290));
		
		qtnLbl = new Label(leftMainPanel, SWT.HORIZONTAL);
		GridData gdLeftMain1 = new GridData();
		gdLeftMain1.horizontalSpan = 2;
		gdLeftMain1.horizontalAlignment = GridData.FILL;
		gdLeftMain1.verticalAlignment = GridData.FILL;
		qtnLbl.setLayoutData(gdLeftMain1);
		qtnLbl.setText("Name of the Question:");
		
		qtnInput = new Text(leftMainPanel, SWT.SINGLE | SWT.BORDER);
		GridData gdLeftMain2 = new GridData(490, 20);
		gdLeftMain2.horizontalSpan = 2;
		gdLeftMain2.horizontalAlignment = GridData.FILL;
		gdLeftMain2.verticalAlignment = GridData.FILL;
		qtnInput.setLayoutData(gdLeftMain2);
		
		qtnTypeLbl = new Label(leftMainPanel, SWT.HORIZONTAL);
		qtnTypeLbl.setText("Type of Question:");
		GridData gdLeftMain3 = new GridData();
		gdLeftMain3.horizontalSpan = 2;
		gdLeftMain3.horizontalAlignment = GridData.FILL;
		gdLeftMain3.verticalAlignment = GridData.FILL;
		qtnTypeLbl.setLayoutData(gdLeftMain3);
		
		// -- Type of Answers Radio Group -- //
		
		radioBtnGroup = new Composite(leftMainPanel, SWT.BORDER | SWT.NONE);
		radioBtnGroup.setLayout(new GridLayout(4, false));
		radioBtnGroup.setLayoutData(new GridData(480, 30));
		
		singleChoiceBtn = new Button(radioBtnGroup, SWT.RADIO);
		singleChoiceBtn.setText("Single Choice");
		// This will be automatically selected on runtime.
		singleChoiceBtn.setSelection(true);
		singleChoiceBtn.addSelectionListener(new EditQtnEvents());
		
		
		multiChoiceBtn = new Button(radioBtnGroup, SWT.RADIO);
		multiChoiceBtn.setText("Multiple Choice");
		multiChoiceBtn.addSelectionListener(new EditQtnEvents());
		
		userInputBtn = new Button(radioBtnGroup, SWT.RADIO);
		userInputBtn.setText("User Input");
		userInputBtn.addSelectionListener(new EditQtnEvents());
		
		// I need to carefully implement this.
		dateChoiceBtn = new Button(radioBtnGroup, SWT.RADIO);
		dateChoiceBtn.setText("Date");
		dateChoiceBtn.addSelectionListener(new EditQtnEvents());
		
		// -- End of Answers Radio Group -- //
		
		answerLbl = new Label(leftMainPanel, SWT.HORIZONTAL);
		answerLbl.setText("Specify an Answer: (Press the 'ENTER' key to add an answer to the list)");
		GridData gdLeftMain4 = new GridData();
		gdLeftMain4.horizontalSpan = 2;
		gdLeftMain4.horizontalAlignment = GridData.FILL;
		gdLeftMain4.verticalAlignment = GridData.FILL;
		answerLbl.setLayoutData(gdLeftMain4);
		
		answerInput = new Text(leftMainPanel, SWT.SINGLE | SWT.BORDER);
		// Adds a 'KeyListener'
		answerInput.addKeyListener(new EditQtnEvents());
		GridData gdLeftMain5 = new GridData(490, 20);
		gdLeftMain5.horizontalSpan = 2;
		gdLeftMain5.horizontalAlignment = GridData.FILL;
		gdLeftMain5.verticalAlignment = GridData.FILL;
		answerInput.setLayoutData(gdLeftMain5);
		
		answerListLbl = new Label(leftMainPanel, SWT.HORIZONTAL);
		answerListLbl.setText("List of Answers:");
		GridData gdLeftMain6 = new GridData();
		gdLeftMain6.horizontalSpan = 2;
		gdLeftMain6.horizontalAlignment = GridData.FILL;
		gdLeftMain6.verticalAlignment = GridData.FILL;
		answerListLbl.setLayoutData(gdLeftMain6);
		
		
		answerList = new List(leftMainPanel, SWT.SINGLE | SWT.BORDER);
		//String data[] = {"Welcome to here", "Welcome to near", "Welcome to there", "Welcome to ahahaha", "One more times for my mums"};
		//answerList.setItems(data);
		
		GridData gdLeftMain7 = new GridData(500, 110);
		gdLeftMain7.horizontalSpan = 2;
		gdLeftMain7.horizontalAlignment = GridData.FILL;
		//gdLeftMain7.verticalAlignment = GridData.FILL;
		answerList.setLayoutData(gdLeftMain7);
		
		//leftMainPanel.setLayout(new GridLayout(3, false));
		
		// -- leftBottomPanel -- //
		
		// The SWT.BORDER is for debug purposes.
		leftBottomPanel = new Composite(leftPanel, SWT.BORDER);
		leftBottomPanel.setLayout(new GridLayout(3, false));
		leftBottomPanel.setLayoutData(new GridData(500, 180));
		
		
		
		
		editAnsBtn = new Button(leftBottomPanel, SWT.PUSH);
		// Add SelectionListener event.
		editAnsBtn.addSelectionListener(new EditQtnEvents());
		editAnsBtn.setEnabled(false);
		editAnsBtn.setText("Edit Answer");
		
		// For some reason, anything added to second column of 'leftMainPanel' from here on, does not appear :/ - SOLVED IT!
		
		applyAnsBtn = new Button(leftBottomPanel, SWT.PUSH);
		// Add SelectionListener event.
		applyAnsBtn.addSelectionListener(new EditQtnEvents());
		applyAnsBtn.setEnabled(false);
		applyAnsBtn.setText("Apply");
		
		
		deleteAnsBtn = new Button(leftBottomPanel, SWT.PUSH);
		// Add SelectionListener event.
		deleteAnsBtn.addSelectionListener(new EditQtnEvents());
		deleteAnsBtn.setEnabled(false);
		deleteAnsBtn.setText("Delete Answer");
		
		
		
		addQtnToListLbl = new Label(leftBottomPanel, SWT.HORIZONTAL);
		addQtnToListLbl.setText("Add Question to the List of Questions:");
		GridData gdLeftMain8 = new GridData();
		gdLeftMain8.horizontalSpan = 3;
		//gdLeftMain8.horizontalAlignment = GridData.FILL;
		//gdLeftMain8.verticalAlignment = GridData.FILL;
		addQtnToListLbl.setLayoutData(gdLeftMain8);
		
		addQtnToListBtn = new Button(leftBottomPanel, SWT.PUSH);
		addQtnToListBtn.setText("Add to List");
		// Add a SelectionListener.
		addQtnToListBtn.addSelectionListener(new EditQtnEvents());
		GridData gdLeftMain9 = new GridData();
		//gdLeftMain9.horizontalSpan = 2;
		//gdLeftMain9.horizontalAlignment = GridData.FILL;
		//gdLeftMain9.verticalAlignment = GridData.FILL;
		addQtnToListBtn.setLayoutData(gdLeftMain9);
		
		exportLbl = new Label(leftBottomPanel, SWT.HORIZONTAL);
		exportLbl.setText("Update Questionnaire:");
		GridData gdLeftMain10 = new GridData();
		gdLeftMain10.horizontalSpan = 3;
		gdLeftMain10.horizontalAlignment = GridData.FILL;
		gdLeftMain10.verticalAlignment = GridData.FILL;
		exportLbl.setLayoutData(gdLeftMain10);
		
		updateBtn = new Button(leftBottomPanel, SWT.PUSH);
		updateBtn.setEnabled(false);
		// Add a 'SelectionListener' event.
		updateBtn.addSelectionListener(new EditQtnEvents());
		updateBtn.setText("Update");
		GridData gdLeftMain11 = new GridData();
		//gdLeftMain11.horizontalSpan = 2;
		updateBtn.setLayoutData(gdLeftMain11);
		
		
		
		
		
		// -- leftBottomPanel (end) -- //
		
		// -- End of code for leftMainPanel -- //
		
		// -- Code for rightPanel -- //
	
		rightPanel = new Composite(shell, SWT.BORDER);
		rightPanel.setLayout(new GridLayout(1, false));
		rightPanel.setLayoutData(new GridData(500, 530));
		
		// -- rightTopPanel -- //
		
		rightTopPanel = new Composite(rightPanel, SWT.BORDER);
		rightTopPanel.setLayout(new GridLayout(2, false));
		rightTopPanel.setLayoutData(new GridData(500, 265));
		
		prevQtnTitleLbl = new Label(rightTopPanel, SWT.HORIZONTAL);
		prevQtnTitleLbl.setText("Questions Previously Stored in the Database");
		GridData gdRightTop1 = new GridData();
		gdRightTop1.horizontalSpan = 2;
		//gdRightTop3.verticalAlignment = GridData.FILL;
		gdRightTop1.horizontalAlignment = GridData.FILL;
		prevQtnTitleLbl.setLayoutData(gdRightTop1);
		
		searchLbl = new Label(rightTopPanel, SWT.HORIZONTAL);
		searchLbl.setText("Search:");
		GridData gdRightTop2 = new GridData(490, 30);
		gdRightTop2.horizontalSpan = 2;
		gdRightTop2.verticalAlignment = GridData.FILL;
		gdRightTop2.horizontalAlignment = GridData.FILL;
		prevQtnTitleLbl.setLayoutData(gdRightTop2);
		
		searchInput = new Text(rightTopPanel, SWT.SINGLE | SWT.BORDER);
		// Add a ModifyListener.
		searchInput.addModifyListener(new EditQtnEvents());
		GridData gdRightTop3 = new GridData(490, 20);
		gdRightTop3.horizontalSpan = 2;
		gdRightTop3.verticalAlignment = GridData.FILL;
		gdRightTop3.horizontalAlignment = GridData.FILL;
		searchInput.setLayoutData(gdRightTop3);
		
		prevQuestionsLbl = new Label(rightTopPanel, SWT.HORIZONTAL);
		prevQuestionsLbl.setText("Questions previously added to the database:");
		GridData gdRightTop4 = new GridData();
		gdRightTop4.horizontalSpan = 2;
		gdRightTop4.verticalAlignment = GridData.FILL;
		gdRightTop4.horizontalAlignment = GridData.FILL;
		prevQuestionsLbl.setLayoutData(gdRightTop4);
		
		prevQtnList = new List(rightTopPanel, SWT.SINGLE | SWT.BORDER);
		//String prevQtndata[] = {"Do you like beef", "Are you a veggie?", "Why are you here?", "Is the NHS helpful?", "Does this work?"};
		//prevQtnList.setItems(prevQtndata);
		GridData gdRightTop5 = new GridData(500, 100);
		gdRightTop5.horizontalSpan = 2;
		gdRightTop5.horizontalAlignment = GridData.FILL;
		//gdRightTop5.verticalAlignment = GridData.FILL;
		prevQtnList.setLayoutData(gdRightTop5);
		
		// Load all the questions from the database into the 'prevQtnList'. - IT WORKS GRACEFULLY! :)
		
		// Initialises the ArrayLists.
		//arrayPrevQtnNames = new ArrayList<String>();
		//arrayPrevQtnType = new ArrayList<String>();
		//arrayPrevAnswers = new ArrayList<String>();
		
		String query = "";
		// UNION returns no duplicates ;)
		query = "SELECT question_name, question_type, possible_answers FROM Questions UNION SELECT question_name, question_type, possible_answers FROM Questions;";
		
		// Perform Query on start up.
		EditQtnEvents.loadQuestions(query);
		
		
		// End of 'prevQtnList. - Loading questions from the database into the 'prevQtnList.
		
		prevAddQtnLbl = new Label(rightTopPanel, SWT.HORIZONTAL);
		prevAddQtnLbl.setText("Add Question to the Question List:");
		GridData gdRightTop6 = new GridData();
		gdRightTop6.horizontalSpan = 2;
		gdRightTop6.verticalAlignment = GridData.FILL;
		gdRightTop6.horizontalAlignment = GridData.FILL;
		prevAddQtnLbl.setLayoutData(gdRightTop6);
		
		prevQtnAddBtn = new Button(rightTopPanel, SWT.PUSH);
		prevQtnAddBtn.addSelectionListener(new EditQtnEvents());
		prevQtnAddBtn.setText("Add to List");
		// Disable the button on startup.
		prevQtnAddBtn.setEnabled(false);
		GridData gdRightTop7 = new GridData();
		gdRightTop7.horizontalSpan = 2;
		
		prevQtnAddBtn.setLayoutData(gdRightTop7);
		
		if(arrayPrevQtnNames.size() > 0)
		{
			// Enable the button if there are questions stored in the database.
			prevQtnAddBtn.setEnabled(true);
		}else
		{
			// Do nothing.
		}
		
		// -- rightTopPanel (end) -- //
		
		// -- rightBottomPanel -- //
		
		rightBottomPanel = new Composite(rightPanel, SWT.BORDER);
		rightBottomPanel.setLayout(new GridLayout(3, false));
		rightBottomPanel.setLayoutData(new GridData(500, 200));
		
		qtnListLbl = new Label(rightBottomPanel, SWT.HORIZONTAL);
		qtnListLbl.setText("Questions Currently Added:");
		GridData gdRightBottom1 = new GridData();
		gdRightBottom1.horizontalSpan = 3;
		gdRightBottom1.verticalAlignment = GridData.FILL;
		gdRightBottom1.horizontalAlignment = GridData.FILL;
		qtnListLbl.setLayoutData(gdRightBottom1);
		
		qtnList = new List(rightBottomPanel, SWT.SINGLE | SWT.BORDER);
		//String qtndata[] = {"Do you like beef", "Are you a veggie?", "Why are you here?", "Is the NHS helpful?", "Does this work?"};
		//qtnList.setItems(prevQtndata);
		GridData gdRightBottom2 = new GridData(500, 100);
		gdRightBottom2.horizontalSpan = 3;
		gdRightBottom2.horizontalAlignment = GridData.FILL;
		//gdRightBottom2.verticalAlignment = GridData.FILL;
		qtnList.setLayoutData(gdRightBottom2);
		
		editQtnBtn = new Button(rightBottomPanel, SWT.PUSH);
		editQtnBtn.setEnabled(false);
		editQtnBtn.addSelectionListener(new EditQtnEvents());
		editQtnBtn.setText("Edit Question");
		
		applyQtnBtn = new Button(rightBottomPanel, SWT.PUSH);
		applyQtnBtn.setEnabled(false);
		applyQtnBtn.addSelectionListener(new EditQtnEvents());
		applyQtnBtn.setText("Apply Changes");
		
		deleteQtnBtn = new Button(rightBottomPanel, SWT.PUSH);
		deleteQtnBtn.setEnabled(false);
		deleteQtnBtn.addSelectionListener(new EditQtnEvents());
		deleteQtnBtn.setText("Delete Question");
				
		// -- rightBottomPanel (end) -- //
		
		
		// -- End of code for rightPanel -- //
		
		// -- Code for bottomPanel -- //
		
		/*bottomPanel = new Composite(shell, SWT.BORDER);
		bottomPanel.setLayout(new RowLayout());
		GridData gd1 = new GridData(1010, 50);
		gd1.horizontalSpan = 2;
		bottomPanel.setLayoutData(gd1);*/
		
		// -- end of code for 'bottomPanel -- //
		
		// Makes the shell active.
		shell.open();
		
		// While the shell (Window) is opened do the following.
		while (!shell.isDisposed()) {
				// Listens for events.
				if (!display.readAndDispatch())
						display.sleep();
		}
		// Gets rid of the display events, etc.
		display.dispose();	
	}

} 