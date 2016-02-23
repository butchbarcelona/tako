package proj.mtc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import proj.mtc.models.User;

public class MainActivity extends AppCompatActivity {

  public final static String TAG = "MTS";

  User currUser ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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

    TextView tvName = (TextView) findViewById(R.id.tv_name);
    tvName.setText("Hi "+currUser.getFirstname()+"!");

  }

  public void onClick(View view){
    Intent intent;
    switch(view.getId()){
      case R.id.btn_list:
        intent  = new Intent(MainActivity.this, PendingListActivity.class);
        break;
      case R.id.btn_reserve:
        default:
        intent  = new Intent(MainActivity.this, RoomDetailsActivity.class);
        break;
    }

    intent.putExtra("firstname",currUser.getFirstname());
    intent.putExtra("id",currUser.getId());
    intent.putExtra("name",currUser.getUsername());
    intent.putExtra("lastname",currUser.getLastname());
    intent.putExtra("middlename",currUser.getMiddlename());
    intent.putExtra("email",currUser.getEmail());
    intent.putExtra("accountType", currUser.getAccount());
    intent.putExtra("course_id", currUser.getCourseid());
    startActivity(intent);


  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater mi = getMenuInflater();
    mi.inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_logout:
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        break;
    }

    return true;
  }
}
