package com.charles.demo.test;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class ObservableTest {
    String tag="William";
    ObservableEmitter<String> emitter;

    Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<String> emitter) {
            ObservableTest.this.emitter=emitter;
            Log.d(tag, "发送线程：" + Thread.currentThread().getName());
            Log.d(tag, "发送：：" + "hello");
            emitter.onNext("hello");
//            Log.d(tag, "发送：：" + "onComplete");
//            emitter.onComplete();
        }
    });

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            Log.d(tag, "onSubscribe");
            //销毁资源，后续的方法不在执行；
            //d.dispose();
        }
        @Override
        public void onNext(@NonNull String s) {
            Log.d(tag, "接收线程：" + Thread.currentThread().getName());
            Log.d(tag, "接收：" + s);
        }
        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(tag, "onError");
        }
        @Override
        public void onComplete() {
            Log.d(tag, "onComplete");
        }
    };

    public void test(){
        stringObservable.subscribe(observer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        emitter.onNext("test");
    }
}
