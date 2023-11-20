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

  void setid(int id) { _id = id; }

  Comment.fromJson(Map<String, dynamic> jsondata) {
    this._comment = jsondata['comment'];
    this._id = jsondata['commentId'];
    this._rating = jsondata['rating'];
    this._userId = jsondata['user_id'];
  }

  Map<String, dynamic> toJson() => {
    'comment' : _comment,
    'id' : _id,
    'productId' : _productId,
    'rating' : _rating,
    'userId' : _userId,
  };

  @override
  String toString() {
    return 'Comment{_comment: $_comment, _id: $_id, _productId: $_productId, _rating: $_rating, _userId: $_userId}';
  }

}