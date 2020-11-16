package com.example.new_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.new_app.BitmapConverter.BitmapHelper;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class EdgeDetectionActivity extends AppCompatActivity {
    Button testbutton;
    ImageView processedpic;
    Button backbutton;
    Button houghbutton;

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
        backbutton = (Button)findViewById(R.id.button_back);
        houghbutton = (Button)findViewById(R.id.button_hough);
        processedpic = (ImageView)findViewById(R.id.imageView_processed);
        Bitmap initialpic = BitmapHelper.getInstance().getBitmap();
        Mat initialmat = new Mat();
        processedpic.setImageBitmap(initialpic);

//        byte[] bytes = getIntent().getByteArrayExtra("bitmapbytes");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);



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

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });

        houghbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Mat lines = new Mat();
//                Mat cdst = Mat.zeros(initialmat.size(), initialmat.type());
//                Imgproc.HoughLinesP(initialmat,lines,1,Math.PI/180,500,100, 50);
//
//                //Draw lines
//                for (int i = 0; i<lines.rows(); i++ ){
//                    double rho = lines.get(i,0)[0],
//                            theta = lines.get(i,0)[1];
//                    String r = String.valueOf(rho);
//                    Log.i("TAGrho", r);
//                    String t = String.valueOf(theta);
//                    Log.i("TAGtheta", t);
//
//                    double a = Math.cos(theta);
//                    double b = Math.sin(theta);
//                    double x0 = rho * a;
//                    double y0 = rho * b;
//                    Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
//                    Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
//                    Imgproc.line(cdst, pt1, pt2, new Scalar(255,0,0), 1, Imgproc.LINE_AA, 0);
//
//                }
//                Bitmap houghbmp = null;
//                houghbmp = Bitmap.createBitmap(cdst.width(), cdst.height(), Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(cdst,houghbmp);
//                processedpic.setImageBitmap(houghbmp);
                /**
                 * The following algorithm is contour detection and draw the contour
                 */
                Log.i("TAG", "line 128 entered contour");
                Mat hierarchy = new Mat();
                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                Imgproc.findContours(initialmat, contours, hierarchy, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
                Mat contourResult = new Mat(initialmat.size(), initialmat.type());
                for (int i = 0; i<contours.size();i++){
                    Imgproc.drawContours(contourResult,contours,i,new Scalar(255,255,255),1);
                }
                Bitmap contourbitmap = null;
                contourbitmap = Bitmap.createBitmap(initialmat.width(), initialmat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(contourResult,contourbitmap);
                processedpic.setImageBitmap(contourbitmap);

                Log.i("TAG", "line 136 finished contour");
            }
        });
    }


}