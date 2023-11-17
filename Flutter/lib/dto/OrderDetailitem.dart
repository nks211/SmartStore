class OrderDetailitem {

  String _img = '';
  int _product_id = 0;
  String _name = '';
  int _unitprice = 0;
  int _quantity = 0;
  int _totalprice = 0;

  String get img => _img;
  int get quantity => _quantity;
  int get unitprice => _unitprice;
  String get name => _name;
  int get product_id => _product_id;
  int get totalprice => _totalprice;

  OrderDetailitem(this._img, this._product_id, this._name, this._unitprice,
      this._quantity, this._totalprice);

  OrderDetailitem.fromJson(Map<String, dynamic> jsondata) {
    this._img = jsondata['img'];
    this._product_id = jsondata['product_id'];
    this._name = jsondata['name'];
    this._unitprice = jsondata['unitprice'];
    this._quantity = jsondata['quantity'];
    this._totalprice = jsondata['totalprice'];
  }

  Map<String, dynamic> toJson() => {
    'img' : _img,
    'quantity' : _quantity,
    'totalprice' : _totalprice,
    'product_id' : _product_id,
    'name' : _name,
    'unitprice' : _unitprice,
  };

  @override
  String toString() {
    return 'OrderDetailitem{_img: $_img, _product_id: $_product_id, _name: $_name, _unitprice: $_unitprice, _quantity: $_quantity, _totalprice: $_totalprice}';
  }
}