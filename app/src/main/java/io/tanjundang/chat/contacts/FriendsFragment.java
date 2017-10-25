package io.tanjundang.chat.contacts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.CommonRecyclerViewAdapter;
import io.tanjundang.chat.base.view.ItemDivider;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/3/28
 */

public class FriendsFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.springView)
    SpringView springView;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    Unbinder unbinder;

    FriendsAdapter mAdapter;
    ArrayList<FriendsResp.FriendsInfo> list = new ArrayList<>();

    public static FriendsFragment getInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friends, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        springView.callFresh();
        return view;
    }

    private void initView() {
        mAdapter = new FriendsAdapter(getContext(), R.layout.list_item_friends, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new ItemDivider(Color.GRAY, ItemDivider.HORIZONTAL));
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new RotationHeader(getContext()));
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

    @OnClick

    private void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<FriendsResp>() {
                    @Override
                    public void onSuccess(FriendsResp resp) {
                        if (resp.isSuccess()) {
                            list.clear();
                            if (resp.getData() != null & !resp.getData().isEmpty()) {
                                list.addAll(resp.getData());
                            }
                            mAdapter.notifyDataSetChanged();
                            springView.onFinishFreshAndLoad();
                        } else {
                            Functions.toast(resp.getMsg());
                        }

                    }

                    @Override
                    public void onFailure(String error) {
                        Functions.toast(error);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class FriendsAdapter extends CommonRecyclerViewAdapter<FriendsResp.FriendsInfo> {
        public FriendsAdapter(Context context, int layoutId, ArrayList<FriendsResp.FriendsInfo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, FriendsResp.FriendsInfo data, int pos) {
            holder.setImageResource(R.id.ivAvatar, null)
                    .setText(R.id.tvName, data.getName(), null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Functions.toast("进入聊天页面");
                        }
                    });
        }
    }

}


