package com.example.testviewpager;

import android.app.Activity;
import android.app.Application;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    private static TextView mtextview;
    private static MyView myView;
    private ExecutorService mthread;
    private MyHandler mhandler;
    private volatile static FirstFragment firstFragment;
    private boolean firstcreate = true;
    private View mview;
    private int n;
    private Point[] ps;
    public FirstFragment(Point[] ps,int n) {
        this.ps = ps;
        this.n = n;
    }


    public static FirstFragment newInstance(Point[] ps,int n) {

        if(null == firstFragment)
        {
            synchronized (FirstFragment.class){
                if(null == firstFragment){
                    firstFragment = new FirstFragment(ps,n);
                }
            }
        }


        return firstFragment;
    }
    public static class MyHandler extends Handler {
        //弱引用
        WeakReference<Activity> weakReference;
        public MyHandler(Activity activity) {
            weakReference = new WeakReference<Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Activity activity = weakReference.get();
            if (activity != null&&msg!=null) {
                //mtextview.setText(String.valueOf(msg.arg1));
                switch (msg.what){
                    case 0x002:
                        Bundle bundle = msg.getData();
                        double x1 = bundle.getDouble("x1");
                        double y1 = bundle.getDouble("y1");
                        double x2 = bundle.getDouble("x2");
                        double y2 = bundle.getDouble("y2");
                        myView.setX1((int)x1);
                        myView.setY1((int)y1);
                        myView.setX2((int)x2);
                        myView.setY2((int)y2);
                        System.out.println("设置");
                        break;
                    case 0x003:
                        mtextview.setText("完成！\n最短距离为"+String.valueOf(
                                msg.getData().getDouble("distance"))+"\n"+
                                "坐标：x1="+String.valueOf(myView.getX1())+
                                "   y1="+String.valueOf(myView.getY1())+"\n"+
                                        "坐标：x2="+String.valueOf(myView.getX2())+
                                        "   y2="+String.valueOf(myView.getY2())+"\n"
                                );
                        break;
                }

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (firstcreate) {
            System.out.println("onCreate");

            mthread = Executors.newSingleThreadExecutor();
            mhandler = new MyHandler(getActivity());
            Test test = new Test(mhandler);
            //go();
            mthread.execute(new Runnable() {
                @Override
                public void run() {
                    test.main(ps,n);
                }
            });
            firstcreate = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null!=mview){
            ((ViewGroup)mview.getParent()).removeView(mview);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mview == null){
            mview = inflater.inflate(R.layout.fragment1_layout, container, false);
        }
        return mview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtextview = view.findViewById(R.id.text1);
        myView = view.findViewById(R.id.view1);
        myView.setPs(ps);
    }

    public class MyRunnable implements Runnable{
        private int flag;
        MyRunnable(int i){
            this.flag = i;
        }
        @Override
        public void run() {
            try {

                while (true){
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what=1;
                    message.arg1=flag;
                    mhandler.sendMessage(message);
                    flag++;
                    System.out.println(Thread.currentThread()+String.valueOf(flag));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void go(){
        System.out.println("开始");
        int i=0;
        mthread.execute(new MyRunnable(i));
    }
}