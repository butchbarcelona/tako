package proj.mtc.services;

import android.os.AsyncTask;

public class RestAsyncTask extends AsyncTask<Void, Void, Void> {
  RestAsyncTaskListener listener;
  public RestAsyncTask(RestAsyncTaskListener listener) {
    this.listener = listener;
  }

  @Override
  protected Void doInBackground(Void... params) {
    this.listener.doInBackground();
    return null;
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
    this.listener.result();
  }


  public interface RestAsyncTaskListener{
    public void doInBackground();
    public void result();
  }
}

