package net.maikenunes.mcontrol.utils;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;

import java.util.List;

/**
 * Created by maike on 18/06/15.
 */
public class ViewCamera extends JavaCameraView {

        private static final String TAG = "ViewCamera";

        public ViewCamera(Context context, AttributeSet attrs) {
            super(context, attrs);
            setCameraIndex(CameraBridgeViewBase.CAMERA_ID_ANY);
//            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
//                Camera.getCameraInfo(i, cameraInfo);
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    setCameraIndex(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT);
//                }else{
//
//                }
//            }

        }

        public List<String> getEffectList() {
            return mCamera.getParameters().getSupportedColorEffects();
        }

        public boolean isEffectSupported() {
            return (mCamera.getParameters().getColorEffect() != null);
        }

        public String getEffect() {
            return mCamera.getParameters().getColorEffect();
        }

        public void setEffect(String effect) {
            Camera.Parameters params = mCamera.getParameters();
            params.setColorEffect(effect);
            mCamera.setParameters(params);
        }

        public List<Camera.Size> getResolutionList() {
            return mCamera.getParameters().getSupportedPreviewSizes();
        }

        public void setResolution(Camera.Size resolution) {
            disconnectCamera();
            mMaxHeight = resolution.height;
            mMaxWidth = resolution.width;
            connectCamera(getWidth(), getHeight());
        }

        public void setResolution(int w,int h) {
            disconnectCamera();
            mMaxHeight = h;
            mMaxWidth = w;

            connectCamera(getWidth(), getHeight());
        }

        public void setAutofocus()
        {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            if (parameters.isVideoStabilizationSupported()) {
                parameters.setVideoStabilization(true);
            }
            mCamera.setParameters(parameters);

        }
        public void setCamFront()
        {
            disconnectCamera();
            setCameraIndex(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT);
            connectCamera(getWidth(), getHeight());
        }
        public void setCamBack()
        {
            disconnectCamera();
            setCameraIndex(org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK);
            connectCamera(getWidth(), getHeight());
        }

        public int numberCameras()
        {
            return	Camera.getNumberOfCameras();
        }

        public Camera.Size getResolution() {
            return mCamera.getParameters().getPreviewSize();
        }

    }
