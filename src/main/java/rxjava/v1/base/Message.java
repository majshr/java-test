package rxjava.v1.base;

public class Message {
    private Integer message;

    public Message(Integer message) {
        super();
        this.message = message;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.valueOf(message);
    }
}
