package com.akshay.medirx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.akshay.medirx.R;
import com.akshay.medirx.common.Constant;
import com.akshay.medirx.databinding.ActivityHomeBinding;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding mHomeBinding;
    private android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private android.webkit.ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private int REQUEST_CODE = 1234;
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.URL);

        mHomeBinding.progressBar.setVisibility(View.VISIBLE);
        mHomeBinding.webView.loadUrl(str);
        mHomeBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        mHomeBinding.webView.getSettings().setJavaScriptEnabled(true);
        mHomeBinding.webView.getSettings().setAllowContentAccess(true);
        mHomeBinding.webView.getSettings().setAllowFileAccess(true);
        mHomeBinding.webView.clearCache(true);
        mHomeBinding.webView.getSettings().setUseWideViewPort(true);
        mHomeBinding.webView.getSettings().setLoadWithOverviewMode(true);
        mHomeBinding.webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
      //  mHomeBinding.webView.getSettings().setBuiltInZoomControls(true);
        mHomeBinding.webView.requestFocus();
        mHomeBinding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mHomeBinding.webView.getSettings().setDomStorageEnabled(true);
        mHomeBinding.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mHomeBinding.webView.getSettings().setUseWideViewPort(true);
        mHomeBinding.webView.getSettings().setSavePassword(true);
        mHomeBinding.webView.getSettings().setSaveFormData(true);
        mHomeBinding.webView.getSettings().setEnableSmoothTransition(true);
        mHomeBinding.webView.getSettings().setUserAgentString(USER_AGENT);
        mHomeBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // redirect whats app and telegram
                if (url.startsWith("tel:") || url.startsWith("whatsapp:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;


            }


           // boolean bReceivedError = false;

          /*  @Override
            public void onReceivedError( WebView view, int errorCode,
                                         String description, String failingUrl) {
                bReceivedError = true;
                view.setVisibility( View.GONE );
                Toast.makeText(view.getContext(), "HTTP error 12"+errorCode, Toast.LENGTH_LONG).show();
            }*/

         /*   @Override
            public void onPageFinished(WebView view, String url) {
                if(!bReceivedError)
                    view.setVisibility( View.VISIBLE );
            }

            @Override
            public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                bReceivedError = true;
                Toast.makeText(view.getContext(), "HTTP error 11"+errorResponse.getStatusCode(), Toast.LENGTH_LONG).show();
            }*/

        });

        mHomeBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mHomeBinding.progressBar.setVisibility(View.INVISIBLE);
                    //  progressBar.setVisibility ( View.GONE ）; // after loading, the progress bar disappears
                } else {
                    mHomeBinding.progressBar.setVisibility(View.VISIBLE);
                    //  progressBar.setProgress (newprogress); // set progress value
                    //  progressBar.setVisibility ( View.VISIBLE ）; // a progress bar is displayed when the web page starts loading
                }
            }

            /**
             *8 (Android 2.2) < = API < = 10 (Android 2.3) callback this method
             */
            private void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg) {
                Log.e("wangj", "run method openfilechooser-1");
                //(2) when the method calls back, it indicates that the version API is < 21. In this case, assign the result to muploadcallbackbelow to make it! = null
                mUploadCallbackBelow = uploadMsg;
                takePhoto();
            }

            /**
             *11 (Android 3.0) < = API < = 15 (Android 4.0.3) callback this method
             */
            public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType) {
                //   Log.e ("wangj", "run method openfilechooser-2 (accepttype:" + accepttype + ")");
                //Here we don't distinguish the input parameters. We take pictures directly
                openFileChooser(uploadMsg);
            }

            /**
             *16 (Android 4.1.2) < = API < = 20 (Android 4.4w. 2) callback this method
             */
            public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                //  Log.e ("wangj", "openfilechooser-3 (accepttype:" + accepttype + "; capture:" + capture + ")");
                //Here we don't distinguish the input parameters. We take pictures directly
                openFileChooser(uploadMsg);
            }

            /**
             *API > = 21 (Android 5.0.1) calls back this method
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Log.e("wangj", "run method onshowfilechooser");
                //(1) when the method calls back, it indicates that the version API > = 21. In this case, assign the result to muploadcallbackabovel to make it! = null
                mUploadCallbackAboveL = valueCallback;
                takePhoto();
                return true;
            }
        });
    }


    /**
     * Call camera
     */
    private void takePhoto() {
        //Adjust the camera in a way that specifies the storage location for taking pictures
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//    startActivityForResult(intent, REQUEST_CODE);

        //If you select a picture (not including taking pictures by camera), you don't need to send a broadcast to refresh the gallery after success
//    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//    i.addCategory(Intent.CATEGORY_OPENABLE);
//    i.setType("image/*");
//    startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    // Onactivityresult callback:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //After the above two assignment operations (1) and (2), we can decide which processing method to adopt according to whether its value is empty
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
                Toast.makeText(this, "an error occurred.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Callback processing of Android API < 21 (Android 5.0)
     *
     * @ param resultcode select the return code of the file or photo
     * @ param data select file or photo return results
     */
    private void chooseBelow(int resultCode, Intent data) {
        Log.e("wangj", "return call method -- choosebelow");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                //This is for file path processing
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("wangj", "system return URI:" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                //Adjust the camera in the way of specified image storage path. If successful, the returned data is empty
                //Log.e("wangj", "custom results":+ imageUri.toString ());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Callback processing of Android API > = 21 (Android 5.0)
     *
     * @ param resultcode select the return code of the file or photo
     * @ param data select file or photo return results
     */
    private void chooseAbove(int resultCode, Intent data) {
        Log.e("wangj", "return call method -- chooseabove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                //Here is the processing of selecting pictures from a file
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("wangj", "system return URI:" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                //  Log.e ("wangj", "custom results":+ imageUri.toString ());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        //It doesn't matter if the broadcast is sent multiple times (that is, if the photos are selected successfully), it just wakes up the system to refresh the media files
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        if (mHomeBinding.webView.canGoBack()) {
            mHomeBinding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
