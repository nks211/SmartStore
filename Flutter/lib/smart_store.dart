import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store_flutter_starter/home/main.dart';
import 'package:smart_store_flutter_starter/mypage/order_detail_page.dart';
import 'package:smart_store_flutter_starter/menuorder/shopping_cart.dart';
import 'package:smart_store_flutter_starter/mypage/user_info.dart';
import 'package:smart_store_flutter_starter/start/page_router.dart';
import 'package:smart_store_flutter_starter/util/common.dart';
import 'package:smart_store_flutter_starter/start/login.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
      statusBarColor: coffeeBrown,
    ));

    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
          primarySwatch: Colors.brown,
        // colorScheme: ColorScheme.fromSeed(seedColor: Colors.brown),
        // useMaterial3: true,
      ),
      home: Login(),
    );
  }
}
