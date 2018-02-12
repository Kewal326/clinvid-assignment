import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import org.json.*;

public class TestingClass
{
public static void main(String[] args) {
    
    String inputStr;
    System.out.println("Please enter the input query");
    Scanner sc = new Scanner(System.in);
    inputStr = sc.nextLine();
    try
    {
    JSONObject json = createJson(inputStr);
    System.out.println(json.toString(4));                   //Printing the JSON as a string with 4 indentation(pretty print)
    }
    catch(Exception e)
    {
        System.out.println(e);
    }
   }
    
    public static JSONObject createJson(String inputStr) throws Exception
    {
        String[] tokens = inputStr.split("\\|", -1);     //Getting individual JSON nodes from input
        JSONObject output = new JSONObject();               // Using org.json's JSONObject class to create json
        output.put("id", tokens[1]);
        
        String nameToken = tokens[2];
        String[] nameArray = nameToken.split("\\>",-1);         //Getting first, middle, last name from 2nd node
        String firstName = nameArray[0].substring(1);
        String middleName = nameArray[1].substring(1);
        String lastName = nameArray[2].substring(1);
        JSONObject nameObject = new JSONObject();
        nameObject.put("first", firstName);
        nameObject.put("middle", middleName);
        nameObject.put("last", lastName);
        output.put("name", nameObject);
        
        String[] addressTokens = tokens[4].split("\\,",-1);         //Since there can be more than 1 address, running a for loop for all the values
        JSONArray addressArr = new JSONArray();
        for(int i = 0 ; i < addressTokens.length; i++)
        {
            String address = addressTokens[i];
            int cityChar = address.indexOf('>');
            String cityName = address.substring(1, cityChar);
            
            int corChar = address.indexOf("<<");
            
            int lonChar = address.indexOf(">", corChar);
            
            int corCharEnd = address.indexOf(">>");
            
            int latChar = address.indexOf("<", lonChar);
            
            String longStr = address.substring(corChar + 2, lonChar);
            String latStr = address.substring(latChar + 1, corCharEnd);
            System.out.println("longStrLent"+longStr.length()   );
        
            
            Double longitude = longStr.length() == 0 ? null : Double.valueOf(longStr);
            Double latitude = latStr.length() == 0 ? null : Double.valueOf(latStr);
            
            JSONObject corObj = new JSONObject();
            corObj.put("long",longitude );
            corObj.put("lat", latitude);
            
            
            JSONObject newJsonOb = new JSONObject();
            newJsonOb.put("name", cityName);
            newJsonOb.put("coords", corObj);
            
            addressArr.put(newJsonOb);
        }
        
        output.put("locations", addressArr);
        output.put("dob",tokens[3]);
        output.put("imageId", tokens[5]);
                
        return output;
    }

}