import '../util/common.dart';
import 'Stamp.dart';
import 'dart:convert';

class User {
  String _id = "";
  String _name = "";
  String _pass = "";
  int _stamps = 0;
  List<Stamp> _stampList = [];
  String _fcmToken = token;

  String get id => _id;
  String get name => _name;
  String get pass => _pass;
  int get stamps => _stamps;
  List<Stamp> get stampList => _stampList;
  String get fcmtoken => _fcmToken;

  User(String id, String pass, String name, {int stamps = 0, List<Stamp> stamplist = const []}) {
    _id = id; _name = name; _pass = pass; _stamps = stamps; _stampList = stamplist;
  }

  User.init();

  User.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._name = jsondata['name'];
    this._pass = jsondata['pass'];
    this._stamps = jsondata['stamps'];
    List<Stamp> list = [];
    for (var data in jsondata['stampList'] as List) {
      list.add(Stamp.fromJson(data));
    }
    this._stampList = list;
    this._fcmToken = jsondata['fcmToken'];
  }

  Map<String, dynamic> toJson() => {
    'id' : _id,
    'isAdmin' : false,
    'name' : _name,
    'pass' : _pass,
    'stampList' : _stampList,
    'stamps' : _stamps,
    'fcmToken' : _fcmToken,
  };

  @override
  String toString() {
    return 'User{_id: $_id, _name: $_name, _pass: $_pass, _stamps: $_stamps, _stampList: $_stampList}';
  }

}