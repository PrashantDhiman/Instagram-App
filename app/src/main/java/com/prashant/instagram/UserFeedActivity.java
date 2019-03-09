package com.prashant.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageView imageView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        Intent intent=getIntent();
        String activeUserName=intent.getStringExtra("username");

        setTitle(activeUserName+"'s Feed");

        linearLayout=findViewById(R.id.linearLayout);
        scrollView=(ScrollView) findViewById(R.id.scrollView);

        fetchImages(activeUserName);

    }

    public void fetchImages(String username){

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Image");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            ParseFile file=(ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null && data!=null){
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                        showImages(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    public void showImages(Bitmap imageBitmap){

        imageView=new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        imageView.setImageBitmap(imageBitmap);
        linearLayout.addView(imageView);

    }

}
