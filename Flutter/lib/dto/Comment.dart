class Comment {

  String _comment = '';
  int _id = 0;
  int _productId = 0;
  double _rating = 0;
  String _userId = '';

  String get comment => _comment;
  int get id => _id;
  String get userId => _userId;
  double get rating => _rating;
  int get productId => _productId;

  Comment(this._comment, this._id, this._productId, this._rating, this._userId);

}