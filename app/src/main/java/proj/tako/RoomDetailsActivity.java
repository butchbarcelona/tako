package proj.tako;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import proj.tako.models.Equipment;
import proj.tako.models.Reservation;
import proj.tako.models.User;
import proj.tako.services.RestCalls;
import proj.tako.services.RestService;
import proj.tako.services.Util;

public class RoomDetailsActivity extends AppCompatActivity {

  public User currUser;
  EditText etDate, etTimeStart, etTimeEnd, etNumAttendees, etPurpose, etContactNumber;
  Spinner spinnerRooms;
  RelativeLayout loading;
  Reservation currReservation;
  GridView gridViewEquipment;
  String eventDate, timeStart, timeEnd;

  RestService restService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_room_details);

    loading = (RelativeLayout)findViewById(R.id.loading_layout);
    gridViewEquipment = (GridView)findViewById(R.id.gridViewEquipment);
    restService = new RestService();
    currReservation = new Reservation();

    Intent intent = getIntent();
    currUser = new User(
              intent.getIntExtra("id", -1),
              intent.getStringExtra("name"),
              intent.getStringExtra("firstname"),
              intent.getStringExtra("lastname"),
              intent.getStringExtra("middlename"),
              intent.getStringExtra("email"),
              intent.getStringExtra("accountType"),
              intent.getIntExtra("course_id", -1));

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Hi ");
    toolbar.setSubtitle(currUser.getFirstname());
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });


    etDate = setUpDatePickerDialog(R.id.et_date_of_event);
    etTimeEnd = setUpTimePickerDialog(R.id.et_time_end);
    etTimeStart = setUpTimePickerDialog(R.id.et_time_start);
    //spinnerRooms.setVisibility(View.GONE);




  }

  Equipment[] equipments;
  public void populateEquipmentCount(){
    for(final Equipment e: equipments) {
      //gets the equipment
      if(e != null) {
        restService.getNumOfEquipment(new RestService.RestServiceListener() {
          @Override
          public void onSuccess(RestCalls callType, String string) {
            e.setNumAvailable(Integer.parseInt(string.replace("[", "").replace("]", "")));

            boolean finished = true;
            for (final Equipment e : equipments) {
              if (e.getNumAvailable() == -1) {
                finished = false;
              }
            }
            if (finished) {
              setUpGridView();
            }
          }

          @Override
          public void onFailure(RestCalls callType, String string) {
            e.setNumAvailable(0);
          }
        }, currReservation.getDate()
          , currReservation.getStart()
          , currReservation.getEnd(), e.getId());
      }
    }
  }



  public void getRooms(){

    if(currReservation.getDate() != null && currReservation.getStart() != null && currReservation.getEnd() != null){


      loading.setVisibility(View.VISIBLE);
      restService.getEquipment(new RestService.RestServiceListener() {
        @Override
        public void onSuccess(RestCalls callType, String string) {
          JSONArray result;
          try {
            result = new JSONArray(string);

            for (int i = 0; i < result.length(); i++) {
              final JSONObject data = result.getJSONObject(i);
              int id = data.getInt("id");

              equipments = new Equipment[result.length()];
              equipments[i] = new Equipment(id, data.getString("venueOrEquipment"), -1);
            }
            populateEquipmentCount();

          } catch (JSONException e) {
            e.printStackTrace();
          }

        }

        @Override
        public void onFailure(RestCalls callType, String string) {

        }
      }, currReservation.getDate()
        , currReservation.getStart()
        , currReservation.getEnd());


      restService.getRooms(new RestService.RestServiceListener() {
        @Override
        public void onSuccess(RestCalls callType, String string) {
          JSONArray result;
          try {
            result = new JSONArray(string);
            String[] arrRooms = new String[result.length()];
            for (int i = 0; i < result.length(); i++) {
              JSONObject data = result.getJSONObject(i);
              arrRooms[i] = data.getString("venueOrEquipment");
            }

            spinnerRooms = setUpSpinner(R.id.spinner_rooms, arrRooms);
          } catch (JSONException e) {
            e.printStackTrace();
          }

          //loading.setVisibility(View.GONE);


        }

        @Override
        public void onFailure(RestCalls callType, String string) {

          Util.getInstance().showDialog(RoomDetailsActivity.this, "Could not get available rooms.", "RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              getRooms();
            }
          });
          loading.setVisibility(View.GONE);
        }
      }, currReservation.getDate()
        , currReservation.getStart()
        , currReservation.getEnd());


    }

  }

  public void setUpGridView(){
    gridViewEquipment.setAdapter(new EquipmentAdapter());
    loading.setVisibility(View.GONE);

  }

  public EditText setUpDatePickerDialog(int editText) {
    final SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    final EditText et = (EditText) findViewById(editText);
    et.setInputType(InputType.TYPE_NULL);
    et.setFocusableInTouchMode(false);
    et.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
          RoomDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {

          public void onDateSet(DatePicker view, int year, int monthOfYear
            , int dayOfMonth) {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            String date = formatter.format(newDate.getTime());
            et.setText(date);
            currReservation.setDate(date.replace("/", "-"));

            getRooms();
          }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH)
          , newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
      }
    });
    return et;
  }


  public EditText setUpTimePickerDialog(int editText) {

    final EditText et = (EditText) findViewById(editText);
    et.setInputType(InputType.TYPE_NULL);
    et.setFocusableInTouchMode(false);
    et.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Calendar newCalendar = Calendar.getInstance();
        new TimePickerDialog(RoomDetailsActivity.this
          , new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String ampm = ((hourOfDay > 11) ? "PM" : "AM");
            String hour;
            String min;

            if (hourOfDay == 0) {
              hour = "12";
            } else if (hourOfDay > 12) {
              hour = String.valueOf(hourOfDay - 12);
            } else if (hourOfDay < 10) {
              hour = "0" + String.valueOf(hourOfDay);
            } else {
              hour = String.valueOf(hourOfDay);
            }

            if (minute < 10) {
              min = "0" + String.valueOf(minute);
            } else {
              min = String.valueOf(minute);
            }

            et.setText(hour + ":" + min + " " + ampm);

            switch(et.getId()){
              case R.id.et_time_end:
                  currReservation.setEnd(((hourOfDay < 10)?"0"+hourOfDay:hourOfDay)
                    +":"+min+":00");
                getRooms();
                break;
              case R.id.et_time_start:
                currReservation.setStart(((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay)
                  + ":" + min + ":00");
                getRooms();
                break;
            }


          }
        }, newCalendar.get(Calendar.HOUR_OF_DAY),
          newCalendar.get(Calendar.MINUTE), false).show();
      }
    });
    return et;
  }

  public Spinner setUpSpinner(int id, String[] items) {
    Spinner spinner = (Spinner) findViewById(id);
    ArrayAdapter<String> adapter =
      new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    return spinner;
  }

  public class EquipmentAdapter extends BaseAdapter{



    public EquipmentAdapter(){

    }

    @Override
    public int getCount() {
      return 0;
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      View view = getLayoutInflater().inflate(R.layout.list_equipment_item,null);

      TextView tvEquipment = (TextView)view.findViewById(R.id.chkBox);
      tvEquipment.setText(equipments[position].getName());

      NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
      numberPicker.setMinValue(0);
      numberPicker.setMaxValue(equipments[position].getNumAvailable());
      numberPicker.setValue(0);

      return view;
    }
  }
}
