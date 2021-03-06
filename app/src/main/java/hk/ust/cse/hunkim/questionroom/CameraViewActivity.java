package hk.ust.cse.hunkim.questionroom;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import hk.ust.cse.hunkim.questionroom.barcode.BarcodeScanner;
import hk.ust.cse.hunkim.questionroom.barcode.CameraView;

public class CameraViewActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");

        if (action != null && action.equals("takePicture")){
            setContentView(R.layout.activity_camera_view);
            dispatchTakePictureIntent(null);
            return;
        }

        final CameraView cameraView = new CameraView(this, null);

        final BarcodeScanner scanner = new BarcodeScanner(this);
        scanner.callback = new BarcodeScanner.TrackingCallback() {
            @Override
            public void track() {
                scanner.setdata(cameraView.getFrame(),
                        cameraView.width(),
                        cameraView.height()
                );
            }
        };
        setContentView(cameraView);
        scanner.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.cameraImageView);
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
