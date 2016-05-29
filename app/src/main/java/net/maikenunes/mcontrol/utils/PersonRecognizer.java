package net.maikenunes.mcontrol.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import com.googlecode.javacv.cpp.opencv_imgproc;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

public  class PersonRecognizer {
	
	FaceRecognizer faceRecognizer;
	public List<File> imageFiles;
	private int mProb=999;
    int[] labels;

    public PersonRecognizer(List<File> files)
    {
        faceRecognizer =  com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer(2,8,8,8,200);
		imageFiles = files;
    }
	
	public boolean train() {

        MatVector images = new MatVector(imageFiles.size());

        int counter = 0;
        IplImage img=null;
        IplImage grayImg;

        labels = new int[imageFiles.size()];

        for (File image : imageFiles) {
        	String p = image.getAbsolutePath();
            img = cvLoadImage(p);
            
            if (img==null) {
				Log.e("Error", "Error cVLoadImage");
			}
            Log.i("image", p);
            
            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(img, grayImg, CV_BGR2GRAY);
            images.put(counter, grayImg);

            labels[counter] = counter;

            counter++;
        }

        if (counter>0) {
            faceRecognizer.train(images, labels);
        }

	    return true;
	}
	
	public boolean canPredict()
	{
		return true;
		
	}
	
	public String predict(Mat m) {
        if (!canPredict()) {
			return "";
		}
		int n[] = new int[1];
		double p[] = new double[1];
		IplImage ipl = MatToIplImage(m, m.width(), m.height());

		faceRecognizer.predict(ipl, n, p);
		
		if (n[0]!=-1) {
			mProb = (int) p[0];
		} else {
			mProb = -1;
        }
        if (n[0] != -1) {
            return String.valueOf(labels[n[0]]);
        } else {
            return "Unkown";
        }
    }

    IplImage MatToIplImage(Mat m,int width, int heigth)
    {
        Bitmap bmp= Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(m, bmp);
        return BitmapToIplImage(bmp,width, heigth);
			
    }

	IplImage BitmapToIplImage(Bitmap bmp, int width, int height) {

		if ((width != -1) || (height != -1)) {
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, false);
			bmp = bmp2;
		}

		IplImage image = IplImage.create(bmp.getWidth(), bmp.getHeight(),
				IPL_DEPTH_8U, 4);

		bmp.copyPixelsToBuffer(image.getByteBuffer());
		
		IplImage grayImg = IplImage.create(image.width(), image.height(),
				IPL_DEPTH_8U, 1);

		cvCvtColor(image, grayImg, opencv_imgproc.CV_BGR2GRAY);

		return grayImg;
	}

	public void load() {
		train();
	}

	public int getProb() {
		return mProb;
	}


}
