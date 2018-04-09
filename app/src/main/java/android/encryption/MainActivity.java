package android.encryption;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Random;
import android.os.Handler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);


    }
    public void onButtonClick100(View view)
    {

        setContentView(R.layout.activity_main);
    }

    public void onButtonClick1(View view) {
        setContentView(R.layout.caesar);
    }

    public void onButtonClick2(View view) {
        setContentView(R.layout.vigenere);
    }

    public void onButtonClick3(View view) {
        setContentView(R.layout.des);
    }

    public void onButtonClick4(View view) {
        setContentView(R.layout.aes);
    }

    public void onButtonClick27(View view) {
        setContentView(R.layout.vigenered);
    }

    public void onButtonClick15(View view) {
        setContentView(R.layout.all);
    }

    public void onButtonClick19(View view) {
        setContentView(R.layout.decryption);
    }

    public void onButtonClick21(View view) {
        setContentView(R.layout.caesard);
    }

    public void onButtonClick24(View view) {
        setContentView(R.layout.aesd);
    }


    private boolean doubleBackToExitPressedOnce = false;
    private boolean doubleBackToExitPressedTwice = false;

    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    public void onBackPressed() {
        setContentView(R.layout.first);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    int key;

    public void onButtonClick5(View view) throws IOException {
        final StringBuffer plain = new StringBuffer();
        char c;
        EditText e1 = (EditText) findViewById(R.id.editText);
        TextView t1 = (TextView) findViewById(R.id.textView4);
        EditText e2 = (EditText) findViewById(R.id.editText2);
        TextView t2 = (TextView) findViewById(R.id.textView3);
        plain.append(e1.getText());

        Button btn_share = (Button) findViewById(R.id.shareit);
        btn_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Cipher text is : " + plain);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        Button button = (Button) findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Key for the ciphered text is : \"" + key + "\" And mode of encryption is \"Caeser Cipher\". It is recommended to write this key at a safe place and delete this message");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });


        try {
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
            Boolean check = checkBox.isChecked();
            if (check) {
                key = Integer.parseInt(RandomStringUtils.randomNumeric(3));
                e2.setText(String.valueOf(key));
            } else {
                key = Integer.parseInt(e2.getText().toString());
            }
            for (int i = 0; i < plain.length(); i++) {
                c = plain.charAt(i);
                int k = (int) c;
                if (k >= 65 && k < 91) {
                    int l = (k + key - 65) % 26 + 65;
                    char p = (char) l;
                    plain.setCharAt(i, p);
                } else if (k >= 97 && k <= 122) {
                    int l = (k + key - 97) % 26 + 97;
                    char p = (char) l;
                    plain.setCharAt(i, p);
                } else {
                    char p = (char) k;
                    plain.setCharAt(i, p);
                }

            }
            t1.setText(plain.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonClick6(View v) {


        EditText e3 = (EditText) findViewById(R.id.editText3);
        EditText e4 = (EditText) findViewById(R.id.editText4);
        final StringBuffer stringBuffer = new StringBuffer();
        final StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer.append(e3.getText());
        TextView t5 = (TextView) findViewById(R.id.textView5);
        int g;
        Button button13 = (Button) findViewById(R.id.button13);
        button13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Key for the ciphered text is : \"" + stringBuffer1 + "\" And mode of encryption is \"Vigenere Cipher\". It is recommended to write this key at a safe place and delete this message");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        Button button14 = (Button) findViewById(R.id.button14);
        button14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, " Ciphered text is : \"" + stringBuffer + "\" And mode of encryption is \"Vigenere Cipher\". You can decrypt the message using the key provided");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });


        try {
            CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
            Boolean check = checkBox2.isChecked();
            if (check) {
                stringBuffer1.append(RandomStringUtils.randomAlphabetic(10));
                e4.setText(String.valueOf(stringBuffer1));
                g = stringBuffer1.length();
            } else {
                stringBuffer1.append(e4.getText());
                g = stringBuffer1.length();
            }

            for (int i = 0; i < stringBuffer.length(); i++) {
                char t = stringBuffer.charAt(i);
                int p = (int) t;
                int j = i % g;
                char t1 = stringBuffer1.charAt(j);
                int p1 = (int) t1;
                if (p >= 65 && p <= 90) {
                    int l = (p - 65 + p1 - 65) % 26 + 65;
                    char f = (char) l;
                    stringBuffer.setCharAt(i, f);
                } else if (p >= 97 && p <= 122) {
                    int l = (p - 97 + p1 - 97) % 26 + 97;
                    char f = (char) l;
                    stringBuffer.setCharAt(i, f);
                } else {
                    char f = (char) p;
                    stringBuffer.setCharAt(i, f);
                }
            }

            t5.setText(String.valueOf(stringBuffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonClick7(View view) {
        try {
            EditText e9 = (EditText) findViewById(R.id.editText5);
            TextView t6 = (TextView) findViewById(R.id.textView6);
            EditText e10 = (EditText) findViewById(R.id.editText10);
            final String stringke;
            String stringb = e9.getText().toString();
            Cipher desCipher;
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox4);
            Button button11 = (Button) findViewById(R.id.button11);
            final String stringkey1;

            Button button12 = (Button) findViewById(R.id.button12);

            Boolean check = checkBox.isChecked();
            if (check) {
                KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
                SecretKey myDesKey = keygenerator.generateKey();
                desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                text = stringb.getBytes();
                e10.setText(myDesKey.toString().substring(32));
                stringke = myDesKey.toString().substring(32);
                stringkey1 = Base64.encodeToString(myDesKey.getEncoded(), Base64.DEFAULT);


                try {

                    FileOutputStream fileout = openFileOutput("abcdef.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(stringke);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);


            } else {
                String stringk = e10.getText().toString();
                byte[] key = stringk.getBytes("UTF-8");
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key, 8);
                SecretKeySpec myDesKey = new SecretKeySpec(key, "DES");
                stringke = stringk;
                stringkey1 = Base64.encodeToString(myDesKey.getEncoded(), Base64.DEFAULT);
                try {

                    FileOutputStream fileout = openFileOutput("abcdef.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(stringke);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            }



            text = stringb.getBytes();
            byte[] textEncrypted = desCipher.doFinal(text);
            String plainte = "";
            plainte = Base64.encodeToString(textEncrypted, 0);
            final String finalPlainte = plainte;
            t6.setText(plainte);

            try {

                FileOutputStream fileout1 = openFileOutput("abcde.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout1);
                outputWriter.write(stringkey1);
                outputWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            button12.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Ciphered text is : \"" + finalPlainte + "\" And mode of encryption is \"DES Encryption\". It is recommended to write this key at a safe place and delete this message");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
            button11.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Key for the Ciphered text is : \"" + stringke + "\" And mode of encryption is \"DES Encryption\". You can decrypt this message using the key provided");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    byte[] text;

    public void onButtonClick8(View view) {

        try {
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox3);
            Boolean check = checkBox.isChecked();
            final String plaintee;
            final String keye;
            final String stringkey1;

            EditText e9 = (EditText) findViewById(R.id.editText6);
            TextView t6 = (TextView) findViewById(R.id.textView8);
            EditText e7 = (EditText) findViewById(R.id.editText7);
            TextView t9 = (TextView) findViewById(R.id.textView9);
            SecretKey myDesKey;
            String stringk;

            String stringb = e9.getText().toString();
            final int AES_KEYLENGTH = 128;
            byte[] iv = new byte[AES_KEYLENGTH / 8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            Cipher desCipher;
            if (check) {
                KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
                myDesKey = keygenerator.generateKey();
                desCipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                text = stringb.getBytes();
                e7.setText(myDesKey.toString().substring(32));
                keye = myDesKey.toString().substring(32);

                byte[] textEncrypted = desCipher.doFinal(text);
                String plainte = "";
                plainte = Base64.encodeToString(textEncrypted, 0);
                t6.setText(plainte);
                plaintee = plainte;
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                String cipherte = "";
                cipherte = String.valueOf(Hex.encodeHex(textDecrypted));
                byte[] bytes = Hex.decodeHex(cipherte.toCharArray());
                String plaintext = new String(bytes, "UTF-8");

                stringkey1 = Base64.encodeToString(myDesKey.getEncoded(), Base64.DEFAULT);

                try {

                    FileOutputStream fileout = openFileOutput("abcd.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(keye);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    FileOutputStream fileout = openFileOutput("abc.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(stringkey1);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                stringk = e7.getText().toString();
                keye = stringk;
                byte[] key = stringk.getBytes("UTF-8");
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                myDesKey = new SecretKeySpec(key, "AES");


                desCipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                text = stringb.getBytes();
                byte[] textEncrypted = desCipher.doFinal(text);
                String plainte = "";
                plainte = Base64.encodeToString(textEncrypted, 0);
                t6.setText(plainte);
                plaintee = plainte;
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                String cipherte = "";
                cipherte = String.valueOf(Hex.encodeHex(textDecrypted));
                byte[] bytes = Hex.decodeHex(cipherte.toCharArray());
                String plaintext = new String(bytes, "UTF-8");

                stringkey1 = Base64.encodeToString(myDesKey.getEncoded(), Base64.DEFAULT);
                try {

                    FileOutputStream fileout = openFileOutput("abcd.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(stringk);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    FileOutputStream fileout = openFileOutput("abc.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                    outputWriter.write(stringkey1);
                    outputWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            Button button15 = (Button) findViewById(R.id.button15);
            button15.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Key for the ciphered text is : \"" + keye + "\" And mode of encryption is \"AES Encryption\". It is recommended to write this key at a safe place and delete this message");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
            Button button16 = (Button) findViewById(R.id.button16);
            button16.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, " Ciphered text is : \"" + plaintee + "\" And mode of encryption is \"AES Encryption\". You can decrypt the message using the key provided");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double timeaes, timedes, timec, timev, lengthc, lengthv, lengthaes, lengthdes;

    public void onButtonClick10(View view) {

        EditText e9 = (EditText) findViewById(R.id.editText9);
        EditText e8 = (EditText) findViewById(R.id.editText8);
        TextView t16 = (TextView) findViewById(R.id.textView16);
        TextView t17 = (TextView) findViewById(R.id.textView17);
        TextView t18 = (TextView) findViewById(R.id.textView18);
        TextView t19 = (TextView) findViewById(R.id.textView19);
        StringBuffer plaintext = new StringBuffer();
        String keyt = null;
        plaintext.append(e8.getText());
        keyt = e9.getText().toString();
        try {
            long start = System.nanoTime();
            final int AES_KEYLENGTH = 128;
            byte[] iv = new byte[AES_KEYLENGTH / 8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            Cipher desCipher;
            byte[] key = keyt.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec myDesKey = new SecretKeySpec(key, "AES");
            desCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            String plaintext2 = plaintext.toString();
            text = plaintext2.getBytes();
            byte[] textEncrypted = desCipher.doFinal(text);
            String plainte = "";
            plainte = String.valueOf(Hex.encodeHex(textEncrypted));
            t19.setText(plainte);
            lengthaes = plainte.length();
            long end = System.nanoTime();
            timeaes = (end - start);


            long start1 = System.nanoTime();
            byte[] key1 = keyt.getBytes("UTF-8");
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            key1 = sha1.digest(key1);
            key1 = Arrays.copyOf(key1, 8);
            SecretKeySpec myDesKey1 = new SecretKeySpec(key1, "DES");

            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey1);
            text = plaintext.toString().getBytes();
            byte[] textEncrypted1 = desCipher.doFinal(text);
            String plaintex = "";
            plaintex = String.valueOf(Hex.encodeHex(textEncrypted1));
            t18.setText(plaintex);
            lengthdes = plaintex.length();
            long end1 = System.nanoTime();
            timedes = (end1 - start1);


            long start2 = System.nanoTime();
            int g = keyt.length();
            StringBuffer plaintext1 = plaintext;
            for (int i = 0; i < plaintext1.length(); i++) {
                char t = plaintext1.charAt(i);
                int p = (int) t;
                int j = i % g;
                char t1 = keyt.charAt(j);
                int p1 = (int) t1;
                if (p >= 65 && p <= 90) {
                    int l = (p - 65 + p1 - 65) % 26 + 65;
                    char f = (char) l;
                    plaintext1.setCharAt(i, f);
                } else if (p >= 97 && p <= 122) {
                    int l = (p - 97 + p1 - 97) % 26 + 97;
                    char f = (char) l;
                    plaintext1.setCharAt(i, f);
                } else {
                    char f = (char) p;
                    plaintext1.setCharAt(i, f);
                }
            }

            t17.setText(String.valueOf(plaintext1));
            lengthv = plaintext1.length();
            long end2 = System.nanoTime();
            timev = end2 - start2;


            long start3 = System.nanoTime();
            StringBuffer plain1 = plaintext;

            int keyc = Integer.parseInt(keyt);
            for (int i = 0; i < plaintext.length(); i++) {
                char c = plain1.charAt(i);
                int k = (int) c;
                if (k >= 65 && k < 91) {
                    int l = (k + keyc - 65) % 26 + 65;
                    char p = (char) l;
                    plain1.setCharAt(i, p);
                } else if (k >= 97 && k <= 122) {
                    int l = (k + keyc - 97) % 26 + 97;
                    char p = (char) l;
                    plain1.setCharAt(i, p);
                } else {
                    char p = (char) k;
                    plain1.setCharAt(i, p);
                }

            }
            t16.setText(plain1.toString());
            lengthc = plain1.length();
            long end3 = System.nanoTime();
            timec = end3 - start3;

            Button button16 = (Button) findViewById(R.id.button18);
            button16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    setContentView(R.layout.analyze);
                    GraphView graph = (GraphView) findViewById(R.id.graph);
                    BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                            new DataPoint(0, timec),
                            new DataPoint(1, timev),
                            new DataPoint(2, timedes),
                            new DataPoint(3, timeaes)
                    });

                    LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                            new DataPoint(0, timec),
                            new DataPoint(1, timev),
                            new DataPoint(2, timedes),
                            new DataPoint(3, timeaes)
                    });

                    series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                        @Override
                        public int get(DataPoint data) {
                            return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                        }
                    });
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(5);
                    series.setSpacing(75);
                    graph.getViewport().setScrollable(true);

                    graph.getViewport().setScalable(true);
                    series.setDrawValuesOnTop(true);
                    series.setTitle("Y-axis : Time taken in nano sec");
                    series1.setTitle("X-axis: caesar,vigenere,des,aes");

                    graph.getLegendRenderer().setVisible(true);
                    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


                    graph.addSeries(series);
                    graph.addSeries(series1);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onButtonClick20(View view) {


        EditText e12 = (EditText) findViewById(R.id.editText11);
        EditText e13 = (EditText) findViewById(R.id.editText13);
        TextView t23 = (TextView) findViewById(R.id.textView23);
        StringBuffer plain = new StringBuffer();
        int key;
        key = Integer.parseInt(e13.getText().toString());
        char c;
        plain.append(e12.getText());
        int pl = key;


        for (int i = 0; i < plain.length(); i++) {
            c = plain.charAt(i);
            int k = (int) c;
            if (k >= 65 && k < 91) {
                int kl = (k - 64 - pl) % 26;
                if (kl < 0)
                    kl = kl + 26 + 64;
                else
                    kl = kl + 64;
                char p = (char) kl;
                plain.setCharAt(i, p);
            } else if (k >= 97 && k <= 122) {
                int k1 = (k - 97 - pl) % 26;
                if (k1 < 0)
                    k1 = k1 + 26 + 97;
                else
                    k1 = k1 + 97;
                char p = (char) k1;
                plain.setCharAt(i, p);
            } else {
                char p = (char) k;
                plain.setCharAt(i, p);
            }

        }
        t23.setText(plain.toString());
    }

    public void onButtonClick22(View view) throws Exception {
        try {
            EditText e12 = (EditText) findViewById(R.id.editText12);
            EditText e14 = (EditText) findViewById(R.id.editText14);
            TextView t26 = (TextView) findViewById(R.id.textView26);
            final int AES_KEYLENGTH = 128;
            byte[] iv = new byte[AES_KEYLENGTH / 8];
            String text1 = "";
            String ke = "";
            Cipher desCipher = null;
            text1 = e12.getText().toString();
            ke = e14.getText().toString();
            byte[] textEncrypted = Base64.decode(text1, 0);

            String r = "";
            FileInputStream fileIn = openFileInput("abcd.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = InputRead.read(inputBuffer)) > 1) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                r += readstring;
            }
            if (ke.equals(r)) {
                String s = "";
                FileInputStream fileInn = openFileInput("abc.txt");
                InputStreamReader InputReadd = new InputStreamReader(fileInn);
                char[] inputBufferr = new char[100];
                int charReadd;
                while ((charReadd = InputReadd.read(inputBuffer)) > 0) {
                    String readstring = String.copyValueOf(inputBuffer, 0, charReadd);
                    s += readstring;
                }
                byte[] encodedKey = Base64.decode(s, Base64.DEFAULT);
                SecretKey myDesKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
                desCipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                String cipherte = "";
                cipherte = String.valueOf(Hex.encodeHex(textDecrypted));
                byte[] bytes = Hex.decodeHex(cipherte.toCharArray());
                String plaintext = new String(bytes, "UTF-8");
                t26.setText(plaintext);

            } else {
                t26.setText("Wrong Key");
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void onButtonClick26(View view) {
        EditText e15 = (EditText) findViewById(R.id.editText15);
        EditText e16 = (EditText) findViewById(R.id.editText16);
        TextView t29 = (TextView) findViewById(R.id.textView29);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer.append(e15.getText());
        stringBuffer1.append(e16.getText());


        for (int i = 0, j = 0; i < stringBuffer.length(); i++) {
            char t = stringBuffer.charAt(i);
            int p = (int) t;
            char t1 = stringBuffer1.charAt(j);
            int p1 = (int) t1;
            if (p >= 65 && p <= 90) {
                int l = (p + 26 - p1) % 26 + 65;
                char f = (char) l;
                stringBuffer.setCharAt(i, f);
            } else if (p >= 97 && p <= 122) {
                int l = (p - p1 + 26) % 26 + 97;
                char f = (char) l;
                stringBuffer.setCharAt(i, f);
            } else {
                char f = (char) p;
                stringBuffer.setCharAt(i, f);
            }
            j = ++j % stringBuffer1.length();
        }
        t29.setText(String.valueOf(stringBuffer));

    }

    public void onButtonClick30(View view) {
        try {
            EditText e17 = (EditText) findViewById(R.id.editText17);
            EditText e18 = (EditText) findViewById(R.id.editText18);
            TextView t31 = (TextView) findViewById(R.id.textView31);

            String text = "";
            text = e17.getText().toString();
            String ke = e18.getText().toString();
            byte[] textEncrypted = Base64.decode(text, 0);

            String r = "";
            FileInputStream fileIn = openFileInput("abcdef.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = InputRead.read(inputBuffer)) > 1) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                r += readstring;
            }
            if (ke.equals(r)) {
                String s = "";
                FileInputStream fileInn = openFileInput("abcde.txt");
                InputStreamReader InputReadd = new InputStreamReader(fileInn);
                char[] inputBufferr = new char[100];
                int charReadd;
                while ((charReadd = InputReadd.read(inputBufferr)) > 0) {
                    String readstring = String.copyValueOf(inputBufferr, 0, charReadd);
                    s += readstring;
                }

                Cipher desCipher;
                byte[] encodedKey = Base64.decode(s, Base64.DEFAULT);
                SecretKey myDesKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");
                desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                String cipherte = "";
                cipherte = String.valueOf(Hex.encodeHex(textDecrypted));
                byte[] bytes = Hex.decodeHex(cipherte.toCharArray());
                String plaintext = new String(bytes, "UTF-8");
                t31.setText(plaintext);
            } else {
                t31.setText("Wrong Key");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onButtonClick31(View view)
    {
        setContentView(R.layout.desd);
    }
}