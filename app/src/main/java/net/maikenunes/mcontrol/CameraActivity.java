package net.maikenunes.mcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.utils.PersonRecognizer;
import net.maikenunes.mcontrol.utils.ViewCamera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "CameraActivity";
    private Mat mRgba;
    private Mat mGray;
    private ViewCamera mOpenCvCameraView;
    private String cameraSelecionada = "FRONTAL";
    CascadeClassifier mJavaDetector;
    Bitmap mBitmap;
    Bitmap mBitmapFace;
    List<Pessoa> pessoas;
    List<File> imgsReco;
    PersonRecognizer personRecon;
    ImageView imgPesFace;
    Handler mHandler;
    static String mCurrentPhotoPath;       //string com caminho da imagem salva no filesystem do device
    static String rPhotoPath;
    static String rPhotoFacePath;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV carregado");

                    InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    if(!cascadeDir.isDirectory()){
                        cascadeDir.mkdirs();
                    }
                    File mCascadeFile = new File(cascadeDir, "lbpcascade.xml");
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(mCascadeFile);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();
                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //mOpenCvCameraView.setCamFront();
                    mOpenCvCameraView.enableView();

                    if(imgsReco != null && imgsReco.size() > 0) {
                        personRecon = new PersonRecognizer(imgsReco);
                        personRecon.load();
                    }

                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        imgPesFace = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        String titleIntent = intent.getStringExtra("TITLE");
        if(!titleIntent.toString().equals("")){
            setTitle(titleIntent);
        }else{
            setTitle("Camera");
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                imgPesFace.setImageBitmap(mBitmapFace);
            }
        };



        // utilizado para identificar em realtime a pessoa
        int evenIdIntent = intent.getIntExtra("evento_id", 0);
        int encIdIntent = intent.getIntExtra("encontro_id", 0);
        if(evenIdIntent != 0 && encIdIntent != 0){
            try {
                EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);
                Encontro enc = new Encontro();
                enc.setId(encIdIntent);
                enc.setEvenId(evenIdIntent);
                pessoas = evenPesDao.getByEventNotPresenc(enc);

                File tempPes = null;
                imgsReco = new ArrayList<>();
                for (int x = 0; x < pessoas.size();x++){
                    if(pessoas.get(x).getFotoFacial() != null){
                        tempPes = new File(pessoas.get(x).getFotoFacial());
                        if(tempPes.isFile()){
                            imgsReco.add(tempPes);
                        }
                    }
                }
            }catch (Exception exc){

            }

        }

        mOpenCvCameraView = (ViewCamera) findViewById(R.id.java_camera_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }



    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }


    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        int faceSize = 0;
        if (Math.round(mGray.rows() * faceSize) > 0) {
            faceSize = Math.round(mGray.rows() * faceSize);
        }

        MatOfRect faces = new MatOfRect();
        mJavaDetector.detectMultiScale(mGray, faces, 1.1, 3, 2, new Size(faceSize, faceSize), new Size());
        Rect[] facesArray = faces.toArray();
        if(facesArray.length > 0){

            Mat m = mGray.submat(facesArray[0]);
            mBitmapFace = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, mBitmapFace);

            mBitmap = Bitmap.createBitmap(mRgba.width(), mRgba.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgba,mBitmap);

            if(mBitmap != null) {
                Message msg = new Message();
                mHandler.sendMessage(msg);
            }
            Core.rectangle(mRgba, facesArray[0].tl(), facesArray[0].br(), new Scalar(0, 255, 0, 255), 3);

            if(imgsReco != null && imgsReco.size() > 0) {
                String textItem = personRecon.predict(m);
                int rankingReco = personRecon.getProb();
                try {
                    int posItem = Integer.parseInt(textItem);
                    if (posItem != -1 && rankingReco < 80) {
                        finishCamera("ok");
                    }
                } catch (Exception e) {

                }
            }

        }

        return mRgba;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_camera:
                if(mOpenCvCameraView != null){
                    if(cameraSelecionada.equals("TRASEIRA")){
                        mOpenCvCameraView.setCamFront();
                        cameraSelecionada = "FRONTAL";
                    } else {
                        mOpenCvCameraView.setCamBack();
                        cameraSelecionada="TRASEIRA";
                    }
                }
                break;

            case R.id.action_close:
                finishCamera("close");
                break;

            case R.id.action_final:
                finishCamera("ok");
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // utilizado para remover ação de voltar pelo botão
    }

    public void finishCamera(String finis) {
        Intent data = new Intent();
        if(mBitmap != null && finis.equals("ok")) {

            File photoFile = null;
            File photoFileFace = null;
            try {
                photoFile     = createImageFile("tempCamera");
                photoFileFace = createImageFile("tempCameraFace");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                FileOutputStream fos = new FileOutputStream(photoFile);
                fos.write(bitmapdata);
                fos.flush();

                bos = new ByteArrayOutputStream();
                mBitmapFace.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                bitmapdata = bos.toByteArray();
                fos = new FileOutputStream(photoFileFace);
                fos.write(bitmapdata);
                fos.flush();

            } catch (IOException ex) {
                Log.e("IMG", "Erro ao gravar arquivo da foto", ex);
            }

            data.putExtra("picture", photoFile.getAbsolutePath());
            data.putExtra("pictureFacial", photoFileFace.getAbsolutePath());
            setResult(Activity.RESULT_OK, data);
        }else{
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    private File createImageFile(String suffixImg) throws IOException {

        // Create an image file name
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "mControl/temp");
        storageDir.mkdirs();
        File image = File.createTempFile(
                "temp"+suffixImg,  //new SimpleDateFormat("HHmmss").format(new Date()),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        if(suffixImg.equals("")){
            rPhotoPath = image.getAbsolutePath();
        }else{
            rPhotoFacePath = image.getAbsolutePath();
        }

        return image;
    }

}
