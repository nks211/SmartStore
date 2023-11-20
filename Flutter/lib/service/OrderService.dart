import 'package:http/http.dart' as http;
import 'package:smart_store_flutter_starter/dto/OrderDetailitem.dart';
import 'dart:convert';
import 'package:smart_store_flutter_starter/util/common.dart';

import '../dto/Orderitem.dart';

class OrderService {

  Future<List<OrderDetailitem>> getOrderDetails(int id) async {
    final String url = BaseUrl + 'rest/order/${id}';
    var response = await http.get(Uri.parse(url));
    List<OrderDetailitem> details = [];
    for (var data in jsonDecode(utf8.decode(response.bodyBytes))) {
      details.add(OrderDetailitem.fromJson(data));
    }
    return details;
  }

  Future<String> makeOrder(Orderitem order) async {
    final String url = BaseUrl + 'rest/order';
    var response = await http.post(Uri.parse(url),
      headers: {
        "content-type": "application/json",
      },
      body: jsonEncode(order.toJson()),
    );
    if (response.statusCode == 200) {
      return response.body;
    }
    else {
      return '';
    }
  }

}