package talen.HJL.TTM;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import talen.HJL.TTM.R;

import org.json.JSONObject;

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
    KProgressHUD hud;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_Login.this, R.color.appcolor2));
        }
        sm = new SessionManager(Activity_Login.this);
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

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
               // login();
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
        //mess("Login failed");
        btn_login.setEnabled(true);
    }
    public  void mess(String mess)
    {
             Snackbar snackbar = Snackbar.make(btn_login, mess, Snackbar.LENGTH_LONG)
            .setAction("Action", null);
             View sbView = snackbar.getView();
             sbView.setBackgroundColor(Color.parseColor("#FA8072"));
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
            mess("Please enter valid Email Id");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }
       else if (password.isEmpty())
        {
            mess("Please enter valid password");
            //txt_pass.setError("Please enter valid password");
            valid = false;
        }



        return valid;
    }
    public  void login_call()
    {
        hud = KProgressHUD.create(Activity_Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);

        String url = "http://templateapp.talenhosting.com/api/user/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                       // _response.setText(response);
                        hud.dismiss();
                       try
                       {
                           JSONObject jobj = new JSONObject(response);
                           String status = jobj.getString("success");
                           if(status.equalsIgnoreCase("true"))
                           {
                               JSONObject object = jobj.getJSONObject("data");
                               String attr1 = object.getString("jwtid");
                               sm.createLoginSession(attr1);
                               sm.setUserId(attr1, attr1, attr1, attr1, attr1, attr1, attr1, attr1, attr1);

                               Intent i=new Intent(getApplicationContext(),MainActivity.class);
                               startActivity(i);
                               finish();
                           }
                           else
                           {
                               mess("Invalid Username or password");
                           }
                       }
                       catch (Exception e)
                       {
                           mess("Something Wrong");
                       }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //_response.setText("That didn't work!");
                hud.dismiss();
                mess("That didn't work!");

            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("email", txt_email.getText().toString());
                params.put("password", txt_pass.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
