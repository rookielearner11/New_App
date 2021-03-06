package com.example.new_app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.new_app.BitmapConverter.BitmapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EdgeDetectionActivity extends AppCompatActivity {
    Button testbutton;
    ImageView processedpic;
    Button houghbutton,sendbutton;
    List<MatOfPoint> contoursConvert = new ArrayList<MatOfPoint>();
    private RequestQueue mQueue;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popup_username, popup_password;
    private Button popup_send,popup_cancel;

    private String username;
    private String password;
    private String usernameAndpassword;
    private String base64;



    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_detection);
        Log.i("TAG", "Line 23");

        //        Initialize the botton
        testbutton = (Button)findViewById(R.id.button_testing);

        houghbutton = (Button)findViewById(R.id.button_hough);

        sendbutton = (Button)findViewById((R.id.button_sendData));

        processedpic = (ImageView)findViewById(R.id.imageView_processed);

        Bitmap initialpic = BitmapHelper.getInstance().getBitmap();
        Mat initialmat = new Mat();
        processedpic.setImageBitmap(initialpic);

        mQueue = Volley.newRequestQueue(this);



        /**OnclickMethod for testbutton
         *
         */
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mat kernel = new Mat(15,15, CvType.CV_32F);

//                processedpic.setImageBitmap(bitmap);


                Utils.bitmapToMat(initialpic,initialmat);

                Imgproc.blur(initialmat, initialmat, new Size(6,6));
                Imgproc.cvtColor(initialmat, initialmat, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.Canny(initialmat, initialmat, 100, 200);
//                Imgproc.dilate(initialmat,initialmat,kernel);
//                Imgproc.filter2D(initialmat,initialmat,-1,kernel);
                Imgproc.blur(initialmat, initialmat, new Size(15,15));
//                Imgproc.dilate(initialmat,initialmat,kernel);
                Bitmap bmp = null;
                bmp = Bitmap.createBitmap(initialmat.width(), initialmat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(initialmat,bmp);
                processedpic.setImageBitmap(bmp);
//                Utils.matToBitmap(initialmat, initialpic);
//                processedpic.setImageBitmap(initialpic);

                Log.i("TAG", "Line 41");
            }
        });

        houghbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * The following codes are about contour detection and draw the contour so that user can see the result
                 */
                Log.i("TAG", "line 128 entered contour");
                Mat hierarchy = new Mat();

                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

                List<Point> pointslist = new ArrayList<Point>();

                Imgproc.findContours(initialmat, contours, hierarchy, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);

                Mat contourResult = new Mat(initialmat.size(), initialmat.type());

                for (int i = 0; i<contours.size();i++){
                    if(i % 2 == 0){
                        MatOfPoint2f tmp = new MatOfPoint2f();
                        MatOfPoint2f tmp2 = new MatOfPoint2f();
                        MatOfPoint tmp3 = new MatOfPoint();
                    String valueOfi = String.valueOf(i);
                    Log.i("TAGi", valueOfi);
                    System.out.println(contours.get(i));
                    contours.get(i).convertTo(tmp,CvType.CV_32FC2);
                    double epsilon = 0.01 * Imgproc.arcLength(tmp,true);
                    Imgproc.approxPolyDP(tmp,tmp2,epsilon,true);
                    tmp2.convertTo(tmp3,CvType.CV_32S);

                    contoursConvert.add(tmp3);
                    }

                }

                for (int j = 0; j<contoursConvert.size();j++){
                    String valueofj = String.valueOf(j);
                    System.out.println(contoursConvert.get(j));
                    Converters.Mat_to_vector_Point(contoursConvert.get(j), pointslist);
                    Imgproc.drawContours(contourResult,contoursConvert,j,new Scalar(255,0,255),4);
                }

                for (int k = 0;k<pointslist.size();k++){
                    String valueofk = String.valueOf(k);
                    Log.i("Tagpointlist", valueofk);
                    System.out.println(pointslist.get(k));
                }

                Bitmap contourbitmap = null;
                contourbitmap = Bitmap.createBitmap(initialmat.width(), initialmat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(contourResult,contourbitmap);
                processedpic.setImageBitmap(contourbitmap);

                Log.i("TAG", "line 136 finished contour");
            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuthDialog();

            }
        });
    }

    private void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(EdgeDetectionActivity.this);
        String url = "https://mypythonx.eastus2.cloudapp.azure.com/api/authenticate/users/tokens";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("line 234" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    //We parse the request string into an Object.
                    //We make the assumption that everything coming from the server
                    //Is a JSON object.

                }
                catch (Exception e) {}
                finally {
                    System.out.println("That didn't work!");
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Authorization", base64);
                System.out.println("line 260");
                System.out.println(params);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void sendAuthDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View infoPopupWindow = getLayoutInflater().inflate(R.layout.popupwindow, null);
        popup_username = (EditText) infoPopupWindow.findViewById(R.id.popup_username);
        popup_password = (EditText) infoPopupWindow.findViewById(R.id.popup_password);
        popup_send = (Button) infoPopupWindow.findViewById(R.id.popup_submitbutton);
        popup_cancel = (Button) infoPopupWindow.findViewById(R.id.popup_cancelbutton);

        dialogBuilder.setView(infoPopupWindow);
        dialog = dialogBuilder.create();
        dialog.show();

        popup_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = popup_username.getText().toString();
                password = popup_password.getText().toString();
                usernameAndpassword = username +":"+ password;
                try {
                    byte[] tmp = usernameAndpassword.getBytes("UTF-8");
                    base64 = "Basic " + Base64.encodeToString(tmp, Base64.DEFAULT);
                    System.out.println(base64);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sendData();
            }
        });

        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}