package com.lemon95.ymtv.view.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lemon95.androidtvwidget.bridge.EffectNoDrawBridge;
import com.lemon95.androidtvwidget.bridge.OpenEffectBridge;
import com.lemon95.androidtvwidget.view.GridViewTV;
import com.lemon95.androidtvwidget.view.ListViewTV;
import com.lemon95.androidtvwidget.view.MainLayout;
import com.lemon95.androidtvwidget.view.MainUpView;
import com.lemon95.ymtv.R;
import com.lemon95.ymtv.adapter.ConditionsAdapter;
import com.lemon95.ymtv.adapter.GridViewAdapter;
import com.lemon95.ymtv.bean.QueryConditions;
import com.lemon95.ymtv.bean.VideoSearchList;
import com.lemon95.ymtv.common.AppConstant;
import com.lemon95.ymtv.presenter.VideoListPresenter;
import com.lemon95.ymtv.utils.LogUtils;
import com.lemon95.ymtv.utils.PreferenceUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends BaseActivity {

    private ListViewTV lemon_video_menu_id;
    private MainUpView mainUpView2;
    OpenEffectBridge mOpenEffectBridge;
    private ProgressBar lemon_movie_details_pro1,lemon_movie_details_pro2;
    private LinearLayout lemon_movie_details_main;
    private boolean isKeyDown = false;
    private VideoListPresenter videoListPresenter = new VideoListPresenter(this);
    private ConditionsAdapter conditionsAdapter;
    private View mOldListView;
    private View mOldGridView;
    private GridViewTV gridView;
    private GridViewAdapter gridViewAdapter;
    private List<VideoSearchList.Data.VideoBriefs> videoList = new ArrayList<>(); //影片
    private ArrayList<QueryConditions> conditionsArrayList;
    private int page = 1;
    private TextView lemon_title;
    private boolean isPage = true; //是否在翻页
    private String totleCount = "0";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void setupViews() {
        initViewMove();
        lemon_video_menu_id = (ListViewTV) findViewById(R.id.lemon_video_menu_id);
        lemon_video_menu_id.setItemsCanFocus(true);
        gridView = (GridViewTV) findViewById(R.id.gridView);
        lemon_movie_details_pro1 = (ProgressBar) findViewById(R.id.lemon_movie_details_pro1);
        lemon_movie_details_pro2 = (ProgressBar) findViewById(R.id.lemon_movie_details_pro2);
        lemon_movie_details_main = (LinearLayout) findViewById(R.id.lemon_movie_details_main);
        lemon_title = (TextView) findViewById(R.id.lemon_title);
        mOpenEffectBridge.setVisibleWidget(true); // 隐藏
        lemon_video_menu_id.requestFocus();
        gridViewAdapter = new GridViewAdapter(videoList,context);
        gridView.setAdapter(gridViewAdapter);
        gridView.setFocusable(false);  //初始化时不让Gridview抢焦点
        lemon_video_menu_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    // 子控件置顶，必需使用ListViewTV才行，
                    // 不然焦点会错乱.
                    // 不要忘记这句关键的话哦.
                    gridView.setFocusable(true);
                    LogUtils.i(TAG, "离开焦点2");
                    view.bringToFront();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        lemon_video_menu_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lemon_video_menu_id.setPoint(position);
                TextView textView = (TextView)view.findViewById(R.id.lemon_video_tv);
                textView.setTextColor(Color.WHITE);
                if (mOldListView != null) {
                    TextView textView2 = (TextView) mOldListView.findViewById(R.id.lemon_video_tv);
                    textView2.setTextColor(getResources().getColor(R.color.lemon_b3aeae));
                } else {
                    TextView textView2 = (TextView)lemon_video_menu_id.getChildAt(0).findViewById(R.id.lemon_video_tv);
                    textView2.setTextColor(getResources().getColor(R.color.lemon_b3aeae));
                }
                mOldListView = view;
                QueryConditions queryConditions = conditionsArrayList.get(position);
                page = 1;
                videoList.clear();
                showPro2();
                mOldGridView = null;
                gridView.setSelection(0);
                mOpenEffectBridge.setVisibleWidget(true); // 隐藏
                mainUpView2.setUnFocusView(gridView.getChildAt(0));
                lemon_title.setText(conditionsArrayList.get(position).getName());
                videoListPresenter.getCombSearch(queryConditions.getAreaId(), queryConditions.getGenreId(), queryConditions.getGroupId(), queryConditions.getChargeMethod(), queryConditions.getVipLevel(), queryConditions.getYear(), queryConditions.getType(), page + "", AppConstant.PAGESIZE);
            }
        });
        lemon_video_menu_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View view = ((ListView)v).getSelectedView();
                TextView textView = (TextView)view.findViewById(R.id.lemon_video_tv);
                if (hasFocus) {
                    mOpenEffectBridge.setVisibleWidget(true);
                    lemon_video_menu_id.setSelector(R.drawable.lemon_liangguang_03);
                   // textView.setTextColor(getResources().getColor(R.color.lemon_b3aeae));
                } else {
                    lemon_video_menu_id.setSelector(R.color.lemon_0E0A0B);
                }
            }
        });
        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 这里注意要加判断是否为NULL.
                 * 因为在重新加载数据以后会出问题.
                 */
               // mOpenEffectBridge.setVisibleWidget(false);
                if (view != null && mOldGridView != null) {
                    view.bringToFront();
                    mainUpView2.setFocusView(view, mOldGridView, 1.1f);
                }
                mOldGridView = view;
                int size = videoList.size();
                if (size - 15 < position && size < Integer.parseInt(totleCount)) {
                    if (isPage) {
                        //翻页
                        page = page + 1;
                        int poi = lemon_video_menu_id.getSelectedItemPosition();
                        QueryConditions queryConditions = conditionsArrayList.get(poi);
                        videoListPresenter.getCombSearch(queryConditions.getAreaId(),queryConditions.getGenreId(),queryConditions.getGroupId(),queryConditions.getChargeMethod(),queryConditions.getVipLevel(),queryConditions.getYear(),queryConditions.getType(),page + "",AppConstant.PAGESIZE);
                        isPage = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoSearchList.Data.VideoBriefs video = videoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("videoId",video.getVideoId());
                bundle.putString("videoType",video.getVideoTypeId());
                startActivity(MovieDetailsActivity.class,bundle);
            }
        });
        gridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.i(TAG, "gridView" + hasFocus);
                if (hasFocus) {
                    mOpenEffectBridge.setVisibleWidget(false);
                    if (mOldGridView == null) {
                        mainUpView2.setFocusView(gridView.getSelectedView(),1.1f);
                        mOldGridView = gridView.getSelectedView();
                    } else {
                        mainUpView2.setFocusView(mOldGridView,1.1f);
                    }
                } else {
                    mainUpView2.setVisibility(View.GONE);
                    mOpenEffectBridge.setVisibleWidget(true); // 隐藏
                    mainUpView2.setUnFocusView(mOldGridView);
                }
            }
        });
    }

    @Override
    protected void initialized() {
        String videoType = getIntent().getStringExtra("videoType");
        TextView lemon_movie_title = (TextView)findViewById(R.id.lemon_movie_title);
        if (AppConstant.MOVICE.equals(videoType)) {
            lemon_movie_title.setText(getString(R.string.lemon95_movie));
        } else if(AppConstant.SERIALS.equals(videoType)) {
            lemon_movie_title.setText("电视剧");
        } else if(AppConstant.FUNNY.equals(videoType)) {
            lemon_movie_title.setText("每日一乐");
        }
        videoListPresenter.getCombQueryConditions(videoType);
    }

    public void showPro() {
        lemon_movie_details_pro1.setVisibility(View.VISIBLE);
        lemon_movie_details_main.setVisibility(View.GONE);
    }

    public void hidePro() {
        lemon_movie_details_pro1.setVisibility(View.GONE);
        lemon_movie_details_main.setVisibility(View.VISIBLE);
        isKeyDown = false;
    }

    public void initViewMove() {
        mainUpView2 = (MainUpView) findViewById(R.id.mainUpView2);
        mOpenEffectBridge = (OpenEffectBridge) mainUpView2.getEffectBridge();
        switchNoDrawBridgeVersion();
    }

    private void switchNoDrawBridgeVersion() {
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(20);
        mainUpView2.setEffectBridge(effectNoDrawBridge); // 4.3以下版本边框移动.
        mainUpView2.setUpRectResource(R.drawable.health_focus_border); // 设置移动边框的图片.
        mainUpView2.setDrawUpRectPadding(new Rect(10, -6, 9, -41)); // 边框图片设置间距.
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
        return isKeyDown;
    }

    /**
     * 显示listView
     * @param conditionsArrayList
     */
    public void showListView(ArrayList<QueryConditions> conditionsArrayList) {
        this.conditionsArrayList = conditionsArrayList;
        conditionsAdapter = new ConditionsAdapter(conditionsArrayList, context);
        lemon_video_menu_id.setAdapter(conditionsAdapter);
        conditionsAdapter.notifyDataSetChanged();
        lemon_title.setText(conditionsArrayList.get(0).getName());
        /*if (lemon_video_menu_id.getChildAt(0) != null) {
            TextView textView = (TextView)lemon_video_menu_id.getChildAt(0).findViewById(R.id.lemon_video_tv);
            textView.setTextColor(Color.WHITE);
        }*/
        hidePro();
    }

    public void showPro2() {
        lemon_movie_details_pro2.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    public void hidePro2() {
        lemon_movie_details_pro2.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    /**
     * 显示gridView
     * @param data
     */
    public void showGridView( VideoSearchList.Data data) {
        isPage = true;
        if (data != null) {
            totleCount = data.getTotalCount();
            videoList.addAll(data.getVideoBriefs());
            gridViewAdapter.notifyDataSetChanged();
            hidePro2();
        }
      //  gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

}