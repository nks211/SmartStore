class OrderDetail {
  int _id = 0;
  int _orderId = 0;
  int _productId = 0;
  int _quantity = 0;

  int get id => _id;
  int get orderId => _orderId;
  int get quantity => _quantity;
  int get productId => _productId;

  OrderDetail(this._productId, this._quantity);

  OrderDetail.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._orderId = jsondata['orderId'];
    this._productId = jsondata['productId'];
    this._quantity = jsondata['quantity'];
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map["id"] = _id;
    map["orderId"] = _orderId;
    map["productId"] = _productId;
    map["quantity"] = _quantity;

    return map;
  }

  @override
  String toString() {
    return 'OrderDetail{_id: $_id, _orderId: $_orderId, _productId: $_productId, _quantity: $_quantity}';
  }

}