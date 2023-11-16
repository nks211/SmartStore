import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:smart_store_flutter_starter/util/common.dart';

class UserService {

  Future<bool> isUsedId(String id) async {
    final String url = BaseUrl + 'rest/user/isUsed?id=' + id;
    var response = await http.get(Uri.parse(url));
    var data = json.decode()
  }

}