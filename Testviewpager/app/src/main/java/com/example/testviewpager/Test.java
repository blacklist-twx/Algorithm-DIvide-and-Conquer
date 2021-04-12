package com.example.testviewpager;



import android.os.Bundle;
import android.os.Message;

import static java.lang.Math.*;

import java.util.*;
import java.util.logging.Handler;

public class Test{
    // 坐标点
    private static Point[] ps_copy;
    private static FirstFragment.MyHandler myHandler;
    public Test(FirstFragment.MyHandler mhandler){
        super();
        this.myHandler =mhandler;
    }


    public static void main(Point[] p,int n) {
        //Scanner scanner = new Scanner(System.in);
        Random r = new Random();

        //int n = scanner.nextInt();
        Point[] ps = p.clone();
        ps_copy = ps.clone();
        // 按照X轴坐标升序排序
        Arrays.sort(ps, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.x < o2.x)
                    return -1;
                if (o1.x > o2.x)
                    return 1;
                if (o1.y < o2.y)
                    return -1;
                if (o1.y > o2.y)
                    return 1;
                return 0;
            }
        });
        ArrayList bundle = minDistance(ps, 0, n - 1);
        double minDis = (double)bundle.get(2);
        int x1 = (int)bundle.get(0);
        int x2 = (int)bundle.get(1);
        System.out.println("分治结果"+minDis+" "+ps_copy[x1].x+" "+ps_copy[x1].y
                +" "+ps_copy[x2].x+" "+ps_copy[x2].y);
        System.out.println("分治结果"+distance(ps_copy[x1],ps_copy[x2]));
        Message message = new Message();
        message.what=0x003;
        Bundle bundle1 = new Bundle();
        bundle1.putDouble("distance",minDis);
        message.setData(bundle1);
        myHandler.sendMessage(message);



    }

    /**
     * 点对之间的最小距离
     *
     * @param ps
     * @param l
     * @param r
     * @return
     */
    public static ArrayList minDistance(Point[] ps, int l, int r) {
        /**
         * 同一个点,不存在点对,距离不能取0,返回最大值
         */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (l == r) {
            ArrayList a = new ArrayList();
            a.add(ps[l].index);
            a.add(ps[r].index);
            a.add(Double.MAX_VALUE);
            return a;
        }
        if (l + 1 == r) {
            ArrayList a = new ArrayList();
            a.add(ps[l].index);
            a.add(ps[r].index);
            a.add(distance(ps[l], ps[r]));
            return a;
        }
        int center = l + (r - l) / 2;
        ArrayList bundle1 = minDistance(ps, l, center);
        ArrayList bundle2 = minDistance(ps, center + 1, r);
        double dis1 = (double)bundle1.get(2);
        double dis2 = (double)bundle2.get(2);
        int a1,a2,b1,b2;
        a1 = (int)bundle1.get(0);
        a2 = (int)bundle1.get(1);
        b1 = (int)bundle2.get(0);
        b2 = (int)bundle2.get(1);
        int m1,m2;
        double minDis = min(dis1, dis2);
        if(minDis==dis1)
        {
            m1 = a1;
            m2 = a2;
        }
        else{
            m1 = b1;
            m2 = b2;
        }

        ArrayList<Point> nearPoints = new ArrayList<>();
        // 选出距离中间线小于minDis的点
        for (Point p : ps) {
            if (abs(ps[center].x - p.x) <= minDis) {
                nearPoints.add(p);
            }
        }
        // 按照Y轴升序排序
        Collections.sort(nearPoints, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.y < o2.y)
                    return -1;
                if (o1.y > o2.y)
                    return 1;
                if (o1.x < o2.x)
                    return -1;
                if (o1.x > o2.x)
                    return 1;
                return 0;
            }
        });
        for (int i = 0; i < nearPoints.size(); i++) {
            for (int j = i + 1; j < nearPoints.size(); j++) {
                if (nearPoints.get(j).y - nearPoints.get(i).y > minDis) {
                    break;// 元素y+1离元素i更远,没必要继续比较
                }
                double d = distance(nearPoints.get(j), nearPoints.get(i));
                if (d < minDis) {
                    minDis = d;
                    m1 = nearPoints.get(j).index;
                    m2 = nearPoints.get(i).index;
                }
            }
        }
        Message msg = new Message();
        Bundle bundle  = new Bundle();
        bundle.putDouble("x1",ps_copy[m1].x);
        bundle.putDouble("y1",ps_copy[m1].y);
        bundle.putDouble("x2",ps_copy[m2].x);
        bundle.putDouble("y2",ps_copy[m2].y);

        msg.setData(bundle);
        msg.what=0x002;
        myHandler.sendMessage(msg);
        ArrayList arrayList = new ArrayList();
        arrayList.add(m1);
        arrayList.add(m2);
        arrayList.add(minDis);
        return arrayList;
    }

    public static double distance(Point p1, Point p2) {
        if (p1 == p2)
            return 0;
        return sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2));
    }

}
