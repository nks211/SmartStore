class Order{
  String? _imgSrc;
  String? _orderDetails;
  int _totalPrice = 0;
  String? _date;

  String? get imgSrc => _imgSrc;
  String? get orderDetails => _orderDetails;
  int get totalPrice => _totalPrice;
  String? get date => _date;

  Order(String src, String details, int price, String date){
    _imgSrc = src;
    _orderDetails = details;
    _totalPrice = price;
    _date = date;
  }

}