class ReComment {

  int _id = 0;
  int _commentId = 0;
  int _productId = 0;
  String _comment = '';

  int get id => _id;
  int get commentId => _commentId;
  String get comment => _comment;
  int get productId => _productId;

  ReComment(this._id, this._commentId, this._productId, this._comment);

  ReComment.fromJson(Map<String, dynamic> jsondata) {
    this._id = jsondata['id'];
    this._commentId = jsondata['commentId'];
    this._productId = jsondata['productId'];
    this._comment = jsondata['comment'];
  }

}