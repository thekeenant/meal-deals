package com.keenant.mealdeal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.keenant.mealdeal.R;
import com.keenant.mealdeal.cove.Cove;
import com.keenant.mealdeal.cove.Deal;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Iconify.with(new FontAwesomeModule());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final MaterialEditText username = (MaterialEditText) findViewById(R.id.username_field);
        username.setNextFocusDownId(R.id.password_field);

        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password_field);
        password.setNextFocusDownId(R.id.login_button);
        password.setNextFocusUpId(R.id.username_field);

        ButtonRectangle login = (ButtonRectangle) findViewById(R.id.login_button);
        login.setNextFocusUpId(R.id.password_field);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticateTask().execute(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private class AuthenticateTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strs) {
            String username = strs[0];
            String password = strs[1];
            try {
                boolean success = Cove.getInstance(LoginActivity.this).authenticate(username, password);
                if (success) {
                    Intent intent = new Intent(LoginActivity.this, MallListActivity.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            fail().show();
                        }
                    });
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private AlertDialog fail() {
        AlertDialog.Builder codeDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_fail, null);
        TextView text = (TextView) view.findViewById(R.id.fail_msg);
        text.setText("Incorrect Login");
        codeDialog.setView(view);

        return codeDialog.create();
    }
}
