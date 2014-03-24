import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Edit_result {

	static JSONArray details;
	protected JSONObject data; 	
	static JSONObject question;
	protected static JSONArray questions;
	static HashMap<String, String> hmap_answer;
	static HashMap<String, String> hmap_status;
	static int noOfComplete;

	public void read(File file){// the file location is passed to this method 
		JSONParser parser = new JSONParser();
		JSONObject object = null;// this will contain the full json object

		try {
			object = (JSONObject)parser.parse(new FileReader(file));// it reads the file and stores it in a object..
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fill_list(object);
	}


	public void fill_list(JSONObject data){
		// oooo, does JSON have something like hasnext() or something
		hmap_answer = new  HashMap<String, String>();
		hmap_status = new HashMap<String, String>();
		questions = (JSONArray) data.get("Results");
		details = (JSONArray) data.get("details");// the questionnaire status is in here



		noOfComplete = 0;

		System.out.println("size of array"+questions.size());
		// now we loop through the results array
		for(int i = 0 ; i < questions.size() ; i++)
		{
			question= (JSONObject) questions.get(i); // getting each object in the result array..
			System.out.println(question.get("question").toString()+ " " +question.get("answer").toString());// debugging to see

			hmap_answer.put(question.get("question").toString(), question.get("answer").toString());// hash map to store the answers




			String status =  question.get("status").toString();
			if(status.contains("Complete"))
			{
				noOfComplete++;
			}

			hmap_status.put(question.get("question").toString(), question.get("status").toString());// hash map to store the status

			view_edit_result.questn_list.add(question.get("question").toString());// populate list
		}
	}
}
