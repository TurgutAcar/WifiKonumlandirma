package com.example.findme.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.findme.R;

public class CustomView extends View {
    private double globalX,globalY;

    public CustomView(Context context) {

        super(context);
        init(null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context);
        init(attrs);
    }

    public CustomView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context,AttributeSet attrs,int defStyleAttr,int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set){


    }
    public void setCoordinates(double x,double y){
        this.globalX = x;
        this.globalY = y;
//use globalX and globalY in `onDraw()` method
        invalidate();
    }
    protected void onDraw(Canvas canvas)
    {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.harita2);
        canvas.drawBitmap(bitmap,0,0,null);

        int x=30;
        int y=30;
        int radius=30;
        //MainActivity  mainActivity=new MainActivity();
        Paint paint=new Paint();

        paint.setColor(Color.parseColor("#CD5C5C"));
       // System.out.println("global1="+globalX+" "+"global2="+globalY);
        canvas.drawCircle((float)globalX,(float)globalY,radius,paint);
        // canvas.drawColor(Color.);
    }


}
