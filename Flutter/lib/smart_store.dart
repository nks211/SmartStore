import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/home/main.dart';
import 'package:smart_store/mypage/order_detail_page.dart';
import 'package:smart_store/menuorder/shopping_cart.dart';
import 'package:smart_store/mypage/user_info.dart';
import 'package:smart_store/start/page_router.dart';
import 'package:smart_store/start/splash.dart';
import 'package:smart_store/util/common.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  token = (await FirebaseMessaging.instance.getToken())!;
  print('token : $token');// 앱 실행 시마다 fcm 토큰 발급
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
      home: Splash(),
    );
  }
}
