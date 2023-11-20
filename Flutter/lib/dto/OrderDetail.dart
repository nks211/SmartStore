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

  Map<String, dynamic> toJson() => {
    'id' : 0,
    'orderId' : 0,
    'productId' : _productId,
    'quantity' : _quantity,
  };

}