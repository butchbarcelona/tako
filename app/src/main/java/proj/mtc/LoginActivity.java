package proj.mtc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import proj.mtc.services.RestCalls;
import proj.mtc.services.RestService;
import proj.mtc.services.Util;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

  /**
   * Id to identity READ_CONTACTS permission request.
   */
  private static final int REQUEST_READ_CONTACTS = 0;

  /**
   * A dummy authentication store containing known user names and passwords.
   * TODO: remove after connecting to a real authentication system.
   */
  private static final String[] DUMMY_CREDENTIALS = new String[]{
    "foo@example.com:hello", "bar@example.com:world"
  };
  /**
   * Keep track of the login task to ensure we can cancel it if requested.
   */
  private UserLoginTask mAuthTask = null;

  // UI references.
  private AutoCompleteTextView userNameView;
  private EditText passwordView;
  private View progressView;
  private View loginFormView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // Set up the login form.
    userNameView = (AutoCompleteTextView) findViewById(R.id.username);
    populateAutoComplete();

    passwordView = (EditText) findViewById(R.id.password);
    passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });

    Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
    mEmailSignInButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });

    loginFormView = findViewById(R.id.login_form);
    progressView = findViewById(R.id.login_progress);
  }

  private void populateAutoComplete() {
    if (!mayRequestContacts()) {
      return;
    }

    getLoaderManager().initLoader(0, null, this);
  }

  private boolean mayRequestContacts() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return true;
    }
    if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
      return true;
    }
    if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
      Snackbar.make(userNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
        .setAction(android.R.string.ok, new View.OnClickListener() {
          @Override
          @TargetApi(Build.VERSION_CODES.M)
          public void onClick(View v) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
          }
        });
    } else {
      requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
    }
    return false;
  }

  /**
   * Callback received when a permissions request has been completed.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode == REQUEST_READ_CONTACTS) {
      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        populateAutoComplete();
      }
    }
  }


  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private void attemptLogin() {
    if (mAuthTask != null) {
      return;
    }

    // Reset errors.
    userNameView.setError(null);
    passwordView.setError(null);

    // Store values at the time of the login attempt.
    String email = userNameView.getText().toString();
    String password = passwordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      passwordView.setError(getString(R.string.error_invalid_password));
      focusView = passwordView;
      cancel = true;
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      userNameView.setError(getString(R.string.error_field_required));
      focusView = userNameView;
      cancel = true;
    } else if (!isEmailValid(email)) {
      userNameView.setError(getString(R.string.error_invalid_username));
      focusView = userNameView;
      cancel = true;
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      showProgress(false);
      focusView.requestFocus();
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);
      mAuthTask = new UserLoginTask(email, password);
      mAuthTask.execute((Void) null);
    }
  }

  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
    return true;//email.contains("@");
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return true;//password.length() > 4;
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      loginFormView.animate().setDuration(shortAnimTime).alpha(
        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      progressView.animate().setDuration(shortAnimTime).alpha(
        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(this,
      // Retrieve data rows for the device user's 'profile' contact.
      Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

      // Select only email addresses.
      ContactsContract.Contacts.Data.MIMETYPE +
        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
      .CONTENT_ITEM_TYPE},

      // Show primary email addresses first. Note that there won't be
      // a primary email address if the user hasn't specified one.
      ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    List<String> emails = new ArrayList<>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      emails.add(cursor.getString(ProfileQuery.ADDRESS));
      cursor.moveToNext();
    }

    addEmailsToAutoComplete(emails);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {

  }

  private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
    //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
    ArrayAdapter<String> adapter =
      new ArrayAdapter<>(LoginActivity.this,
        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    userNameView.setAdapter(adapter);
  }


  private interface ProfileQuery {
    String[] PROJECTION = {
      ContactsContract.CommonDataKinds.Email.ADDRESS,
      ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
  }

  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String userName;
    private final String password;


    UserLoginTask(String email, String password) {
      userName = email;
      this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      // TODO: attempt authentication against a network service.

      RestService restService = new RestService();
      restService.checkUser(new RestService.RestServiceListener() {
        @Override
        public void onSuccess(RestCalls callType, String string) {

          try {
            JSONObject result = new JSONObject(string);

            //goto MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("firstname",result.getString("firstname"));
            intent.putExtra("id",result.getInt("id"));
            intent.putExtra("name",result.getInt("name"));
            intent.putExtra("lastname",result.getString("lastname"));
            intent.putExtra("middlename",result.getString("middlename"));
            intent.putExtra("email",result.getString("email"));
            intent.putExtra("accountType",result.getString("accountType"));
            intent.putExtra("course_id", result.getInt("course_id"));

            showProgress(false);

            startActivity(intent);
            finish();

          } catch (JSONException e) {
            e.printStackTrace();
            onFailure(callType, e.getMessage());
          }
        }

        @Override
        public void onFailure(RestCalls callType, String string) {
          Util.getInstance().showDialog(LoginActivity.this, "Invalid credentials.", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              showProgress(false);
              dialog.dismiss();
            }
          });
        }
      }, userName, password);


      return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      mAuthTask = null;
      if (success) {
        //finish();
      } else {
        showProgress(false);

        passwordView.setError(getString(R.string.error_incorrect_password));
        passwordView.requestFocus();
      }
    }

    @Override
    protected void onCancelled() {
      mAuthTask = null;
      showProgress(false);
    }
  }
}
