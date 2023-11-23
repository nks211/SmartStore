import 'package:intl/intl.dart';

class Note {
  int _id = 0;
  String _title = '';
  String _content = '';
  String _order_time = '';
  String _sender_id = '';
  String _receiver_id = '';
  bool _is_read = false;

  int get id => _id;
  String get title => _title;
  bool get is_read => _is_read;
  String get receiver_id => _receiver_id;
  String get sender_id => _sender_id;
  String get order_time => _order_time;
  String get content => _content;

  Note(this._title, this._content, this._sender_id, this._receiver_id);

  Note.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._title = jsondata['title'];
    this._content = jsondata['content'];
    this._is_read = jsondata['read'];
    this._sender_id = jsondata['senderId'];
    this._receiver_id = jsondata['receiverId'];
    this._order_time = DateFormat('yyyy-MM-dd aa hh:mm:ss')
        .format(DateTime.parse(jsondata['date']).toLocal());
  }

  Map<String, dynamic> toJson() => {
    'content' : _content,
    'date' : _order_time,
    'id' : _id,
    'read' : true,
    'receiverId' : _receiver_id,
    'senderId' : _sender_id,
    'title' : _title,
  };

}