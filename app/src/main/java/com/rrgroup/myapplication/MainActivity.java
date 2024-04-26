package com.rrgroup.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.zires.switchsegmentedcontrol.ZiresSwitchSegmentedControl;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private boolean loggedIn = false;
    RecyclerView recyclerView;
    RecyclerView MarksRecyclerView;
    RecyclerView TTRecyclerView;
    ConstraintLayout parent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String userID="",password="";
    View MarkLayout;
    View TTLayout;
    View CGPA;
    View GPA;
    View about;
    View settings;
    View privPol;
    View legal;

    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView three_lines;
    private String lastUpdated;
    private TextView preText;
    Button retriveBtn;
    LinearLayout top_name;
    ProgressDialog progressDialog;
    SwitchMaterial aSwitch;
    SwitchMaterial bSwitch;
    Boolean isAutoMode=false;
    ZiresSwitchSegmentedControl all_tt_switch;
    boolean isTT;

    List<String> mondaySubjectsCode;
    List<String> tuesdaySubjectsCode;
    List<String> wednesdaySubjectsCode;
    List<String> thursdaySubjectsCode;
    List<String> fridaySubjectsCode;
    List<String> saturdaySubjectsCode;
    boolean isScholarship;
    String newHtml;
    String share_url="";


    private static final Map<String, String> subjectMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchJsonTask().execute(AppConfig.jsonURL);
        webView = findViewById(R.id.webView);
        recyclerView = findViewById(R.id.recyclerView);
        MarksRecyclerView = findViewById(R.id.MarksRecyclerView);
        TTRecyclerView = findViewById(R.id.TTRecyclerView);
        parent = findViewById(R.id.parent);
        drawerLayout = findViewById(R.id.drawer_layout);
        MarkLayout = findViewById(R.id.markLayout);
        TTLayout = findViewById(R.id.ttLayout);
        CGPA = findViewById(R.id.CGPALayout);
        GPA = findViewById(R.id.GPALayout);
        settings = findViewById(R.id.sett_layout);
        about = findViewById(R.id.aboutLayout);
        privPol = findViewById(R.id.ppLayout);
        legal = findViewById(R.id.legalLayout);
        preText = findViewById(R.id.preText);
        top_name = findViewById(R.id.top_name_layout);
        sharedPreferences = getSharedPreferences("my_html", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        aSwitch = findViewById(R.id.switch_);
        bSwitch = findViewById(R.id.bswitch_);
        all_tt_switch = findViewById(R.id.all_tt_switch);

        boolean isChecked = sharedPreferences.getBoolean("isChecked",false);
        if(isChecked){
            aSwitch.setChecked(true);
            isAutoMode = true;
        }
        else {
            aSwitch.setChecked(false);
            isAutoMode = false;
        }

        boolean isScholarshipMode = sharedPreferences.getBoolean("isScholarship",false);
        if(isScholarshipMode){
            bSwitch.setChecked(true);
            isScholarship = true;
        }
        else {
            bSwitch.setChecked(false);
            isScholarship = false;
        }

        isTT = sharedPreferences.getBoolean("isTT",false);
        if(isTT){
            all_tt_switch.setChecked(true);
        }
        else {
            all_tt_switch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAutoMode = true;
                    editor.putBoolean("isChecked",isAutoMode);
                    editor.apply();
                } else {
                    isAutoMode = false;
                    editor.putBoolean("isChecked",isAutoMode);
                    editor.apply();
                }
            }
        });

        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isScholarship = true;
                    editor.putBoolean("isScholarship",isScholarship);
                    editor.apply();
                } else {
                    isScholarship = false;
                    editor.putBoolean("isScholarship",isScholarship);
                    editor.apply();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Reload App!");
                builder.setMessage("Changes applied\nPlease reload the app");

                builder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

                builder.setCancelable(true);
                builder.show();
            }
        });

        retriveBtn = findViewById(R.id.retriveDetails);

        retriveBtn.setOnClickListener(view -> loadWebView(false));

        try {
            boolean firstTime = sharedPreferences.getBoolean("first_time", true);
            if (firstTime) {
                showPrivacyPolicyDialog();
                retriveBtn.setVisibility(View.VISIBLE);
                top_name.setVisibility(View.GONE);
            }
        }catch (Exception e){e.printStackTrace();}

        try {
            boolean isLogin = sharedPreferences.getBoolean("login_status", false);
            if(isLogin){
                retriveBtn.setVisibility(View.GONE);
                preText.setVisibility(View.VISIBLE);
            }else {
                retriveBtn.setVisibility(View.VISIBLE);
//                preText.setVisibility(View.GONE);
                top_name.setVisibility(View.GONE);
            }
        }catch (Exception e){e.printStackTrace();}

       try {
           String saved_uid = sharedPreferences.getString("saved_uid", "");
           String saved_pass = sharedPreferences.getString("saved_pass","");
           String uTime = sharedPreferences.getString("updateTime","");
           lastUpdated=uTime;
           userID=saved_uid;
           password=saved_pass;
           Log.d("UserID", userID);
           Log.d("Pass", password);
       }catch (Exception e){e.printStackTrace();}


        webView.setVisibility(View.GONE);


        ImageView button = findViewById(R.id.ref_btn);
        button.setOnClickListener(view ->loadWebView(isAutoMode));


        try{
            String  ttable = sharedPreferences.getString("ttable","");
            parseSubjectMap(ttable);
            parseTT(ttable);
            String prev_html = sharedPreferences.getString("myKey", "");
            parseAttendanceData(prev_html,isTT);
            newHtml = prev_html;
            String prev_name = sharedPreferences.getString("myKey_n","");
            parseName(prev_name);
            String  prev_mark = sharedPreferences.getString("marks","");
            parseMark(prev_mark);
        }catch (Exception e){e.printStackTrace();}
        gpaCal();
        cgpaCal();

        three_lines = findViewById(R.id.threeDot);
        three_lines.setOnClickListener(v -> showCustomDialog());
        Button button2 = findViewById(R.id.button_sett);
        button2.setOnClickListener(v ->showCustomDialog());
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_home);
        menuItem.setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    three_lines.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.VISIBLE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_marks) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.VISIBLE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_gpa) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.VISIBLE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_cgpa) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.VISIBLE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_privacy) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.VISIBLE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                }else if (itemId == R.id.nav_legal) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.VISIBLE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                }else if (itemId == R.id.nav_about) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.VISIBLE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                }else if (itemId == R.id.nav_tt) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.VISIBLE);
                    settings.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_set) {
                    three_lines.setVisibility(View.INVISIBLE);
                    parent.setVisibility(View.GONE);
                    MarkLayout.setVisibility(View.GONE);
                    CGPA.setVisibility(View.GONE);
                    GPA.setVisibility(View.GONE);
                    about.setVisibility(View.GONE);
                    privPol.setVisibility(View.GONE);
                    legal.setVisibility(View.GONE);
                    TTLayout.setVisibility(View.GONE);
                    settings.setVisibility(View.VISIBLE);
                } else if (itemId == R.id.nav_share) {
                    try{
                        if(share_url.equals("")){
                            String message = "Check out *SRM Tracker* App!\nDownload it here: *"+AppConfig.shareUrl+"*";

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                            startActivity(Intent.createChooser(shareIntent, "Share via"));
                        }else {
                            String message = "Check out *SRM Tracker* App!\nDownload it here: *"+share_url+"*";

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                            startActivity(Intent.createChooser(shareIntent, "Share via"));
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        String message = "Check out *SRM Tracker* App!\nDownload it here: *"+AppConfig.shareUrl+"*";

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                    }

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        String prev_html = sharedPreferences.getString("myKey", "");
        all_tt_switch.setOnToggleSwitchChangeListener(isChecked1 -> {
            if(isChecked1){
                    isTT = false;
                    parseAttendanceData(newHtml,isTT);
                    editor.putBoolean("isTT",false);
                    editor.apply();

            }else {
                    isTT = true;
                    parseAttendanceData(newHtml,isTT);
                    editor.putBoolean("isTT",true);
                    editor.apply();

            }
        });
    }
    private void copyLanguageDataIfNeeded() {
        String tessFolderPath = getFilesDir() + "/tesseract/tessdata";
        String filePath = tessFolderPath + "/eng.traineddata";

        File tessFolder = new File(tessFolderPath);
        if (!tessFolder.exists()) {
            tessFolder.mkdirs();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                InputStream in = getAssets().open("tessdata/eng.traineddata");
                OutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String solveCaptcha(Bitmap captchaBitmap) {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();

        String datapath = getFilesDir() + "/tesseract/";

        File tessdataDir = new File(datapath + "tessdata/");
        if (tessdataDir.exists()) {
            Log.d("Directory Exists", "Directory Exists ");
        }else {
            copyLanguageDataIfNeeded();
        }

        tessBaseAPI.init(datapath, "eng");
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        captchaBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        tessBaseAPI.setImage(captchaBitmap);
        String recognizedText = tessBaseAPI.getUTF8Text();

        tessBaseAPI.end();

        return recognizedText.trim();
    }

    private void loadWebView(Boolean autoMode) {
        progressDialog.show();
        final boolean[] finish = {false};
//        if(autoMode){
//            progressDialog.show();
//        }
//        progressDialog.show();
        webView.clearCache(true);
//        loggedIn=false;

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);

                if(autoMode){
                    CookieManager cookieManager1 = CookieManager.getInstance();
                    String cookie = cookieManager1.getCookie(url);

                    try {
                        String captchaText = new Captcha().execute(cookie).get();
                        Log.d(TAG, "CAPTCHA: "+captchaText);
                        String script = "javascript:(function() {document.getElementById('ccode').value = '"+captchaText+"';document.getElementById('login').value = '"+userID+"';document.getElementById('passwd').value = '"+password+"';})()";


                        String clickButtonScript = "javascript:(function() { " +
                                "document.querySelector('.btn.btn-custom.btn-user.btn-block').click();" +
                                "})()";
                        view.loadUrl(script);

                        if(captchaText != null){
                            view.loadUrl(clickButtonScript);
                        }

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    progressDialog.dismiss();
                    String script = "javascript:(function() {document.getElementById('login').value = '"+userID+"';document.getElementById('passwd').value = '"+password+"';})()";
                    view.loadUrl(script);
                }


                if (url.equals("https://sp.srmist.edu.in/srmiststudentportal/students/template/HRDSystem.jsp")) {

                    if(!finish[0]){
                        finish[0] =true;
                        view.loadUrl("javascript:window.Android.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        loggedIn = true;
                        editor.putBoolean("login_status",loggedIn);
                        editor.apply();
                        CookieManager cookieManager = CookieManager.getInstance();
                        String cookie = cookieManager.getCookie(url);
                        String name = "https://sp.srmist.edu.in/srmiststudentportal/students/template/HRDSystem.jsp";
                        String attendanceUrl = "https://sp.srmist.edu.in/srmiststudentportal/students/report/studentAttendanceDetails.jsp";
                        String internalMarksUrl = "https://sp.srmist.edu.in/srmiststudentportal/students/report/studentInternalMarkDetails.jsp";
                        String timeTableUrl= "https://sp.srmist.edu.in/srmiststudentportal/students/report/studentTimeTableDetails.jsp";

                        new MyAsyncTask().execute(name,cookie,"false");
                        new MyAsyncTask().execute(attendanceUrl, cookie,"false");
                        new MyAsyncTask().execute(internalMarksUrl,cookie,"false");
                        MyAsyncTask myTask1 = new MyAsyncTask();
                        myTask1.execute(timeTableUrl,cookie,"true");

                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());
                        String formattedDate = dateFormat.format(currentDate);
                        lastUpdated = formattedDate;
                        editor.putString("updateTime", lastUpdated);
                        editor.apply();
                        webView.setVisibility(View.GONE);

                        myTask1.setOnTaskCompletedListener(result -> {

                                parent.setVisibility(View.VISIBLE);
                                retriveBtn.setVisibility(View.GONE);
                                top_name.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();

                        });



                    }

                }
            }
        });
        webView.loadUrl("https://sp.srmist.edu.in/srmiststudentportal/students/loginManager/youLogin.jsp");

        if(!autoMode){
            webView.setVisibility(View.VISIBLE);
            parent.setVisibility(View.GONE);
        }
    }
    public class Captcha extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... cookies) {
            try {
                URL url = new URL("https://sp.srmist.edu.in/srmiststudentportal/captchas");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);

                // same session setting
                if (cookies.length > 0 && cookies[0] != null) {
                    connection.setRequestProperty("Cookie", cookies[0]);
                }

                connection.connect();
                String cookie = connection.getHeaderField("Set-Cookie");
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                String captchaText = solveCaptcha(bitmap);
                Log.d("Captcha Text", "getCaptchaImage: " + captchaText);
                return captchaText;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String captchaText) {
            // kuch nhi karna
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String isTimeTable = "false";

        private Consumer<String> onTaskCompletedListener;

        public void setOnTaskCompletedListener(Consumer<String> listener) {
            this.onTaskCompletedListener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String cookie = params[1];
            isTimeTable = params[2];

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Cookie", cookie);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    reader.close();
                    return content.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && isTimeTable.equals("false")) {
//                System.out.println("Content from AsyncTask: " + result);
                try{
                    parseAttendanceData(result,isTT);
                    newHtml = result;
                    editor.putString("myKey", result);
                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    parseName(result);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    parseMark(result);
                    editor.putString("marks",result);
                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }


            } else if (isTimeTable.equals("true")) {
                try{
                    parseSubjectMap(result);
                    parseTT(result);
                    editor.putString("ttable",result);
                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            if (onTaskCompletedListener != null) {
                onTaskCompletedListener.accept("Data");
            }


        }

    }

    public void parseTT(String htmlContent) {
        List<String> mondaySubjects = new ArrayList<>();
        List<String> tuesdaySubjects = new ArrayList<>();
        List<String> wednesdaySubjects = new ArrayList<>();
        List<String> thursdaySubjects = new ArrayList<>();
        List<String> fridaySubjects = new ArrayList<>();
        List<String> saturdaySubjects = new ArrayList<>();
        mondaySubjectsCode = new ArrayList<>();
        tuesdaySubjectsCode = new ArrayList<>();
        wednesdaySubjectsCode = new ArrayList<>();
        thursdaySubjectsCode = new ArrayList<>();
        fridaySubjectsCode = new ArrayList<>();
        saturdaySubjectsCode = new ArrayList<>();
        List<String> periodM = new ArrayList<>();
        List<String> periodTue = new ArrayList<>();
        List<String> periodWed = new ArrayList<>();
        List<String> periodThur = new ArrayList<>();
        List<String> periodFri = new ArrayList<>();
        List<String> periodSat = new ArrayList<>();

        Document doc = Jsoup.parse(htmlContent);
        Elements rows = doc.select("tbody tr");

        for (Element row : rows) {
            Elements cells = row.select("td");

            if (cells.size() >= 1) {
                String day = cells.get(0).text().trim();

                for (int i = 1; i < cells.size(); i++) {
                    String subjectCode = cells.get(i).text().trim();

                    String pTime = "";
                    if(i==1){
                        pTime = "8:00 AM";
                    } else if (i==2) {
                        pTime = "8:50 AM";
                    } else if (i==3) {
                        pTime= "9:50 AM";
                    } else if (i==4) {
                        pTime = "10:40 AM";
                    } else if (i==5) {
                        pTime = "12:20 PM";
                    } else if (i==6) {
                        pTime = "1:10 PM";
                    } else if (i==7) {
                        pTime = "2:00 PM";
                    } else if (i==8) {
                        pTime = "2:50 PM";
                    }

                    // subject desc and name getting from the map
                    String subjectDescription = subjectMap.get(subjectCode);

                    if(!subjectCode.equals("-")){
                        switch (day) {
                            case "Monday":
                                mondaySubjects.add(subjectDescription);
                                mondaySubjectsCode.add(subjectCode);
                                periodM.add(pTime);
                                Log.d("Monday Subject", subjectCode + " - " + subjectDescription);
                                break;
                            case "Tuesday":
                                tuesdaySubjects.add(subjectDescription);
                                tuesdaySubjectsCode.add(subjectCode);
                                periodTue.add(pTime);
                                Log.d("Tue Subject", subjectCode + " - " + subjectDescription);
                                break;
                            case "Wednesday":
                                wednesdaySubjects.add(subjectDescription);
                                wednesdaySubjectsCode.add(subjectCode);
                                periodWed.add(pTime);
                                Log.d("Wed Subject", subjectCode + " - " + subjectDescription);
                                break;
                            case "Thursday":
                                thursdaySubjects.add(subjectDescription);
                                thursdaySubjectsCode.add(subjectCode);
                                periodThur.add(pTime);
                                Log.d("Thur Subject", subjectCode + " - " + subjectDescription);
                                break;
                            case "Friday":
                                fridaySubjects.add(subjectDescription);
                                fridaySubjectsCode.add(subjectCode);
                                periodFri.add(pTime);
                                Log.d("Fri Subject", subjectCode + " - " + subjectDescription);
                                break;
                            case "Saturday":
                                saturdaySubjects.add(subjectDescription);
                                saturdaySubjectsCode.add(subjectCode);
                                periodSat.add(pTime);
                                Log.d("Sat Subject", subjectCode + " - " + subjectDescription);
                                break;
                        }
                    }
                }
            }
        }

        runOnUiThread(() -> {
            if (TTRecyclerView.getAdapter() != null) {
                TTRecyclerView.getAdapter().notifyDataSetChanged();
            }
            TTDay(mondaySubjectsCode,mondaySubjects,periodM);
            Spinner days = findViewById(R.id.days);
            String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.custom_tt_spinner_item, daysArray);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            days.setAdapter(dayAdapter);
            days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedDay = (String) parentView.getItemAtPosition(position);
                    if(selectedDay.equals("Monday")){
                        TTDay(mondaySubjectsCode,mondaySubjects,periodM);
                    } else if (selectedDay.equals("Tuesday")) {
                        TTDay(tuesdaySubjectsCode,tuesdaySubjects,periodTue);
                    } else if (selectedDay.equals("Wednesday")) {
                        TTDay(wednesdaySubjectsCode,wednesdaySubjects,periodWed);
                    }else if (selectedDay.equals("Thursday")){
                        TTDay(thursdaySubjectsCode,thursdaySubjects,periodThur);
                    } else if (selectedDay.equals("Friday")) {
                        TTDay(fridaySubjectsCode,fridaySubjects,periodFri);
                    } else if (selectedDay.equals("Saturday")) {
                        TTDay(saturdaySubjectsCode,saturdaySubjects,periodSat);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // kuch nhi karna
                }
            });

        });
    }

    private void TTDay(List<String> SubjectsCode,List<String> Subjects, List<String> period){
        TTAdapter attAdapter = new TTAdapter(SubjectsCode, Subjects, period);
        LinearLayoutManager layoutManager_d = new LinearLayoutManager(MainActivity.this);
        TTRecyclerView.setLayoutManager(layoutManager_d);
        TTRecyclerView.setAdapter(attAdapter);
        TextView preText = findViewById(R.id.preText3);
        if (attAdapter.getItemCount() > 0) {
            preText.setVisibility(View.GONE);
            TTRecyclerView.setVisibility(View.VISIBLE);
        } else {
            preText.setVisibility(View.VISIBLE);
            TTRecyclerView.setVisibility(View.GONE);
        }
    }

    public static void parseSubjectMap(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements rows = doc.select("div.card-body table.mb-0 tbody tr");

        for (Element row : rows) {
            Elements cells = row.select("td");

            if (cells.size() == 2) {
                String subjectCode = cells.get(0).text().trim();
                String subjectDescription = cells.get(1).text().trim();
                Log.d("Sub Code", subjectCode);
                Log.d("Sub name", subjectDescription);

                subjectMap.put(subjectCode, subjectDescription);
            }
        }
    }
    public void parseMark(String htmlContent) {

        List<String> sub_code = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        List<String> mark = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);

        Element tableBody = doc.select("tbody").first();

        if (tableBody != null) {
            sub_code.clear();
            sub.clear();
            mark.clear();
            Elements rows = tableBody.select("tr");

            for (Element row : rows) {
                Elements cells = row.select("td");

                if (cells.size() >= 3) {
                    String subjectCode = cells.get(0).text();
                    String marks = cells.get(2).text();
                    String subject = cells.get(1).text();
                    sub_code.add(subjectCode);
                    sub.add(subject);
                    mark.add(marks);

                    Log.d("Subject", "Subject Code: " + subjectCode + ", Marks: " + marks+"Subject :"+subject);
                }
            }
        }
        runOnUiThread(() -> {
            if (MarksRecyclerView.getAdapter() != null) {
                MarksRecyclerView.getAdapter().notifyDataSetChanged();
            }
            MarkAdapter attAdapter = new MarkAdapter(sub_code, sub, mark);
            LinearLayoutManager layoutManager_d = new LinearLayoutManager(MainActivity.this);
            MarksRecyclerView.setLayoutManager(layoutManager_d);
            MarksRecyclerView.setAdapter(attAdapter);
            TextView textView = findViewById(R.id.preText2);
            if (attAdapter.getItemCount() > 0) {
                MarksRecyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                MarksRecyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void parseName(String html) {
        Document doc = Jsoup.parse(html);

        Element footer = doc.selectFirst(".sidenav-footer-content");

        if (footer != null) {

            Elements subtitles = footer.select(".sidenav-footer-subtitle");
            if (subtitles.size() >= 2) {
                String registerNumber = subtitles.get(0).text();
                String name = subtitles.get(1).text();
;
                TextView name_= findViewById(R.id.name);
                TextView name2 = findViewById(R.id.name2);
                name2.setText("Name : "+name+"\nReg No. : "+registerNumber);
                name_.setText("Name : "+name+"\nReg No. : "+registerNumber+"\n\nLast Updated : "+lastUpdated);
                editor.putString("myKey_n", html);
                editor.apply();
            } else {
                System.out.println("Unable to extract name and register number.");
            }
        } else {
            System.out.println("Footer section not found.");
        }


    }
    private void gpaCal(){
        RecyclerView recyclerView = findViewById(R.id.GPARecyclerView);
        List<String> dataList = new ArrayList<>();
        dataList.clear();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        AtomicInteger index = new AtomicInteger(dataList.size());
        for(int i = 1; i <= 3; i++){
            dataList.add(String.valueOf(i));
            index.getAndIncrement();
        }

        GpaAdapter adapter = new GpaAdapter(dataList,recyclerView);
        recyclerView.setAdapter(adapter);

        Button clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(v -> gpaCal());
        Button addBtn = findViewById(R.id.add_sub);
        addBtn.setOnClickListener(view -> {
            index.getAndIncrement();
            dataList.add(new String(String.valueOf(index.get())));
            adapter.notifyItemInserted(dataList.size() - 1);
        });
        Log.d("item count",String.valueOf(adapter.getItemCount()));
        Button calBtn = findViewById(R.id.calculate);
        calBtn.setOnClickListener(v -> {
            int totalGotGradePoint = 0;
            int totalGradePoint = 0;
            for (int i = 0; i < adapter.getItemCount(); i++) {
                String selectedGrade = adapter.getSelectedGrade(i);
                String selectedCredit = adapter.getSelectedCredit(i);

                Log.d("item count",String.valueOf(adapter.getItemCount()));
                Log.d("Selected Values", "Item " + (i + 1) + ": Grade - " + selectedGrade + ", Credit - " + selectedCredit);

                if(!selectedCredit.equals("Credit")){
                    if(selectedGrade.equals("O")){
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*10;
                    } else if (selectedGrade.equals("A+")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*9;
                    } else if (selectedGrade.equals("A")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*8;
                    } else if (selectedGrade.equals("B+")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*7;
                    } else if (selectedGrade.equals("B")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*6;
                    } else if (selectedGrade.equals("C")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*5;
                    } else if (selectedGrade.equals("F")) {
                        totalGotGradePoint = totalGotGradePoint+Integer.parseInt(selectedCredit)*0;
                    }
                    totalGradePoint = totalGradePoint + Integer.parseInt(selectedCredit)*10;
                }else {

                }
            }


            Float gpa;
            if (totalGradePoint != 0) {
                gpa = (Float.valueOf(totalGotGradePoint)/Float.valueOf(totalGradePoint))*10;
            } else {
                gpa = (float) 0;
            }
            Log.d("GPA", String.valueOf(gpa));

            CGPA.setVisibility(View.VISIBLE);
            GPA.setVisibility(View.GONE);
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_cgpa);
            menuItem.setChecked(true);

            LinearLayout gpa_display = findViewById(R.id.gpa_disp);
            gpa_display.setVisibility(View.VISIBLE);
            TextView yourCGPA = findViewById(R.id.yourGPA);

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setMinimumFractionDigits(1);
            decimalFormat.setMaximumFractionDigits(2);
            String formattedGPA = decimalFormat.format(gpa);
            yourCGPA.setText(formattedGPA);
        });

    }
    private void cgpaCal(){
        EditText numOfSem = findViewById(R.id.num_sem);
        EditText currSem = findViewById(R.id.curr_sem);
        EditText currCGPA = findViewById(R.id.curr_cgpa);
        Button calBtn = findViewById(R.id.cal_cgpa);
        Button cal_gpa = findViewById(R.id.cal_gpa_btn);

        cal_gpa.setOnClickListener(v -> {
            CGPA.setVisibility(View.GONE);
            GPA.setVisibility(View.VISIBLE);
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_gpa);
            menuItem.setChecked(true);
        });

        calBtn.setOnClickListener(v -> {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View focusedView = getCurrentFocus();
            if (focusedView != null) {
                inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }

            TextView yourCGPA = findViewById(R.id.yourCGPA);
            if(numOfSem.getText().toString().isEmpty() || currSem.getText().toString().isEmpty() || currCGPA.getText().toString().isEmpty()){
                yourCGPA.setText("0.0");
                LinearLayout cgpa_display = findViewById(R.id.cgpa_disp);
                cgpa_display.setVisibility(View.VISIBLE);
            }else{
                int nSem = Integer.parseInt(numOfSem.getText().toString());
                Float cSem = Float.valueOf(currSem.getText().toString());
                Float cCgpa = Float.valueOf(currCGPA.getText().toString());
                Float cgpa = ((cCgpa*nSem)+cSem)/(nSem+1);
                Log.d("CGPA", String.valueOf(cgpa));
                LinearLayout cgpa_display = findViewById(R.id.cgpa_disp);
                cgpa_display.setVisibility(View.VISIBLE);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setMinimumFractionDigits(1);
                decimalFormat.setMaximumFractionDigits(2);
                String formattedCGPA = decimalFormat.format(cgpa);
                yourCGPA.setText(formattedCGPA);
            }

        });

    }

    private void parseAttendanceData(String htmlContent, boolean isTimeTable) {

        List<String> sub_code = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        List<String> per = new ArrayList<>();
        List<String> max = new ArrayList<>();
        List<String> att = new ArrayList<>();


        Document doc = Jsoup.parse(htmlContent);


        Element table = doc.select("table.table").first();

        if (table != null) {
            Elements rows = table.select("tr");
            sub_code.clear();
            sub.clear();
            per.clear();
            max.clear();
            att.clear();

            Element r = rows.get(rows.size()-1);
            Elements c = r.select("td");


            String totalCls = c.get(1).text().trim();
            String attCls = c.get(2).text().trim();
            String attPer = c.get(4).text().trim();
            sub_code.add("");
            sub.add("TOTAL");
            per.add(attPer);
            max.add(totalCls);
            att.add(attCls);

            Log.d("Overall",totalCls);
            Log.d("Attended", attCls);

            if(isTimeTable){

                Calendar calendar = Calendar.getInstance();
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String dayOfWeekStr;
                switch (dayOfWeek) {
                    case Calendar.SUNDAY:
                        dayOfWeekStr = "Sunday";
                        break;
                    case Calendar.MONDAY:
                        dayOfWeekStr = "Monday";
                        break;
                    case Calendar.TUESDAY:
                        dayOfWeekStr = "Tuesday";
                        break;
                    case Calendar.WEDNESDAY:
                        dayOfWeekStr = "Wednesday";
                        break;
                    case Calendar.THURSDAY:
                        dayOfWeekStr = "Thursday";
                        break;
                    case Calendar.FRIDAY:
                        dayOfWeekStr = "Friday";
                        break;
                    case Calendar.SATURDAY:
                        dayOfWeekStr = "Saturday";
                        break;
                    default:
                        dayOfWeekStr = "Unknown";
                }

                if(dayOfWeekStr.equals("Monday")){
                    for (String item : mondaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }
                }
                else if (dayOfWeekStr.equals("Tuesday")) {
                    for (String item : tuesdaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }

                }
                else if (dayOfWeekStr.equals("Wednesday")) {
                    for (String item : wednesdaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }

                }
                else if (dayOfWeekStr.equals("Thursday")) {
                    for (String item : thursdaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }

                }
                else if (dayOfWeekStr.equals("Friday")) {
                    for (String item : fridaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }

                }
                else if (dayOfWeekStr.equals("Saturday")) {
                    for (String item : saturdaySubjectsCode) {
                        for (int i = 0; i < rows.size() - 1; i++) {
                            Element row = rows.get(i);
                            Elements cells = row.select("td");

                            if (cells.size() >= 1) {
                                String courseCode = cells.get(0).text().trim();
                                String courseDescription = cells.get(1).text().trim();
                                String attendancePercentage = cells.get(5).text().trim();
                                String maxClass = cells.get(2).text().trim();
                                String attClass = cells.get(3).text().trim();

                                if(item.equals(courseCode)){
                                    sub_code.add(courseCode);
                                    sub.add(courseDescription);
                                    per.add(attendancePercentage);
                                    max.add(maxClass);
                                    att.add(attClass);
                                }
                            }
                        }
                    }

            }
            }
            else {
                for (int i = 0; i < rows.size() - 1; i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    if (cells.size() >= 1) {
                        String courseCode = cells.get(0).text().trim();
                        String courseDescription = cells.get(1).text().trim();
                        String attendancePercentage = cells.get(5).text().trim();
                        String maxClass = cells.get(2).text().trim();
                        String attClass = cells.get(3).text().trim();


                        sub_code.add(courseCode);
                        sub.add(courseDescription);
                        per.add(attendancePercentage);
                        max.add(maxClass);
                        att.add(attClass);

                        Log.d("Sub Code", courseCode);
                        Log.d("Sub", courseDescription);
                        Log.d("Per", attendancePercentage);


                    }
                }
            }
        }

        runOnUiThread(() -> {
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            AttAdapter attAdapter = new AttAdapter(sub_code, sub, per,max,att,isScholarship);
            LinearLayoutManager layoutManager_d = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager_d);
            recyclerView.setAdapter(attAdapter);
            preText = findViewById(R.id.preText);
            if (attAdapter.getItemCount() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                all_tt_switch.setVisibility(View.VISIBLE);
                preText.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                all_tt_switch.setVisibility(View.GONE);
                preText.setVisibility(View.VISIBLE);
            }


        });
    }
    private void showCustomDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        Configuration config = new Configuration(getResources().getConfiguration());
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_NO;
        dialog.getContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText usernameEditText = dialog.findViewById(R.id.editTextUsername);
        EditText passwordEditText = dialog.findViewById(R.id.editTextPassword);
        Button saveButton = dialog.findViewById(R.id.buttonSave);
        Button cancelButton =dialog.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(v1 -> dialog.dismiss());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = usernameEditText.getText().toString();
                editor.putString("saved_uid", userID);
                editor.apply();
                password = passwordEditText.getText().toString();
                editor.putString("saved_pass", password);
                editor.apply();
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if(webView.getVisibility()==View.VISIBLE){
                webView.setVisibility(View.GONE);
                webView.clearCache(true);
                parent.setVisibility(View.VISIBLE);
                webView.clearCache(true);

            } else if (parent.getVisibility()==View.GONE) {
                three_lines.setVisibility(View.VISIBLE);
                parent.setVisibility(View.VISIBLE);
                MarkLayout.setVisibility(View.GONE);
                CGPA.setVisibility(View.GONE);
                GPA.setVisibility(View.GONE);
                about.setVisibility(View.GONE);
                privPol.setVisibility(View.GONE);
                legal.setVisibility(View.GONE);
                TTLayout.setVisibility(View.GONE);
                settings.setVisibility(View.GONE);
                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.nav_home);
                menuItem.setChecked(true);
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }
    private void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Privacy Policy");
        builder.setMessage("By using this app, you agree to our privacy policy. We do not store any user data.");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPreferences.edit().putBoolean("first_time", false).apply();
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
    private class FetchJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    String version = jsonObject.getString("version");
                    String link = jsonObject.getString("link");
                    boolean isSkip = jsonObject.getBoolean("is_skip");
                    share_url = jsonObject.getString("share_url");

                    if (!AppConfig.version.equals(version)) {
                        showUpdateDialog(link, isSkip);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }
    private void showUpdateDialog(String link,boolean isSkip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("A newer version is available. Do you want to update?");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (link != null && !link.isEmpty()) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                    startActivity(intent);


                    new DownloadApkTask().execute(link);
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            finishAffinity();
//                        }
//                    }, 500);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });

        if(isSkip){
            builder.setNeutralButton("Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private class DownloadApkTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String apkUrl = params[0];
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SRMTracker.apk");
                if (apkFile.exists()) {
                    String fileName = apkFile.getName();
                    String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
                    String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
                    String newFileName = fileNameWithoutExtension + "_" + System.currentTimeMillis() + fileExtension;
                    apkFile = new File(apkFile.getParent(), newFileName);
                }

                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(apkFile);

                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                return apkFile.getAbsolutePath();

            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(params[0]));
                startActivity(intent);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String filePath) {
            super.onPostExecute(filePath);
            progressDialog.dismiss();
            if (filePath != null) {
                installApk(filePath);
            } else {
            }
        }
    }

    private void installApk(String filePath) {
        File apkFile = new File(filePath);

        Uri apkUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", apkFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

}
