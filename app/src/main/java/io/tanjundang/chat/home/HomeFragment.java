package io.tanjundang.chat.home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseFragment;
import io.tanjundang.chat.base.utils.PermissionTool;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/3/28
 */

public class HomeFragment extends BaseFragment   {


    ArrayList<String> permissionList = new ArrayList<>();

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        permissionList.add(Manifest.permission.CAMERA);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.getInstance(getContext()).onRequestPermissionsResult(permissionList, requestCode, permissions, grantResults);
    }

}


