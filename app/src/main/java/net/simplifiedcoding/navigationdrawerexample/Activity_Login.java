package net.simplifiedcoding.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heenali on 20/2/2019.
 */

public class Activity_Login extends AppCompatActivity {

    TextView txt_forgotpass,txt_regi;
    Button btn_login;
    EditText txt_pass,txt_email;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_Login.this, R.color.appcolor2));
        }
        cd = new ConnectionDetector(Activity_Login.this);
        txt_email=(EditText) findViewById(R.id.txt_email);
        txt_pass=(EditText) findViewById(R.id.txt_pass);
        txt_forgotpass=(TextView)findViewById(R.id.txt_forgotpass);
        txt_regi=(TextView)findViewById(R.id.txt_regi);
        btn_login=(Button) findViewById(R.id.btn_login);

        txt_forgotpass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent i=new Intent(getApplicationContext(),Activity_SendeMail.class);
               startActivity(i);
            }
        });

        txt_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(getApplicationContext(),Activity_regi.class);
                startActivity(i);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

               /* Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();*/
                login();
            }
        });


    }
    public void login() {
        Log.d("mess", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        onLoginSuccess();
    }
    public void onLoginFailed()
    {
        mess("Login failed");


        btn_login.setEnabled(true);
    }
    public  void mess(String mess)
    {
             Snackbar snackbar = Snackbar.make(btn_login, mess, Snackbar.LENGTH_LONG)
            .setAction("Action", null);
             View sbView = snackbar.getView();
             sbView.setBackgroundColor(Color.RED);
             snackbar.show();
    }
    public void onLoginSuccess()
    {
        btn_login.setEnabled(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_login.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (cd.isConnectingToInternet())
        {
            login_call();
        }
        else
        {

            mess("Check your internet connection.");
        }

    }
    public boolean validate() {
        boolean valid = true;

        String mobile_number = txt_email.getText().toString();
        String password = txt_pass.getText().toString();

        if (mobile_number.isEmpty()) {
            txt_email.setError("Please enter valid Email Id");
            valid = false;
        } else {
            txt_email.setError(null);
        }

        if (password.isEmpty()) {
            txt_pass.setError("Please enter valid password");
            valid = false;
        } else {
            txt_pass.setError(null);
        }

        return valid;
    }
    public  void login_call()
    {
        RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);

        String url = "http://httpbin.org/post";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                       // _response.setText(response);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //_response.setText("That didn't work!");
                Toast.makeText(getApplicationContext(),"That didn't work!",Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
