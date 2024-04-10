# MVCWeb-HTTPServer
MVC Web フレームワークと HTTP サーバー

Webフレームワークはコード分離にMVCアーキテクチャを採用

モデル層は、自作の MySQL ベースの MyBatis のようなデータ永続層を使用して、POJ0 と SQL の間の型マッピングとバインディングを提供します。
コントローラー層は、HashMap とメソッド リファレンスを使用して、ルート配布、カスタム リクエスト、カスタム レスポンスのカプセル化を実装します。
View レイヤーは FreeMarker を使用して、シングルトン レンダリング テンプレートの生成をカプセル化します。

HTTPサーバー

基盤となるSocketを直接利用してTCP/IP通信を実現すると同時に、HTTPプロトコルの解析、HTTPリクエストの受信、レスポンスの生成と返信を実現し、マルチスレッドによる同時アクセスを実現します。


MVC web framework and HTTP server

Web framework adopts MVC architecture for code separation

The Model model layer uses a self-made MyBatis-based data persistence layer based on MySQL to provide type mapping and binding between POJ0 and SQL. The Controller layer uses HashMap and Method Reference to implement routing distribution, custom requests and custom response encapsulation. ; The View layer uses FreeMarker to encapsulate singleton rendering template generation.

HTTP server

It directly uses the underlying Socket to realize TCP/IP communication, and at the same time realizes the analysis of the HTTP protocol, realizes the reception of HTTP requests, generates responses and returns them; and realizes concurrent access through multi-threading.

MVC Web框架和HTTP服务器

Web框架采用MVC架构进行代码分离

Model模型层使用自制的基于MySQL的类MyBatis的数据持久层，提供POJ0和SQL之间的类型映射和绑定，
Controller控制器层使用HashMap和Method Reference实现路由分发、自定义请求和自定义响应封装；
View视图层使用FreeMarker封装单例渲染模板生成。

HTTP服务器

直接使用底层Socket实现TCP/IP通信，同时实现了对HTTP协议的解析，实现了HTTP请求接收生成响应并返回；通过多线程实现并发访问。
