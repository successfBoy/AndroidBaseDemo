'use strict';
import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
class MyReactNativeApp extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>{'Hello, reactnative'}</Text>
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
