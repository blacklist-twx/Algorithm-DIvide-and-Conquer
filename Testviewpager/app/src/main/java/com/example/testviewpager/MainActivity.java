package com.example.testviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

import com.google.android.material.tabs.TabLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private Point[] ps;
    private int n=100;
    public Point[] initRandom(int n){
        Random r = new Random();

        Point[] ps = new Point[n];
        for (int i = 0; i < n; i++) {
            double x = r.nextInt(1000);
            double y = r.nextInt(1000);
            ps[i] = new Point(x, y,i);
        }
        return ps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            //设置页面延伸到刘海区显示
            getWindow().setAttributes(lp);
        }
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.vp);
        ps = initRandom(n);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new FirstFragment(ps,n).newInstance(ps,n), "分治算法");
        adapter.addFragment(new SecondFragment(ps,n).newInstance(ps,n), "暴力算法");
        //adapter.addFragment(new ThirdFragment().newInstance(), "Tab 3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


}