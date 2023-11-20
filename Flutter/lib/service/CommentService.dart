import 'dart:convert';

import 'package:smart_store_flutter_starter/util/common.dart';
import 'package:http/http.dart' as http;

import '../dto/Comment.dart';

class CommentService {

  Future<List<Comment>> getproductcomments(int id) async {
    final String url = BaseUrl + 'rest/product/' + id.toString();
    var response = await http.get(Uri.parse(url));
    List<Comment> result = [];
    for (var data in jsonDecode(utf8.decode(response.bodyBytes))) {
      Comment comment = Comment.fromJson(data);
      result.add(comment);
    }
    return result;
  }

  Future<double> getaveragerating(int id) async {
    final String url = BaseUrl + 'rest/product/' + id.toString();
    var response = await http.get(Uri.parse(url));
    if (jsonDecode(response.body).length > 0) {
      return jsonDecode(response.body)[0]['avg'];
    }
    else {
      return 0;
    }
  }

  Future<String> makeComment(Comment comment) async {
    final String url = BaseUrl + 'rest/comment';
    var response = await http.post(Uri.parse(url),
      headers: {
        "content-type": "application/json",
      },
      body: jsonEncode(comment.toJson()),
    );
    return response.body;
  }

  Future<String> updateComment(Comment comment) async {
    final String url = BaseUrl + 'rest/comment';
    var response = await http.put(Uri.parse(url),
      headers: {
        "content-type": "application/json",
      },
      body: jsonEncode(comment.toJson()),
    );
    return response.body;
  }

  Future<String> deleteComment(int id) async {
    final String url = BaseUrl + 'rest/comment/' + id.toString();
    var response = await http.delete(Uri.parse(url));
    return response.body;
  }

}