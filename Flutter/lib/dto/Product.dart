class Product {

  int _id = 0;
  String _img = '';
  String _name = '';
  int _price = 0;
  String _type = '';

  int get id => _id;
  String get img => _img;
  String get type => _type;
  int get price => _price;
  String get name => _name;

  Product(this._id, this._img, this._name, this._price, this._type);

}