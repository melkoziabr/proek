package com.example.www;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Thread secThread;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.image_view);
        String url="https://i.pinimg.com/564x/9b/98/aa/9b98aac753fe54592a7968d075166c6c.jpg";
        Picasso.get().load(url).into(imageView);
        init();
    }
    private void init(){
        runnable= new Runnable() {
            @Override
            public void run() {
                TextView result = (TextView) findViewById(R.id.txtone);
                try {
                    String conclusion = getContent("https://jsonplaceholder.typicode.com/posts/1");
                    Gson g= new Gson();
                    Jsonplaceholder jsonplaceholder=g.fromJson(conclusion,Jsonplaceholder.class);
                    result.setText(conclusion+"\n"+jsonplaceholder.title()+"\n"+jsonplaceholder.body());
                    DonwloadImage();
                } catch (IOException e) {
                    result.setText(e.getMessage());
                }
            }
        };
        secThread=new Thread(runnable);
        secThread.start();
    }
    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    private void DonwloadImage(){
        TextView imgtxt = (TextView) findViewById(R.id.imageText);
        URL url = null;
        try {
            url = new URL("https://i.pinimg.com/564x/9b/98/aa/9b98aac753fe54592a7968d075166c6c.jpg");
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
        } catch (MalformedURLException e) {
            imgtxt.setText("Не удалось загрузить изображение");
        } catch (IOException e) {
            imgtxt.setText("Не удалось загрузить изображение");
        }
    }
    private void Down(){

    }
}
class Jsonplaceholder{
    private int userId;
    private int id;
    private String title;
    private String body;
    public Jsonplaceholder(){

    }
    public int userId(){
        return this.userId;
    }
    public int id(){
        return this.id;
    }
    public String title(){
        return this.title;
    }
    public String body(){
        return this.body;
    }
}