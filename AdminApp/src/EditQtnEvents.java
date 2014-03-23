

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;




class EditQtnEvents implements SelectionListener, KeyListener, ModifyListener
{
	//public static ArrayList<String> arrayQuestionNames = new ArrayList<String>();
	//public static ArrayList<String> arrayQuestionType = new ArrayList<String>();
	//public static ArrayList<String> arrayAnswers = new ArrayList<String>();




	public static ArrayList<String> arrayQuestionNames;
	public static ArrayList<String> arrayQuestionType;
	public static ArrayList<String> arrayAnswers;




	//public static String EditQ.questionnaireName;
	//public static int EditQ.questionnaireID;




	public static Connection connectToDB;
	public static Statement statement;




	//public FileDialog loadQtnDialog;


	private static 	MessageBox wmessages, succ_message;






	public void widgetDefaultSelected(SelectionEvent e)
	{
		// This function will not require any further implementation.		
	}
	public void widgetSelected(SelectionEvent e)
	{
		wmessages = new MessageBox(EditQ.shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(EditQ.shell,SWT.ICON_INFORMATION|SWT.OK);



		if(e.getSource() == EditQ.singleChoiceBtn)
		{
			// Enable the 'answerInput' field.
			EditQ.answerInput.setEnabled(true);




		}
		if(e.getSource() == EditQ.multiChoiceBtn)
		{
			// Enable the 'answerInput' field.
			EditQ.answerInput.setEnabled(true);
		}
		if(e.getSource() == EditQ.userInputBtn)
		{
			// Disable the 'answerInput' field.
			EditQ.answerInput.setEnabled(false);	
		}
		if(e.getSource() == EditQ.dateChoiceBtn)
		{
			// Disable the 'answerInput' field.
			EditQ.answerInput.setEnabled(false);
		}




		//  -- 'addQtnToList' - Adding the Questions to the List.	-- //




		if(e.getSource() == EditQ.addQtnToListBtn)
		{
			// Calls the method which adds the Question to the List. - Works as expected! :) - I can't see no obvious bugs ;D
			maintainList(EditQ.addQtnToListBtn);
		}	
		//  -- END of 'addQtnToList' - Adding the Questions to the List.	-- //








		// -- 'editAnsBtn' -- Editing answers in the list. -- //




			if(e.getSource() == EditQ.editAnsBtn)
			{
				// Debug purposes.
				System.out.println("'editAnsBtn was clicked.'");
				// If the 'answerList' is not empty...
				if(EditQ.answerList.getItemCount() > 0)
				{
					// Enable the 'applyAnsBtn'
					EditQ.applyAnsBtn.setEnabled(true);




					// Grab the selected String.
					String selectedAnswer = EditQ.answerList.getSelection()[0];




					// Debug purposes.
					System.out.println("Editing \"" + selectedAnswer + "\"");




					// Places the selected text in the textfield so the user can edit.
					EditQ.answerInput.setText(selectedAnswer);
					// Highlight the text
					// I need this to work somehow! lol
					//EditQ.answerInput.setSelection(0, selectedAnswer.length());




					// Disables the 'editAnsBtn' after the button has been clicked.
					EditQ.editAnsBtn.setEnabled(false);




				}
			}




			// -- 'applyAnsBtn' -- Apply the new answer -- //




			if(e.getSource() == EditQ.applyAnsBtn)
			{
				// This sections works the way I want it to.
				// Grab the selected String.
				String selectedAnswer = EditQ.answerList.getSelection()[0];
				// Grab the selected index.
				int indexOfAnswerList = EditQ.answerList.getSelectionIndex();
				// New input
				EditQ.answerInput.getText();
				// Remove old answer.
				EditQ.answerList.remove(selectedAnswer);
				// Adds the new answer to the 'answerList'.
				EditQ.answerList.add(EditQ.answerInput.getText(), indexOfAnswerList);




				// Disables the 'applyAnsBtn' after the button has been clicked.
				EditQ.applyAnsBtn.setEnabled(false);
				// enables the 'editAnsBtn' after the button 'applyAnsBtn has been clicked.
				EditQ.editAnsBtn.setEnabled(true); // -- I need to check this section for possible errors.




				// select the first answer.
				EditQ.answerList.select(indexOfAnswerList);




			}




			// -- End of 'applyAnsBtn' -- Applies the changed name -- //




			// -- END of 'editAnsBtn' -- Editing/ answers in the list. -- //




			// -- 'deleteAnsBtn' -- Deleting answers in the list //




			if(e.getSource() == EditQ.deleteAnsBtn)
			{
				//If the answerList is not empty...
				if(EditQ.answerList.getItemCount() > 0)
				{




					System.out.println("deleteAnsBtn");
					// Grab the selected String.
					String selectedAnswer = EditQ.answerList.getSelection()[0];




					// Deletes the answer from the list.
					EditQ.answerList.remove(selectedAnswer);


					

					if(EditQ.answerList.getItemCount() < 1)
					{
						  // Enable the 'editAnsBtn', 'applyAnsBtn' and 'deleteAnsBtn' buttons
							EditQ.editAnsBtn.setEnabled(false);
							EditQ.applyAnsBtn.setEnabled(false);
							EditQ.deleteAnsBtn.setEnabled(false);
					}




					// select the first answer.
					EditQ.answerList.select(0);		
				}
			}		
			// -- 'deleteAnsBtn' -- Deleting answers in the list //




			// -- 'updateBtn' - Exports the Questionnaire to a Text File. -- //




			// This entire section works as expected :D
			if(e.getSource() == EditQ.updateBtn)
			{
				// If a title for the questionnaire has been specified, do the following...
				if(EditQ.titleInput.getCharCount() > 1)
				{
					// If a questionnaire has not been selected from the FileDialog...
					if(EditQ.questionnaireName.isEmpty())
					{	
						succ_message.setMessage("Please Select a questionnaire");
						succ_message.setText("Alert!");
						succ_message.open();
						// Do nothing.
						System.out.println("No questionnaire has been selected");
					}else
					{
						// For debug purposes.
						String exportString = "";


						// Export.
						String jsonString = "";


						int noOfQtns = arrayQuestionNames.size();																				
						String query = "";




						try
						{
							// This is needed to set up the SQLite Driver, and also
							// WE MUST ALSO PROVIDE THE 'sqlite-jdbc.jar' file before we submit it.
							Class.forName("org.sqlite.JDBC");
							// Connect to the Database.
							connectToDB = DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
							// Queries will be executed from this instance.
							statement = connectToDB.createStatement();




							// For some reason 'EditQ.questionnaireID' is equal to where it should not be 0 :/




							// Delete the existing questions from the 'Questions' table.
							query = "DELETE FROM Questions WHERE question_id=" + EditQ.questionnaireID + ";";




							// Debug purposes.
							// For some reason 'EditQ.questionnaireID' is equal to where it should not be 0  and EditQ.questionnaireName is null too.
							// Okay, it is sorted now. For some reason, it doesn't initialise in time if I declare the variables in this class so
							// I declared it in 'EditQ.java' instead.
							System.out.println("QuestionnaireID from 'updateBtn': " + EditQ.questionnaireID);
							System.out.println("QuestionnaireName from 'updateBtn': " + EditQ.questionnaireName);


							statement.executeUpdate(query);


							// Modifying the JSON Export...


							jsonString = "{\n" +
																"\"questionnaire\" : [" + "\n" ;








							for(int x = 0;x<noOfQtns;x++)
							{
								// Insert the question_name (and its other values) into the 'Questions' table.
								query = "INSERT INTO Questions(question_id, question_name, question_type, possible_answers)" +
												"VALUES(" + EditQ.questionnaireID +  ", '" + arrayQuestionNames.get(x) + "', '" + arrayQuestionType.get(x) + "',\"" + arrayAnswers.get(x) + "\");";
								// This is working fine.
								statement.executeUpdate(query);




							}




							// I NEED TO TEST THIS PART OUT THOROUGHLY.
							// Updates the 'Questionnaires' table with the questionnaire title ;) - Works hehe :)
							query = "UPDATE Questionnaires SET Questionnaire_name='" + EditQ.titleInput.getText() + "' WHERE Questionnaire_name='" + EditQ.questionnaireName + "' AND Questionnaire_id=" + EditQ.questionnaireID + ";";
							statement.executeUpdate(query);




							statement.close();	 // Works.
							connectToDB.close(); // Works.




						}catch(ClassNotFoundException cnfe)
						{
							cnfe.printStackTrace();




						}catch(SQLException sqle)
						{
							sqle.printStackTrace();
						}




						for(int x = 0;x<noOfQtns;x++)
						{
							// Debug purposes.
							exportString = exportString + (arrayQuestionNames.get(x) + "+" + arrayQuestionType.get(x) + "+" + arrayAnswers.get(x) + "and the length of answers is: " + arrayAnswers.get(x).length() + "\n");




							// Builds the JSON output.
							jsonString = jsonString + "{ \"question\":\"" + arrayQuestionNames.get(x) + "\", \"type\":\"" + arrayQuestionType.get(x) + "\", \"answers\":\"" + arrayAnswers.get(x) + "\"},\n";		




						}
						// Drops the trailing comma, and other crap: "},\n - that is 3 letters in length
						jsonString = jsonString.substring(0, (jsonString.length()-3));
						// Seals up the overall JSON object with: '}]}'
						jsonString = jsonString + "}\n]\n}";
						// I could have done this in the loop but, it could be exhaustive for the computer
						// as checking whether we have reached the last question in a if statement might slow things down 




						// Debug purposes.
						System.out.println(exportString);
						System.out.println("The Questionnaire has been exported:\n" + jsonString);

					
						
						succ_message.setMessage("The Questionnaire has been updated successfully");
						succ_message.setText("Success!");
						succ_message.open();



						// So far so good - I NEED TO MAKE IT SO THAT IT UPDATES THE JSON FILE TOO ! ;D




						// BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("./exports/questionnaire.json")));
						// I will later implement this section so it can detect what OS the application is being ran on. 		
						try
						{




							// Overwrites the existing JSON File, assigning a value to uniquely identify the file ;)
							// I need to change this bit too.
							BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("./exports/questionnaire_" + EditQ.questionnaireID +".json")));
							// Write to the file.
							bufferedWriter.write(jsonString); 
							bufferedWriter.close();	// Closes the stream.




						}catch(NullPointerException ne)
						{




						}catch(IOException ioe)
						{




						}




						// Should load up the 'prevQtnList' in real time ;)
						// UNION returns no duplicates ;)
						query = "SELECT question_name, question_type, possible_answers FROM Questions UNION SELECT question_name, question_type, possible_answers FROM Questions;";




						// Perform Query on start up.
						loadQuestions(query);




						if(EditQ.arrayPrevQtnNames.size() > 0)
						{
							// Enable the button if there are questions stored in the database.
							EditQ.prevQtnAddBtn.setEnabled(true);
						}else
						{
							// Do nothing.
						}




						// -- Test this section thoroughly (when time allows me to) -- //




						// Disable the update button.
						EditQ.updateBtn.setEnabled(false);
						// Disable the 'editQtnBtn'
						EditQ.editQtnBtn.setEnabled(false);
						// Disable the 'deleteQtnBtn'
						EditQ.deleteQtnBtn.setEnabled(false);








						// Empty the Arrays for that correspond to 'qtnList'.
						arrayQuestionNames = new ArrayList<String>();
						arrayQuestionType = new ArrayList<String>();
						arrayAnswers = new ArrayList<String>();




						// Clear the 'qtnList'.
						EditQ.qtnList.removeAll();
						// Clear the 'titleInput' field
						EditQ.titleInput.setText("");


						// I need to look into this section throughly
						EditQ.shell.dispose();
						//EditQ.shell.getDisplay().dispose();
						// -- End of test -- //
					}




				}else
				{
					wmessages.setMessage("Please specify a title for the questionnaire");
					wmessages.setText("Alert!");
					wmessages.open();
					// Debug purposes.
					System.out.println("Please specify a title for the questionnaire");
				}




			}




			// --  END of 'updateBtn' - Overwrites the an existing json File, and updates the database. -- //




			// -- 'editQtnBtn' - Allows the administrator to edit questionnaires -- //




			if(e.getSource() == EditQ.editQtnBtn)
			{
				// If the list is not empty.
				if(EditQ.qtnList.getItemCount() > 0)
				{
					// Prevent any user interaction until the user has clicked 'applyQtnBtn'.
					EditQ.qtnList.setEnabled(false);




					// Prevent the user from adding a question to a list during 'editing mode'.
					EditQ.addQtnToListBtn.setEnabled(false);




					// Prevent the user from adding a question from the 'prevQtnList' to the current list during 'editing mode'.
					EditQ.prevQtnAddBtn.setEnabled(false);




					// Disable the 'editQtnBtn'
					EditQ.editQtnBtn.setEnabled(false);
					// Disable the 'deleteQtnBtn'.
					EditQ.deleteQtnBtn.setEnabled(false);




					// Enable the 'applyQtnBtn'
					EditQ.applyQtnBtn.setEnabled(true);




					// Disable the 'updateBtn'
					EditQ.updateBtn.setEnabled(false);




					// Index of the selected question.
					int selectedQtnIndex = EditQ.qtnList.getSelectionIndex();
					// Set the text input to the question name you selected to modify.
					EditQ.qtnInput.setText((arrayQuestionNames.get(selectedQtnIndex)));
					// Set the radio button to the type you specified for the question.
					String currentQuestionType = arrayQuestionType.get(selectedQtnIndex);




					// Debug.
					System.out.println("The currentQuestionType =" + currentQuestionType);








					// I don't think I need this anymore aha. Ok, maybe I do aha. -
					// Using the '==' operator instead of using '.equals()', the following four conditions ends up being false...
					// ...if the question name, etc, was obtained came from the array that was initialised from the database...
					// ... Well, it works now. Thank goodness.




					// from the database using the '==' operator instead of using '.contains()' 
					if(currentQuestionType.equals("SingleChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);
						EditQ.singleChoiceBtn.setSelection(true);
						EditQ.multiChoiceBtn.setSelection(false);
						EditQ.userInputBtn.setSelection(false);
						EditQ.dateChoiceBtn.setSelection(false);




						// Enable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
						EditQ.editAnsBtn.setEnabled(true);
						EditQ.deleteAnsBtn.setEnabled(true);
						EditQ.answerInput.setEnabled(true);
					}
					else if(currentQuestionType.equals("MultiChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);
						EditQ.multiChoiceBtn.setSelection(true);
						EditQ.singleChoiceBtn.setSelection(false);
						EditQ.userInputBtn.setSelection(false);
						EditQ.dateChoiceBtn.setSelection(false);




						// Enable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
						EditQ.editAnsBtn.setEnabled(true);
						EditQ.deleteAnsBtn.setEnabled(true);
						EditQ.answerInput.setEnabled(true);








					}
					else if(currentQuestionType.equals("UserInputChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);




						EditQ.userInputBtn.setSelection(true);
						EditQ.singleChoiceBtn.setSelection(false);
						EditQ.multiChoiceBtn.setSelection(false);
						EditQ.dateChoiceBtn.setSelection(false);
					}
					else if(currentQuestionType.equals("DateChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);




						EditQ.dateChoiceBtn.setSelection(true);
						EditQ.singleChoiceBtn.setSelection(false);
						EditQ.multiChoiceBtn.setSelection(false);
						EditQ.userInputBtn.setSelection(false);
					}else
					{
						// Debug purposes.
						System.out.println("For some reason, none of the IF Statements are getting called but, this one gets called. This Question equals:" + currentQuestionType + ".");
					}




					// Gets the String of answers according to the question selected.
					String currentQtnAnswer = arrayAnswers.get((selectedQtnIndex));
					// Debug purposes.
					System.out.println("The String of answers=" + currentQtnAnswer);








						// Automatically select the first answer from 'answerList' if either single choice or multi choice selected...				
						if(EditQ.singleChoiceBtn.getSelection() || EditQ.multiChoiceBtn.getSelection())
						{
							// Split the String of answers into an array.
							String answers[] = currentQtnAnswer.split("@");




							// Set the 'answerList' to the answers according to the question selected.
							EditQ.answerList.setItems(answers);




							// Set the 'answerList' to automatically select the first question.	- THIS MAY NOT WORK WITH 'userInputBtn' and 'dateChoiceBtn'.
							EditQ.answerList.setSelection(0);




							// Debug purposes.
							System.out.println("Answers for the selected Question is: " + currentQtnAnswer + " and the length of the array is: " + answers.length);




						}else
						{




							// Debug purposes.
							System.out.println("Answer for the selected Question is: " + currentQtnAnswer + " and the length of the String is: " + currentQtnAnswer.length());		
						}












				}
			}




			// -- END of 'editQtnBtn' - Allows the administrator to edit questionnaires -- //




			// -- 'applyQtnBtn' - Applies the changes made to the question -- //




			if(e.getSource() == EditQ.applyQtnBtn)
			{
				maintainList(EditQ.applyQtnBtn); // I can't find any obvious bugs ;) !
			}




			// -- End of 'applyQtnBtn' - Applies the changes made to the question -- //




			// -- 'deleteQtnBtn' - Delete a question from the current list of questions -- //




			if(e.getSource() == EditQ.deleteQtnBtn)
			{
				//If the 'qtnList' is not empty...
				if(EditQ.qtnList.getItemCount() > 0)
				{
					int selectedQtnIndex = EditQ.qtnList.getSelectionIndex();

					


					System.out.println("deleteQtnBtn has been pressed.");
					// Grab the String representation of the question selected.
					//String selectedQuestion = EditQ.answerList.getSelection()[0];




					// Delete the Question from the list.
					EditQ.qtnList.remove(selectedQtnIndex);
					// Delete the Question from 'arrayQuestionNames'
					arrayQuestionNames.remove(selectedQtnIndex);
					// Delete the questionType from 'arrayQuestionType'
					arrayQuestionType.remove(selectedQtnIndex);
					// Delete the String of answers from 'arrayAnswers'
					arrayAnswers.remove(selectedQtnIndex);

					
		succ_message.setMessage("The Selected question has been deleted from the questionnaire");
		succ_message.setText("Alert!");
		succ_message.open();

					if(EditQ.qtnList.getItemCount() < 1)
					{
						  // Disaable the 'editQtnBtn', 'applyQtnBtn' and 'deleteQtnBtn' buttons
							EditQ.editQtnBtn.setEnabled(false);
							EditQ.applyQtnBtn.setEnabled(false);
							EditQ.deleteQtnBtn.setEnabled(false);




							// Disable 'updateBtn'
							EditQ.updateBtn.setEnabled(false);




					}




					// select the first answer.
					EditQ.qtnList.select(0);		
				}
			}		
			// -- END of 'deleteQtnBtn' - Delete a question from the current list of questions -- //




			// -- 'prevQtnAdd' - Adding previously stored questions from the database to the main list -- //




			if(e.getSource() == EditQ.prevQtnAddBtn)
			{
				// Adds the selected question from 'prevQtnList' to the main list, 'qtnList'.
				addPreviousQuestion();




			}








			// -- END of 'prevQtnAdd' - Adding previously stored questions from the database to the main list -- //




			// -- 'openMenuItem' code -- This section is no longer needed :) //






			// -- End of 'openMenuItem' code -- //




	}	// End of the 'widgetSelected(SelectionEvent e)' function.




	public void keyPressed(KeyEvent e)
	{
		wmessages = new MessageBox(EditQ.shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(EditQ.shell,SWT.ICON_INFORMATION|SWT.OK);

		if(e.getSource() == EditQ.answerInput)
		{
			// If the carriage return key is pressed - i.e. the ENTER key...
			if(e.keyCode == SWT.CR)
			{
				// I need to add some code for when the ENTER key is pressed during the process of an edit - i.e. 'editAnsBtn'. PLEASE CODE!!!




				// Debug purposes.
				System.out.println("Enter button was clicked:");
				// Store answer in a temporary String.
				String questionAnswer = EditQ.answerInput.getText();
				// If the answer is at least 2 characters in length...
				if(questionAnswer.length() > 1)
				{
					// Add answer to answerList.
					EditQ.answerList.add(questionAnswer);








					// Enable the 'editAnsBtn' and 'deleteAnsBtn' buttons - Rewrite this code for when an item in the list is selected.
					EditQ.editAnsBtn.setEnabled(true);
					EditQ.deleteAnsBtn.setEnabled(true);




					// Clear the field.
					EditQ.answerInput.setText("");




					// select the first answer.
					EditQ.answerList.select(0);




				}
















			}




		}
	} // End of 'keyPressed(KeyEvent e)...' event.




	public void keyReleased(KeyEvent e)
	{
		// This function does not require any further implemention.
	}




	public void modifyText(ModifyEvent e)
	{
		wmessages = new MessageBox(EditQ.shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(EditQ.shell,SWT.ICON_INFORMATION|SWT.OK);

		// 'searchInput' -- //




		if(e.getSource() == EditQ.searchInput)
		{
			// Query statement.




			String query = "";




			if(EditQ.searchInput.getCharCount() > 0)
			{
				// Use %LIKE%
				String searchInput = EditQ.searchInput.getText();
				// Retrieves the questions from the database that contains the expression of specfied in 'searchInput', and stores it in a ResultSet.
				// UNION returns no duplicates ;)
				query = "SELECT question_name, question_type, possible_answers FROM Questions WHERE question_name LIKE '%" + searchInput + "%' UNION SELECT question_name, question_type, possible_answers FROM Questions WHERE question_name LIKE '%" + searchInput + "%';";
				// Performs the Query. - WORKS GRACEFULLY
				loadQuestions(query);




			}else
			{
				// Automatically load all the questions hehe.
				// UNION returns no duplicates ;)
				query = "SELECT question_name, question_type, possible_answers FROM Questions UNION SELECT question_name, question_type, possible_answers FROM Questions;";
				// Performs the Query - WORKS GRACEFULLY
				loadQuestions(query);
			}
		}




		// End of 'searchInput' field -- //




	} // End of 'modifyText(ModifyEvent e)'








	// Custom methods
	public void maintainList(Button btn)
	{
		// Temp variable that will hold the question name that was inputted.
		String questionName = "";
		// A temp variable that will hold the type of question selected.
		String questionType = "";
		// A temp variable that will hold a list of answers.
		String questionAnswer = "";


		wmessages = new MessageBox(EditQ.shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(EditQ.shell,SWT.ICON_INFORMATION|SWT.OK);






		// I need to add more validations.
		// If the field has at least two characters...
		if(EditQ.qtnInput.getCharCount() > 1)
		{		
				// Store the name of the question inputted (or that is already in the 'qtnInput' field).
				questionName = EditQ.qtnInput.getText();




				// The three radio buttons for the 'Type of Question'
				if(EditQ.singleChoiceBtn.getSelection() == true)
				{
					questionType = "SingleChoice";
				}
				else if(EditQ.multiChoiceBtn.getSelection() == true)
				{
					questionType = "MultiChoice";
				}
				else if(EditQ.userInputBtn.getSelection() == true)
				{
					questionType = "UserInputChoice";
				}
				else if(EditQ.dateChoiceBtn.getSelection() == true)
				{
					questionType = "DateChoice";
				}




				// If the 'answerList' is not empty, or either of the selected radio buttons are pressed...
				if(EditQ.answerList.getItemCount() > 0 || EditQ.userInputBtn.getSelection() || EditQ.dateChoiceBtn.getSelection())
				{
					// Add each answer to a String that will form a String of answers.
					int lengthOfList = EditQ.answerList.getItemCount();




					// If the 'userInputBtn' or 'dateChoiceBtn' are selected, 'questionAnswer' will have an empty String value.
					if(EditQ.userInputBtn.getSelection() || EditQ.dateChoiceBtn.getSelection())
					{
						// Do nothing. (i.e. 'questionAnswer' will have a value of "" - In other words, an empty string but, still has a length of 1).




					}else
					{
						// Otherwise, do the following...
						for(int x = 0;x<lengthOfList;x++)
						{
								if((x+1) == lengthOfList)
								{
									// This will drop the '-' character for when we add the last answer to the String.
									questionAnswer = questionAnswer + EditQ.answerList.getItem(x);
								}else
								{
									// Errr, yeah... lol.
									questionAnswer = questionAnswer + (EditQ.answerList.getItem(x) + "@");	
								}
							}
					}








					// if the 'addQtnToListBtn' was clicked...
					if(btn == EditQ.addQtnToListBtn)
					{
						// Add the name of the question inputted, to the array.
						arrayQuestionNames.add(questionName);




						// Adds the questionType to the 'arrayQuestionType'
						arrayQuestionType.add(questionType);




						// Now we add the String of answers to 'arrayAnswers'
						arrayAnswers.add(questionAnswer);
						// So far so good :)




						// Add the Question to the List  'qtnList' - Currently added questions.
						EditQ.qtnList.add(questionName);








						// ------ I NEED TO MODIFY THIS SECTION SO I CAN ADD IT FOR THE OTHER BUTTONS TOO! ALL DONE ----- //




						// Enable 'editQtnBtn', 'applyQtnBtn', and 'deleteQtnBtn'
						EditQ.editQtnBtn.setEnabled(true);
						//EditQ.applyQtnBtn.setEnabled(true);
						EditQ.deleteQtnBtn.setEnabled(true);




						// Enable 'updateBtn'
						EditQ.updateBtn.setEnabled(true);




						// Disable the 'editAnsBtn' and 'deleteAnsBtn'
						EditQ.editAnsBtn.setEnabled(false);
						EditQ.deleteAnsBtn.setEnabled(false);








						// Set the 'qtnList' to automatically select the most recent question added. - WORKS NICELY! :)
						EditQ.qtnList.setSelection(EditQ.qtnList.getItemCount()-1);












						// Clean up the 'qtnInput' field, 'answerInput' field, 'answerList'
						EditQ.qtnInput.setText("");
						EditQ.answerInput.setText("");
						EditQ.answerList.removeAll();








						// Debug purposes.
						System.out.println("questionName: " + questionName + ", questionType: " + questionType + " questionAnswer=" + questionAnswer);	




						// ------ I NEED TO MODIFY THIS SECTION SO I CAN ADD IT FOR THE OTHER BUTTONS TOO! ALL DONE ----- //			








					}
					// if the 'applyQtnBtn' was clicked...
					else if(btn == EditQ.applyQtnBtn)
					{
						int selectedQtnIndex = EditQ.qtnList.getSelectionIndex();
						// Keep a reference of the old question that we can later use to delete, edit, etc. 
						String oldQuestionName = questionName;




						// Allow the user to, once again, interact with the 'qtnList'.
						EditQ.qtnList.setEnabled(true);




						// Allow the user to, once again, add a question to the 'qtnList'.
						EditQ.addQtnToListBtn.setEnabled(true);
						// Allow the user to, once again, add a question from 'prevQtnList' to the main list.
						EditQ.prevQtnAddBtn.setEnabled(true);




						// Delete the old Question from the 'qtnList'.
						EditQ.qtnList.remove(selectedQtnIndex);
						// Delete the old Question from the array too.
						arrayQuestionNames.remove(selectedQtnIndex);				
						// Add the modfied name of the question inputted, to the 'qtnList'
						EditQ.qtnList.add(questionName, selectedQtnIndex);
						// Add the modified name of the question inputted, to the array.
						arrayQuestionNames.add(selectedQtnIndex, questionName);












						// Delete the old 'questionType' from the 'arrayQuestionType'
						arrayQuestionType.remove(selectedQtnIndex);
						// Adds the new modified 'questionType' to 'arrayQuestionType'
						arrayQuestionType.add(selectedQtnIndex, questionType);
						// Delete the old 'questionAnswer' from 'arrayAnswers'
						arrayAnswers.remove(selectedQtnIndex);
						// Now we add the new String of answers to 'arrayAnswers'
						arrayAnswers.add(selectedQtnIndex, questionAnswer);
						// So far so good :) - CONFIRMED!




						// Clean up the 'qtnInput' field, 'answerInput' field, and 'answerList'
						EditQ.qtnInput.setText("");
						EditQ.answerInput.setText("");
						EditQ.answerList.removeAll();




						// Set radio button 'singleChoiceBtn' to be selected. - I could delete this.
						EditQ.singleChoiceBtn.setSelection(true);
						EditQ.multiChoiceBtn.setSelection(false);
						EditQ.userInputBtn.setSelection(false);
						EditQ.dateChoiceBtn.setSelection(false);
						// CONTINUE FROM HERE!








						// Enable the 'editQtnBtn'
						EditQ.editQtnBtn.setEnabled(true);
						// Disable the 'applyQtn'
						EditQ.applyQtnBtn.setEnabled(false);
						// Enable the 'deleteQtnBtn'.
						EditQ.deleteQtnBtn.setEnabled(true);
						// Enable the 'updateBtn'
						EditQ.updateBtn.setEnabled(true);




						// Disable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
						EditQ.editAnsBtn.setEnabled(false);
						EditQ.deleteAnsBtn.setEnabled(false);
						EditQ.applyAnsBtn.setEnabled(false);












						// Sets the list to select the modifed question.
						EditQ.qtnList.setSelection(selectedQtnIndex);				




					}




				}else
				{
					wmessages.setMessage("Please Provide Answers");
					wmessages.setText("Alert!");
					wmessages.open();
				// if 'answerList' - A dialog box should pop up if there are no answers supplied!
					System.out.println("At least input an answer!");
				}						
		}else
		{		
			wmessages.setMessage("Please Type in a Question");
			wmessages.setText("Alert!");
			wmessages.open();
			
			// if 'qtnInput' - A dialog box should pop up if the text is empty!
			System.out.println("Type in a Question!");
		}




		//-------------------------------//-------------------------------//-------------------------------//








	}




	public static void loadQuestions(String query)
	{
		// Empty the ArrayLists.
		EditQ.arrayPrevQtnNames = new ArrayList<String>();
		EditQ.arrayPrevQtnType = new ArrayList<String>();
		EditQ.arrayPrevAnswers = new ArrayList<String>();








		try
		{
			//String query = "";
			// This is needed to set up the SQLite Driver, and also
			// WE MUST ALSO PROVIDE THE 'sqlite-jdbc.jar' file before we submit it.
			Class.forName("org.sqlite.JDBC");
			// Connect to the Database.
			connectToDB = DriverManager.getConnection("jdbc:sqlite:./database.sqlite");
			// Queries will be executed from this instance.
			statement = connectToDB.createStatement();








			ResultSet rs = statement.executeQuery(query);




			// Loop through each row...
			while(rs.next())
			{
				// Retrieve the Question name
				EditQ.arrayPrevQtnNames.add(rs.getString("question_name"));
				// Retrieve the Question type
				EditQ.arrayPrevQtnType.add(rs.getString("question_type"));
				// Rerieve the String of answers for each question.
				EditQ.arrayPrevAnswers.add(rs.getString("possible_answers"));
			}




			// Works aha.




			String prevQtns[] = {};
			// Returns an array with the type according to the type of array passed in. In this case, a String.
			prevQtns = EditQ.arrayPrevQtnNames.toArray(prevQtns);
			// Load the Questions from the database onto the 'prevQtnList.
			EditQ.prevQtnList.setItems(prevQtns);




			/// Set it to automatically select the first question - I will get back to this.
			EditQ.prevQtnList.select(0);	
















		}catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}




	public void addPreviousQuestion()
	{
		// Now, it is time to test this out...




		// THE FINALE LIES HERE! - F****, so long.


		wmessages = new MessageBox(EditQ.shell,SWT.ICON_WARNING|SWT.OK);
		succ_message = new MessageBox(EditQ.shell,SWT.ICON_INFORMATION|SWT.OK);


		if(EditQ.prevQtnList.getItemCount() > 0)
		{
			int selectedPrevQtnIndex = EditQ.prevQtnList.getSelectionIndex();




			// I would need to think long and hard for this one section as I don't have a database to test it on yet.




			// Get the question name selected on 'prevQtnList'.
			String prevQtnName = EditQ.arrayPrevQtnNames.get(selectedPrevQtnIndex);
			// Adds the question name to the main arrays.
			arrayQuestionNames.add(prevQtnName);
			// Also add the Question to the List  'qtnList' - Currently added questions.
			EditQ.qtnList.add(prevQtnName);	




			// Get the question type of the selected question from 'prevQtnList.
			String prevQtnType = EditQ.arrayPrevQtnType.get(selectedPrevQtnIndex);
			// Adds the question type to the main arrays
			arrayQuestionType.add(prevQtnType);




			// Get the String of answers for the selected question from 'prevQtnList'
			String prevQtnAnswers = EditQ.arrayPrevAnswers.get(selectedPrevQtnIndex);
			// Adds the String of answers to the main arrays.
			arrayAnswers.add(prevQtnAnswers);




			// Enable the 'editQtnBtn'.
			EditQ.editQtnBtn.setEnabled(true);
			// Enable the 'deleteQtnBtn'.
			EditQ.deleteQtnBtn.setEnabled(true);
			// Enable the 'updateBtn'
			EditQ.updateBtn.setEnabled(true);




			/// Set it to automatically select the recently added question in the list. - I will get back to this.
			EditQ.qtnList.select(selectedPrevQtnIndex);	




		}else
		{
			// Debug purposes.
			System.out.println("There are no questions currently stored in the database.");
		}


		/*
		// Debug purposes.
		System.out.println("-------------------- Questions in the Database Array --------------------");
		
		for(int x = 0;x<EditQ.arrayPrevQtnNames.size();x++)
		{
			System.out.println(EditQ.arrayPrevQtnNames.get(x) + " : " + EditQ.arrayPrevQtnType.get(x) + " : " + EditQ.arrayPrevAnswers.get(x));
		}
		
		// Debug purposes.
		System.out.println("-------------------- Questions in the Main Array --------------------");
		
		for(int x = 0;x<arrayQuestionNames.size();x++)
		{
			System.out.println(arrayQuestionNames.get(x) + " : " + arrayQuestionType.get(x) + " : " + arrayAnswers.get(x));
		}
		
		*/
	}




}
