package com.example.testviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    private Point[] ps;
    private int x1=100;
    private int y1=100;
    private int x2=200;
    private int y2=200;
    public void setX1(int x){
        this.x1 = x;
    }
    public void setY1(int y){
        this.y1 = y;
    }
    public void setX2(int x){
        this.x2 = x;
    }
    public void setY2(int y){
        this.y2 = y;
    }
    public int getX1(){
        return x1;
    }
    public int getY1(){
        return y1;
    }
    public int getX2(){
        return x2;
    }
    public int getY2(){
        return y2;
    }
    public MyView(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public void setPs(Point[] ps) {
        this.ps = ps;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint0 = new Paint();
        paint0.setColor(Color.parseColor("#969696"));
        paint0.setStrokeWidth(10);
        Paint paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint1.setStrokeWidth(25);
        Paint paint2 = new Paint();
        paint2.setColor(Color.GREEN);
        paint2.setStrokeWidth(25);
        for(int i=0;i<100;i++){
            //canvas.drawPoint((int)ps[i].x,(int)ps[i].y,paint0);
            canvas.drawCircle((int)ps[i].x,(int)ps[i].y,10,paint0);
        }
        //canvas.drawPoint(x1,y1,paint1);
        canvas.drawCircle(x1,y1,10,paint1);
        //canvas.drawPoint(x2,y2,paint2);
        canvas.drawCircle(x2,y2,10,paint2);
        invalidate();
        //System.out.println("draw"+x+" "+y);
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        canvas.drawPoint(1000,1000,paint);
//        canvas.drawCircle(100,100,50,paint);
//        invalidate();
//        System.out.println("draw");
//    }
}
