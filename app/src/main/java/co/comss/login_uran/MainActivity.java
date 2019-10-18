package co.comss.login_uran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private String user, pass;
    private TextView noAccount;
    EditText username, password;
    private JsonRequest jrq;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usuario);
        password = findViewById(R.id.password);
        Button btnSesion = findViewById(R.id.enviar);
        noAccount = findViewById(R.id.btnNoTengoCuenta);
        rq = Volley.newRequestQueue(getApplicationContext());

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar_sesion();
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "¡oh!, aun no he desarrollado esta funcion, intenta mas tarde :D", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iniciar_sesion() {
        user = username.getText().toString();
        pass = password.getText().toString();
        String url = "https://auransql.000webhostapp.com/sesion.php?user=" + user + "&pwd=" + pass;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No Se encontró el usuario " +error.toString()+ user, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Se encontró el usuario " + user, Toast.LENGTH_SHORT).show();
        User usuario = new User();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try {
            jsonObject = jsonArray.getJSONObject(0);
            usuario.setUser(jsonObject.optString("user"));
            usuario.setPwd(jsonObject.optString("pwd"));
            usuario.setNames(jsonObject.optString("names"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intencion = new Intent(MainActivity.this, HomeActivity.class);
        intencion.putExtra("names", usuario.getNames());
        startActivity(intencion);
    }
}
