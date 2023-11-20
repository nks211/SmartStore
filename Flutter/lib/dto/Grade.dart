class Grade {
  String _img = '';
  int _step = 0;
  int _to = 0;
  String _title = '';

  String get img => _img;
  int get step => _step;
  String get title => _title;
  int get to => _to;


  Grade.init();

  Grade(this._img, this._step, this._to, this._title);

  Grade.fromJson(Map<String, dynamic> jsondata) {
    this._img = jsondata['img'];
    this._step = jsondata['step'];
    this._to = jsondata['to'];
    this._title = jsondata['title'];
  }

}