package io.tanjundang.chat.me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.tanjundang.chat.R;
import io.tanjundang.chat.base.BaseActivity;
import io.tanjundang.chat.base.Global;
import io.tanjundang.chat.base.api.BusinessApi;
import io.tanjundang.chat.base.entity.CreateMomentsResp;
import io.tanjundang.chat.base.network.ApiObserver;
import io.tanjundang.chat.base.network.HttpReqTool;
import io.tanjundang.chat.base.utils.Functions;
import io.tanjundang.chat.base.utils.PhotoPickTool;
import io.tanjundang.chat.base.utils.PicUploadTool;
import io.tanjundang.chat.base.view.ninegridview.NineGridView;

import static io.tanjundang.chat.base.utils.PhotoPickTool.REQUEST_CODE_CHOOSE;

/**
 * @Author: TanJunDang
 * @Email: TanJunDang@126.com
 * @Date: 2018/1/12
 * @Description: 新增朋友圈
 */

public class AddMomentsActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;

    @BindView(R.id.multipleGridView)
    NineGridView nineGridView;
    List<Uri> selectedList = new ArrayList<>();
    ArrayList<String> picUrls = new ArrayList<>();

    public static void Start(Context context) {
        Intent intent = new Intent(context, AddMomentsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moments);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvSubTitle.setVisibility(View.VISIBLE);
        tvSubTitle.setText("发送");
        tvTitle.setText("");
//        multipleGridView.addPic("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=3bf901b332adcbef01347900949449e0/aec379310a55b319009fba0240a98226cffc1766.jpg");
//        multipleGridView.addPic("https://tanjundang.github.io/img/lrving.jpg");
//        multipleGridView.addPic("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3866823653,3481734704&fm=173&s=D69082633E8070554910DDDD030080A1&w=500&h=306&img.JPEG");
//        multipleGridView.addPic("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3143316835,1781534804&fm=173&s=20E2732690422F5D1D06D6910300009D&w=500&h=333&img.JPEG");
//        multipleGridView.addPic("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3909146119,2483223891&fm=175&s=1E34C0052A61330517A828870300C0E2&w=218&h=146&img.JPEG");
//        multipleGridView.addPic("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2448380558,788942888&fm=173&s=E30DB044CE737C9EC7BA81910300D09B&w=218&h=146&img.JPEG");

        nineGridView.setOnClickListener(new NineGridView.onClickListener() {
            @Override
            public void addPic() {
                PhotoPickTool.getInstance().selectPhoto(AddMomentsActivity.this);
            }

            @Override
            public void delPic(int index) {
                picUrls.remove(index);
            }
        });
    }

    @OnClick({R.id.tvSubTitle})
    public void onClick(View v) {
        if (v.equals(tvSubTitle)) {
            String content = etContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Functions.toast("请输入发送内容");
                return;
            }
            HttpReqTool.getInstance()
                    .createApi(BusinessApi.class)
                    .addMoments(content, picUrls.toArray(new String[picUrls.size()]))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<CreateMomentsResp>() {
                        @Override
                        public void onSuccess(CreateMomentsResp resp) {
                            if (resp.isSuccess()) {
                                finish();
                            } else {
                                Functions.toast(resp.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            selectedList = Matisse.obtainResult(data);

            Observable.fromIterable(selectedList)
                    .map(new Function<Uri, File>() {
                        @Override
                        public File apply(Uri uri) throws Exception {
                            return Functions.uri2File(AddMomentsActivity.this, uri);
                        }
                    })
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) throws Exception {
                            PicUploadTool
                                    .getInstance()
                                    .upload(file, file.getName(), Global.getInstance()
                                            .getQiniuToken(), new PicUploadTool.Callback() {
                                        @Override
                                        public void onSuccess(String url) {
                                            if (!picUrls.contains(url))
                                                picUrls.add(url);
                                            nineGridView.addPic(url);
                                            rlRoot.invalidate();
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Functions.toast(error);
                                        }
                                    });
                        }
                    });

        }
    }
}
