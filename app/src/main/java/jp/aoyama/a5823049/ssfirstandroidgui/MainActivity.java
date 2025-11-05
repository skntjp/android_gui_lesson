package jp.aoyama.a5823049.ssfirstandroidgui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    MediaPlayer mp;
    ListView listView;
    String f_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.layout_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate（膨らます）the menu. This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView btnTv = (TextView) findViewById(R.id.playing_view);

        /*switch(item.getItemId()) {
            case R.id.action_settings:
                tv.append("\nGo to action settings");
                return true;
                break;
            case R.id.action_edit:
                tv.append("\nGo to action edit");
                return true;
                break;
            case R.id.action_preview:
                tv.append("\nGo to action View");
                return true;
                break;
            case R.id.action_save:
                return true;
                break;
        }*/
        if(item.getItemId()==R.id.action_settings){
            btnTv.append("\nGo to action settings");
            return true;
        }
        if(item.getItemId()==R.id.action_edit){
            btnTv.setText("");
            FileInputStream fis = null;
            try {
                fis = openFileInput(f_name);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try {
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    btnTv.append(line + "\n");
                }

            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            }
            //btnTv.append("\nEdit content");
            return true;
        }
        if(item.getItemId()==R.id.action_open){
            btnTv.append("\nPreview result");
            setContentView(R.layout.open_layout);
            listView = findViewById(R.id.file_list);
            listView.setOnItemClickListener(this);
            String[] files = fileList();
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, files);
            listView.setAdapter(adapter);
            return true;
        }
        if(item.getItemId()==R.id.action_save){
            btnTv.append("\nSave content");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateTime = sdf.format(new Date());
            String fileName = "sssetactivity_content-" + currentDateTime + ".txt";
            TextView playingView = findViewById(R.id.playing_view);
            String fileContents = playingView.getText().toString();
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
            return true;
        }
        if(item.getItemId()==android.R.id.home) {
            setContentView(R.layout.layout_image);

//            btnTv.append("\nGo Back Home!");

        }
        return true;
    }
    public void myBtnClick(View v) {
        TextView btnTv = (TextView) findViewById(R.id.btn_tv_id);
        if (v.getId() == R.id.btn1) {
            btnTv.setText("I Press SS First Button");
        }
        else if (v.getId() == R.id.btn2) {
            btnTv.setText("I Press SS Second Button");
        }

    }
    public void playerBtnClick(View v) {
        TextView tv = (TextView) findViewById(R.id.playing_view);
        if (v.getId() == R.id.play_btn) {
            mp= new MediaPlayer();
            mp = MediaPlayer.create(this, R.raw.kalimba); // media_resouce_name には res フォルダに追加したメディアファイルの名前（拡張子なし）

            /*try {
                mp.setDataSource(this, Uri.parse("http://sc21vlweb01.cc.ag.aoyama.ac.jp/user/t23428/sleep_away.mp3"));
                mp.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/

            mp.start();




        }
        else if (v.getId() == R.id.stop_btn) {
            if (mp != null) {
                mp.stop();
            }
        }
        else if (v.getId() == R.id.pause_btn) {
            mp.pause();
        }
        else if (v.getId() == R.id.rec_btn) {
            tv.setText("Media duration is " + secTime(mp.getDuration()));
            tv.append("\n\nCurrent position is " + secTime(mp.getCurrentPosition()));
        }

    }
    public void changeView (View v) {
        if (v.getId() == R.id.set_btn) {
            Intent intent = new Intent(MainActivity.this, SetActivity.class);
            startActivity(intent);
            displayToast(getString(R.string.set_str));
        }
    }
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public void onItemClick(AdapterView adapterView, View view, int i, long l) {
        f_name = adapterView.getItemAtPosition(i).toString();
        setContentView(R.layout.layout_image);
        TextView btnTv = findViewById(R.id.playing_view);
        btnTv.setText("opened file " + f_name);

    }
    private String secTime(int time_stamp) {
        String sTime = "";
        int sec = time_stamp/1000;
        int min = sec / 60;
        sTime = sTime.concat(String.valueOf(min));
        sTime = sTime.concat(":"+String.valueOf(sec-min * 60));
        return sTime;
    }


}