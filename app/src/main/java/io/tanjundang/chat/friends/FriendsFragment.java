package io.tanjundang.chat.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.FriendsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.view.CommonHolder;
import io.tanjundang.chat.base.view.HeaderAdapter;
import io.tanjundang.chat.base.view.ItemDivider;
import io.tanjundang.chat.me.PersonalInfoActivity;

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
    TextView tvNewFriend;
    TextView tvGroupChat;

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
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        dialog.show();
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new FriendsAdapter(getContext(), R.layout.list_item_friends, list);
        ItemDivider divider = new ItemDivider(ContextCompat.getColor(getContext(), R.color.divider_color_gray), ItemDivider.HORIZONTAL, Functions.dp2px(1));
        divider.setHeaderEnable(true);
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
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
        mAdapter.setHeaderView(R.layout.list_item_header_friends);
        tvNewFriend = ButterKnife.findById(mAdapter.getHeaderView(), R.id.tvNewFriend);
        tvGroupChat = ButterKnife.findById(mAdapter.getHeaderView(), R.id.tvGroupChat);
        tvNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewFriendActivity.Start(getContext());
            }
        });
        tvGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupChatActivity.Start(getContext());
            }
        });
    }

    private void getData() {
        HttpReqTool.getInstance().createApi(BusinessApi.class)
                .getFriends()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<FriendsResp>() {
                    @Override
                    public void onSuccess(FriendsResp resp) {
                        dialog.dismiss();
                        if (resp.isSuccess()) {
                            list.clear();
                            if (resp.getData() != null & !resp.getData().isEmpty()) {
                                list.addAll(resp.getData());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class FriendsAdapter extends HeaderAdapter<FriendsResp.FriendsInfo> {
        public FriendsAdapter(Context context, int layoutId, ArrayList<FriendsResp.FriendsInfo> list) {
            super(context, layoutId, list);
        }

        @Override
        public void convert(CommonHolder holder, final FriendsResp.FriendsInfo data, int pos) {
            holder.setImageResource(R.id.ivAvatar, null)
                    .setText(R.id.tvName, data.getName(), null)
                    .itemClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PersonalInfoActivity.Start(getContext(), data);
                        }
                    });
        }
    }

}


