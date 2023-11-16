import 'Stamp.dart';

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

  User(String id, String name, String pass, {int stamps = 0, List<Stamp> stamplist = const []}) {
    _id = id; _name = name; _pass = pass; _stamps = stamps; _stampList = stamplist;
  }

}