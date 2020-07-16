package disputor.stu01base.entity;

/**
 * 事件, 放到Disputor队列中, 发布订阅的
 */
public class LongEvent {
    private long value;

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}