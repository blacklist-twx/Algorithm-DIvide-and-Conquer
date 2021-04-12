package com.example.testviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    private volatile static SecondFragment secondFragment;
    private static TextView mtextview;
    private static MyView myView;
    private ExecutorService mthread;
    private MyHandler mhandler;
    private boolean firstcreate = true;
    private View mview;
    private int n ;
    private Point[] ps;

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
                        System.out.println("设置2"+myView.getX1());
                        break;
                    case 0x003:
                        Bundle b = msg.getData();
                        mtextview.setText("完成！最短距离是"+String.valueOf(b.get("min"))+"\n"+
                                "坐标：x1="+String.valueOf(b.getDouble("x1"))+
                                        "   y1="+String.valueOf(b.getDouble("y1"))+"\n"+
                                        "坐标：x2="+String.valueOf(b.getDouble("x2"))+
                                        "   y2="+String.valueOf(b.getDouble("y2"))+"\n");
                        myView.setX1((int)b.getDouble("x1"));
                        myView.setY1((int)b.getDouble("y1"));
                        myView.setX2((int)b.getDouble("x2"));
                        myView.setY2((int)b.getDouble("y2"));
                        break;
                }

            }
        }
    }
    public SecondFragment(Point[] ps,int n) {
        this.ps = ps;
        this.n = n;
    }


    public static SecondFragment newInstance(Point[] ps,int n) {

        if(null == secondFragment)
        {
            synchronized (FirstFragment.class){
                if(null == secondFragment){
                    secondFragment = new SecondFragment(ps,n);
                }
            }
        }
        return secondFragment;
    }
    public ArrayList findmin(double[][] d, int n){
        double min = Double.POSITIVE_INFINITY;
        int x=0;int y=0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(d[i][j]<min&&i!=j)
                {
                    min = d[i][j];
                    x=i;
                    y=j;
                }
            }
        }
        ArrayList a = new ArrayList();
        a.add(min);
        a.add(x);
        a.add(y);
        return a;
    }
    public void RunEnum(Point[] ps,int n){
        double distance[][] = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                distance[i][j] = Test.distance(ps[i],ps[j]);
                System.out.println(distance[i][j]);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putDouble("x1",ps[i].x);
                bundle.putDouble("y1",ps[i].y);
                bundle.putDouble("x2",ps[j].x);
                bundle.putDouble("y2",ps[j].y);
                msg.what=0x002;
                msg.setData(bundle);
                mhandler.sendMessage(msg);
            }
        }
        ArrayList a = findmin(distance,n);
        double min = (double)a.get(0);
        int x = (int)a.get(1);
        int y = (int)a.get(2);
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putDouble("min",min);
        double x1=ps[x].x;
        double y1=ps[x].y;
        double x2=ps[y].x;
        double y2=ps[y].y;
        bundle.putDouble("x1",x1);
        bundle.putDouble("y1",y1);
        bundle.putDouble("x2",x2);
        bundle.putDouble("y2",y2);
        msg.what=0x003;
        msg.setData(bundle);
        mhandler.sendMessage(msg);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(firstcreate){
            mthread= Executors.newSingleThreadExecutor();
            mhandler = new MyHandler(getActivity());
            //go();
            mthread.execute(new Runnable() {
                @Override
                public void run() {
                    RunEnum(ps,n);
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
            mview = inflater.inflate(R.layout.fragment2_layout, container, false);
        }
        return mview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtextview = view.findViewById(R.id.text2);
        myView = view.findViewById(R.id.view2);
        myView.setPs(ps);
    }
}