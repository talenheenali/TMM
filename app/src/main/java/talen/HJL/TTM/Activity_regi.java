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
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
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
 * Created by Heenali on 16/3/2019.
 */

public class Activity_regi extends AppCompatActivity {

    ImageView back_btn;
    KProgressHUD hud;
    EditText txt_email,txt_pass,txt_cofpass,txt_fname,txt_lname;
    Button btn_regi;
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_regi.this, R.color.appcolor2));
        }
        back_btn=(ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regi();
            }
        });
    }
    public void regi() {
        Log.d("mess", "regi");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        btn_regi.setEnabled(false);
        Success();
    }
    public void Success()
    {
        String password = txt_pass.getText().toString();
        String cofpass = txt_cofpass.getText().toString();
        if (password.equalsIgnoreCase(cofpass))
        {
            btn_regi.setEnabled(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btn_regi.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            if (cd.isConnectingToInternet())
            {
                reg_call();
            }
            else
            {

                mess("Check your internet connection.");
            }


        }
        else
        {

            mess("Please enter valid Confirm password & your password");
        }

    }
    public boolean validate() {
        boolean valid = true;

        String mobile_number = txt_email.getText().toString();
        String password = txt_pass.getText().toString();
        String cofpass = txt_cofpass.getText().toString();
        String fname = txt_fname.getText().toString();
        String lname = txt_lname.getText().toString();

        if (fname.isEmpty())
        {
            mess("Please enter valid Firstname");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }
        else if (lname.isEmpty())
        {
            mess("Please enter valid Lastname");
            // txt_email.setError("Please enter valid Email Id");
            valid = false;

        }
       else  if (mobile_number.isEmpty())
        {
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
      else  if (cofpass.isEmpty())
        {
            mess("Please enter valid Confirm password");
            //txt_pass.setError("Please enter valid password");
            valid = false;
        }


        return valid;
    }
    public void onLoginFailed()
    {
        //mess("Login failed");
        btn_regi.setEnabled(true);
    }
    public  void init()
    {
        cd = new ConnectionDetector(Activity_regi.this);
        txt_email=(EditText)findViewById(R.id.txt_email);
        txt_pass=(EditText)findViewById(R.id.txt_pass);
        txt_cofpass=(EditText)findViewById(R.id.txt_cofpass);
        btn_regi=(Button) findViewById(R.id.btn_regi);
        txt_lname=(EditText)findViewById(R.id.txt_lname);
        txt_fname=(EditText)findViewById(R.id.txt_fname);

    }
    public  void reg_call()
    {
        hud = KProgressHUD.create(Activity_regi.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setWindowColor(getResources().getColor(R.color.appcolor1))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RequestQueue queue = Volley.newRequestQueue(Activity_regi.this);

        String url = "http://templateapp.talenhosting.com/api/user/register";

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
                                String mess = object.getString("message");
                                mess(mess);
                               // finish();
                                txt_email.setText("");
                                txt_pass.setText("");
                                txt_cofpass.setText("");
                            }
                            else
                            {
                                mess("Wrong");
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
                params.put("firstname", txt_fname.getText().toString());
                params.put("lastname", txt_lname.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public  void mess(String mess)
    {
        Snackbar snackbar = Snackbar.make(btn_regi, mess, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.parseColor("#FA8072"));
        snackbar.show();
    }
}
