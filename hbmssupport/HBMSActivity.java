package com.hbms.hbmssupport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.hbms.webservice.WebserviceCallParams;
import com.hbms.webservice.WebserviceCallSupportObject;
import com.hbms.webservice.WebserviceHardReset;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class HBMSActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextToSpeech tts;
    HashMap<String, String> params;
    String operationID = "11";
    String HOST = null;
    Boolean szenarioTTS = true;
    PopupWindow popupWindow;
    Boolean TTSSpeakSwitch;
    Boolean TTSVibration;
    String matchStatusfailorEnd = "";
    Boolean CodeOKrun = true;
    Timer timerSupport;
    Timer timerParam;
    SharedPreferences.Editor editor;
    SoapObject PersonSoapObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HOST = getString(R.string.host);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private modeƒ
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


        LinearLayout content = (LinearLayout) findViewById(R.id.ContentFrame);
        content.setVisibility(View.INVISIBLE);
        LinearLayout failFrame1 = (LinearLayout) findViewById(R.id.layoutFail);
        failFrame1.setVisibility(View.INVISIBLE);

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


        LinearLayout myview = (LinearLayout) findViewById(R.id.mainLinearLayout);
        myview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                int action = touchevent.getAction();

                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN:

                        int count = touchevent.getPointerCount();

                       /* if (count == 2) {
                            Intent myIntent = new Intent(getApplicationContext(), MainActivity_mgmt.class);
                            tts.stop();
                            tts.shutdown();
                            timerParam.cancel();
                            timerSupport.cancel();
                            startActivity(myIntent);
                           // overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        }*/

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
                                                                       siri.setImageResource(R.drawable.siri);
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
                                                                       siri.setImageResource(R.drawable.siria);


                                                                   }
                                                               });
                                                           }
                                                       }
                    );

                    tts.setLanguage(Locale.GERMAN);


                } else {
                    Log.println(Log.INFO, "Finde", "--->TTS Fehler!!!!");
                }
            }

            public void shutDown() {
                tts.shutdown();
            }
        });



        setRepeatingAsyncTaskParameter();
        setRepeatingAsyncTaskSupport();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);


        siri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView hiddenText = (TextView) findViewById(R.id.hiddenText);
                if (hiddenText.getText() == "null") {
                    final ImageView siri = (ImageView) findViewById(R.id.imageViewSiri);
                    siri.getAnimation().cancel();

                    String st1 = getString(R.string.welcomeHBMS1);
                    String user = ((TextView) findViewById(R.id.textViewUser)).getText() + "";
                    String st2 = getString(R.string.welcomeHBMS2);

                    speakTTSSiri(st1 + user + ", " + st2);
                } else {

                    if (matchStatusfailorEnd.equals("MATCH_FAIL")) {
                        TextView rightDescription = (TextView) findViewById(R.id.errorText);
                        speakTTSSiri(rightDescription.getText().toString());
                    }

                    if (matchStatusfailorEnd.equals("MATCH_OKEndtrue")) {
                        speakTTSSiri("Sie haben es geschafft!");
                    }

                    if (matchStatusfailorEnd.equals("MATCH_OKEndfalse")) {
                        TextView rightDescription = (TextView) findViewById(R.id.textViewRightDescription);
                        speakTTSSiri(rightDescription.getText().toString());
                    }


                }
            }
        });

    }


    @Override
    public void onStop() {
        tts.stop();
        super.onDestroy();
        Log.println(Log.INFO, "SOAP", "--->close!!!!");
        timerParam.cancel();
        timerSupport.cancel();
        super.onStop();
        super.onDestroy();
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_resetClient) {
            if (wifiConnect()) {


            }

        } else if (id == R.id.nav_resethard) {
            if (wifiConnect()) {
                WebserviceHardResetAsync reset = new WebserviceHardResetAsync();
                reset.execute();
                nav_resetClient();
            }


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void nav_resetClient() {
        tts.shutdown();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);

    }


    private void setRepeatingAsyncTaskParameter() {
        final Handler handler = new Handler();
        timerSupport = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (wifiConnect() && CodeOKrun) {
                            try {

                                CAllGetSupportObj getSupportObj = new CAllGetSupportObj();
                                Log.println(Log.INFO, "Finde", "-->Lade SoapObjekt");
                                getSupportObj.execute();


                            } catch (Exception e) {
                                Log.println(Log.INFO, "Finde", "-->Fehler Callsync" + e.toString());
                            }
                        }

                    }
                });
            }
        };
        timerSupport.schedule(task, 0, 4 * 1000);  // interval of one minute

    }


    private void setRepeatingAsyncTaskSupport() {
        final Handler handler = new Handler();
        timerParam = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (wifiConnect() && CodeOKrun) {
                            try {

                                CallGetParam getParam = new CallGetParam();
                                Log.println(Log.INFO, "Finde", "--->Lade Parameter");
                                getParam.execute();

                            } catch (Exception e) {
                                Log.println(Log.INFO, "Finde", "-->Fehler Callsync" + e.toString());
                            }
                        }
                    }
                });
            }
        };
        timerParam.schedule(task, 0, 4 * 1000);  // interval of one minute

    }


    private Boolean wifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Log.println(Log.INFO, "Finde", "Wifi ok");

            return true;
        } else {
            Log.println(Log.INFO, "Finde", "Wifi Fehler");
            Toast.makeText(getApplicationContext(), "Keine WIFI-Verbindung! Einstellungen überprüfen", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void setPopupImage(final ImageView imageViewPopup) {

        imageViewPopup.setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkPopupOpen();
                setBackgroundAlphaTrans();
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                //ImageZoomleft + Picasso
                String sleft = imageViewPopup.getTag() + "";
                File file = new File(sleft);
                ImageView imageZoom = (ImageView) popupView.findViewById(R.id.ImageviewPopUp);

                if (sleft.contains("http://"))
                    Picasso.with(getApplicationContext()).load(sleft).into(imageZoom);
                else
                    Picasso.with(getApplicationContext()).load(file).into(imageZoom);


                LinearLayout btnDismiss = (LinearLayout) popupView.findViewById(R.id.popup);
                btnDismiss.setOnClickListener(new LinearLayout.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setBackgroundAlphaNormal();
                        popupWindow.dismiss();

                    }
                });
                popupWindow.showAtLocation(imageViewPopup, Gravity.CENTER, 0, 0);

            }
        });
    }

    public void setImagePicasso(ImageView imageview, VideoView videoView, SoapObject soapOpject) {

        File file2 = new File(Environment.getExternalStorageDirectory() + "/HBMS/" + soapOpject.getProperty("filePath").toString());
        if (file2 != null) {
            //Picture local
            if (file2.exists()) {
                Picasso.with(getApplicationContext()).load(file2).into(imageview);
                imageview.setTag(file2.toString());

            }
            //picture extern
            else {
                Picasso.with(getApplicationContext()).load(HOST + soapOpject.getProperty("filePath").toString()).into(imageview);
                imageview.setTag(HOST + soapOpject.getProperty("filePath").toString());


            }
        }

        setPopupImage(imageview);
        videoView.setVisibility(View.INVISIBLE);
        imageview.setVisibility(View.VISIBLE);
        LinearLayout failFrame = (LinearLayout) findViewById(R.id.layoutFail);
        failFrame.setVisibility(View.INVISIBLE);

    }

    public void setVideoView(final VideoView videoView, ImageView imageview, SoapObject soapOpject, final Boolean play) {
        videoView.setVisibility(View.VISIBLE);
        imageview.setVisibility(View.INVISIBLE);
        Uri videourl = Uri.parse(Environment.getExternalStorageDirectory() + "/MOVIES/" + soapOpject.getProperty("filePath").toString());
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setVideoURI(videourl);
        videoView.seekTo(100);
        videoView.forceLayout();
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (play) {
                    mp.setLooping(false);
                    // final ImageView imageViewnPopup = (ImageView) findViewById(R.id.leftImage);
                    // imageViewnPopup.setOnClickListener(null);
                    tts.speak("", TextToSpeech.QUEUE_FLUSH, params);
                    videoView.seekTo(0);
                    mp.start();
                }
            }


        });
        LinearLayout failFrame = (LinearLayout) findViewById(R.id.layoutFail);
        failFrame.setVisibility(View.INVISIBLE);
    }

    public void ErrorMessage(Exception ex, String methodeName, String result) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(HBMSActivity.this);
            builder.setTitle("Error: " + methodeName);
            builder.setMessage(Log.getStackTraceString(ex) + "---------\n" + result);
            builder.setPositiveButton("OK", null);
            CodeOKrun = false;
            builder.show();

        } catch (Exception e) {
        }
    }

    private void vibrationTablet() {
        if (TTSVibration) {
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }

    private void setBackgroundAlphaNormal() {
        LinearLayout content1 = (LinearLayout) findViewById(R.id.mainLinearLayout);
        content1.setBackgroundColor(Color.WHITE);
        content1.setAlpha((float) 1.0);
    }

    private void setBackgroundAlphaTrans() {
        LinearLayout content1 = (LinearLayout) findViewById(R.id.mainLinearLayout);
        content1.setBackgroundColor(Color.WHITE);
        content1.setAlpha((float) 0.1);
    }

    private void speakTTS(String text) {
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

    private void checkPopupOpen() {
        try {
            popupWindow.dismiss();
            setBackgroundAlphaNormal();
        } catch (Exception ex) {

        }
    }

    class WebserviceHardResetAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            WebserviceHardReset reset = new WebserviceHardReset();
            reset.Call(HOST);
            return null;
        }
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

                TextView hiddenText = (TextView) findViewById(R.id.hiddenText);
                hiddenText.setText("null");

                if (result != null) {
                    if (!result.getProperty(0).toString().equals("ErrorSoapObj")) {
                        szenarioTTS = true;
                        String operationIDWS = result.getProperty(1).toString();
                        hiddenText.setText(operationIDWS);
                        Log.println(Log.INFO, "Finde", "---------------------------->SoapObjekt ist " + result);


                        if (!operationIDWS.equals(operationID)) {
                            LinearLayout leftFrame = (LinearLayout) findViewById(R.id.leftFrame);
                            leftFrame.setVisibility(View.VISIBLE);
                            LinearLayout rightFrame = (LinearLayout) findViewById(R.id.rightFrame);
                            rightFrame.setVisibility(View.VISIBLE);
                            View lineView = findViewById(R.id.LineView);
                            lineView.setVisibility(View.VISIBLE);
                            operationID = operationIDWS;
                            checkPopupOpen();
                            vibrationTablet();

                            rightFrame.setBackgroundResource(R.drawable.sharpeaktive);
                            String matchStatus = result.getProperty(0).toString();
                            String endOperation = result.getProperty(9).toString();
                            String germanOperationName = result.getProperty(3).toString();


                            ((ImageView) findViewById(R.id.leftImage)).setImageDrawable(null);
                            ((ImageView) findViewById(R.id.rightImage)).setImageDrawable(null);
                            ((VideoView) findViewById(R.id.videoViewRight)).setVideoURI(null);
                            ((VideoView) findViewById(R.id.videoViewLeft)).setVideoURI(null);


                            if (matchStatus.equals("MATCH_OK") && endOperation.equals("true")) {

                                //leftContent
                                Log.println(Log.INFO, "Output", " 3 matchatus: MATCH_OK +endOperation");
                                matchStatusfailorEnd = "MATCH_OKEndtrue";
                                TextView leftHeader = (TextView) findViewById(R.id.textViewLeftHeader);
                                leftHeader.setText(germanOperationName);
                                TextView leftDesc = (TextView) findViewById(R.id.textViewLeftDescription);


                                SoapObject s = (SoapObject) result.getProperty(10);
                                SoapObject s1 = (SoapObject) s.getProperty(0);
                                String typeRight = s1.getProperty("type").toString();

                                if (typeRight.equals("Image")) {

                                    setImagePicasso((ImageView) findViewById(R.id.leftImage), (VideoView) findViewById(R.id.videoViewLeft), s1);
                                }

                                if (typeRight.equals("Video")) {
                                    setVideoView((VideoView) findViewById(R.id.videoViewLeft), (ImageView) findViewById(R.id.leftImage), s1, false);
                                }

                                //rightContent
                                ImageView imageViewRight = (ImageView) findViewById(R.id.rightImage);


                                SoapObject soapRight = new SoapObject();
                                soapRight.addProperty("filePath", "picture/haken.png");
                                setImagePicasso((ImageView) findViewById(R.id.rightImage), (VideoView) findViewById(R.id.videoViewRight), soapRight);

                                TextView rightHeader = (TextView) findViewById(R.id.textViewRightHeader);
                                rightHeader.setText("      ");
                                TextView rightDesc = (TextView) findViewById(R.id.textViewRightDescription);
                                rightDesc.setText("      ");
                                leftDesc.setText("      ");
                                leftDesc.setVisibility(View.INVISIBLE);

                                speakTTS(getString(R.string.lastOperation));

                            }


                            if (matchStatus.equals("MATCH_OK") && endOperation.equals("false") || matchStatus.equals("MATCH_REVERSE")) {

                                matchStatusfailorEnd = "MATCH_OKEndfalse";

                                if (matchStatus.equals("MATCH_REVERSE")) {

                                    rightFrame.setBackgroundResource(R.drawable.sharpeakwrong);
                                } else {
                                    rightFrame.setBackgroundResource(R.drawable.sharpeaktive);
                                }

                                Log.println(Log.INFO, "Output", " 3 matchatus: MATCH_OK  endOperationFalse MATCH_REVERSE ");
                                String nextOperationGermanName = result.getProperty(19).toString();
                                String nextOperationGermanDescription = result.getProperty(21).toString();

                                TextView leftHeader = (TextView) findViewById(R.id.textViewLeftHeader);
                                leftHeader.setText(germanOperationName);
                                TextView leftDesrc = (TextView) findViewById(R.id.textViewLeftDescription);


                                leftDesrc.setText("");

                                SoapObject s = (SoapObject) result.getProperty(10);
                                SoapObject s1 = (SoapObject) s.getProperty(0);


                                //Video,Image
                                String type = s1.getProperty("type").toString();

                                if (type.equals("Image")) {
                                    setImagePicasso((ImageView) findViewById(R.id.leftImage), (VideoView) findViewById(R.id.videoViewLeft), s1);
                                }

                                if (type.equals("Video")) {
                                    setVideoView((VideoView) findViewById(R.id.videoViewLeft), (ImageView) findViewById(R.id.leftImage), s1, false);
                                    Log.println(Log.INFO, "Test", type);
                                }

                                //rightContent

                                TextView rightHeader = (TextView) findViewById(R.id.textViewRightHeader);
                                rightHeader.setText(nextOperationGermanName);
                                TextView rightDesc = (TextView) findViewById(R.id.textViewRightDescription);
                                rightDesc.setText(nextOperationGermanDescription);

                                //same place as right
                                leftDesrc.setText(nextOperationGermanDescription);
                                leftDesrc.setVisibility(View.INVISIBLE);


                                SoapObject sRight = (SoapObject) result.getProperty(22);
                                SoapObject s1Right = (SoapObject) sRight.getProperty(0);
                                String typeRight = s1Right.getProperty("type").toString();

                                if (typeRight.equals("Image")) {
                                    setImagePicasso((ImageView) findViewById(R.id.rightImage), (VideoView) findViewById(R.id.videoViewRight), s1Right);
                                }

                                if (typeRight.equals("Video")) {
                                    setVideoView((VideoView) findViewById(R.id.videoViewRight), (ImageView) findViewById(R.id.rightImage), s1Right, true);
                                }
                                speakTTS(rightDesc.getText() + "");
                            }


                            if (matchStatus.equals("MATCH_FAIL")) {
                                Log.println(Log.INFO, "Output", " 3 matchatus: MATCH_FAIL ");
                                matchStatusfailorEnd = "MATCH_FAIL";
                                leftFrame.setVisibility(View.INVISIBLE);
                                rightFrame.setVisibility(View.INVISIBLE);
                                lineView.setVisibility(View.INVISIBLE);
                                Log.println(Log.INFO, "Output", "Mach_Fail");
                                LinearLayout failFrame1 = (LinearLayout) findViewById(R.id.layoutFail);
                                failFrame1.setVisibility(View.VISIBLE);
                                VideoView videoViewLeft = (VideoView) findViewById(R.id.videoViewLeft);
                                videoViewLeft.stopPlayback();
                                VideoView videoViewRight = (VideoView) findViewById(R.id.videoViewRight);
                                videoViewRight.stopPlayback();

                                videoViewLeft.setVisibility(View.INVISIBLE);
                                videoViewRight.setVisibility(View.INVISIBLE);
                                tts.stop();
                                speakTTS(getString(R.string.ErrorOperation));
                            }


                            if (matchStatus.equals("MATCH_WRONG")) {
                                matchStatusfailorEnd = "MATCH_OKEndfalse";
                                Log.println(Log.INFO, "Output", " 3 matchatus: MATCH_WRONG ");
                                String reverseOperationGermanName = result.getProperty(13).toString();
                                String reverseOperationGermanDescription = result.getProperty(15).toString();

                                TextView leftHeader = (TextView) findViewById(R.id.textViewLeftHeader);
                                leftHeader.setText(germanOperationName);
                                TextView leftDesrc = (TextView) findViewById(R.id.textViewLeftDescription);
                                leftDesrc.setText("");

                                SoapObject s = (SoapObject) result.getProperty(10);
                                SoapObject s1 = (SoapObject) s.getProperty(0);


                                String type = s1.getProperty("type").toString();

                                if (type.equals("Image")) {
                                    setImagePicasso((ImageView) findViewById(R.id.leftImage), (VideoView) findViewById(R.id.videoViewLeft), s1);
                                }

                                if (type.equals("Video")) {
                                    setVideoView((VideoView) findViewById(R.id.videoViewLeft), (ImageView) findViewById(R.id.leftImage), s1, false);
                                }

                                //rightContent

                                TextView rightHeader = (TextView) findViewById(R.id.textViewRightHeader);
                                rightHeader.setText(reverseOperationGermanName);
                                TextView rightDesc = (TextView) findViewById(R.id.textViewRightDescription);
                                rightDesc.setText(reverseOperationGermanDescription);

                                leftDesrc.setText(reverseOperationGermanDescription);
                                leftDesrc.setVisibility(View.INVISIBLE);

                                rightFrame.setBackgroundResource(R.drawable.sharpeakwrong);

                                SoapObject sLeft = (SoapObject) result.getProperty(16);
                                SoapObject s1Right = (SoapObject) sLeft.getProperty(0);


                                String typeRight = s1Right.getProperty("type").toString();

                                if (typeRight.equals("Image")) {
                                    setImagePicasso((ImageView) findViewById(R.id.rightImage), (VideoView) findViewById(R.id.videoViewRight), s1Right);
                                }

                                if (typeRight.equals("Video")) {
                                    setVideoView((VideoView) findViewById(R.id.videoViewRight), (ImageView) findViewById(R.id.rightImage), s1Right, true);
                                }

                                speakTTS(rightDesc.getText() + "");


                            }


                        } else {
                            Log.println(Log.INFO, "Finde", "-->SoapObjekt schon geladen");
                        }

                        LinearLayout content = (LinearLayout) findViewById(R.id.ContentFrame);
                        content.setVisibility(View.VISIBLE);

                    }
                } else {
                    //progressView.setVisibility(View.VISIBLE);
                    //progressView.startProgress();
                }


            } catch (Exception e) {
                ErrorMessage(e, "CallGetSoapObject", result + "");
            }
        }
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

                    SoapObjectParam.soapObjParam = result;


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
                    Log.println(Log.INFO, "Finde", "Lade Parameter ok:" + result + "");


                } else {
                    Toast.makeText(getApplicationContext(), "Parameter nicht initialisiert! ", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

                ErrorMessage(e, "CallGetParam", result + "");
            }
        }

    }


}
