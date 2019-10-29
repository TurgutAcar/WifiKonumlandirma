package com.example.findme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findme.view.CustomView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private WifiManager wifiManager;
    private List<ScanResult> results;
    double sonuc;
    double r1, r2, r3, x1, y1, x2, y2, x3, y3, a, b, c, d, e, f;
    public double konumx;
    public double konumy;
    double xx1, yy1, xx2, yy2, xx3, yy3;
    int min;
    double tmp;
    double rr1, rr2, rr3;
    int i = 0, n = 0, j = 0;
    public static double Xk_kalmanEskiTahmin = 0.0;
    public static double Pk_eskiHataKovaryansi = 1;
    public static double Xk_kalmanEskiTahmin2 = 0.0;
    public static double Pk_eskiHataKovaryansi2 = 1;
    public static double Xk_kalmanEskiTahmin3 = 0.0;
    public static double Pk_eskiHataKovaryansi3 = 1;
    public static double Xk_kalmanEskiTahmin4 = 0.0;
    public static double Pk_eskiHataKovaryansi4 = 1;
    public static double Xk_kalmanEskiTahmin5 = 0.0;
    public static double Pk_eskiHataKovaryansi5 = 1;
    public static double hata = 0.1;
    public static double Q = 0.05;
    double[] deger = new double[6];
    SQLiteDatabase database;
    int zaman = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(new CustomView(this));



        try {
            database = this.openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
        } catch (Exception e) {
            e.printStackTrace();
        }
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        final Handler handler = new Handler();
        final Timer timer;
        timer = new Timer();


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        scanWifi();
                    }
                });
            }
        };


        timer.schedule(timerTask, 0, 4000);


    }
    private void scanWifi() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        Toast.makeText(this, "Finds Location...", Toast.LENGTH_SHORT).show();
        // this.unregisterReceiver(wifiReceiver);
    }
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();

            unregisterReceiver(this);


            for (ScanResult scanResult : results) {
                double rssiInOneMeter = -45.0;
                double mesafe = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter, 6.9476) + 0.54992;
             /*if (scanResult.BSSID.equals("4c:fa:ca:45:0c:50")){//  if (scanResult.BSSID.equals("ac:4e:91:5f:04:e0")) {
                    double xk = 2.10, yk = 20.0;
                    double rssiInOneMeter2 = -45.0;
                    double Xk_kalmanYeniTahmin = Xk_kalmanEskiTahmin;
                    double Pk_yeniHataKovaryansi = Pk_eskiHataKovaryansi + Q;

                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci = Pk_yeniHataKovaryansi / (Pk_yeniHataKovaryansi + hata);

                    double Xk_kalmanHesabi = Xk_kalmanYeniTahmin + Kk_kalmanKazanci * (scanResult.level - Xk_kalmanYeniTahmin);
                    Pk_yeniHataKovaryansi = (1 - Kk_kalmanKazanci) * Pk_eskiHataKovaryansi;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi = Pk_yeniHataKovaryansi;
                    Xk_kalmanEskiTahmin = Xk_kalmanHesabi;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe2 = 0.42093 * Math.pow(Xk_kalmanHesabi / rssiInOneMeter2, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk));
                        statement.bindString(2, String.valueOf(yk));
                        statement.bindString(3, String.valueOf(mesafe2));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (scanResult.BSSID.equals("f8:4a:bf:58:0b:50")) {
                    double xk1 = 25.40, yk1 = 6.0;
                    double rssiInOneMeter3 = -45.0;
                    double mesafe3 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter3, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk1));
                        statement.bindString(2, String.valueOf(yk1));
                        statement.bindString(3, String.valueOf(mesafe3));
                        statement.execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (scanResult.BSSID.equals("f8:4a:bf:58:12:70")) {
                    double xk2 = 22.10, yk2 = 3.40;
                    double rssiInOneMeter4 = -45.0;
                    double mesafe4 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter4, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk2));
                        statement.bindString(2, String.valueOf(yk2));
                        statement.bindString(3, String.valueOf(mesafe4));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (scanResult.BSSID.equals("f8:4a:bf:58:0c:f0")) {
                    double xk3 = 28.0, yk3 = 23.60;
                    double rssiInOneMeter5 = -45.0;
                    double mesafe5 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter5, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk3));
                        statement.bindString(2, String.valueOf(yk3));
                        statement.bindString(3, String.valueOf(mesafe5));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (scanResult.BSSID.equals("f8:4a:bf:58:0c:00")) {
                    double xk4 = 28.0, yk4 = 18.60;
                    double rssiInOneMeter6 = -45.0;
                    double mesafe6 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter6, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk4));
                        statement.bindString(2, String.valueOf(yk4));
                        statement.bindString(3, String.valueOf(mesafe6));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (scanResult.BSSID.equals("ac:4e:91:5f:03:d0")) {
                    double xk5 = 43.85, yk5 = 11.50;
                    double rssiInOneMeter7 = -45.0;
                    double mesafe7 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter7, 6.9476) + 0.54992;
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk5));
                        statement.bindString(2, String.valueOf(yk5));
                        statement.bindString(3, String.valueOf(mesafe7));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                if (scanResult.BSSID.equals("d2:7e:35:a0:93:e9")) { //MERT FAİK
                    double xk6 = 4.5, yk6 = 0.0;
                    double rssiInOneMeter8 = -45.0;
                    double Xk_kalmanYeniTahmin5 = Xk_kalmanEskiTahmin5;
                    double Pk_yeniHataKovaryansi5 = Pk_eskiHataKovaryansi5 + Q;
                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci5 = Pk_yeniHataKovaryansi5 / (Pk_yeniHataKovaryansi5 + hata);
                    double Xk_kalmanHesabi5 = Xk_kalmanYeniTahmin5 + Kk_kalmanKazanci5 * (scanResult.level - Xk_kalmanYeniTahmin5);
                    Pk_yeniHataKovaryansi5 = (1 - Kk_kalmanKazanci5) * Pk_eskiHataKovaryansi5;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi5 = Pk_yeniHataKovaryansi5;
                    Xk_kalmanEskiTahmin5 = Xk_kalmanHesabi5;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe8 = 0.42093 * Math.pow(Xk_kalmanHesabi5 / rssiInOneMeter8, 6.9476) + 0.54992;
                  //  double mesafe8 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter8, 6.9476) + 0.54992;
                    if (mesafe8!=0){
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk6));
                        statement.bindString(2, String.valueOf(yk6));
                        statement.bindString(3, String.valueOf(mesafe8));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                }
                if (scanResult.BSSID.equals("2e:85:de:bb:bf:6f")) {
                    double xk7 = 4.5, yk7 = 6.6;       //CAGATAY
                    double rssiInOneMeter9 = -45.0;
                    double Xk_kalmanYeniTahmin4 = Xk_kalmanEskiTahmin4;
                    double Pk_yeniHataKovaryansi4 = Pk_eskiHataKovaryansi4 + Q;
                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci4 = Pk_yeniHataKovaryansi4 / (Pk_yeniHataKovaryansi4 + hata);
                    double Xk_kalmanHesabi4 = Xk_kalmanYeniTahmin4 + Kk_kalmanKazanci4 * (scanResult.level - Xk_kalmanYeniTahmin4);
                    Pk_yeniHataKovaryansi4 = (1 - Kk_kalmanKazanci4) * Pk_eskiHataKovaryansi4;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi4 = Pk_yeniHataKovaryansi4;
                    Xk_kalmanEskiTahmin4 = Xk_kalmanHesabi4;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe9 = 0.42093 * Math.pow(Xk_kalmanHesabi4 / rssiInOneMeter9, 6.9476) + 0.54992;
                    //double mesafe9 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter9, 6.9476) + 0.54992;
                    if (mesafe9!=0){
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk7));
                        statement.bindString(2, String.valueOf(yk7));
                        statement.bindString(3, String.valueOf(mesafe9));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                }
                if (scanResult.BSSID.equals("2e:b7:0d:ff:e6:a8")) {
                    double xk8=5.5,yk8=4.8                   //TURGUT
                            ;  // double xk8 = 22.50, yk8 = 8.50;
                    double rssiInOneMeter10 = -45.0;
                    double Xk_kalmanYeniTahmin = Xk_kalmanEskiTahmin;
                    double Pk_yeniHataKovaryansi = Pk_eskiHataKovaryansi + Q;
                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci = Pk_yeniHataKovaryansi / (Pk_yeniHataKovaryansi + hata);
                    double Xk_kalmanHesabi = Xk_kalmanYeniTahmin + Kk_kalmanKazanci * (scanResult.level - Xk_kalmanYeniTahmin);
                    Pk_yeniHataKovaryansi = (1 - Kk_kalmanKazanci) * Pk_eskiHataKovaryansi;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi = Pk_yeniHataKovaryansi;
                    Xk_kalmanEskiTahmin = Xk_kalmanHesabi;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe10 = 0.42093 * Math.pow(Xk_kalmanHesabi / rssiInOneMeter10, 6.9476) + 0.54992;
                 //   double mesafe10 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter10, 6.9476) + 0.54992;
                    if (mesafe10!=0){
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk8));
                        statement.bindString(2, String.valueOf(yk8));
                        statement.bindString(3, String.valueOf(mesafe10));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                }
                if (scanResult.BSSID.equals("b4:6b:fc:98:e2:3e")) {   //TALHA
                    double xk9 =4.5, yk9 = 5.1;
                    double rssiInOneMeter11 = -45.0;
                    double Xk_kalmanYeniTahmin2 = Xk_kalmanEskiTahmin2;
                    double Pk_yeniHataKovaryansi2 = Pk_eskiHataKovaryansi2 + Q;
                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci2 = Pk_yeniHataKovaryansi2 / (Pk_yeniHataKovaryansi2 + hata);
                    double Xk_kalmanHesabi2 = Xk_kalmanYeniTahmin2 + Kk_kalmanKazanci2 * (scanResult.level - Xk_kalmanYeniTahmin2);
                    Pk_yeniHataKovaryansi2 = (1 - Kk_kalmanKazanci2) * Pk_eskiHataKovaryansi2;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi2 = Pk_yeniHataKovaryansi2;
                    Xk_kalmanEskiTahmin2 = Xk_kalmanHesabi2;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe11 = 0.42093 * Math.pow(Xk_kalmanHesabi2 / rssiInOneMeter11, 6.9476) + 0.54992;
                    //double mesafe11 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter11, 6.9476) + 0.54992;
                    if (mesafe11!=0){
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk9));
                        statement.bindString(2, String.valueOf(yk9));
                        statement.bindString(3, String.valueOf(mesafe11));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}
                }
                if (scanResult.BSSID.equals("e6:d5:3d:9e:7c:bd")) {  //EREN
                    double xk10 = 5.5, yk10 = 4.2;
                    double rssiInOneMeter12 = -45.0;
                    double Xk_kalmanYeniTahmin3 = Xk_kalmanEskiTahmin3;
                    double Pk_yeniHataKovaryansi3 = Pk_eskiHataKovaryansi3 + Q;
                    //ölçümleri düzeltme
                    double Kk_kalmanKazanci3 = Pk_yeniHataKovaryansi3 / (Pk_yeniHataKovaryansi3 + hata);
                    double Xk_kalmanHesabi3 = Xk_kalmanYeniTahmin3 + Kk_kalmanKazanci3 * (scanResult.level - Xk_kalmanYeniTahmin3);
                    Pk_yeniHataKovaryansi3 = (1 - Kk_kalmanKazanci3) * Pk_eskiHataKovaryansi3;

                    //eski değerleri atama
                    Pk_eskiHataKovaryansi3 = Pk_yeniHataKovaryansi3;
                    Xk_kalmanEskiTahmin3 = Xk_kalmanHesabi3;
                    //bulunan sonuç bir sonraki adım için eski tahmin olacak
                    double mesafe12 = 0.42093 * Math.pow(Xk_kalmanHesabi3/ rssiInOneMeter12, 6.9476) + 0.54992;
                   // System.out.println("yazdirkyk="+mesafe12);
                   // double mesafe12 = 0.42093 * Math.pow(scanResult.level / rssiInOneMeter12, 6.9476) + 0.54992;
                    if (mesafe12!=0){
                    try {
                        database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(2))");
                        String sqlString = "INSERT INTO alanbilgi (xdegeri,ydegeri,uzaklik) VALUES(?,?,?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1, String.valueOf(xk10));
                        statement.bindString(2, String.valueOf(yk10));
                        statement.bindString(3, String.valueOf(mesafe12));
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}
                }
                System.out.println("turgut");

                System.out.println(scanResult.SSID + "- RSS: " + scanResult.level + "- Frequency: " + scanResult.frequency + "  mesafe:" + mesafe + "MAC:" + scanResult.BSSID);

            }
            try {
                database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik INT(2))");
                Cursor cursor = database.rawQuery("SELECT *FROM alanbilgi", null);
                double x = cursor.getColumnIndex("xdegeri");
                double y = cursor.getColumnIndex("ydegeri");
                double md = cursor.getColumnIndex("uzaklik");
                cursor.moveToFirst();
                n = 0;
                i = 0;
                while (cursor != null) {

                    deger[i] = cursor.getDouble((int) md);
                    //  System.out.println("kordinatiii=" + cursor.getInt(x) + cursor.getInt(y) + "yazdir=" + cursor.getDouble((int) md));
                    i++;
                    n++;
                    cursor.moveToNext();
                }
            } catch(
                    Exception e)

            {
                e.printStackTrace();
            }


            System.out.println("deger0="+deger[0]+"deger1="+deger[1]+"deger2"+deger[2]);


            for(i =0;i<n -1;i++)

            {
                min = i;
                for (j = i; j < n; j++) {

                    if (deger[j] < deger[min]) {
                        min = j;
                    }
                }
                tmp = deger[i];
                deger[i] = deger[min];
                deger[min] = tmp;
            }
            System.out.println("sirala="+deger[0]+"sonu2="+deger[1]+"sonu3="+deger[2]);

            rr1 =deger[0];
            rr2 =deger[1];
            rr3 =deger[2];
            System.out.println("rr1="+rr1 +"rr2="+rr2 +"rr3="+rr3);
            try
            {
                database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS alanbilgi (xdegeri DOUBLE(2),ydegeri DOUBLE(2),uzaklik DOUBLE(3))");
                Cursor cursor = database.rawQuery("SELECT *FROM alanbilgi", null);
                double xkor = cursor.getColumnIndex("xdegeri");
                double ykor = cursor.getColumnIndex("ydegeri");
                double mesafed = cursor.getColumnIndex("uzaklik");
                cursor.moveToFirst();
                while (cursor != null) {
                    if (rr1 == cursor.getDouble((int) mesafed)) {
                        //  int xx1, yy1;
                        xx1 = cursor.getDouble((int) xkor);
                        yy1 = cursor.getDouble((int) ykor);
                        System.out.println("x=" + xx1 + " " + "y=" + yy1 + " " + "mesafe=" + cursor.getDouble((int) mesafed));
                    } else if (rr2 == cursor.getDouble((int) mesafed)) {
                        //int xx2, yy2;
                        xx2 = cursor.getDouble((int) xkor);
                        yy2 = cursor.getDouble((int) ykor);
                        System.out.println("x=" + xx2 + " " + "y=" + yy2 + " " + "mesafe=" + cursor.getDouble((int) mesafed));

                    } else if (rr3 == cursor.getDouble((int) mesafed)) {
                        // int xx3, yy3;
                        xx3 = cursor.getDouble((int) xkor);
                        yy3 = cursor.getDouble((int) ykor);
                        System.out.println("x=" + xx3 + " " + "y=" + yy3 + " " + "mesafe=" + cursor.getDouble((int) mesafed));

                    }
                    cursor.moveToNext();
                }

            } catch(
                    Exception e)

            {
                e.printStackTrace();
            }
            try

            {
                database = openOrCreateDatabase("AlanBilgileri", MODE_PRIVATE, null);
                database.delete("alanbilgi", null, null);

            } catch(
                    Exception e)

            {
                e.printStackTrace();
            }

            a=(-2.0*xx1)+(2.0*xx2); //birinci denklemin x i
            System.out.println("ayazdir="+xx1+xx2+xx3);
            System.out.println("aaa="+a);
            b=(-2.0*yy1)+(2.0*yy2); //birinci denklemin y si
            System.out.println("bbb="+b);
            c=(rr1*rr1)-(rr2*rr2)-(xx1*xx1)+(xx2*xx2)-(yy1*yy1)+(yy2*yy2); //ikinci denklemi ilkinden çıkardık.
            System.out.println("ccc="+c);
            d=(-2.0*xx2)+(2.0*xx3); //ikinci denlemin x i
            System.out.println("ddd="+d);
            e=(-2.0*yy2)+(2.0*yy3); //ikinci denklemin y si
            System.out.println("eee="+e);
            f=(rr2*rr2)-(rr3*rr3)-(xx2*xx2)+(xx3*xx3)-(yy2*yy2)+(yy3*yy3); //üçüncü denklemi ikincisinden çıkardık.
            System.out.println("fff="+f);
            konumx=(((c*e)-(f*b))/((e*a)-(b*d)));
            konumy=(((c*d)-(a*f))/((b*d)-(a*e)));
            System.out.println("benim konumum0="+konumx+" "+konumy);
            //NumberFormat nf = NumberFormat.getInstance();
            //  nf.setMaximumFractionDigits(2);
            // String xkonum = nf.format(konumx);
            //String ykonum = nf.format(konumy);
            //   xKonum.setText("X:"+xkonum);
            //     yKonum.setText("Y:"+ykonum);

            konumx=konumx*52.32; //1439
            konumy=konumy*86.78;  //2239
            CustomView customView = new CustomView((getApplicationContext()));
            customView.setCoordinates(konumx, konumy);
            setContentView(customView);
            // NumberFormat nf=NumberFormat.getInstance();
            //nf.setMaximumFractionDigits(2);
            //xkonum=nf.format(konumx);
            //ykonum=nf.format(konumy);
            System.out.println("benim konumum1="+konumx+" "+konumy);


            // customView.onDrawing();

        }
    };
}


















