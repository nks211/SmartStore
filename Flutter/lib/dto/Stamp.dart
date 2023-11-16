class Stamp {
  int _id = 0;
  int _orderId = 0;
  int _quantity = 0;
  String _userId = "";

  int get id => _id;
  int get orderId => _orderId;
  int get quantity => _quantity;
  String get userId => _userId;

  Stamp(int id, int orderId, int quantity, String userId) {
    _id = id; _orderId = orderId; _quantity = quantity; _userId = userId;
  }

}