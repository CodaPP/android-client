package com.ialex.foodsavr.presentation.screen.barcode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ialex.foodsavr.R;
import com.yanzhenjie.zbar.camera.CameraPreview;
import com.yanzhenjie.zbar.camera.ScanCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarcodeActivity extends AppCompatActivity {

    public static String EXTRA_BARCODE = "barcode";

    @BindView(R.id.capture_preview)
    CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        ButterKnife.bind(this);

        mPreview.setScanCallback(callback);
    }

    /**
     * Start camera.
     */
    private void startScan() {
        mPreview.start();
    }

    /**
     * Stop camera.
     */
    private void stopScan() {
        mPreview.stop();
    }

    /**
     * Result.
     */
    private ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(String content) {
            // Successfully.
            Intent intent = new Intent();
            intent.putExtra(EXTRA_BARCODE, content);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onPause() {
        // Must be called here, otherwise the camera should not be released properly.
        stopScan();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScan();
    }
}
