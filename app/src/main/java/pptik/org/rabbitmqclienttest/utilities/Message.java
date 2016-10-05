package pptik.org.rabbitmqclienttest.utilities;

/**
 * Created by arysetijadiprihatmanto on 10/5/16.
 */
public class Message {

    private String text;

    public Message(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
