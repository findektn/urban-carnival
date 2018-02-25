package com.hbms.matchFragment;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.hbms.hbmssupport.R;
import com.hbms.hbmssupport.SoapObjectOperationParam;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by gertmorak on 01.12.16.
 */

public class MatchOKLayout extends Application {
    String HOST;
    PopupWindow popupWindow;
    View view;
    Activity activity;

    HashMap<String, String> params;


    public MatchOKLayout() {
    }


    public void setLayout(Activity activity1, View view1) {

        view = view1;
        activity = activity1;

        HOST = activity.getString(R.string.host);
        final SoapObject soapObject = SoapObjectOperationParam.soapObjectOperationParam;

        String value = soapObject.getProperty("matchStatus").toString();

        if (value.equals("MATCH_FAIL")) {

            ((TextView) activity.findViewById(R.id.spokenText)).setText(soapObject.getProperty("spokenText").toString());
            setTextFragment(view, soapObject, R.id.nextOperationGermanName, "errorMessageGerman");

        }


        if (value.equals("MATCH_WRONG")) {

            ((TextView) activity.findViewById(R.id.spokenText)).setText(soapObject.getProperty("spokenText").toString());
            setTextFragment(view, soapObject, R.id.germanOperationName, "germanOperationName");
            setTextFragment(view, soapObject, R.id.germanOperationDescription, "germanOperationDescription");

            SoapObject s1 = (SoapObject) ((SoapObject) soapObject.getProperty("operationMediaItems")).getProperty(0);
            String mediatype = s1.getProperty("type").toString();

            if (mediatype.equals("Image")) {
                setImagePicasso(view, (ImageView) view.findViewById(R.id.leftImage), (VideoView) view.findViewById(R.id.videoViewLeft), s1);
            }

            if (mediatype.equals("Video")) {
                setVideoView((VideoView) view.findViewById(R.id.videoViewLeft), (ImageView) view.findViewById(R.id.leftImage), s1, false);
            }

            setTextFragment(view, soapObject, R.id.nextOperationGermanName, "reverseOperationGermanName");
            setTextFragment(view, soapObject, R.id.nextOperationGermanDescription, "reverseOperationGermanDescription");

            SoapObject mediatypeSoap = ((SoapObject) soapObject.getProperty("reverseOperationMediaItems"));
            SoapObject mediatypeSoap1 = (SoapObject) mediatypeSoap.getProperty(0);

            String nextmediatype = mediatypeSoap1.getProperty("type").toString();

            if (nextmediatype.equals("Image")) {
                setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage), (VideoView) view.findViewById(R.id.videoViewRight), mediatypeSoap1);

            }

            if (nextmediatype.equals("Video")) {
                setVideoView((VideoView) view.findViewById(R.id.videoViewRight), (ImageView) view.findViewById(R.id.rightImage), mediatypeSoap1, false);
            }

        }

        //########################################### Match_OK########################################
        if (value.equals("MATCH_OK")) {
            ((TextView) activity.findViewById(R.id.spokenText)).setText(soapObject.getProperty("spokenText").toString());
            setTextFragment(view, soapObject, R.id.germanOperationName, "germanOperationName");
            setTextFragment(view, soapObject, R.id.germanOperationDescription, "germanOperationDescription");
            SoapObject s1 = (SoapObject) ((SoapObject) soapObject.getProperty("operationMediaItems")).getProperty(0);
            String mediatype = s1.getProperty("type").toString();


            if (mediatype.equals("Image")) {
                setImagePicasso(view, (ImageView) view.findViewById(R.id.leftImage), (VideoView) view.findViewById(R.id.videoViewLeft), s1);
            }

            if (mediatype.equals("Video")) {
                setVideoView((VideoView) view.findViewById(R.id.videoViewLeft), (ImageView) view.findViewById(R.id.leftImage), s1, false);
            }


            int count = ((SoapObject) soapObject.getProperty("nextOperations")).getPropertyCount();
            Log.println(Log.INFO, "SOAP", "NextOperation Count: " + count);


            if ((count == 1) && value.equals("MATCH_OK")) {

                SoapObject nextOperations = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations, R.id.nextOperationGermanName, "nextOperationGermanName");
                setTextFragment(view, nextOperations, R.id.nextOperationGermanDescription, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems = (SoapObject) ((SoapObject) nextOperations.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype = nextOperationMediaItems.getProperty("type").toString();

                if (nextmediatype.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage), (VideoView) view.findViewById(R.id.videoViewRight), nextOperationMediaItems);

                }

                if (nextmediatype.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight), (ImageView) view.findViewById(R.id.rightImage), nextOperationMediaItems, false);
                }

            }

            if ((count == 2) && value.equals("MATCH_OK")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                Log.println(Log.INFO, "Sonja", "****>" + nextOperationMediaItems1.toString());
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }
                if (nextmediatype1.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight1), (ImageView) view.findViewById(R.id.rightImage1), nextOperationMediaItems1, false);
                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                if (nextmediatype2.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight2), (ImageView) view.findViewById(R.id.rightImage1), nextOperationMediaItems2, false);
                }

            }


            if ((count == 3) && value.equals("MATCH_OK")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage2), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                SoapObject nextOperations3 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanName3, "nextOperationGermanName");
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanDescription3, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems3 = (SoapObject) ((SoapObject) nextOperations3.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype3 = nextOperationMediaItems3.getProperty("type").toString();

                if (nextmediatype3.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage3), (VideoView) view.findViewById(R.id.videoViewRight3), nextOperationMediaItems3);

                }

            }


            if (count == 4 && value.equals("MATCH_OK")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage2), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                SoapObject nextOperations3 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(2);
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanName3, "nextOperationGermanName");
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanDescription3, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems3 = (SoapObject) ((SoapObject) nextOperations3.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype3 = nextOperationMediaItems3.getProperty("type").toString();

                if (nextmediatype3.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage3), (VideoView) view.findViewById(R.id.videoViewRight3), nextOperationMediaItems3);

                }
                SoapObject nextOperations4 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(3);
                setTextFragment(view, nextOperations4, R.id.nextOperationGermanName4, "nextOperationGermanName");
                setTextFragment(view, nextOperations4, R.id.nextOperationGermanDescription4, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems4 = (SoapObject) ((SoapObject) nextOperations4.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype4 = nextOperationMediaItems4.getProperty("type").toString();

                if (nextmediatype4.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage4), (VideoView) view.findViewById(R.id.videoViewRight4), nextOperationMediaItems4);


                }


            }

            if ((count == 5) && value.equals("MATCH_OK")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage2), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                SoapObject nextOperations3 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(2);
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanName3, "nextOperationGermanName");
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanDescription3, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems3 = (SoapObject) ((SoapObject) nextOperations3.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype3 = nextOperationMediaItems3.getProperty("type").toString();

                if (nextmediatype3.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage3), (VideoView) view.findViewById(R.id.videoViewRight3), nextOperationMediaItems3);

                }
                SoapObject nextOperations4 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(3);
                setTextFragment(view, nextOperations4, R.id.nextOperationGermanName4, "nextOperationGermanName");
                setTextFragment(view, nextOperations4, R.id.nextOperationGermanDescription4, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems4 = (SoapObject) ((SoapObject) nextOperations4.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype4 = nextOperationMediaItems4.getProperty("type").toString();

                if (nextmediatype4.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage4), (VideoView) view.findViewById(R.id.videoViewRight4), nextOperationMediaItems4);

                }


                SoapObject nextOperations5 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(4);
                setTextFragment(view, nextOperations5, R.id.nextOperationGermanName5, "nextOperationGermanName");
                setTextFragment(view, nextOperations5, R.id.nextOperationGermanDescription5, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems5 = (SoapObject) ((SoapObject) nextOperations5.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype5 = nextOperationMediaItems5.getProperty("type").toString();

                if (nextmediatype5.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage5), (VideoView) view.findViewById(R.id.videoViewRight5), nextOperationMediaItems4);

                }

            }

        }

        //########################################### Match_Reverse########################################
        if (value.equals("MATCH_REVERSE")) {
            ((TextView) activity.findViewById(R.id.spokenText)).setText(soapObject.getProperty("spokenText").toString());
            setTextFragment(view, soapObject, R.id.germanOperationName, "germanOperationName");
            setTextFragment(view, soapObject, R.id.germanOperationDescription, "germanOperationDescription");
            SoapObject s1 = (SoapObject) ((SoapObject) soapObject.getProperty("operationMediaItems")).getProperty(0);
            String mediatype = s1.getProperty("type").toString();


            if (mediatype.equals("Image")) {
                setImagePicasso(view, (ImageView) view.findViewById(R.id.leftImage), (VideoView) view.findViewById(R.id.videoViewLeft), s1);
            }

            if (mediatype.equals("Video")) {
                setVideoView((VideoView) view.findViewById(R.id.videoViewLeft), (ImageView) view.findViewById(R.id.leftImage), s1, false);
            }


            int count = ((SoapObject) soapObject.getProperty("nextOperations")).getPropertyCount();
            Log.println(Log.INFO, "SOAP", "NextOperation Count: " + count);


            if (count == 1 && value.equals("MATCH_REVERSE")) {

                SoapObject nextOperations = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations, R.id.nextOperationGermanName, "nextOperationGermanName");
                setTextFragment(view, nextOperations, R.id.nextOperationGermanDescription, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems = (SoapObject) ((SoapObject) nextOperations.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype = nextOperationMediaItems.getProperty("type").toString();

                if (nextmediatype.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage), (VideoView) view.findViewById(R.id.videoViewRight), nextOperationMediaItems);

                }

                if (nextmediatype.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight), (ImageView) view.findViewById(R.id.rightImage), nextOperationMediaItems, false);
                }

            }

            if (count == 2 && value.equals("MATCH_REVERSE")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                Log.println(Log.INFO, "Sonja", "****>" + nextOperationMediaItems1.toString());
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }
                if (nextmediatype1.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight1), (ImageView) view.findViewById(R.id.rightImage1), nextOperationMediaItems1, false);
                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                if (nextmediatype2.equals("Video")) {
                    setVideoView((VideoView) view.findViewById(R.id.videoViewRight2), (ImageView) view.findViewById(R.id.rightImage1), nextOperationMediaItems2, false);
                }

            }


            if (count == 3  && value.equals("MATCH_REVERSE")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage2), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                SoapObject nextOperations3 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(2);
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanName3, "nextOperationGermanName");
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanDescription3, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems3 = (SoapObject) ((SoapObject) nextOperations3.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype3 = nextOperationMediaItems3.getProperty("type").toString();

                if (nextmediatype3.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage3), (VideoView) view.findViewById(R.id.videoViewRight3), nextOperationMediaItems3);

                }

            }

            if (count == 4  && value.equals("MATCH_REVERSE")) {

                SoapObject nextOperations1 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(0);
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanName1, "nextOperationGermanName");
                setTextFragment(view, nextOperations1, R.id.nextOperationGermanDescription1, "nextOperationGermanDescription");

                SoapObject nextOperationMediaItems1 = (SoapObject) ((SoapObject) nextOperations1.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype1 = nextOperationMediaItems1.getProperty("type").toString();

                if (nextmediatype1.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage1), (VideoView) view.findViewById(R.id.videoViewRight1), nextOperationMediaItems1);

                }

                SoapObject nextOperations2 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(1);
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanName2, "nextOperationGermanName");
                setTextFragment(view, nextOperations2, R.id.nextOperationGermanDescription2, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems2 = (SoapObject) ((SoapObject) nextOperations2.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype2 = nextOperationMediaItems2.getProperty("type").toString();

                if (nextmediatype2.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage2), (VideoView) view.findViewById(R.id.videoViewRight2), nextOperationMediaItems2);

                }

                SoapObject nextOperations3 = (SoapObject) ((SoapObject) soapObject.getProperty("nextOperations")).getProperty(2);
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanName3, "nextOperationGermanName");
                setTextFragment(view, nextOperations3, R.id.nextOperationGermanDescription3, "nextOperationGermanDescription");
                SoapObject nextOperationMediaItems3 = (SoapObject) ((SoapObject) nextOperations3.getProperty("nextOperationMediaItems")).getProperty(0);
                String nextmediatype3 = nextOperationMediaItems3.getProperty("type").toString();

                if (nextmediatype3.equals("Image")) {
                    setImagePicasso(view, (ImageView) view.findViewById(R.id.rightImage3), (VideoView) view.findViewById(R.id.videoViewRight3), nextOperationMediaItems3);

                }

            }
        }


    }


    public void setTextFragment(View view, SoapObject s1, int idname, String jsonName) {

        ((TextView) view.findViewById(idname)).setText(s1.getProperty(jsonName).toString());

    }

    public void setVideoView(final VideoView videoView, ImageView imageview, SoapObject soapOpject, final Boolean play) {
        videoView.setVisibility(View.VISIBLE);
        imageview.setVisibility(View.INVISIBLE);
        Uri videourl = Uri.parse(Environment.getExternalStorageDirectory() + "/MOVIES/" + soapOpject.getProperty("filePath").toString());
        MediaController mediaController = new MediaController(view.getContext());
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
                    videoView.seekTo(0);
                    mp.start();
                }
            }


        });


    }

    public void setImagePicasso(View view, ImageView imageview, VideoView videoView, SoapObject soapOpject) {

        File file2 = new File(Environment.getExternalStorageDirectory() + "/HBMS/" + soapOpject.getProperty("filePath").toString());
        if (file2 != null) {
            //Picture local
            if (file2.exists()) {
                Picasso.with(view.getContext()).load(file2).into(imageview);
                imageview.setTag(file2.toString());

            }
            //picture extern
            else {
                Picasso.with(view.getContext()).load(HOST + soapOpject.getProperty("filePath").toString()).into(imageview);
                imageview.setTag(HOST + soapOpject.getProperty("filePath").toString());

            }

        }
        setPopupImage(imageview);
        videoView.setVisibility(View.INVISIBLE);

    }

    public void setPopupImage(final ImageView imageViewPopup) {

        imageViewPopup.setOnClickListener(new ImageView.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                checkPopupOpen();
                setBackgroundAlphaTrans();
                LayoutInflater layoutInflater
                        = (LayoutInflater) view.getContext()
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
                    Picasso.with(view.getContext()).load(file).into(imageZoom);


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

    private void checkPopupOpen() {
        try {
            popupWindow.dismiss();
            setBackgroundAlphaNormal();
        } catch (Exception ex) {

        }
    }

    private void setBackgroundAlphaNormal() {
        LinearLayout content1 = (LinearLayout) activity.findViewById(R.id.mainLinearLayout);
        content1.setBackgroundColor(Color.WHITE);
        content1.setAlpha((float) 1.0);
    }

    private void setBackgroundAlphaTrans() {
        LinearLayout content1 = (LinearLayout) activity.findViewById(R.id.mainLinearLayout);
        content1.setBackgroundColor(Color.WHITE);
        content1.setAlpha((float) 0.1);
    }


}