package com.example.mouse.task;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Random;

import static com.example.mouse.task.R.id.btn_input_text;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    private static final String TAG = "MainActivity";
    private RelativeLayout layoutMenuRight;
    int i = 0;
    float viewRotation;
    double fingerRotation;
    double newFingerRotation;
    FloatingActionButton fab_locate_chosen;
    FloatingActionButton fab_image_open;
    FloatingActionButton fab_color_chosen;
    FloatingActionButton fab_undo;
    ImageView img_view;
    RelativeLayout layoutColorRed;
    RelativeLayout layoutColorPink;
    RelativeLayout layoutColorBlue;
    RelativeLayout layoutColorGreen;
    RelativeLayout layoutColorOrange;
    RelativeLayout layoutColorBlack;
    RelativeLayout layoutColorYellow;
    RelativeLayout layoutColorViolet;
    boolean colorMenu = true;
    private static int RESULT_LOAD_IMAGE = 1;
    int x=0,y=0;
    FrameLayout layout_img_view;
    FrameLayout mFrame;
    TextView tv;
    EditText edt_input_text;
    Button btn_cancel_text;
    Button btn_oke_text;
    String text;
    boolean addText = false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fabColorChosen();
        fabImageOpen();
        fabTextOnImage();
        fabUndo();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        Resources res = getResources(); //resource handle
        //khoi tao cac FAB
        fab_locate_chosen = (FloatingActionButton) findViewById(R.id.fab_locate_chosen);
        fab_image_open = (FloatingActionButton) findViewById(R.id.fab_image_open);
        fab_color_chosen = (FloatingActionButton) findViewById(R.id.fab_color_chosen);
        fab_undo = (FloatingActionButton) findViewById(R.id.fab_back_chosen);
        // khoi tao cac RelativeLayoutColor
        layoutColorRed = (RelativeLayout) findViewById(R.id.color1);
        layoutColorPink = (RelativeLayout) findViewById(R.id.color2);
        layoutColorBlue = (RelativeLayout) findViewById(R.id.color3);
        layoutColorGreen = (RelativeLayout) findViewById(R.id.color4);
        layoutColorOrange = (RelativeLayout) findViewById(R.id.color5);
        layoutColorBlack = (RelativeLayout) findViewById(R.id.colo6);
        layoutColorYellow = (RelativeLayout) findViewById(R.id.color7);
        layoutColorViolet = (RelativeLayout) findViewById(R.id.color8);

        layoutColorRed.setOnClickListener(this);
        layoutColorPink.setOnClickListener(this);
        layoutColorBlue.setOnClickListener(this);
        layoutColorGreen.setOnClickListener(this);
        layoutColorOrange.setOnClickListener(this);
        layoutColorBlack.setOnClickListener(this);
        layoutColorYellow.setOnClickListener(this);
        layoutColorViolet.setOnClickListener(this);
        //khoi tao ImageView
        img_view = (ImageView) findViewById(R.id.img_view);
        layout_img_view = (FrameLayout) findViewById(R.id.layout_img_view);
        layoutMenuRight = (RelativeLayout) findViewById(R.id.layoutMenuRight);
        //edt = new EditText(MainActivity.this);
        mFrame = new FrameLayout(MainActivity.this);
        Drawable drawable = res.getDrawable(R.drawable.circle_bg3); //new Image that was added to the res folder
        layoutMenuRight.setBackground(drawable);
        layoutMenuRight.setOnTouchListener(this);
    }

    private void fabImageOpen(){
        fab_image_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null  != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("picturePath", picturePath).commit();
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.img_view);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }


    private void fabTextOnImage(){

        fab_locate_chosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText = false;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.input_text_dialog, null);
                dialogBuilder.setView(dialogView);

                edt_input_text = (EditText) dialogView.findViewById(R.id.edt_input_text);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                btn_oke_text =(Button) dialogView.findViewById(R.id.btn_input_text);
                btn_oke_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        text = edt_input_text.getText().toString();
                        alertDialog.cancel();
                        img_view.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if(addText == false){
                                    x=(int) motionEvent.getX();
                                    y=(int) motionEvent.getY();
                                    imageShowText();
                                    addText = true;
                                }

                                return true;
                            }
                        });
                    }
                });

                btn_cancel_text = (Button) dialogView.findViewById(R.id.btn_cancel_dialog);
                btn_cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });

    }

    private void imageShowText(){
        if(x!=0 && y!=0)
        {
            FrameLayout mFrame=new FrameLayout(MainActivity.this);
            tv=new TextView(MainActivity.this);
            FrameLayout.LayoutParams mParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mFrame.setLayoutParams(mParams);
            mFrame.setPadding(x, y, 0, 0);
            tv.setLayoutParams(mParams);
            tv.setText(text);
            tv.setTextSize(20);
            mFrame.addView(tv);
            layout_img_view.addView(mFrame);

        }
    }


    private void fabColorChosen(){
        fab_color_chosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorMenu == false){
                    ColorShow();
                    colorMenu = true;
                }else
                {
                    ColorHide();
                    colorMenu = false;
                }

            }
        });
    }

    private void fabUndo(){
        fab_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == R.id.layoutMenuRight){
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();

            final float xc = layoutMenuRight.getWidth()/2;
            final float yc = layoutMenuRight.getHeight()/2;
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    viewRotation = layoutMenuRight.getRotation();
                    fingerRotation = Math.toDegrees(Math.atan2(x - xc, yc - y));
                    Log.e(TAG,"down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    newFingerRotation = Math.toDegrees(Math.atan2(x - xc, yc - y));
                    layoutMenuRight.setRotation((float)(viewRotation + newFingerRotation - fingerRotation));

                    break;
                case MotionEvent.ACTION_UP:
                    Log.e(TAG,"up");
                    fingerRotation = newFingerRotation = 0.0f;
                    break;

            }
        }
        return true;
    }

    private void ColorShow(){
        layoutMenuRight.setVisibility(View.VISIBLE);
    }

    private void ColorHide(){
        layoutMenuRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.color1:
                tv.setTextColor(getResources().getColor(R.color.colorRed));
                break;
            case R.id.color2:
                tv.setTextColor(getResources().getColor(R.color.colorHotPink));
                break;
            case R.id.color3:
                tv.setTextColor(getResources().getColor(R.color.colorDeepSkyBlue));
                break;
            case R.id.color4:
                tv.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case R.id.color5:
                tv.setTextColor(getResources().getColor(R.color.colorOrange));
                break;
            case R.id.colo6:
                tv.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.color7:
                tv.setTextColor(getResources().getColor(R.color.colorYellow));
                break;
            case R.id.color8:
                tv.setTextColor(getResources().getColor(R.color.colorBlueViolet));
                break;
        }
    }
}
