package com.example.owner.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import static butterknife.ButterKnife.findById;

public class MainActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.btnSearch) Button btnSearch;

    // The URL for the books
    public static final String BEGINNING_OF_URL = "http://content.guardianapis.com/search?q=";
    public static final String END_OF_URL = "&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSearch)
    public void submit(){
        String searchString = "";
        EditText edtSearchString = (EditText) findViewById(R.id.edtSearchString);
        searchString = edtSearchString.getText().toString();
        String URLstring = BEGINNING_OF_URL + searchString + END_OF_URL;
        Log.i("LOG","Inside onClickListener " + URLstring);

        Intent mIntent = new Intent(MainActivity.this, NewsActivity.class);
        mIntent.putExtra("urlString", URLstring);
        startActivity(mIntent);
    }
}
