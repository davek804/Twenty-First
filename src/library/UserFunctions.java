package library;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
 
    private JSONParser jsonParser;
 
    private static String loginURL = "http://davidjkelley.net/android_api/";
    private static String registerURL = "http://davidjkelley.net/android_api/";
    private static String questionURL = "http://davidjkelley.net/android_api/getQuestions/";
    private static String submitURL = "http://davidjkelley.net/android_api/submit/";
    private static String demoURL = "http://davidjkelley.net/android_api/demo/";
    private static String checkURL = "http://davidjkelley.net/android_api/check/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String question_tag = "question";
    private static String submit_tag = "submit";
    private static String demo_tag = "demo";
    private static String check_tag = "check";
    private static String getDemo_tag = "getDemos";
 
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    
    public JSONObject getDemographics(String user) {
    	List<NameValuePair> param = new ArrayList<NameValuePair>();
    	param.add(new BasicNameValuePair("tag", getDemo_tag));
    	param.add(new BasicNameValuePair("user", user));
    	JSONObject object = jsonParser.getDemos(demoURL, param);

    	return object;
    }
    
    public void addDemographics(List<NameValuePair> params) {
    	
    	params.add(new BasicNameValuePair("tag", demo_tag));
    	jsonParser.submitDemos(demoURL, params);
    }
    
    public JSONArray getQuestions(String category)  {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("tag", question_tag));
    	params.add(new BasicNameValuePair("category", category));
    	//JSONObject json;
    	JSONArray questionList = jsonParser.getQuestionsJSONFromUrl(questionURL, params);

    	//return json
    	return questionList;
    }
 
    //login with user provided email/pass
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        //Log.v("userfunctions", "loginuser");
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        return json;
    }
 
    //register a new user with name/email/pass
    public JSONObject registerUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        //params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
 
    //determine if the user is logged in
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    //logout the user
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

	public void submitQuestion(String email, String category, int id,
			String title, int response, String responseText) {

		  List<NameValuePair> params = new ArrayList<NameValuePair>();
		  params.add(new BasicNameValuePair("tag", submit_tag));
		  params.add(new BasicNameValuePair("email", email));
		  params.add(new BasicNameValuePair("category", category));
		  params.add(new BasicNameValuePair("id", Integer.toString(id)));
		  params.add(new BasicNameValuePair("title", title));
		  params.add(new BasicNameValuePair("responseNumber", Integer.toString(response)));
		  params.add(new BasicNameValuePair("responseText", responseText));
	        
		jsonParser.submitQuestion(submitURL, params);
	}

	public boolean hasUserAnswered(JSONObject question, String user) throws JSONException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", check_tag));
		params.add(new BasicNameValuePair("category", question.getString("category").toString()));
		params.add(new BasicNameValuePair("id", question.getString("id").toString()));
		params.add(new BasicNameValuePair("user", user));
		
		boolean check = jsonParser.checkQuestion(checkURL, params);
		return check;
	}
}