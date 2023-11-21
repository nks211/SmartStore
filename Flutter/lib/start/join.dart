import 'package:flutter/material.dart';
import 'package:smart_store/util/common.dart';

import '../dto/User.dart';
import '../service/UserService.dart';

class Join extends StatefulWidget {
  const Join({super.key});

  @override
  State<Join> createState() => _JoinState();
}

class _JoinState extends State<Join> {
  var userService = UserService();
  var idcontroller = TextEditingController();
  var passcontroller = TextEditingController();
  var namecontroller = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(
              height: 100,
            ),
            Container(
              alignment: Alignment.center,
              margin: EdgeInsets.all(20),
              height: 200,
              child: Image.asset("assets/logo.png"),
            ),
            Text(
              'Join',
              style: textLogin,
            ),
            SizedBox(
              height: 30,
            ),
            Container(
              padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
              child: Row(
                children: [
                  Container(
                    width: 250,
                    margin: EdgeInsets.symmetric(horizontal: 10),
                    child: TextField(
                      controller: idcontroller,
                      decoration: InputDecoration(
                        hintText: '아이디를 입력하세요.',
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(10)),
                          borderSide: BorderSide(width: 1, color: coffeeBrown),
                        ),
                      ),
                    ),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                      backgroundColor: coffeeBrown,
                      padding: EdgeInsets.all(5),
                    ),
                    onPressed: () {
                      userService.isUsedId(idcontroller.text).then((value) {
                        if (value) {
                          showToast("사용 중인 아이디입니다.");
                        } else {
                          showToast("사용 가능한 아이디입니다.");
                        }
                      });
                    },
                    child: Container(
                      margin: EdgeInsets.symmetric(horizontal: 5),
                      width: 50,
                      height: 50,
                      child: Image.asset(
                        "assets/check.png",
                        fit: BoxFit.fill,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 40, vertical: 10),
              child: TextField(
                controller: passcontroller,
                obscureText: true,
                decoration: InputDecoration(
                  hintText: '비밀번호를 입력하세요.',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                    borderSide: BorderSide(width: 1, color: coffeeBrown),
                  ),
                ),
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 40, vertical: 10),
              child: TextField(
                controller: namecontroller,
                decoration: InputDecoration(
                  hintText: '별명을 입력하세요.',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                    borderSide: BorderSide(width: 1, color: coffeeBrown),
                  ),
                ),
              ),
            ),
            Container(
              padding: EdgeInsets.symmetric(vertical: 20),
              child: ElevatedButton(
                  onPressed: () {
                    if (idcontroller.text != '' &&
                        passcontroller.text != '' &&
                        namecontroller.text != '') {
                      var id = idcontroller.text;
                      var pass = passcontroller.text;
                      var name = namecontroller.text;
                      userService.isUsedId(id).then((value) {
                        if (!value) {
                          var newuser = User(id, pass, name);
                          userService.joinUser(newuser).then((value) {
                            if (value == 'true') {
                              Navigator.pop(context);
                            }
                          });
                        } else {
                          showToast("아이디가 중복됩니다.");
                        }
                      });
                    } else {
                      if (idcontroller.text == '') {
                        showToast("아이디를 입력해주세요.");
                      }
                      if (passcontroller.text == '') {
                        showToast("비밀번호를 입력해주세요.");
                      }
                      if (namecontroller.text == '') {
                        showToast("별명을 입력해주세요.");
                      }
                    }
                    // Navigator.pop(context);
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
                      child: Text(
                        'Join',
                        style: TextStyle(color: Colors.white),
                      ))),
            ),
          ],
        ),
      ),
    );
  }
}
