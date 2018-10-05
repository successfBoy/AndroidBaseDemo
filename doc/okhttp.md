一.Okhttp介绍

    Okhttp是一个优秀的网络请求框架。
    volley 是一个Google提供的网络请求框架，依靠HttpCient。Google 在android
    6.0 的SDK中去掉了HttpCinet，所以OKhttp 就开始越来越受大家的欢迎。

  添加OkHttp的依赖

      在对应的Module的gradle中添加
        compile 'com.squareup.okhttp3:okhttp:3.5.0'
      然后同步一下项目即可


二.OKhttp进行Get请求
    试用Okhttp进行get请求只需要四步即可完成


