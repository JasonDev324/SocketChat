package io.tanjundang.chat.me;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.Global;

/**
 * @Author: TanJunDang
 * @Date: 2017/10/17
 * @Description: 个人资料
 */

public class MeFragment extends Fragment {

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvAlbum)
    TextView tvAlbum;
    @BindView(R.id.tvWallet)
    TextView tvWallet;
    @BindView(R.id.tvFriends)
    TextView tvFriends;
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;

    Unbinder unbinder;

    public static MeFragment getInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvName.setText(Global.getInstance().getNickname());
        tvEmail.setText(Global.getInstance().getEmail());
        return view;
    }


    @OnClick({R.id.ivAvatar, R.id.tvAlbum,
            R.id.tvWallet, R.id.tvFriends,
            R.id.tvUpdate})
    public void onClick(View v) {
        if (v.equals(ivAvatar)) {

        } else if (v.equals(tvAlbum)) {

        } else if (v.equals(tvWallet)) {

        } else if (v.equals(tvFriends)) {

        } else if (v.equals(tvUpdate)) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
