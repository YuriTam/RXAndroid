package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RXBindingActivity extends AppCompatActivity {

    @BindView(R.id.btn_bind)
    Button btnBind;
    @BindView(R.id.et_test)
    EditText etTest;
    @BindView(R.id.lv_test)
    ListView lvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding);
        ButterKnife.bind(this);

        bindClick();
        btinLongClick();
        bindTextView();
    }

    private void bindTextView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1 );
        lvTest.setAdapter( adapter );

        RxTextView.textChanges(etTest)
                .debounce(600,TimeUnit.MILLISECONDS)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String keyWord) {
                        List<String> dataList = new ArrayList<String>();
                        if (!TextUtils.isEmpty(keyWord)){
                            for ( String s : getData()) {
                                if (s != null) {
                                    if (s.contains(keyWord)) {
                                        dataList.add(s);
                                    }
                                }
                            }
                        }
                        return dataList ;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        adapter.clear();
                        adapter.addAll(strings);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private List<String> getData() {
        List<String> mList = new ArrayList<>();
        mList.add("52");
        mList.add("10");
        mList.add("21");
        mList.add("11");
        mList.add("201");
        mList.add("2132");
        mList.add("3211");
        mList.add("210");
        mList.add("45");
        mList.add("132");
        mList.add("102");
        mList.add("52130");
        return mList;
    }

    private void btinLongClick() {
        RxView.longClicks(btnBind)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showToast("长按");
                    }
                });
    }

    private void bindClick() {
        RxView.clicks(btnBind)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showToast("单击");
                    }
                });
    }

    private void showToast(String msg){
        Toast.makeText(RXBindingActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    private void test(){
        Observable.merge(Observable.just(1),
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {

                    }
                }))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                });

        Observable.concat(Observable.just(2),Observable.just(3),Observable.just(9))
                .first()
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                });
    }
}
