package guaMVC;

import guaMVC.routes.Route;
import guaMVC.routes.RouteAjaxTodo;
import guaMVC.routes.RouteTodo;
import guaMVC.routes.RouteUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

class GuaServlet implements Runnable {
    Socket connection;
    
    public GuaServlet(Socket connection) {
        this.connection = connection;
    }

    private static byte[] responseForPath(Request request) {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.putAll(Route.routeMap());
        map.putAll(RouteUser.routeMap());
        map.putAll(RouteTodo.routeMap());
        map.putAll(RouteAjaxTodo.routeMap());


        Function<Request, byte[]> function = map.getOrDefault(request.path, Route::route404);
        byte[] response =  function.apply(request);
        return response;
    }
    
    @Override
    public void run() {
        try  {
            Socket socket = this.connection;
            // 客户端连接上来了
//            Utility.log("client 连接成功");
            // 读取客户端请求数据
            String request = SocketOperator.socketReadAll(socket);
            byte[] response;
            if (request.length() > 0) {
                // 输出响应的数据
//                Utility.log("请求:\n%s", request);
                // 解析 request 得到 path
                Request r = new Request(request);

                // 根据 path 来判断要返回什么数据
                response = responseForPath(r);
            } else {
                response = new byte[1];
//                Utility.log("接受到了一个空请求");
            }
            SocketOperator.socketSendAll(socket, response);
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        } finally {
            try {
                this.connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


public class Server {
    static ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        int port = 9000;
//        run(port);
        runWithThread(port);
    }
    
    

    private static void run(Integer port) {
        // 监听请求
        // 获取请求数据
        // 发送响应数据
        // 我们的服务器使用 9000 端口
        // 不使用 80 的原因是 1024 以下的端口都要管理员权限才能使用
        // int port = 9000;
        Utility.log("服务器启动, 访问 http://localhost:%s", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // accept 方法会一直停留在这里等待连接
                try (Socket socket = serverSocket.accept()) {
                    // 客户端连接上来了
//                    Utility.log("client 连接成功");
                    // 读取客户端请求数据
                    String request = SocketOperator.socketReadAll(socket);
                    byte[] response;
                    if (request.length() > 0) {
                        // 输出响应的数据
//                        Utility.log("请求:\n%s", request);
                        // 解析 request 得到 path
                        Request r = new Request(request);

                        // 根据 path 来判断要返回什么数据
                        response = responseForPath(r);
                    } else {
                        response = new byte[1];
//                        Utility.log("接受到了一个空请求");
                    }
                    SocketOperator.socketSendAll(socket, response);
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }


    private static void runWithThread(Integer port) {
        // 监听请求
        // 获取请求数据
        // 发送响应数据
        // 我们的服务器使用 9000 端口
        // 不使用 80 的原因是 1024 以下的端口都要管理员权限才能使用
        // int port = 9000;
        Utility.log("服务器启动, 访问 http://localhost:%s", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // accept 方法会一直停留在这里等待连接
                try  {
                    Socket socket = serverSocket.accept();
                    // 客户端连接上来了
//                    Utility.log("client 连接成功");
                    // 读取客户端请求数据
                    GuaServlet servlet = new GuaServlet(socket);
                    Thread r = new Thread(servlet);
                    pool.execute(r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
        }
    }

    private static byte[] responseForPath(Request request) {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.putAll(Route.routeMap());
        map.putAll(RouteUser.routeMap());
        map.putAll(RouteTodo.routeMap());
        map.putAll(RouteAjaxTodo.routeMap());


        Function<Request, byte[]> function = map.getOrDefault(request.path, Route::route404);
        byte[] response =  function.apply(request);
        return response;
    }

}
