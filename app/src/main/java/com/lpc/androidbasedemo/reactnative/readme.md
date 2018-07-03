android原生植入RN

一、导入js
生成package.json文件
在项目根目录下运行（弹出对话，只管回车就行）：

AppledeMacBook-Pro:00 macbook$ npm init
This utility will walk you through creating a package.json file.
It only covers the most common items, and tries to guess sensible    defaults.
See `npm help json` for definitive documentation on these fields
and exactly what they do.
Use `npm install <pkg> --save` afterwards to install a package and
save it as a dependency in the package.json file.
Press ^C at any time to quit.
name: (00)
version: (1.0.0)
description:
entry point: (index.js)
test command:
git repository:
keywords:
author:
license: (ISC)
About to write to /Users/macbook/work/react-native/00/package.json:
{
  "name": "00",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
   "license": "ISC"
}
Is this ok? (yes)
在package.json文件中加入启动命令

"start": "node node_modules/react-native/local-cli/cli.js start"
下载react-native
运行：

npm install --save react-native
curl -o .flowconfig https://raw.githubusercontent.com/facebook/react-native/master/.flowconfig
新建js入口文件－－index.android.js

'use strict';
import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, 1</Text>
      </View>
    )
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});
AppRegistry.registerComponent('HelloWorld', () => HelloWorld);
二、配置项目
项目添加react-native依赖
在build.gradle(Module: app)中添加

compile "com.facebook.react:react-native:+" // From node_modules
添加RN maven 入口
在build.gradle(Project: [ProjectName])中添加

allprojects {
  repositories {
    ...
  maven {
    // All of React Native (JS, Android binaries) is installed from npm
    url "$rootDir/node_modules/react-native/android"
   }
  }
}
开启reload模式
在AndroidManifest.xml中添加

<uses-permission android:name="android.permission.INTERNET" />
三、js入口
新建一个activity

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;

public class MainActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

 //创建一个ReactRootView，把它设置成Activity的主视图
    mReactRootView = new ReactRootView(this);
    mReactInstanceManager = ReactInstanceManager.builder()

         .setApplication(getApplication())
            .setBundleAssetName("index.android.bundle")
            .setJSMainModuleName("index.android")
            .addPackage(new MainReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build();
    mReactRootView.startReactApplication(mReactInstanceManager, "HelloWorld", null);

    setContentView(mReactRootView);
}

@Override
public void invokeDefaultOnBackPressed() {
    super.onBackPressed();
}

//传递一些Activity的生命周期事件到ReactInstanceManager
//这是的JavaScript代码可以控制当前用户按下返回按钮的时候作何处理（譬如控制导航切换等等）。
//如果JavaScript端不处理相应的事件，你的invokeDefaultOnBackPressed方法会被调用。
//默认情况，这会直接结束你的Activity。
@Override
protected void onPause() {
    super.onPause();

    if (mReactInstanceManager != null) {
        mReactInstanceManager.onPause();
    }
}

@Override
protected void onResume() {
    super.onResume();

    if (mReactInstanceManager != null) {
        mReactInstanceManager.onResume(this, this);
    }
}

@Override
public void onBackPressed() {
    if (mReactInstanceManager != null) {
        mReactInstanceManager.onBackPressed();
    } else {
        super.onBackPressed();
    }
}

//我们需要改动一下开发者菜单。
//默认情况下，任何开发者菜单都可以通过摇晃或者设备类触发，不过这对模拟器不是很有用。
//所以我们让它在按下Menu键的时候可以显示
@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
        mReactInstanceManager.showDevOptionsDialog();
        return true;
    }
    return super.onKeyUp(keyCode, event);
}
创建上面activity的节点

<activity
  android:name=".MyReactActivity"
  android:label="@string/app_name"
  android:theme="@style/Theme.AppCompat.Light.NoActionBar">
</activity>
运行
npm start
报错
Error:Execution failed for task ':app:processDebugManifest'. > Manifest merger failed with multiple
原因：导入的库在build.gradle中的minSdkVersion与你的应用的minSdkVersion不匹配
解决：app要求应用最小系统版本和库要求系统最小版本不一致,改成一样的就行了