package com.example.amyparker.calculator;

import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

TextToSpeech tts;

    //Add = 1, Subtract = 2, Multiply = 3, Division = 4
    int operation = -1;

    // Instantiate the cache
    Cache cache ; // 1MB cap
    // Set up the network to use HttpURLConnection as the HTTP client.
    Network network ;
    // Instantiate the RequestQueue with the cache and network.
    RequestQueue queue ;
    String url = "http://numbersapi.com/";

    private void initQueue(){
        // Instantiate the cache
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        queue = new RequestQueue(cache, network);
        queue.start();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.UK);
                Log.d("LOG", "Text to speech initialized status " +i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initQueue();
        setContentView(R.layout.activity_main);

        final View add = findViewById(R.id.add);
        final View multiply = findViewById(R.id.multiply);
        final View subtract = findViewById(R.id.subtract);
        final View divide = findViewById(R.id.divide);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (operation == 1) {
                    operation = -1;
                    add.setBackgroundColor(0);
                } else {
                    operation = 1;
                    add.setBackgroundColor(Color.DKGRAY);
                    subtract.setBackgroundColor(0);
                    divide.setBackgroundColor(0);
                    multiply.setBackgroundColor(0);
                }
            }

        });


        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (operation == 2) {
                    operation = -1;
                    subtract.setBackgroundColor(0);
                } else {
                    operation = 2;
                    subtract.setBackgroundColor(Color.DKGRAY);
                    add.setBackgroundColor(0);
                    divide.setBackgroundColor(0);
                    multiply.setBackgroundColor(0);


                }
            }
        });
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (operation == 3) {
                    operation = -1;
                    multiply.setBackgroundColor(0);
                } else {
                    operation = 3;
                    multiply.setBackgroundColor(Color.DKGRAY);
                    subtract.setBackgroundColor(0);
                    divide.setBackgroundColor(0);
                    add.setBackgroundColor(0);


                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (operation == 4) {
                    operation = -1;
                    divide.setBackgroundColor(0);
                } else {
                    operation = 4;
                    divide.setBackgroundColor(Color.DKGRAY);
                    subtract.setBackgroundColor(0);
                    add.setBackgroundColor(0);
                    multiply.setBackgroundColor(0);


                }
            }
        });

        TextView result = (TextView) findViewById(R.id.result);

        final EditText left = (EditText) findViewById(R.id.first_number);
        final EditText right = (EditText) findViewById(R.id.second_number);


        final View calculate = findViewById(R.id.calculate);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (left.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "YOU GOTTA PUT A NUMBER ON THE LEFT!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (right.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "YOU GOTTA PUT A NUMBER ON THE RIGHT!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (operation == -1) {
                    Toast.makeText(MainActivity.this, "WHAT ARE YOU TRYNA DO??", Toast.LENGTH_SHORT).show();
                    return;
                }
                calculate();
            }
        });

        final View clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left.setText(null);
                right.setText(null);
                TextView result = (TextView) findViewById(R.id.result);
                result.setText(null);
                operation = -1;
                multiply.setBackgroundColor(0);
                subtract.setBackgroundColor(0);
                divide.setBackgroundColor(0);
                add.setBackgroundColor(0);
            }

        });


    }

    public void calculate() {
        TextView result = (TextView) findViewById(R.id.result);

        final EditText left = (EditText) findViewById(R.id.first_number);
        EditText right = (EditText) findViewById(R.id.second_number);

        float val = 0;
        int leftValue = Integer.parseInt(left.getText().toString());
        int rightValue = Integer.parseInt(right.getText().toString());

        if (operation == 1) {
            val = add(leftValue, rightValue);
        } else if (operation == 2) {
            val = subtract(leftValue, rightValue);
        } else if (operation == 3) {
            val = multiply(leftValue, rightValue);
        } else if (operation == 4) {
            val = divide(leftValue, rightValue);
        }


        result.setText("Result is : " + val);

        getFactForNumber(val);
    }

    public int add(int a, int b) {return a + b;}

    public float divide(float a, float b) {
        return a / b;
    }

    public int multiply(int a, int b) {return a * b;}

    public int subtract(int a, int b) {
        return a - b;
    }

    public void getFactForNumber(float number) {

        int numberValue = (int) number;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + numberValue,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "OH that reminds me! " + response, Toast.LENGTH_LONG).show();
                        tts. speak(response, TextToSpeech.QUEUE_FLUSH, null);
                        // Do something with the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
