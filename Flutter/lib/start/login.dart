import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store_flutter_starter/dto/Orderitem.dart';
import 'package:smart_store_flutter_starter/service/UserService.dart';
import 'package:smart_store_flutter_starter/start/page_router.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

import '../dto/Grade.dart';
import '../dto/User.dart';
import 'join.dart';

class Login extends StatefulWidget {
  @override
  _Login createState() => _Login();
}

class _Login extends State<Login> {

  var userservice = UserService();
  var idcontroller = TextEditingController();
  var passcontroller = TextEditingController();

  // 최초 로그인 시 회원 정보 받아온 다음 메인 화면으로 이동함
  void passpage() {
    Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => PageRouter()));
  }

  @override
  Widget build(BuildContext context) {

    //기존에 로그인된 정보가 있으면 자동으로 페이지 이동
    Future<SharedPreferences> preferences = SharedPreferences.getInstance();
    preferences.then((value) {
      if (value.getString('id') != null) {
        passpage();
      }
    });

    return Scaffold(
      body: SingleChildScrollView(
        child: Container(
          alignment: Alignment.center,
          child: ListView(
            shrinkWrap: true,
            children: [
              SizedBox(
                height: 120,
              ),
              Container(
                alignment: Alignment.center,
                margin: EdgeInsets.all(20),
                height: 200,
                child: Image.asset("assets/logo.png"),
              ),
              Align(
                alignment: Alignment.center,
                child: Text(
                  'Login', style: textLogin,
                ),
              ),
              SizedBox(
                height: 30,
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
                child: TextField(
                  controller: idcontroller,
                  decoration: InputDecoration(
                    hintText: 'ID',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      borderSide: BorderSide(width: 1, color: coffeeBrown),
                    ),
                  ),
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
                child: TextField(
                  controller: passcontroller,
                  decoration: InputDecoration(
                    hintText: 'PW',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      borderSide: BorderSide(width: 1, color: coffeeBrown),
                    ),
                  ),
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 20, horizontal: 30),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () {
                          if (idcontroller.text != '' && passcontroller.text != '') {
                            var id = idcontroller.text;
                            var pass = passcontroller.text;
                            var loginuser = User(id, pass, "");
                            userservice.loginUser(loginuser).then((value) {
                              if (id == value.id && pass == value.pass) {
                                showToast("로그인되었습니다");
                                preferences.then((_) {
                                  _.setString('id', value.id);
                                  _.setString('pass', value.pass);
                                });
                                passpage();
                              }
                            }).catchError((e) => showToast("아이디나 비밀번호를 확인해주세요."));
                          }
                          else {
                            showToast("아이디와 비밀번호를 입력해주세요.");
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.all(Radius.circular(10)),
                          ),
                          backgroundColor: coffeeBrown,
                        ),
                        child: Container(
                            alignment: Alignment.center,
                            width: 120,
                            height: 50,
                            child: Text('Login', style: TextStyle(color: Colors.white),))
                    ),
                    ElevatedButton(
                        onPressed: (){
                          Navigator.push(context, MaterialPageRoute(builder: (context) => Join()));
                        },
                        style: ElevatedButton.styleFrom(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.all(Radius.circular(10)),
                          ),
                          backgroundColor: coffeeBrown,
                        ),
                        child: Container(
                            alignment: Alignment.center,
                            width: 120,
                            height: 50,
                            child: Text('Join', style: TextStyle(color: Colors.white),))
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
