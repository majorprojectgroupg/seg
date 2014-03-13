import java.util.ArrayList;


import org.eclipse.swt.widgets.Button;
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




class CreateQtnEvents implements SelectionListener, KeyListener, ModifyListener
{
	//public static ArrayList<String> arrayQuestionNames = new ArrayList<String>();
	//public static ArrayList<String> arrayQuestionType = new ArrayList<String>();
	//public static ArrayList<String> arrayAnswers = new ArrayList<String>();


	public static ArrayList<String> arrayQuestionNames;
	public static ArrayList<String> arrayQuestionType;
	public static ArrayList<String> arrayAnswers;


	public static Connection connectToDB;
	public static Statement statement;




	public void widgetDefaultSelected(SelectionEvent e)
	{
		// This function will not require any further implementation.		
	}
	public void widgetSelected(SelectionEvent e)
	{


		if(e.getSource() == CreateQ.singleChoiceBtn)
		{
			// Enable the 'answerInput' field.
			CreateQ.answerInput.setEnabled(true);


		}
		if(e.getSource() == CreateQ.multiChoiceBtn)
		{
			// Enable the 'answerInput' field.
			CreateQ.answerInput.setEnabled(true);
		}
		if(e.getSource() == CreateQ.userInputBtn)
		{
			// Disable the 'answerInput' field.
			CreateQ.answerInput.setEnabled(false);	
		}
		if(e.getSource() == CreateQ.dateChoiceBtn)
		{
			// Disable the 'answerInput' field.
			CreateQ.answerInput.setEnabled(false);
		}


		//  -- 'addQtnToList' - Adding the Questions to the List.	-- //


		if(e.getSource() == CreateQ.addQtnToListBtn)
		{
			// Calls the method which adds the Question to the List. - Works as expected! :) - I can't see no obvious bugs ;D
			maintainList(CreateQ.addQtnToListBtn);
		}	
		//  -- END of 'addQtnToList' - Adding the Questions to the List.	-- //




		// -- 'editAnsBtn' -- Editing answers in the list. -- //


			if(e.getSource() == CreateQ.editAnsBtn)
			{
				// Debug purposes.
				System.out.println("'editAnsBtn was clicked.'");
				// If the 'answerList' is not empty...
				if(CreateQ.answerList.getItemCount() > 0)
				{
					// Enable the 'applyAnsBtn'
					CreateQ.applyAnsBtn.setEnabled(true);


					// Grab the selected String.
					String selectedAnswer = CreateQ.answerList.getSelection()[0];


					// Debug purposes.
					System.out.println("Editing \"" + selectedAnswer + "\"");


					// Places the selected text in the textfield so the user can edit.
					CreateQ.answerInput.setText(selectedAnswer);
					// Highlight the text
					// I need this to work somehow! lol
					//CreateQ.answerInput.setSelection(0, selectedAnswer.length());


					// Disables the 'editAnsBtn' after the button has been clicked.
					CreateQ.editAnsBtn.setEnabled(false);


				}
			}


			// -- 'applyAnsBtn' -- Apply the new answer -- //


			if(e.getSource() == CreateQ.applyAnsBtn)
			{
				// This sections works the way I want it to.
				// Grab the selected String.
				String selectedAnswer = CreateQ.answerList.getSelection()[0];
				// Grab the selected index.
				int indexOfAnswerList = CreateQ.answerList.getSelectionIndex();
				// New input
				CreateQ.answerInput.getText();
				// Remove old answer.
				CreateQ.answerList.remove(selectedAnswer);
				// Adds the new answer to the 'answerList'.
				CreateQ.answerList.add(CreateQ.answerInput.getText(), indexOfAnswerList);


				// Disables the 'applyAnsBtn' after the button has been clicked.
				CreateQ.applyAnsBtn.setEnabled(false);
				// enables the 'editAnsBtn' after the button 'applyAnsBtn has been clicked.
				CreateQ.editAnsBtn.setEnabled(true); // -- I need to check this section for possible errors.


				// select the first answer.
				CreateQ.answerList.select(indexOfAnswerList);


			}


			// -- End of 'applyAnsBtn' -- Applies the changed name -- //


			// -- END of 'editAnsBtn' -- Editing/ answers in the list. -- //


			// -- 'deleteAnsBtn' -- Deleting answers in the list //


			if(e.getSource() == CreateQ.deleteAnsBtn)
			{
				//If the answerList is not empty...
				if(CreateQ.answerList.getItemCount() > 0)
				{


					System.out.println("deleteAnsBtn");
					// Grab the selected String.
					String selectedAnswer = CreateQ.answerList.getSelection()[0];


					// Deletes the answer from the list.
					CreateQ.answerList.remove(selectedAnswer);


					if(CreateQ.answerList.getItemCount() < 1)
					{
						  // Enable the 'editAnsBtn', 'applyAnsBtn' and 'deleteAnsBtn' buttons
							CreateQ.editAnsBtn.setEnabled(false);
							CreateQ.applyAnsBtn.setEnabled(false);
							CreateQ.deleteAnsBtn.setEnabled(false);
					}


					// select the first answer.
					CreateQ.answerList.select(0);		
				}
			}		
			// -- 'deleteAnsBtn' -- Deleting answers in the list //


			// -- 'exportBtn' - Exports the Questionnaire to a Text File. -- //


			if(e.getSource() == CreateQ.exportBtn)
			{
				// If a questionnaire title has been specified...	
				if(CreateQ.titleInput.getCharCount() > 1)
				{
					// --- PASTE HERE ---


					// For debug purposes.
					String exportString = "";


					// Export.
					String jsonString = "";


					int noOfQtns = arrayQuestionNames.size();																				
					int maxID = 0;
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


						// Get the maximum ID from the Database. - No errors - Apparently, this is empty.
						ResultSet rs = statement.executeQuery("SELECT max(question_id) FROM Questions;");




						while(rs.next())
						{
							//String questionName = rs.getString("question_name");
							// Are you f**king kidding me!!!! All I had to do was also specify the aggregate function when retrieving the value. F***k, fml lol.
							// It does work now.
							maxID = rs.getInt("max(question_id)");


							System.out.println("The maximum ID in the Questions is: " + maxID);
						}


						// Creating the JSON Export...
						// Each export will assign the questions with the same ID until the...
						// ... the next export, and then next set of questions will have the same but, ID+1 ;)
						// 
						jsonString = "{\n" +
															"\"questionnaire\" : [" + "\n" ;




						for(int x = 0;x<noOfQtns;x++)
						{
							// Insert the question_name (and its other values) into the 'Questions' table.
							query = "INSERT INTO Questions(question_id, question_name, question_type, possible_answers)" +
											"VALUES(" + (maxID+1) +  ", '" + arrayQuestionNames.get(x) + "', '" + arrayQuestionType.get(x) + "',\"" + arrayAnswers.get(x) + "\");";
							// This is working fine.
							statement.executeUpdate(query);


						}




						// Insert the 'question_id', 'questionnaire_name' and 'questionnaire_location' into the 'Questionnaires' table ;) - WORK NOW!
						String questionnaire_name = CreateQ.titleInput.getText();
						query = "INSERT INTO Questionnaires(Question_id, Questionnaire_name, Questionnaire_location) VALUES(" + (maxID+1) + ", '" + questionnaire_name + "', './exports/questionnaire_"+(maxID+1)+".json');";
							
					// logging what the user did
					String query2= "INSERT INTO log Values('"+login.username+"','"+login.date_timestamp+"','The User Created "+" "+CreateQ.titleInput.getText()+" Questionnaire')";
						
						statement.executeUpdate(query);
						statement.executeUpdate(query2);

						statement.close();	 // Works.
						connectToDB.close(); // Works.
						


					}catch(ClassNotFoundException cnfe)
					
					{


					}catch(SQLException sqle)
					
					{


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


					// So far so good - I NEED TO MAKE IT SO THAT IT CREATES THE JSON FILE TOO ! ;D


					// BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("./exports/questionnaire.json")));
					// I will later implement this section so it can detect what OS the application is being ran on. 		
					try
					{


						// Creates a JSON File, assigning a value to uniquely identify the file ;)
						BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("./exports/questionnaire_" + (maxID+1) +".json")));
						// Write to the file.
						bufferedWriter.write(jsonString); 
						bufferedWriter.close();	// Closes the stream.
						
						// clearing all the arrays when creating a new questionnaire data from the pervious questionnaire will not be added.
						arrayQuestionType= new ArrayList<String>();
						arrayAnswers= new ArrayList<String>();
						arrayQuestionNames= new ArrayList<String>();
					
						
						CreateQ.qtnList.removeAll();
						
						CreateQ.titleInput.setText("");

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


					if(CreateQ.arrayPrevQtnNames.size() > 0)
					{
						// Enable the button if there are questions stored in the database.
						CreateQ.prevQtnAddBtn.setEnabled(true);
					}else
					{
						// Do nothing.
					}


					// --- PASTE HERE ---


				}else
				{
					// Debug purposes.
					System.out.println("Specify a title for the questionnaire before exporting the file");
				}		


			}


			// --  END of 'exportBtn' - Exports the Questionnaire to a Text File. -- //


			// -- 'editQtnBtn' - Allows the administrator to edit questionnaires -- //


			if(e.getSource() == CreateQ.editQtnBtn)
			{
				// If the list is not empty.
				if(CreateQ.qtnList.getItemCount() > 0)
				{
					// Prevent any user interaction until the user has clicked 'applyQtnBtn'.
					CreateQ.qtnList.setEnabled(false);


					// Prevent the user from adding a question to a list during 'editing mode'.
					CreateQ.addQtnToListBtn.setEnabled(false);


					// Prevent the user from adding a question from the 'prevQtnList' to the current list during 'editing mode'.
					CreateQ.prevQtnAddBtn.setEnabled(false);


					// Disable the 'editQtnBtn'
					CreateQ.editQtnBtn.setEnabled(false);
					// Disable the 'deleteQtnBtn'.
					CreateQ.deleteQtnBtn.setEnabled(false);


					// Enable the 'applyQtnBtn'
					CreateQ.applyQtnBtn.setEnabled(true);


					// Index of the selected question.
					int selectedQtnIndex = CreateQ.qtnList.getSelectionIndex();
					// Set the text input to the question name you selected to modify.
					CreateQ.qtnInput.setText((arrayQuestionNames.get(selectedQtnIndex)));
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
						CreateQ.singleChoiceBtn.setSelection(true);
						CreateQ.multiChoiceBtn.setSelection(false);
						CreateQ.userInputBtn.setSelection(false);
						CreateQ.dateChoiceBtn.setSelection(false);


						// Enable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
						CreateQ.editAnsBtn.setEnabled(true);
						CreateQ.deleteAnsBtn.setEnabled(true);
						CreateQ.answerInput.setEnabled(true);
					}
					else if(currentQuestionType.equals("MultiChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);
						CreateQ.multiChoiceBtn.setSelection(true);
						CreateQ.singleChoiceBtn.setSelection(false);
						CreateQ.userInputBtn.setSelection(false);
						CreateQ.dateChoiceBtn.setSelection(false);


						// Enable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
						CreateQ.editAnsBtn.setEnabled(true);
						CreateQ.deleteAnsBtn.setEnabled(true);
						CreateQ.answerInput.setEnabled(true);




					}
					else if(currentQuestionType.equals("UserInputChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);


						CreateQ.userInputBtn.setSelection(true);
						CreateQ.singleChoiceBtn.setSelection(false);
						CreateQ.multiChoiceBtn.setSelection(false);
						CreateQ.dateChoiceBtn.setSelection(false);
					}
					else if(currentQuestionType.equals("DateChoice"))
					{
						// Debug purposes.
						System.out.println("This Question equals: " + currentQuestionType);


						CreateQ.dateChoiceBtn.setSelection(true);
						CreateQ.singleChoiceBtn.setSelection(false);
						CreateQ.multiChoiceBtn.setSelection(false);
						CreateQ.userInputBtn.setSelection(false);
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
						if(CreateQ.singleChoiceBtn.getSelection() || CreateQ.multiChoiceBtn.getSelection())
						{
							// Split the String of answers into an array.
							String answers[] = currentQtnAnswer.split("@");


							// Set the 'answerList' to the answers according to the question selected.
							CreateQ.answerList.setItems(answers);


							// Set the 'answerList' to automatically select the first question.	- THIS MAY NOT WORK WITH 'userInputBtn' and 'dateChoiceBtn'.
							CreateQ.answerList.setSelection(0);


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


			if(e.getSource() == CreateQ.applyQtnBtn)
			{
				maintainList(CreateQ.applyQtnBtn); // I can't find any obvious bugs ;) !
			}


			// -- End of 'applyQtnBtn' - Applies the changes made to the question -- //


			// -- 'deleteQtnBtn' - Delete a question from the current list of questions -- //


			if(e.getSource() == CreateQ.deleteQtnBtn)
			{
				//If the 'qtnList' is not empty...
				if(CreateQ.qtnList.getItemCount() > 0)
				{
					int selectedQtnIndex = CreateQ.qtnList.getSelectionIndex();


					System.out.println("deleteQtnBtn has been pressed.");
					// Grab the String representation of the question selected.
					//String selectedQuestion = CreateQ.answerList.getSelection()[0];


					// Delete the Question from the list.
					CreateQ.qtnList.remove(selectedQtnIndex);
					// Delete the Question from 'arrayQuestionNames'
					arrayQuestionNames.remove(selectedQtnIndex);
					// Delete the questionType from 'arrayQuestionType'
					arrayQuestionType.remove(selectedQtnIndex);
					// Delete the String of answers from 'arrayAnswers'
					arrayAnswers.remove(selectedQtnIndex);


					if(CreateQ.qtnList.getItemCount() < 1)
					{
						  // Disaable the 'editQtnBtn', 'applyQtnBtn' and 'deleteQtnBtn' buttons
							CreateQ.editQtnBtn.setEnabled(false);
							CreateQ.applyQtnBtn.setEnabled(false);
							CreateQ.deleteQtnBtn.setEnabled(false);


							// Disable 'exportBtn'
							CreateQ.exportBtn.setEnabled(false);


					}


					// select the first answer.
					CreateQ.qtnList.select(0);		
				}
			}		
			// -- END of 'deleteQtnBtn' - Delete a question from the current list of questions -- //


			// -- 'prevQtnAdd' - Adding previously stored questions from the database to the main list -- //


			if(e.getSource() == CreateQ.prevQtnAddBtn)
			{
				// Adds the selected question from 'prevQtnList' to the main list, 'qtnList'.
				addPreviousQuestion();


			}




			// -- END of 'prevQtnAdd' - Adding previously stored questions from the database to the main list -- //


	}	// End of the 'widgetSelected(SelectionEvent e)' function.


	public void keyPressed(KeyEvent e)
	{
		if(e.getSource() == CreateQ.answerInput)
		{
			// If the carriage return key is pressed - i.e. the ENTER key...
			if(e.keyCode == SWT.CR)
			{
				// I need to add some code for when the ENTER key is pressed during the process of an edit - i.e. 'editAnsBtn'. PLEASE CODE!!!


				// Debug purposes.
				System.out.println("Enter button was clicked:");
				// Store answer in a temporary String.
				String questionAnswer = CreateQ.answerInput.getText();
				// If the answer is at least 2 characters in length...
				if(questionAnswer.length() > 1)
				{
					// Add answer to answerList.
					CreateQ.answerList.add(questionAnswer);




					// Enable the 'editAnsBtn' and 'deleteAnsBtn' buttons - Rewrite this code for when an item in the list is selected.
					CreateQ.editAnsBtn.setEnabled(true);
					CreateQ.deleteAnsBtn.setEnabled(true);


					// Clear the field.
					CreateQ.answerInput.setText("");


					// select the first answer.
					CreateQ.answerList.select(0);


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
		// 'searchInput' -- //


		if(e.getSource() == CreateQ.searchInput)
		{
			// Query statement.
			String query = "";


			if(CreateQ.searchInput.getCharCount() > 0)
			{
				// Use %LIKE%
				String searchInput = CreateQ.searchInput.getText();
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


		if(CreateQ.titleInput.getCharCount() > 1)
		{


			// I need to add more validations.
			// If the field has at least two characters...
			if(CreateQ.qtnInput.getCharCount() > 1)
			{		
					// Store the name of the question inputted (or that is already in the 'qtnInput' field).
					questionName = CreateQ.qtnInput.getText();


					// The three radio buttons for the 'Type of Question'
					if(CreateQ.singleChoiceBtn.getSelection() == true)
					{
						questionType = "SingleChoice";
					}
					else if(CreateQ.multiChoiceBtn.getSelection() == true)
					{
						questionType = "MultiChoice";
					}
					else if(CreateQ.userInputBtn.getSelection() == true)
					{
						questionType = "UserInputChoice";
					}
					else if(CreateQ.dateChoiceBtn.getSelection() == true)
					{
						questionType = "DateChoice";
					}


					// If the 'answerList' is not empty, or either of the selected radio buttons are pressed...
					if(CreateQ.answerList.getItemCount() > 0 || CreateQ.userInputBtn.getSelection() || CreateQ.dateChoiceBtn.getSelection())
					{
						// Add each answer to a String that will form a String of answers.
						int lengthOfList = CreateQ.answerList.getItemCount();


						// If the 'userInputBtn' or 'dateChoiceBtn' are selected, 'questionAnswer' will have an empty String value.
						if(CreateQ.userInputBtn.getSelection() || CreateQ.dateChoiceBtn.getSelection())
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
										questionAnswer = questionAnswer + CreateQ.answerList.getItem(x);
									}else
									{
										// Errr, yeah... lol.
										questionAnswer = questionAnswer + (CreateQ.answerList.getItem(x) + "@");	
									}
								}
						}




						// if the 'addQtnToListBtn' was clicked...
						if(btn == CreateQ.addQtnToListBtn)
						{
							// Add the name of the question inputted, to the array.
							arrayQuestionNames.add(questionName);


							// Adds the questionType to the 'arrayQuestionType'
							arrayQuestionType.add(questionType);


							// Now we add the String of answers to 'arrayAnswers'
							arrayAnswers.add(questionAnswer);
							// So far so good :)


							// Add the Question to the List  'qtnList' - Currently added questions.
							CreateQ.qtnList.add(questionName);




							// ------ I NEED TO MODIFY THIS SECTION SO I CAN ADD IT FOR THE OTHER BUTTONS TOO! ALL DONE ----- //


							// Enable 'editQtnBtn', 'applyQtnBtn', and 'deleteQtnBtn'
							CreateQ.editQtnBtn.setEnabled(true);
							//CreateQ.applyQtnBtn.setEnabled(true);
							CreateQ.deleteQtnBtn.setEnabled(true);


							// Enable 'exportBtn'
							CreateQ.exportBtn.setEnabled(true);


							// Disable the 'editAnsBtn' and 'deleteAnsBtn'
							CreateQ.editAnsBtn.setEnabled(false);
							CreateQ.deleteAnsBtn.setEnabled(false);




							// Set the 'qtnList' to automatically select the most recent question added. - WORKS NICELY! :)
							CreateQ.qtnList.setSelection(CreateQ.qtnList.getItemCount()-1);






							// Clean up the 'qtnInput' field, 'answerInput' field, 'answerList'
							CreateQ.qtnInput.setText("");
							CreateQ.answerInput.setText("");
							CreateQ.answerList.removeAll();




							// Debug purposes.
							System.out.println("questionName: " + questionName + ", questionType: " + questionType + " questionAnswer=" + questionAnswer);	


							// ------ I NEED TO MODIFY THIS SECTION SO I CAN ADD IT FOR THE OTHER BUTTONS TOO! ALL DONE ----- //			




						}
						// if the 'applyQtnBtn' was clicked...
						else if(btn == CreateQ.applyQtnBtn)
						{
							int selectedQtnIndex = CreateQ.qtnList.getSelectionIndex();
							// Keep a reference of the old question that we can later use to delete, edit, etc. 
							String oldQuestionName = questionName;


							// Allow the user to, once again, interact with the 'qtnList'.
							CreateQ.qtnList.setEnabled(true);


							// Allow the user to, once again, add a question to the 'qtnList'.
							CreateQ.addQtnToListBtn.setEnabled(true);
							// Allow the user to, once again, add a question from 'prevQtnList' to the main list.
							CreateQ.prevQtnAddBtn.setEnabled(true);


							// Delete the old Question from the 'qtnList'.
							CreateQ.qtnList.remove(selectedQtnIndex);
							// Delete the old Question from the array too.
							arrayQuestionNames.remove(selectedQtnIndex);				
							// Add the modfied name of the question inputted, to the 'qtnList'
							CreateQ.qtnList.add(questionName, selectedQtnIndex);
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
							CreateQ.qtnInput.setText("");
							CreateQ.answerInput.setText("");
							CreateQ.answerList.removeAll();


							// Set radio button 'singleChoiceBtn' to be selected. - I could delete this.
							CreateQ.singleChoiceBtn.setSelection(true);
							CreateQ.multiChoiceBtn.setSelection(false);
							CreateQ.userInputBtn.setSelection(false);
							CreateQ.dateChoiceBtn.setSelection(false);
							// CONTINUE FROM HERE!




							// Enable the 'editQtnBtn'
							CreateQ.editQtnBtn.setEnabled(true);
							// Disable the 'applyQtn'
							CreateQ.applyQtnBtn.setEnabled(false);
							// Enable the 'deleteQtnBtn'.
							CreateQ.deleteQtnBtn.setEnabled(true);


							// Disable 'editAnsBtn', deleteAnsBtn' and the 'answerInput' field.
							CreateQ.editAnsBtn.setEnabled(false);
							CreateQ.deleteAnsBtn.setEnabled(false);
							CreateQ.applyAnsBtn.setEnabled(false);






							// Sets the list to select the modifed question.
							CreateQ.qtnList.setSelection(selectedQtnIndex);				


						}


					}else
					{
						// if 'answerList' - A dialog box should pop up if there are no answers supplied!
						System.out.println("At least input an answer!");
					}						
			}else
			{
				// if 'qtnInput' - A dialog box should pop up if the text is empty!
				System.out.println("Type in a Question!");
			}
		}else
		{
			// Debug purposes.
			System.out.println("Specify a title for the questionnaire");
		}


		//-------------------------------//-------------------------------//-------------------------------//




	}


	public static void loadQuestions(String query)
	{
		// Empty the ArrayLists.
		CreateQ.arrayPrevQtnNames = new ArrayList<String>();
		CreateQ.arrayPrevQtnType = new ArrayList<String>();
		CreateQ.arrayPrevAnswers = new ArrayList<String>();




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
				CreateQ.arrayPrevQtnNames.add(rs.getString("question_name"));
				// Retrieve the Question type
				CreateQ.arrayPrevQtnType.add(rs.getString("question_type"));
				// Rerieve the String of answers for each question.
				CreateQ.arrayPrevAnswers.add(rs.getString("possible_answers"));
			}


			// Works aha.


			String prevQtns[] = {};
			// Returns an array with the type according to the type of array passed in. In this case, a String.
			prevQtns = CreateQ.arrayPrevQtnNames.toArray(prevQtns);
			// Load the Questions from the database onto the 'prevQtnList.
			CreateQ.prevQtnList.setItems(prevQtns);


			/// Set it to automatically select the first question - I will get back to this.
			CreateQ.prevQtnList.select(0);	








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


		if(CreateQ.prevQtnList.getItemCount() > 0)
		{
			int selectedPrevQtnIndex = CreateQ.prevQtnList.getSelectionIndex();


			// I would need to think long and hard for this one section as I don't have a database to test it on yet.


			// Get the question name selected on 'prevQtnList'.
			String prevQtnName = CreateQ.arrayPrevQtnNames.get(selectedPrevQtnIndex);
			// Adds the question name to the main arrays.
			arrayQuestionNames.add(prevQtnName);
			// Also add the Question to the List  'qtnList' - Currently added questions.
			CreateQ.qtnList.add(prevQtnName);	


			// Get the question type of the selected question from 'prevQtnList.
			String prevQtnType = CreateQ.arrayPrevQtnType.get(selectedPrevQtnIndex);
			// Adds the question type to the main arrays
			arrayQuestionType.add(prevQtnType);


			// Get the String of answers for the selected question from 'prevQtnList'
			String prevQtnAnswers = CreateQ.arrayPrevAnswers.get(selectedPrevQtnIndex);
			// Adds the String of answers to the main arrays.
			arrayAnswers.add(prevQtnAnswers);


			// Enable the 'editQtnBtn'.
			CreateQ.editQtnBtn.setEnabled(true);
			// Enable the 'deleteQtnBtn'.
			CreateQ.deleteQtnBtn.setEnabled(true);
			// Enable the 'exportBtn'
			CreateQ.exportBtn.setEnabled(true);


			/// Set it to automatically select the recently added question in the list. - I will get back to this.
			CreateQ.qtnList.select(selectedPrevQtnIndex);	


		}else
		{
			// Debug purposes.
			System.out.println("There are no questions currently stored in the database.");
		}










		/*
		// Debug purposes.
		System.out.println("-------------------- Questions in the Database Array --------------------");
		
		for(int x = 0;x<CreateQ.arrayPrevQtnNames.size();x++)
		{
			System.out.println(CreateQ.arrayPrevQtnNames.get(x) + " : " + CreateQ.arrayPrevQtnType.get(x) + " : " + CreateQ.arrayPrevAnswers.get(x));
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
