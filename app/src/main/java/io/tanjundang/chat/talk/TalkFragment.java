package io.tanjundang.chat.talk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.tanjundang.chat.R;

public class TalkFragment extends Fragment {

    public static TalkFragment getInstance() {
        TalkFragment fragment = new TalkFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

}
