package proj.mtc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import proj.mtc.models.Reservation;
import proj.mtc.models.Room;
import proj.mtc.models.User;
import proj.mtc.services.RestCalls;
import proj.mtc.services.RestService;
import proj.mtc.services.Util;

/**
 * Created by mbarcelona on 2/23/16.
 */
public class PendingListActivity extends AppCompatActivity {

  ListView lvPending;
  RelativeLayout loading;

  ArrayList<Reservation> reservations;

  User currUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pending_list);


    ActionBar actionBar = this.getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);


    Intent intent = this.getIntent();
    currUser = new User(
      intent.getIntExtra("id", -1),
      intent.getStringExtra("name"),
      intent.getStringExtra("firstname"),
      intent.getStringExtra("lastname"),
      intent.getStringExtra("middlename"),
      intent.getStringExtra("email"),
      intent.getStringExtra("accountType"),
      intent.getIntExtra("course_id", -1));

    loading = (RelativeLayout)findViewById(R.id.loading_layout);
    lvPending = (ListView) findViewById(R.id.lv_main);

    reservations = new ArrayList<Reservation>();

    //get data
    loading.setVisibility(View.VISIBLE);
    final RestService restService = new RestService();
    restService.getReservations(new RestService.RestServiceListener() {
      @Override
      public void onSuccess(RestCalls callType, String string) {

        try {
          JSONArray resultArray = null;
          resultArray = new JSONArray(string);

          for (int i = 0; i < resultArray.length(); i++) {
            JSONObject reservation = resultArray.getJSONObject(i);

            int userId = reservation.getInt("user_id");
            if (currUser.getId() == userId) {

              reservations.add(new Reservation(reservation.getInt("id")
                , reservation.getInt("attendants"), userId
                , reservation.getInt("venue"), reservation.getString("dateTimeStart")
                , reservation.getString("dateTimeEnd"), reservation.getString("dateTimeEnd")
                , reservation.getString("status"), reservation.getString("event")
                , reservation.getString("remarks"), reservation.getString("contactnumber")
              ));
            }
          }

          restService.getAllRooms(new RestService.RestServiceListener() {
            @Override
            public void onSuccess(RestCalls callType, String string) {

              ArrayList<Room> rooms = new ArrayList<Room>();

              try {
                JSONArray resRooms = new JSONArray(string);
                for(int i = 0; i < resRooms.length(); i++){
                  rooms.add(new Room(resRooms.getJSONObject(i).getInt("id")
                    ,resRooms.getJSONObject(i).getString("venueOrEquipment")
                    ,resRooms.getJSONObject(i).getString("details")
                    ,resRooms.getJSONObject(i).getString("serial")
                  ));
                }

                for(Reservation reservation: reservations){
                  String venue = "";
                  for(Room room: rooms) {
                    if(room.getId() == reservation.getVenue()){
                      venue = room.getName();
                      break;
                    }
                  }
                  reservation.setStrVenue(venue);
                }

                lvPending.setAdapter(new PendingListAdapter());
              } catch (JSONException e) {
                e.printStackTrace();
              }
              loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(RestCalls callType, String string) {

              loading.setVisibility(View.GONE);
            }
          });


        } catch (JSONException e) {
          e.printStackTrace();
          loading.setVisibility(View.GONE);
        }
      }

      @Override
      public void onFailure(RestCalls callType, String string) {
        Util.getInstance().showDialog(PendingListActivity.this, "Could not get your reservations.", "OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            loading.setVisibility(View.GONE);
          }
        });
      }
    });


  }

  public class PendingListAdapter extends BaseAdapter{

    @Override
    public int getCount() {
      return reservations.size();
    }

    @Override
    public Object getItem(int position) {
      return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = getLayoutInflater().inflate(R.layout.list_room_item, null);

      TextView tvEvent, tvAttendees, tvDate, tvTime, tvPurpose, tvContactNum, tvVenue, tvStatus;
      tvEvent = (TextView)view.findViewById(R.id.tvEvent);


      tvAttendees = (TextView)view.findViewById(R.id.tvAttendees);
      tvTime = (TextView)view.findViewById(R.id.tvTime);
      tvDate = (TextView)view.findViewById(R.id.tvDate);
      tvPurpose = (TextView)view.findViewById(R.id.tvPurpose);
      tvContactNum = (TextView)view.findViewById(R.id.tvContactNumber);
      tvVenue = (TextView)view.findViewById(R.id.tvVenue);
      tvStatus = (TextView)view.findViewById(R.id.tvStatus);

      tvEvent.setText(reservations.get(position).getEvent());
      tvPurpose.setText(reservations.get(position).getRemarks());
      tvContactNum.setText(reservations.get(position).getContactNum());
      tvVenue.setText(reservations.get(position).getStrVenue());
      tvDate.setText(reservations.get(position).getDate().split(" ")[0]);
      tvStatus.setText(reservations.get(position).getStatus());
      tvAttendees.setText(reservations.get(position).getAttendants()+"");
      tvTime.setText(reservations.get(position).getStart().split(" ")[1]
        +" - "+reservations.get(position).getEnd().split(" ")[1]);

      return view;
    }
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
