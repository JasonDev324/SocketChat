package io.tanjundang.chat.me;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.MomentsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/11
 * @Description: 朋友圈
 */

public class MomentsActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springView)
    SpringView springView;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    MomentsAdapter mAdapter;

    ArrayList<MomentsResp.MomentsItemInfo> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("朋友圈");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.ic_menu_camera);
        mAdapter = new MomentsAdapter(this, R.layout.list_item_moments, list);
        recyclerView.addItemDecoration(new ItemDivider(R.color.divider_color_gray, ItemDivider.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new RotationHeader(this));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    @OnClick({R.id.ivRight})
    public void onClick(View v) {
        if (v.equals(ivRight)) {
            AddMomentsActivity.Start(this);
        }
    }

    private void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getMoments("mine")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<MomentsResp>() {
                    @Override
                    public void onSuccess(MomentsResp resp) {
                        dialog.dismiss();
                        if (resp.isSuccess()) {
                            list.clear();
                            MomentsResp.MomentsInfo info = resp.getData();
                            if (info == null) return;

                            if (info.getData() != null & !info.getData().isEmpty()) {
                                list.addAll(info.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Functions.toast(resp.getMsg());
                        }
                        springView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        Functions.toast(error);
                        springView.onFinishFreshAndLoad();
                    }
                });
    }

    public class MomentsAdapter extends CommonRecyclerViewAdapter<MomentsResp.MomentsItemInfo> {

        public MomentsAdapter(Context context, int layoutId, ArrayList list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, MomentsResp.MomentsItemInfo data, int pos) {
            holder.setText(R.id.tvName, data.getContent(), null)
                    .setText(R.id.tvContent, data.getContent(), null)
                    .setText(R.id.tvDate, data.getCreated_at(), null);
        }

    }
}
