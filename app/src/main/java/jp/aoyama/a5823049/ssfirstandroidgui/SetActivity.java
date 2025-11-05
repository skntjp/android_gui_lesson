package jp.aoyama.a5823049.ssfirstandroidgui;

import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String spinnerLabel;


    public static class SavedBundle {
        private static Bundle savedBundle;
        public static void setSavedBundle(Bundle bundle) {
            savedBundle = bundle;
        }
        public static Bundle getSavedBundle() {
            return savedBundle;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Tag", "hello");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner spinner = (Spinner) findViewById(R.id.phone_category);
        spinner.setOnItemSelectedListener(this);


        EditText editName = findViewById(R.id.name);
        EditText studentNum = findViewById(R.id.student_num);
        EditText eMail = findViewById(R.id.email);
        EditText phoneNum = findViewById(R.id.phone_num);
        Spinner phone_category = findViewById(R.id.phone_category);
        RadioGroup mobility = findViewById(R.id.mobility);
        Bundle savedBundle = SavedBundle.getSavedBundle();
        if (savedBundle != null) {
            Log.v("tag", "set bundle is not null.");
            editName.setText(savedBundle.getString("name"));
            studentNum.setText(savedBundle.getString("student_num"));
            eMail.setText(savedBundle.getString("email"));
            phoneNum.setText(savedBundle.getString("phone_num"));
            phone_category.setSelection(savedBundle.getInt("phone_category"));
            mobility.check(savedBundle.getInt("mobility"));
        }
    }
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar_setting, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    @Override
    public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
        spinnerLabel = adapterView.getItemAtPosition(i).toString();
        displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView adapterView) { }
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void onRadioButtonClicked(View view) {
        RadioGroup rg = (RadioGroup)findViewById(R.id.mobility);
        int id = rg.getCheckedRadioButtonId();
        RadioButton checkedRadioBtn = (RadioButton) findViewById(id);

    }
    public void onSaveButtonClicked(View v) {
        Bundle set_bundle = new Bundle();
        EditText editName = findViewById(R.id.name);
        EditText studentNum = findViewById(R.id.student_num);
        EditText eMail = findViewById(R.id.email);
        EditText phoneNum = findViewById(R.id.phone_num);
        Spinner phone_category = findViewById(R.id.phone_category);
        RadioGroup mobility = findViewById(R.id.mobility);
        set_bundle.putString("name", editName.getText().toString());
        set_bundle.putString("student_num", studentNum.getText().toString());
        set_bundle.putString("email", eMail.getText().toString());
        set_bundle.putString("phone_num", phoneNum.getText().toString());
        set_bundle.putInt("phone_category", phone_category.getSelectedItemPosition());
        set_bundle.putInt("mobility", mobility.getCheckedRadioButtonId());
        Log.v("tag", set_bundle.getString("name"));
        SavedBundle.setSavedBundle(set_bundle);

        saveBundleToFile(set_bundle);
    }
    private void saveBundleToFile(Bundle bundle) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        String fileName = "sssetactivity_pref-" + currentDateTime + ".txt";
        String fileContents = bundle.toString();

        FileOutputStream fos = null; //
        try {
            // 内部ストレージにファイルを書き込む
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            displayToast("設定データを " + fileName + " に保存しました。");
            Log.v("SetActivity", "Saved to: " + getFilesDir() + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            displayToast("データの保存に失敗しました。");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}