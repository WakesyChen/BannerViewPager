package com.example.hzday7_4_bannerviewpager;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BannerViewpager extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout ll;
    private int btnNUm=5;
    private int Imgs[]={R.mipmap.img_1,R.mipmap.img_2,R.mipmap.img_3,R.mipmap.img_4,R.mipmap.img_5};
    private MyPagerAdapter myPagerAdapter;
    private List<ImageView>mlist;
    private  List<ImageView>imgList;
    private MyAsynctask async;
    private boolean isStop=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==123){
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_viewpager);
        initView();
        initData();
       /* //方法一：使用handler更新UI
        new Thread(new Runnable() {
            @Override

            public void run() {
                while (!isStop) {
                    try {
                        Thread.sleep(2500);
                     handler.sendEmptyMessage(123);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
*/
        //方法二：使用runOnUIThread更新UI，无法使用义务人无，因为无法反复执行更新UI的操作
       new Thread(new Runnable() {
           @Override
           public void run() {
               while(!isStop){
                   try {
                       Thread.sleep(2500);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

                       }
                   });

               }


           }
       }).start();
    }

    private void initData() {

        //初始化数据源以及底部按键
        for (int i = 0; i < btnNUm; i++) {
            ImageView imageView=new ImageView(this);
            imageView.setImageResource(Imgs[i]);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mlist.add(imageView);//viewpager中添加图片

            ImageView imageView2=new ImageView(this);
            imageView2.setEnabled(true);
            imageView2.setImageResource(R.drawable.selector);
            imageView2.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            imgList.add(imageView2);//        把底部按键添加到List中
            ll.addView(imageView2);//轮播按键中添加图片

        }

        ImageView img0= imgList.get(0);//设置第一个按键为选中
        img0.setEnabled(false);
        myPagerAdapter=new MyPagerAdapter(mlist);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < Imgs.length; i++) {
                    ImageView img1= imgList.get(i);
                    img1.setEnabled(true);//先设置所有按键状态为未选中

                }
                ImageView img= imgList.get(position%mlist.size());//viewpager切换界面，按键也跟着切换
                img.setEnabled(false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //自动轮播不能再用点击事件，不然很多bug
     /*   //给底部图片设置点击事件
        for (int i = 0; i < Imgs.length; i++) {
            final ImageView imgB =imgList.get(i);

           //把当前Imageview的位置传到点击方法中
            Log.i("tag", i + "di");
            imgB.setTag(i);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Toast.makeText(BannerViewpager.this, "我是第" + position, Toast.LENGTH_SHORT).show();

//                    ImageView img2= (ImageView) ll.getChildAt(position);
                    //v.setEnabled(true);//设置点击的按键为true;
                    v.setEnabled(false);
                    viewPager.setCurrentItem(position%mlist.size());
                }
            });

        }*/

    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        ll= (LinearLayout) findViewById(R.id.ll);
        mlist=new ArrayList<>();
        imgList=new ArrayList<>();


    }
    class  MyAsynctask extends AsyncTask<Void,Void,Integer>{
        private  int position;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer  doInBackground(Void... params) {
                new Thread(new Runnable() {
                    @Override

                    public void run() {
                        while (!isStop) {
                            try {
                                position = viewPager.getCurrentItem();
                                Thread.sleep(1000);
                                Log.i("position", "页面" + position);
                                position++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }).start();


            return position;
        }

        @Override
        protected void onPostExecute(Integer position) {
            super.onPostExecute(position);
            viewPager.setCurrentItem(position % mlist.size());
        }
    }

    @Override
    public boolean isDestroyed() {//退出时停止线程
        isStop=true;
        return super.isDestroyed();
    }
}
