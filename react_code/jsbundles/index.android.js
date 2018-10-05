'use strict';
import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
import MyNativeAndroidToast from '../native_modules/MyNativeAndroidToast';
class MyReactNativeApp extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text onPress={()=>MyNativeAndroidToast.show('click',MyNativeAndroidToast.SHORT)} style={styles.hello}>{'Hello, reactnative'}</Text>
      </View>
    )
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'white'
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
    color: 'black'
  },
});
AppRegistry.registerComponent('MyReactNativeApp', () => MyReactNativeApp);
