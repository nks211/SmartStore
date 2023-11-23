import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/service/UserService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/User.dart';
import '../start/page_router.dart';

class UserDetailPage extends StatefulWidget {
  final String name;

  UserDetailPage({required this.name});

  @override
  State<UserDetailPage> createState() => _UserDetailPageState();
}

class _UserDetailPageState extends State<UserDetailPage> {
  String path = platform == Platform.naver? 'assets/naverlogo.png'
      : 'assets/user.png';
  String id = '';
  String pass = '';
  String newpass = '';
  String check = '새로 사용할 비밀번호를 입력하세요';
  Future<SharedPreferences> preference = SharedPreferences.getInstance();
  TextEditingController namecontroller = TextEditingController();
  TextEditingController passcontroller = TextEditingController();
  var userservice = UserService();

  // 비밀번호 재확인 알림창
  void secondcheck(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) => AlertDialog(
              title: Text(
                '다시 한번 비밀번호를 입력하세요.',
                style: textStyle20B,
              ),
              contentPadding: EdgeInsets.all(20),
              content: TextField(
                controller: passcontroller,
                obscureText: true,
              ),
              actions: [
                TextButton(
                    onPressed: () {
                      if (passcontroller.text == newpass) {
                        setState(() {
                          pass = newpass;
                          showToast('비밀번호가 변경되었습니다.');
                        });
                      } else {
                        showToast('입력한 비밀번호 값이 이전과 다릅니다. 다시 시도하세요.');
                      }
                      setState(() {
                        newpass = '';
                        passcontroller.text = '';
                      });
                      Navigator.pop(context);
                    },
                    child: Text('확인')),
              ],
            ));
  }

  // 회원 정보 1회에 한하여 가져옴
  @override
  void initState() {
    namecontroller.text = widget.name;
    preference.then((value) {
      setState(() {
        if (value.getString('id') != null) {
          id = value.getString('id')!;
        }
        if (value.getString('pass') != null) {
          pass = value.getString('pass')!;
        }
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        shrinkWrap: true,
        children: [
          Container(
            alignment: Alignment.center,
            padding: EdgeInsets.symmetric(vertical: 15),
            child: Text(
              '회원정보 조회 / 수정',
              style: textStyleRed30,
            ),
          ),
          Container(
            height: 650,
            alignment: Alignment.center,
            margin: EdgeInsets.all(20),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.all(Radius.circular(10)),
              border: Border.all(color: coffeeDarkBrown, width: 2),
            ),
            child: Column(
              children: [
                Container(
                  margin: EdgeInsets.all(20),
                  padding: EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    color: Colors.grey,
                    borderRadius: BorderRadius.all(Radius.circular(20)),
                  ),
                  child: Image.asset(
                    path,
                    width: 150,
                  ),
                ),
                // ElevatedButton(
                //   onPressed: () {
                //   },
                //   style: ElevatedButton.styleFrom(
                //     shape: RoundedRectangleBorder(
                //       borderRadius: BorderRadius.all(Radius.circular(5)),
                //     ),
                //     backgroundColor: coffeePointRed,
                //   ),
                //   child: Container(
                //     alignment: Alignment.center,
                //     width: 120,
                //     height: 50,
                //     child: Text(
                //       '프로필 사진 변경',
                //       style: textStyle15,
                //     ),
                //   ),
                // ),
                Container(
                  padding: EdgeInsets.symmetric(horizontal: 40, vertical: 20),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'ID',
                        style: textStyle20B.apply(color: Colors.black),
                      ),
                      Text(
                        id,
                        style: textStyle15,
                      ),
                    ],
                  ),
                ),
                Container(
                  padding: EdgeInsets.symmetric(horizontal: 40, vertical: 20),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Name',
                        style: textStyle20B.apply(color: Colors.black),
                      ),
                      Container(
                        width: 200,
                        child: TextField(
                          controller: namecontroller,
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  padding: EdgeInsets.symmetric(horizontal: 40, vertical: 20),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Password',
                        style: textStyle20B.apply(color: Colors.black),
                      ),
                      ElevatedButton(
                        onPressed: () {
                          if (newpass == '') {
                            showDialog(
                                context: context,
                                builder: (context) => AlertDialog(
                                      title: Text(
                                        '새로 사용할 비밀번호를 입력하세요',
                                        style: textStyle20B,
                                      ),
                                      contentPadding: EdgeInsets.all(20),
                                      content: TextField(
                                        controller: passcontroller,
                                        obscureText: true,
                                      ),
                                      actions: [
                                        TextButton(
                                            onPressed: () {
                                              setState(() {
                                                newpass = passcontroller.text;
                                                passcontroller.text = '';
                                              });
                                              Navigator.pop(context);
                                              secondcheck(context);
                                            },
                                            child: Text('확인'))
                                      ],
                                    ));
                          } else {
                            secondcheck(context);
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.all(Radius.circular(5)),
                          ),
                          backgroundColor: coffeeDarkBrown,
                        ),
                        child: Container(
                          alignment: Alignment.center,
                          width: 120,
                          height: 50,
                          child: Text(
                            '비밀번호 바꾸기',
                            style: textStyle15,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                InkWell(
                  onTap: () {
                    User newuser = User(id, pass, namecontroller.text);
                    userservice.updateUser(newuser).then((value) {
                      if (value == 'true') {
                        preference.then((_) {
                          _.setString('id', id);
                          _.setString('pass', pass);
                        });
                        Navigator.pop(context);
                        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => PageRouter()));
                      }
                    });
                  },
                  child: Container(
                    alignment: Alignment.center,
                    margin: EdgeInsets.symmetric(horizontal: 30, vertical: 15),
                    color: coffeeBackground,
                    height: 40,
                    child: Text(
                      '확인',
                      style: textStyle15,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
