package guaMVC.routes;

import guaMVC.GuaTemplate;
import guaMVC.Request;
import guaMVC.Utility;
import guaMVC.models.Message;
import guaMVC.models.Session;
import guaMVC.models.User;
import guaMVC.service.MessageService;
import guaMVC.service.SessionService;
import guaMVC.service.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Route {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/", Route::routeIndex);
        map.put("/message", Route::routeMessage);
        map.put("/static", Route::routeImage);

        return map;
    }
    
    
    public static String responseWithHeader(int code, HashMap<String, String> headerMap, String body) {
        String header = String.format("HTTP/1.1 %s\r\r", code);

        for (String key: headerMap.keySet()) {
            String value = headerMap.get(key);
            String item = String.format("%s: %s \r\n", key, value);
            header = header + item;
        }
        String response =  String.format("%s\r\n\r\n%s", header, body);
        return response;
    }

       public static String html(String filename) {
        String dir = "templates";
        String path = dir + "/" + filename;
        Utility.log("html path: %s", path);
        byte[] body = new byte[1];
        // 读取文件
        // 如果想读取 image 文件下的文件, 就用 image/doge.gif
        try (InputStream is = Utility.fileStream(path)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String r = new String(body);
        return r;

    }

    public static User currentUser(Request request) {
        // 尽快返回, 如果 cookie 里面没有存 session_id, 直接返回游客身份
        if (!request.cookies.containsKey("session_id")) {
            return UserService.guest();
        }
        String sessionId = request.cookies.get("session_id");
        Session s = SessionService.findBySessionId(sessionId);
        if (s == null) {
            return  UserService.guest();
        }
        Integer userId = s.userId;
        User user = UserService.findById(userId);
        if (user == null) {
            return UserService.guest();
        } else {
            return user;
        }
    }
    public static byte[] routeIndex(Request request) {
//        User current = currentUser(request);

//        String body = html("index.html");

        HashMap<String, Object> d = new HashMap<>();
//        String s = current.username;
        d.put("username", "游客");
//        String body = GuaTemplate.render(d, "index.ftl");
        String body = "json";
        String header = String.format("HTTP/1.1 200\r\nContent-Type: text/html\r\nConnection: close\r\nContent-Length: %s\r\n\r\n",
                body.getBytes(StandardCharsets.UTF_8).length);
        String response = header + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] routeMessage(Request request) {
        HashMap<String, String> data = null;
        if (request.method.equals("POST")) {
            Utility.log("this is post");
            data = request.form;
        } else if (request.method.equals("GET")) {
            Utility.log("this is get");
            data = request.query;
        } else {
            String m = String.format("unknown method (%s)", request.method);
            throw new RuntimeException(m);
        }
        Utility.log("get request args: %s", data);
        if (data != null) {
            MessageService.add(data);
        }

        HashMap<String, Object> d = new HashMap<>();
        Message m1 = new Message();
        m1.author = "test";
        m1.message = "test_message";
        d.put("m1", m1);

        ArrayList<Message> messages = MessageService.load();
        d.put("messageList", messages);
        String body = GuaTemplate.render(d, "html_basic.ftl");


        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String response = header + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] routeMessageAdd() {
        String body = "messageAdd";
        String header = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n";
        String response = header + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] routeLogin() {
        String body =  html("Utils.login.html");
        String response = "HTTP/1.1 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] routeImage(Request request) {
        String filepath = request.query.get("file");
        String dir = "static";
        String path = dir + "/" + filepath;
        Utility.log("routeImage: %s", path);
        String contentType = "";
        if (filepath.endsWith(".js")) {
            contentType = "application/javascript; charset=utf-8";
        } else if (filepath.endsWith("css")) {
            contentType = "text/css; charset=utf-8;";
        } else {
            contentType = " image/gif";
        }
        // bodyw
        String header = String.format("HTTP/1.1 200 very OK\r\nContent-Type: %s;\r\n\r\n", contentType);
        byte[] body = new byte[1];
        // 读取文件
        // 如果想读取 image 文件下的文件, 就用 image/doge.gif
        try (InputStream is = Utility.fileStream(path)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] part1 = header.getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[part1.length + body.length];
        System.arraycopy(part1, 0, response, 0, part1.length);
        System.arraycopy(body, 0, response, part1.length, body.length);

        // 也可以用 ByteArrayOutputStream
        // ByteArrayOutputStream response2 = new ByteArrayOutputStream();
        // response2.write(header.getBytes());
        // response2.write(body);
        return response;
    }

    public static byte[] route404(Request request) {
        String body = "<html><body><h1>404</h1><br><img src='/static?file=doge2.gif'></body></html>";
        String response = "HTTP/1.1 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }
}
