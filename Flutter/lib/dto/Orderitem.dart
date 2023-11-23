import 'package:intl/intl.dart';
import 'package:smart_store/dto/OrderDetail.dart';
import 'package:smart_store/dto/OrderDetailitem.dart';

class Orderitem {

  int _id = 0;
  String _userId = '';
  String _orderTable = '웹주문';
  String _orderTime = '';
  String _completed = 'N';
  List<OrderDetailitem> _details = [];
  List<OrderDetail> _jsondetails = [];

  int get id => _id;
  String get userId => _userId;
  String get completed => _completed;
  String get orderTime => _orderTime;
  String get orderTable => _orderTable;
  List<OrderDetailitem> get details => _details;
  List<OrderDetail> get jsondetails => _jsondetails;


  Orderitem(this._id, this._userId, this._orderTable, this._orderTime,
      this._completed);


  void setDetails(List<OrderDetailitem> value) {
    _details = value;
  }

  void jsonDetails() {
    for (var detail in _details) {
      _jsondetails.add(OrderDetail(detail.product_id, detail.quantity));
    }
  }

  Orderitem.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._userId = jsondata['userId'];
    this._orderTable = jsondata['orderTable'];
    this._orderTime = DateFormat('yyyy-MM-dd aa hh:mm:ss')
        .format(DateTime.parse(jsondata['orderTime']).toLocal()); // 서버에서 받아온 시간 현지화
    this._completed = jsondata['completed'];
  }

  Map<String, dynamic> toJson() => {
    'completed' : _completed,
    'details' : _jsondetails,
    'orderTable' : _orderTable,
    'orderTime' : _orderTime,
    'userId' : _userId
  };

  @override
  String toString() {
    return 'Orderitem{_id: $_id, _userId: $_userId, _orderTable: $_orderTable, _orderTime: $_orderTime, _completed: $_completed, _details: $_details, _jsondetails: $_jsondetails}';
  }
}