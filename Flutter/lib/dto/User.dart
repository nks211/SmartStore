import 'Stamp.dart';
import 'dart:convert';

class User {
  String _id = "";
  String _name = "";
  String _pass = "";
  int _stamps = 0;
  List<Stamp> _stampList = [];

  String get id => _id;
  String get name => _name;
  String get pass => _pass;
  int get stamps => _stamps;
  List<Stamp> get stampList => _stampList;

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
  }

  Map<String, dynamic> toJson() => {
    'id' : _id,
    'name' : _name,
    'pass' : _pass,
    'stampList' : _stampList.map((e) => jsonEncode(e.toJson())).toList(),
    'stamps' : _stamps,
  };

}