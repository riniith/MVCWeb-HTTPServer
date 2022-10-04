package guaMVC.service;

import guaMVC.models.Message;
import guaMVC.models.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;

// impot ../Utils

public class MessageService {
    public static Message add(HashMap<String, String> form) {
        String author = form.get("author");
        String message = form.get("message");
        Message m = new Message();
        m.author = author;
        m.message = message;

        ArrayList<Message> all = load();
        all.add(m);
        save(all);

        return m;
    }

//    public static void save(ArrayList<Message> list) {
//        String all = "";
//        for (Message m: list) {
//            StringBuilder s = new StringBuilder();
//            s.append(m.author);
//            s.append("\n");
//            s.append(m.message);
//            s.append("\n");
//            all = all + s.toString();
//        }
//
//        Utility.log("save all <%s>", all);
//        String filename = "Message.txt";
//        Utility.save(filename, all);
//    }

    public static void save(ArrayList<Message> list) {
        String className = Message.class.getSimpleName();
        ModelFactory.save(className, list, (model) -> {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(model.author);
            lines.add(model.message);
            return lines;
        } );

    }


    public static ArrayList<Message> load() {
        String className = Message.class.getSimpleName();
        ArrayList<Message> all = ModelFactory.load(className, 2, (modelData) -> {
            String author = modelData.get(0);
            String message = modelData.get(1);;

            Message m = new Message();
            m.author = author;
            m.message = message;

            return m;
        });
        return all;
    }


//    public static ArrayList<Message> load() {
//        String filename = "Message.txt";
//        String data = Utility.load(filename);
//        Utility.log("load data: <%s>", data);
//
//        String[] lines = data.split("\n");
//        ArrayList<Message> all = new ArrayList<>();
//
//        for (int i = 0; i < lines.length; i = i + 2) {
//            String author = lines[i];
//            String message = lines[i + 1];
//            // Utils.log("author: %s, messasge: %s", author, message);
//            Message m = new Message();
//            m.author = author;
//            m.message = message;
//            all.add(m);
//        }
//
//        return all;
//    }

    public static String messageListShowString() {
        ArrayList<Message> all = load();
        StringBuilder result = new StringBuilder();

        for (Message m: all) {
            String s = String.format("%s: %s <br>", m.author, m.message);
            result.append(s);
        }
        return result.toString();
    }
}
