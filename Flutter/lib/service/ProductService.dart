import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:smart_store/util/common.dart';

import '../dto/Product.dart';

class ProductService {

  Future<List<Product>> getproductmenu() async {
    final String url = BaseUrl + 'rest/product';
    var response = await http.get(Uri.parse(url));
    List<Product> result = [];
    for (var data in jsonDecode(utf8.decode(response.bodyBytes))) {
      result.add(Product.fromJson(data));
    }
    return result;
  }

}