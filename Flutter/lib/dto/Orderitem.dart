import 'package:smart_store_flutter_starter/dto/OrderDetailitem.dart';

class Orderitem {

  int _id = 0;
  String _userId = '';
  String _orderTable = '';
  String _orderTime = '';
  String _completed = 'N';
  List<OrderDetailitem> _details = [];

  int get id => _id;
  String get userId => _userId;
  String get completed => _completed;
  String get orderTime => _orderTime;
  String get orderTable => _orderTable;
  List<OrderDetailitem> get details => _details;

  Orderitem(this._id, this._userId, this._orderTable, this._orderTime,
      this._completed);


  set details(List<OrderDetailitem> value) {
    _details = value;
  }

  Orderitem.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._userId = jsondata['userId'];
    this._orderTable = jsondata['orderTable'];
    this._orderTime = jsondata['orderTime'];
    this._completed = jsondata['completed'];
  }

  Map<String, dynamic> toJson() => {
    'id' : _id,
    'userId' : _userId,
    'orderTable' : _orderTable,
    'orderTime' : _orderTime,
    'completed' : _completed,
    'details' : null,
  };

}