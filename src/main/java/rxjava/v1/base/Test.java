package rxjava.v1.base;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Test {
    // 创建一个发布订阅模式的代码
    public static void main1(String[] args) {
        // 第一步, 初始化Observable
        Observable.create(new ObservableOnSubscribe<Message>() {
            // 创建 Observable 时，回调的是 ObservableEmitter ，字面意思即发射器，
            // 并且直接 throws Exception。

            // 当 Observable 被订阅的时候, 方法会被调用, 事件序列就会依照设定依次触发
            // 这里就是调用几次onNext, 最后完成时调用一次onComplete

            // 被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，
            // 即观察者模式。
            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                System.out.println("Observable exit 1" + "\n");
                e.onNext(new Message(1));

                e.onNext(new Message(2));

                // onComplete就表示完成, 之后再调用onNext就不管用了
                e.onComplete();

                System.out.println("Observable exit 2" + "\n");
                // 观察者不再接收本消息
                e.onNext(new Message(2));
                e.onComplete();
            }
        }).subscribe(new Observer<Message>() { // 第三部: 订阅

            // 第二部: 初始化Observer(观察者)
            private int i = 0;
            private Disposable mDisposable;

            @Override
            public void onComplete() {
                System.out.println("onComplete: " + i + "\n");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : value : " + e.getMessage() + "\n");
            }

            // RxJava2 在创建的 Observer 中，多了一个回调方法：onSubscribe，传递参数
            // 为Disposable
            // Disposable 相当于 RxJava 1.x 中的 Subscription， 用于解除订阅。
            @Override
            public void onSubscribe(Disposable disposable) {
                this.mDisposable = disposable;
            }

            @Override
            public void onNext(Message param) {
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，
                    // 让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    System.out.println("观察者中断接收信息!");
                }
            }
        });
    }

    // 订阅者使用Disposable自己停止订阅
    public static void main2(String[] args) {
        // 第一步, 初始化Observable
        Observable.create(new ObservableOnSubscribe<Message>() {

            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                System.out.println("Observable exit 1" + "\n");
                e.onNext(new Message(1));

                System.out.println("Observable exit 2" + "\n");
                e.onNext(new Message(2));

                // 发送第三个消息时, 观察者接收不到, 因为发送消息2后, 观察者就中断接收了
                System.out.println("Observable exit 3" + "\n");
                e.onNext(new Message(3));
                e.onComplete();
            }
        }).subscribe(new Observer<Message>() { // 第三部: 订阅

            // 第二部: 初始化Observer(观察者)
            private int i = 0;
            private Disposable mDisposable;

            @Override
            public void onComplete() {
                System.out.println("onComplete: " + i + "\n");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : value : " + e.getMessage() + "\n");
            }

            // RxJava2 在创建的 Observer 中，多了一个回调方法：onSubscribe，传递参数
            // 为Disposable
            // Disposable 相当于 RxJava 1.x 中的 Subscription， 用于解除订阅。
            @Override
            public void onSubscribe(Disposable disposable) {
                this.mDisposable = disposable;
            }

            @Override
            public void onNext(Message param) {
                i++;
                System.out.println("观察者接收到消息: " + param);
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，
                    // 让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    System.out.println("观察者中断接收信息!");
                }
            }
        });
    }

    // 多个线程测试
    public static void main(String[] args) {
        Observable<Message> observable = Observable.create(new ObservableOnSubscribe<Message>() {

            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                System.out.println("Observable exit 1" + "\n");
                e.onNext(new Message(1));

                System.out.println("Observable exit 2" + "\n");
                e.onNext(new Message(2));

                // 发送第三个消息时, 观察者接收不到, 因为发送消息2后, 观察者就中断接收了
                System.out.println("Observable exit 3" + "\n");
                e.onNext(new Message(3));
                e.onComplete();
            }
        });

        Observer<Message> observer1 = new Observer<Message>() { // 第三部: 订阅

            private Disposable mDisposable;

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : value : " + e.getMessage() + "\n");
            }

            @Override
            public void onNext(Message param) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("观察者1接收到消息: " + param);
            }

            @Override
            public void onSubscribe(Disposable d) {
                this.mDisposable = d;
            }
        };

        Observer<Message> observer2 = new Observer<Message>() { // 第三部: 订阅

            private Disposable mDisposable;

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : value : " + e.getMessage() + "\n");
            }


            @Override
            public void onNext(Message param) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("观察者2接收到消息: " + param);
            }

            @Override
            public void onSubscribe(Disposable d) {
                this.mDisposable = d;
            }
        };

        // Schedulers.io() -- RxCachedThreadScheduler
        // computation -- RxComputationThreadPool-1
        observable.observeOn(Schedulers.computation()).subscribe(observer1);

        // 通过多次subscribe(), 不能订阅多次, 而是每次subscribe都会触发发布动作
        // observable.subscribe(observer2);
    }
}
