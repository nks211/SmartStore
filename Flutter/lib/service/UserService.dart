import 'dart:io';

import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:smart_store/util/common.dart';

import '../dto/User.dart';

class UserService {

  Future<bool> isUsedId(String id) async {
    final String url = BaseUrl + 'rest/user/isUsed?id=' + id;
    var response = await http.get(Uri.parse(url));
    var data = jsonDecode(response.body);
    return data;
  }

  Future<String> joinUser(User user) async {
    final String url = BaseUrl + 'rest/user';
    var response = await http.post(Uri.parse(url),
        headers: {
          "content-type": "application/json",
        },
        body: jsonEncode(user.toJson()),
    );
    return response.body;
  }

  Future<User> loginUser(User user) async {
    final String url = BaseUrl + 'rest/user/login';
    var response = await http.post(Uri.parse(url),
        headers: {
          "content-type": "application/json",
        },
        body: jsonEncode(user.toJson()),
    );
    return User.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  Future<Map<String, dynamic>> userInfo(User user) async {
    final String url = BaseUrl + 'rest/user/info';
    var response = await http.post(Uri.parse(url),
        headers: {
          "content-type": "application/json",
        },
        body: jsonEncode(user.toJson()),
    );
    return jsonDecode(utf8.decode(response.bodyBytes));
  }

  Future<String> updateUser(User user) async {
    final String url = BaseUrl + 'rest/user';
    var response = await http.put(Uri.parse(url),
      headers: {
        "content-type": "application/json",
      },
      body: jsonEncode(user.toJson()),
    );
    return response.body;
  }

}