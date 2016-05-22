package echo.cn.socket;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText addr;
    private Button on, off;
    private Socket socket;
    private PrintStream output;
    private boolean isLinked = false;
    /* 服务器地址 */
    private final String HOST_IP = "192.168.191.4";

    /* 服务器端口 */
    private final int HOST_PORT = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addr = (EditText) findViewById(R.id.addr);
        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);

        on.setOnClickListener(this);
        off.setOnClickListener(this);

        off.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.on:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLinked){
                            socket();
                        }
                        if (isLinked){
                            turnON();
                        }
                    }
                }).start();
                if (isLinked){
                    off.setEnabled(true);
                    on.setEnabled(false);
                }
                break;
            case R.id.off:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        turnOFF();
                    }
                }).start();
                on.setEnabled(true);
                off.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void socket() {
        String ip = addr.getText().toString();
        Pattern pattern = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$");
        Matcher matcher = pattern.matcher(ip);
        if(matcher.matches()){
            try {
                socket = new Socket(ip, HOST_PORT);
                output = new PrintStream(socket.getOutputStream(), true, "utf-8");
                isLinked = true;
            } catch (Exception e) {
                System.out.println("host exception: " + e.toString());
            }
        }
    }

    private void turnOFF() {
        output.print("off");
    }

    private void turnON() {
        output.print("on");

    }
}
