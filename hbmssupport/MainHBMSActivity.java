package com.hbms.hbmssupport;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.hbms.matchFragment.MatchFailFragment;
import com.hbms.matchFragment.MatchOk1Fragment;
import com.hbms.matchFragment.MatchOk2Fragment;
import com.hbms.matchFragment.MatchOk3Fragment;
import com.hbms.matchFragment.MatchOk4Fragment;
import com.hbms.matchFragment.MatchOk5Fragment;
import com.hbms.matchFragment.MatchResetFragment;
import com.hbms.matchFragment.MatchReverse1Fragment;
import com.hbms.matchFragment.MatchReverse2Fragment;
import com.hbms.matchFragment.MatchReverse3Fragment;
import com.hbms.matchFragment.MatchWrongFragment1;
import com.hbms.usermanagment.UserManagementFragment;
import com.hbms.webservice.WebserviceCallParams;
import com.hbms.webservice.WebserviceCallSupportObject;
import org.ksoap2.serialization.SoapObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainHBMSActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextToSpeech tts;
    HashMap<String, String> params;
    String operationID = "11";
    String HOST = null;
    TimerTask taskObject;
    Boolean CodeOKrun = true;
    Timer timerSupport;
    Timer timerParam;
    SharedPreferences.Editor editor;
    Boolean TTSSpeakSwitch;
    Boolean TTSVibration;
    Boolean TTSStatus=true;
    Boolean VibrationStatus=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

   try {
       HOST = getString(R.string.host);
       params = new HashMap<String, String>();

       params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

       SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
       editor = pref.edit();


//        String vibPref= pref.getString("vibration", null);
       TTSSpeakSwitch = pref.getBoolean("text2speek", getResources().getBoolean(R.bool.TTSSpeakSwitch));
       TTSVibration = pref.getBoolean("vibration", getResources().getBoolean(R.bool.TTSVibration));

       Log.println(Log.INFO, "pref", "****>" + TTSVibration + " " + TTSSpeakSwitch);

       ImageView ttsView = (ImageView) findViewById(R.id.imageViewTablet);

       if (TTSSpeakSwitch) {
           ttsView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
           ttsView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
       } else {
           ttsView.getLayoutParams().height = 0;
           ttsView.getLayoutParams().width = 0;
       }
       ttsView.requestLayout();

       NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);

       final ToggleButton item = (ToggleButton) navigationView1.getMenu().getItem(1).getActionView().findViewById(R.id.toggleButton);
       Log.println(Log.INFO, "pref", "****>" + TTSSpeakSwitch);
       item.setChecked(TTSSpeakSwitch);

       item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               TTSSpeakSwitch = isChecked;
               ImageView ttsView = (ImageView) findViewById(R.id.imageViewTablet);
               if (isChecked) {
                   ttsView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                   ttsView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                   editor.putBoolean("text2speek", true);
                   editor.commit();
               } else {
                   ttsView.getLayoutParams().height = 0;
                   ttsView.getLayoutParams().width = 0;
                   editor.putBoolean("text2speek", false);
                   editor.commit();
               }
               ttsView.requestLayout();
           }
       });


       NavigationView navigationViewVibration = (NavigationView) findViewById(R.id.nav_view);
       final ToggleButton itemVibration = (ToggleButton) navigationViewVibration.getMenu().getItem(2).getActionView().findViewById(R.id.toggleButton);
       itemVibration.setChecked(TTSVibration);
       itemVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               TTSVibration = isChecked;
               if (isChecked) {
                   TTSVibration = true;
                   editor.putBoolean("vibration", true); // Storing boolean - true/false
                   editor.commit();
               } else {
                   TTSVibration = false;
                   editor.putBoolean("vibration", false); // Storing boolean - true/false
                   editor.commit();
               }
           }
       });


       ImageView myview = (ImageView) findViewById(R.id.imageViewHbms);
       myview.setOnTouchListener(new View.OnTouchListener() {

           @Override
           public boolean onTouch(View v, MotionEvent touchevent) {
               int action = touchevent.getAction();

               switch (action & MotionEvent.ACTION_MASK) {
                   case MotionEvent.ACTION_POINTER_DOWN:

                       int count = touchevent.getPointerCount();

                       if (count == 2) {
                           TextView fragmentSwitch = (TextView) findViewById(R.id.FragmentSwitch);
                           if (fragmentSwitch.getText().equals("1")) {
                               ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);
                               siri.setVisibility(View.VISIBLE);

                               FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                               fragmentSwitch.setText("2");
                               fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                               fragmentTransaction.replace(R.id.fragment_container, new MatchResetFragment(), "Fragment1");
                               fragmentTransaction.addToBackStack(null);
                               operationID = "oo2";
                               setRepeatingAsyncTaskParameter();
                               setRepeatingAsyncTaskSupport();
                               Log.println(Log.INFO, "Finger", "--->aktiv");
                               TTSStatus = false;
                               VibrationStatus = false;

                               fragmentTransaction.commit();


                           } else {
                               FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                               fragmentSwitch.setText("1");
                               fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                               fragmentTransaction.replace(R.id.fragment_container, new UserManagementFragment(), "Fragment1");
                               fragmentTransaction.addToBackStack(null);
                               Log.println(Log.INFO, "Finger", "--->user");
                               ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);
                               siri.setVisibility(View.INVISIBLE);
                               fragmentTransaction.commit();
                               tts.stop();
                               timerParam.cancel();
                               timerSupport.cancel();
                           }
/*
                           Intent myIntent = new Intent(getApplicationContext(), MainActivity_mgmt.class);
                            tts.stop();
                            tts.shutdown();
                            timerParam.cancel();
                            timerSupport.cancel();
                           startActivity(myIntent);
                           overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);*/
                       }
                       break;
               }
               return true;


           }
       });

       params = new HashMap<String, String>();
       params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
       tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
           @Override
           public void onInit(int status) {
               if (status == TextToSpeech.SUCCESS) {
                   tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                                          @Override
                                                          public void onDone(final String utteranceId) {
                                                              runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);
                                                                      siri.setImageResource(R.drawable.siri1);
                                                                  }
                                                              });
                                                          }

                                                          @Override
                                                          public void onError(String utteranceId) {

                                                          }

                                                          @Override
                                                          public void onStart(String utteranceId) {
                                                              runOnUiThread(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);
                                                                      siri.setImageResource(R.drawable.siria1);
                                                                  }
                                                              });
                                                          }
                                                      }
                   );

                   tts.setLanguage(Locale.GERMAN);

               } else {
                   Log.println(Log.INFO, "SOAP", "--->TTS Fehler!!!!");
               }
           }

           public void shutDown() {
               tts.shutdown();
           }
       });

       setRepeatingAsyncTaskParameter();
       setRepeatingAsyncTaskSupport();

       final ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);

       siri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               TextView spokenText = (TextView) findViewById(R.id.spokenText);
               speakTTSSiri(spokenText.getText().toString());
           }
       });

   }
   catch (Exception ex)
   {
       Log.println(Log.INFO, "SOAP", "onCreate: " + Log.getStackTraceString(ex));
   }
    }


//##################################################################################################
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setRepeatingAsyncTaskSupport() {
        final Handler handler = new Handler();
        timerParam = new Timer();
        TimerTask taskParam = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            CallGetParam getParam = new CallGetParam();
                            getParam.execute();
                            }
                        catch (Exception e) {
                            Log.println(Log.INFO, "SOAP", "-->Fehler Callsync" + e.toString());
                        }
                    }
                });
            }
        };
        timerParam.schedule(taskParam, 0, 4 * 1000);  // interval of one minute

    }

    class CallGetParam extends AsyncTask<String, String, SoapObject> {
        @Override
        protected SoapObject doInBackground(String... params) {
            try {
                WebserviceCallParams cs = new WebserviceCallParams();
                return cs.Call(HOST);

            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            try {

                if (result != null) {

                    SoapObjectParam.soapObjParam=result;

                    TextView ed1 = (TextView) findViewById(R.id.textViewUser);
                    ed1.setText(result.getProperty(0).toString());

                    TextView temp = (TextView) findViewById(R.id.textViewGrade);
                    temp.setText(result.getProperty(2).toString());

                    final ImageView weather = (ImageView) findViewById(R.id.imageViewWeather);
                    ImageView daytime = (ImageView) findViewById(R.id.imageViewDaytime);

                    ImageView tablet = (ImageView) findViewById(R.id.imageViewTablet);
                    temp.setText(result.getProperty(3).toString());

                    SoapObject obj = (SoapObject) result.getProperty(4);

                    //weather icons
                    if (obj.toString().toLowerCase().contains("sun")) {
                        weather.setImageResource(R.mipmap.ic_sun);
                    } else {
                        weather.setImageResource(R.mipmap.ic_rainning);
                    }

                    //morning/evening icons
                    if (obj.toString().toLowerCase().contains("morning")) {
                        daytime.setImageResource(R.mipmap.ic_morning);
                    } else {
                        daytime.setImageResource(R.mipmap.ic_evening);
                    }

                    //tablet/TTSg icons
                    if (result.getProperty(1).toString().toLowerCase().contains("tablet")) {
                        tablet.setImageResource(R.mipmap.ic_tablet);
                    } else {
                        tablet.setImageResource(R.mipmap.ic_speech);
                    }
                    Log.println(Log.INFO, "PARAM", "Lade UserParameter ok:" + result + "");


                } else {
                    if (wifiConnect()) {
                        Toast.makeText(getApplicationContext(), "Parameter nicht initialisiert! ", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {

                //ErrorMessage(e, "CallGetParam", result + "");
            }
        }
    }


    private void setRepeatingAsyncTaskParameter() {
        final Handler handler = new Handler();
        timerSupport = new Timer();
        taskObject = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (wifiConnect() && CodeOKrun) {
                            try {

                                CAllGetSupportObj getSupportObj = new CAllGetSupportObj();
                                getSupportObj.execute();


                            } catch (Exception e) {
                                Log.println(Log.INFO, "Error", "-->Fehler Callsync" + e.toString());
                            }
                        }
                    }
                });
            }
        };
        timerSupport.schedule(taskObject, 0, 2 * 1000);  // interval of one minute

    }


    class CAllGetSupportObj extends AsyncTask<String, String, SoapObject> {

        @Override
        protected SoapObject doInBackground(String... params) {
            WebserviceCallSupportObject cs = new WebserviceCallSupportObject();
            return cs.Call(HOST);
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            try {

                if (result != null) {

                    SoapObjectOperationParam.soapObjectOperationParam = result;
                    String value = result.getProperty("matchStatus").toString();
                    String operationIDWS = result.getProperty("operationId").toString();
                    TextView hiddenText = (TextView) findViewById(R.id.hiddenText);
                    Log.println(Log.INFO, "SOAP", "" + result.toString());

                    if (!operationIDWS.equals(operationID)) {
                        operationID=operationIDWS;
                        hiddenText.setText(operationID);
                        vibrationTablet();
                        String s=SoapObjectOperationParam.soapObj;
                        SoapObjectOperationParam.d("Soap",s);

                            if (value.equals("Reset")) {
                                Log.println(Log.INFO, "SOAP", "Reset");
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new MatchResetFragment(), "StartFragment");
                                ((TextView) findViewById(R.id.spokenText)).setText(" ");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                            else if (value.equals("MATCH_WRONG")) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                Log.println(Log.INFO, "SOAP", "MATCH_WRONG");
                                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                fragmentTransaction.replace(R.id.fragment_container, new MatchWrongFragment1(), "Fragment1");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                DelayTTS(result.getProperty("spokenText").toString());
                            }

                            else if  (value.equals("MATCH_FAIL")) {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                Log.println(Log.INFO, "SOAP", "MATCH_FAIL");
                                fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                fragmentTransaction.replace(R.id.fragment_container, new MatchFailFragment(), "Fragment1");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                DelayTTS(result.getProperty("spokenText").toString());
                            }

                            else if (value.equals("MATCH_OK")) {
                                Log.println(Log.INFO, "SOAP", "MATCH_OK");
                                int count = ((SoapObject) result.getProperty("nextOperations")).getPropertyCount();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                Log.println(Log.INFO, "SOAP", +count+"");

                                switch (count) {
                                    case 1:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchOk1Fragment(), "Fragment1");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;

                                    case 2:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchOk2Fragment(), "Fragment2");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;

                                    case 3:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchOk3Fragment(), "Fragment3");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;
                                    case 4:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchOk4Fragment(), "Fragment4");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;

                                    case 5:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchOk5Fragment(), "Fragment5");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;
                                }




                                DelayTTS(result.getProperty("spokenText").toString());

                            }
                            else if (value.equals("MATCH_REVERSE")) {
                                Log.println(Log.INFO, "SOAP", "MATCH_REVERSE");
                                int count = ((SoapObject) result.getProperty("nextOperations")).getPropertyCount();
                                Log.println(Log.INFO, "SOAP", "***********************************:"+count);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                switch (count) {
                                    case 1:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchReverse1Fragment(), "Fragment1");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;

                                    case 2:
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchReverse2Fragment(), "Fragment2");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;

                                    case 3:
                                        Log.println(Log.INFO, "SOAP", "***********************************:");
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchReverse3Fragment(), "Fragment3");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;
                                    case 4:    //Hack
                                        Log.println(Log.INFO, "SOAP", "***********************************:");
                                        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                                        fragmentTransaction.replace(R.id.fragment_container, new MatchReverse3Fragment(), "Fragment3");
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        break;
                                }

                                DelayTTS(result.getProperty("spokenText").toString());

                            }

                            else {
                                hiddenText.setText("null");
                            //    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            //    fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                            //    ((TextView) findViewById(R.id.spokenText)).setText(" ");

                            //    fragmentTransaction.replace(R.id.fragment_container, new StartFragment(), "StartFragment");
                            //    fragmentTransaction.addToBackStack(null);
                            //    fragmentTransaction.commit();
                            }
                    }
                    else
                        Log.println(Log.INFO, "SOAP", "no new SoapObject:");
                        Log.println(Log.INFO, "SOAP", "   ");
                }

                }catch(Exception e){
                    ErrorMessage(e, "CallGetSoapObject Error", result + "");
                }
            }
}



public void DelayTTS(final String spokenText){
    if (TTSStatus) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speakTTS(spokenText);
            }
        }, 2000);
    }
    else
    {
        TTSStatus = true;
    }
}

    private Boolean wifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Log.println(Log.INFO, "WIFI", "Wifi ok");
            return true;
        } else {
            Log.println(Log.INFO, "WIFI", "Wifi Fehler");
            Toast.makeText(getApplicationContext(), "Keine WIFI-Verbindung! Einstellungen überprüfen", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void ErrorMessage(Exception ex, String methodeName, String result) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainHBMSActivity.this);
            builder.setTitle("Error: " + methodeName);
            builder.setMessage(Log.getStackTraceString(ex) + "---------\n" + result);
            builder.setPositiveButton("OK", null);
            CodeOKrun = false;
            builder.show();

        } catch (Exception e) {
        }
    }

    public void speakTTS(String text) {
            if (TTSSpeakSwitch) {
                params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            }
        }




    private void speakTTSSiri(String text) {

        params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);

    }

    private void vibrationTablet() {
        if (VibrationStatus){
        if (TTSVibration) {
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
        }}
        else
            VibrationStatus=true;
    }

    @Override
    public void onStop() {
        tts.stop();
        Log.println(Log.INFO, "SOAP", "--->close!!!!");
        timerParam.cancel();
        timerSupport.cancel();
        super.onStop();
        finish();
    }



}
