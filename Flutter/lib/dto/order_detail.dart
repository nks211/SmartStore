class OrderDetail{
  String? _imgSrc;
  String? _name;
  int _price = 0;
  int _quantity = 0;
  int _totalPrice = 0;


  String? get imgSrc => _imgSrc;
  String? get name => _name;
  int get price => _price;
  int get quantity => _quantity;
  int get totalPrice => _totalPrice;

  OrderDetail(String src, String name, int price, int quantity){
    _imgSrc = src;
    _name = name;
    _price = price;
    _quantity = quantity;
    _totalPrice = price*quantity;
  }

}