package proj.tako.services;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import proj.tako.MainActivity;
import proj.tako.models.Equipment;
import proj.tako.models.NameValuePair;

public class RestService {

  public interface RestServiceListener{
    public void onSuccess(RestCalls callType, String string);
    public void onFailure(RestCalls callType, String string);
  }

  public String mainUrl = "http://system.mtcroomreservation.x10host.com/";

  public RestService(){

  }



  public void checkUser(final RestServiceListener listener, String username, String password){

    username.replace(" ","+");

    if(username != null && password != null
      && !username.toLowerCase().contains("delete") //since no auth need to check for possible injections
      && !password.toLowerCase().contains("delete")
      && !username.toLowerCase().contains(";")
      && !password.toLowerCase().contains(";"))
    {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      final String getPath = mainUrl + "/getUserByUsernamePassword?username=" + username + "&password=" + password;

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "GET", params);
        }

        @Override
        public void result() {

          if (jsonResults == null || jsonResults.trim().isEmpty()) {
            listener.onFailure(RestCalls.GET_USER_DETAILS, "failed");
          } else {
            listener.onSuccess(RestCalls.GET_USER_DETAILS, jsonResults);
          }

        }
      }).execute();
    }
  }



  public void getRooms(final RestServiceListener listener, String date, String timeStart, String timeEnd){

    try {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new NameValuePair("datetimeend",date + " " + timeEnd));
      params.add(new NameValuePair("datetimestart",date + " " + timeStart));
      final String getPath;
        getPath = mainUrl + "/getReservableRooms?"+getQuery(params);

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "GET", params);
        }

        @Override
        public void result() {

          if (jsonResults == null || jsonResults.trim().isEmpty()) {
            listener.onFailure(RestCalls.GET_AVAILABLE_ROOMS, "failed");
          } else {
            listener.onSuccess(RestCalls.GET_AVAILABLE_ROOMS, jsonResults);
          }

        }
      }).execute();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void getEquipment(final RestServiceListener listener, String date, String timeStart, String timeEnd){

    try {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new NameValuePair("datetimeend",date + " " + timeEnd));
      params.add(new NameValuePair("datetimestart",date + " " + timeStart));
      final String getPath;
      getPath = mainUrl + "/getAvailableEquipment?"+getQuery(params);

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "GET", params);
        }

        @Override
        public void result() {

          if (jsonResults == null || jsonResults.trim().isEmpty()) {
            listener.onFailure(RestCalls.GET_EQUIPMENT, "failed");
          } else {
            listener.onSuccess(RestCalls.GET_EQUIPMENT, jsonResults);
          }

        }
      }).execute();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  public void getNumOfEquipment(final RestServiceListener listener, String date, String timeStart, String timeEnd, int equipmentId){

    try {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new NameValuePair("datetimeend",date + " " + timeEnd));
      params.add(new NameValuePair("datetimestart",date + " " + timeStart));
      params.add(new NameValuePair("reserveable_id",""+equipmentId));
      final String getPath;
      getPath = mainUrl + "/getAvailableQuantity?"+getQuery(params);

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "GET", params);
        }

        @Override
        public void result() {

          if (jsonResults == null || jsonResults.trim().isEmpty()) {
            listener.onFailure(RestCalls.GET_NUM_EQUIPMENT, "failed");
          } else {
            listener.onSuccess(RestCalls.GET_NUM_EQUIPMENT, jsonResults);
          }

        }
      }).execute();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  public void reserveRoom(final RestServiceListener listener, String userId, String date
    , String timeStart, String timeEnd, String event, String purpose, String attendants
    , String contactNumber, ArrayList<Equipment> equipments, String venue){

    try {
      final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

      params.add(new NameValuePair("user_id", userId));




      Log.d(MainActivity.TAG, timeStart);
      Log.d(MainActivity.TAG, timeEnd);
      params.add(new NameValuePair("dateStart",date));// + " " + timeStart));
      //params.add(new NameValuePair("dateEnd",date + " " + timeEnd));
      params.add(new NameValuePair("timeStart", timeStart));
      params.add(new NameValuePair("timeEnd", timeEnd));
      params.add(new NameValuePair("event",event));
      params.add(new NameValuePair("purpose",purpose));
      params.add(new NameValuePair("attendants",attendants));
      params.add(new NameValuePair("contactnumber",contactNumber));


      params.add(new NameValuePair("venue",venue ));

      if(equipments != null && !equipments.isEmpty()) {
        String reservedItems = "";
        for(Equipment equipment: equipments) {
          if(reservedItems.trim().isEmpty()){
            reservedItems += equipment.getId();
          }else {
            reservedItems += ","+equipment.getId();
          }
          params.add(new NameValuePair("quantity"+equipment.getId(), ""+equipment.getReserved()));
        }
        params.add(new NameValuePair("reservedItems", reservedItems));
      }

      final String getPath;
      getPath = mainUrl + "/apply?"+getQuery(params);

      new RestAsyncTask(new RestAsyncTask.RestAsyncTaskListener() {

        String jsonResults;

        @Override
        public void doInBackground() {
          jsonResults = get(getPath, "POST", params);
        }

        @Override
        public void result() {

          if (jsonResults == null) {
            listener.onFailure(RestCalls.RESERVE, "failed");
          } else {
            listener.onSuccess(RestCalls.RESERVE, jsonResults);
          }

        }
      }).execute();

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  /***
   *
   * @param strUrl
   * @return string of JSON
   */
  private String get(final String strUrl, String requestMethod, final ArrayList<NameValuePair> params){


    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {

      URL url = new URL(strUrl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(40000);
      conn.setConnectTimeout(40000);
      conn.setRequestMethod(requestMethod);


      if(requestMethod.equals("POST")) {
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
      }

      conn.connect();


      InputStream in = new BufferedInputStream(conn.getInputStream());

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null) {
        jsonResults.append(line);
      }


      /*InputStreamReader in = new InputStreamReader(conn.getInputStream());

      int read;
      char[] buff = new char[1024];
      while ((read = in.read(buff)) != -1) {
        jsonResults.append(buff, 0, read);
      }*/

    } catch (MalformedURLException e) {
      Log.e(MainActivity.TAG, "Error processing URL", e);
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      Log.e(MainActivity.TAG, "Error connecting API", e);
      return null;
    } catch(Exception e) {
      e.printStackTrace();

      Log.e(MainActivity.TAG, "Error connecting API", e);
    }  finally
    {
      if (conn != null) {
        conn.disconnect();
      }
    }

    return jsonResults.toString();

  }

  private static final ScheduledExecutorService worker =
    Executors.newSingleThreadScheduledExecutor();


  private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (NameValuePair pair : params)
    {
      if (first)
        first = false;
      else
        result.append("&");

      result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
      result.append("=");
      if(pair.getName().equals("timeEnd") || pair.getName().equals("timeStart") || pair.getName().equals("dateStart"))
        result.append(pair.getValue());
      else
        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
    }

    Log.d(MainActivity.TAG,result.toString());
    return result.toString();
  }

}
