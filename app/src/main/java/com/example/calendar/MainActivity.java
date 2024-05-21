package com.example.calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    TextView date;
    Button btnwork;
    EditText edtwork,edtH,edtM;
    ListView lv1;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    private static final String PREFS_NAME = "myPrefs";
    private static final String KEY_LIST = "myList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtH= findViewById(R.id.editH);
        edtM= findViewById(R.id.editM);
        edtwork= findViewById(R.id.editWork);
        btnwork= findViewById(R.id.button);
        lv1= findViewById(R.id.lv1);
        date= findViewById(R.id.txtdate);
        arrayList= new ArrayList<>();
        arrayList =loadData();
        arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);
        lv1.setAdapter(arrayAdapter);
        Date current= Calendar.getInstance().getTime();
        java.text.SimpleDateFormat simpleDateFormat= new java.text.SimpleDateFormat("dd/MM/yyyy");
        date.setText("Hôm nay: " +simpleDateFormat.format(current));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayList.remove(i);
                arrayAdapter.notifyDataSetChanged();
                saveData();
            }
        });
        btnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtwork.getText().toString().equals("") || edtH.getText().toString().equals("") || edtM.getText().toString().equals("")) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Info missing");
                    builder.setMessage("Enter all information");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            builder.show();
                        }
                    });

                }
                else{
                    String str =edtwork.getText().toString() + " - " +edtH.getText().toString() + " : " + edtM.getText().toString();
                    arrayList.add(str);
                    arrayAdapter.notifyDataSetChanged();
                    edtH.setText("");
                    edtH.setText("");
                    edtwork.setText("");
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveData(); // Lưu dữ liệu vào SharedPreferences khi ứng dụng bị tạm dừng
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray(arrayList);

        editor.putString(KEY_LIST, jsonArray.toString());
        editor.apply();
    }

    private ArrayList<String> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_LIST, null);
        ArrayList<String> list = new ArrayList<>();

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
