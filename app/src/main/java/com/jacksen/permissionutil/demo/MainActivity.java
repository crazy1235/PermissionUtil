package com.jacksen.permissionutil.demo;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jacksen.permissionutil.OnPermissionDenied;
import com.jacksen.permissionutil.OnPermissionGranted;
import com.jacksen.permissionutil.PermissionUtil;
import com.jacksen.permissionutil.ShowRationale;

import static com.jacksen.permissionutil.demo.R.id.make_call_btn;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_REQUEST_CALL_PHONE = 001;

    private LinearLayout layout;
    private Button makeCallBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.main_layout);
        makeCallBtn = (Button) findViewById(make_call_btn);

        makeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CODE_REQUEST_CALL_PHONE);
                    }
                }else{
                    makeCall();
                }*/

                PermissionUtil.requestPermissions(MainActivity.this, CODE_REQUEST_CALL_PHONE, true, Manifest.permission.CALL_PHONE);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*if (CODE_REQUEST_CALL_PHONE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);*/
        PermissionUtil.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }

    /**
     *
     */
    @OnPermissionGranted(CODE_REQUEST_CALL_PHONE)
    private void makeCall() {
        /*Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + "10086");
        intent.setData(uri);
        startActivity(intent);*/

        Log.d("MainActivity", "make a call success");
    }


    @OnPermissionDenied(CODE_REQUEST_CALL_PHONE)
    private void makeCallError() {
        Log.d("MainActivity", "make a call error.....");
    }

    @ShowRationale(CODE_REQUEST_CALL_PHONE)
    private void showRationale() {
        Snackbar.make(layout, "1111", Snackbar.LENGTH_LONG).setAction("click me", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.requestPermissions(MainActivity.this, CODE_REQUEST_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        }).show();
    }
}
