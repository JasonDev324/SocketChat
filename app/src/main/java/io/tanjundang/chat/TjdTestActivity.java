package io.tanjundang.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.tanjundang.chat.base.view.ninegridview.NineGridView;

public class TjdTestActivity extends AppCompatActivity {

    @BindView(R.id.rlRootView)
    RelativeLayout rlRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tjd_test);
        ButterKnife.bind(this);

        View view = LayoutInflater.from(this).inflate(R.layout.test_item_layout, null);
        NineGridView nineGridView = (NineGridView) view.findViewById(R.id.nineGridView);
        nineGridView.addPic("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=3bf901b332adcbef01347900949449e0/aec379310a55b319009fba0240a98226cffc1766.jpg");
        rlRootView.addView(nineGridView);

    }
}
