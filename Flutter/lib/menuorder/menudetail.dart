import 'package:flutter/material.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/dto/OrderDetailitem.dart';
import 'package:smart_store/dto/ReComment.dart';
import 'package:smart_store/service/CommentService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/Comment.dart';
import '../dto/Product.dart';

class MenuDetail extends StatefulWidget {
  final Product menuitem;

  MenuDetail({required this.menuitem});

  @override
  State<MenuDetail> createState() => _MenuDetailState();
}

class _MenuDetailState extends State<MenuDetail> {

  int count = 1;
  double rate = 3.0;
  double ratebar = 3.0;
  TextEditingController controller = TextEditingController();
  int index = -1;
  FocusNode focusmode = FocusNode();
  bool edited = false;
  var commentservice = CommentService();
  List<Comment> comments = [];
  List<ReComment> recomments = [];
  List<bool> editmode = [];
  List<bool> showrecomments = [];
  Widget commentlist = Container(height: 150,);


  Future<bool> validmode(int position) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    return comments[position].userId == preferences.getString('id');
  }

  void allcomments() {
    commentservice.getproductcomments(widget.menuitem.id).then((value) {
      setState(() {
        comments = value;
        editmode = List.generate(comments.length, (index) => false);
        showrecomments = List.generate(comments.length, (index) => false);
      });
      commentservice.getaveragerating(widget.menuitem.id).then((value) {
        setState(() {
          ratebar = value;
        });
      });
    }).catchError((e) {
      setState(() {
        commentlist = Container(height: 150,);
      });
    });
  }

  void allrecomments(int productid) {
    commentservice.getRecomments(productid).then((value) {
      setState(() {
        recomments = value;
      });
    });
  }

  // comment와 recomment 값을 토대로 위젯 생성
  void commentload() {
    if (comments.length > 0) {
      setState(() {
        commentlist = SizedBox(
          height: 150,
          child: ListView.builder(
            shrinkWrap: true,
            itemCount: comments.length,
            itemBuilder: (BuildContext context, int position) {
              return Container(
                margin: EdgeInsets.all(10),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          comments[position].comment,
                          style: textStyle15,
                        ),
                        Container(
                          width: 100,
                          child: Row(
                            mainAxisAlignment:
                            MainAxisAlignment.spaceBetween,
                            children: [
                              FutureBuilder(future: validmode(position),
                                  builder: (BuildContext context, AsyncSnapshot snapshot) {
                                    if (snapshot.hasData == false) return Container();
                                    else if (snapshot.hasError) return Container();
                                    else return Visibility(
                                        visible: snapshot.data,
                                        child: CircleAvatar(
                                          radius: 15,
                                          backgroundColor: coffeeDarkBrown,
                                          child: IconButton(
                                            onPressed: () {
                                              setState(() {
                                                edited = !edited;
                                                editmode[position] =
                                                !editmode[position];
                                              });
                                              if (edited) {
                                                setState(() {
                                                  index = position;
                                                });
                                                focusmode.requestFocus();
                                              } else {
                                                index = -1;
                                                focusmode.unfocus();
                                              }
                                            },
                                            padding: EdgeInsets.zero,
                                            icon: Icon(
                                              editmode[position]
                                                  ? Icons.undo
                                                  : Icons.edit,
                                              color: coffeeBrown,
                                            ),
                                          ),
                                        ),
                                      );
                                  }),
                              FutureBuilder(future: validmode(position),
                                  builder: (BuildContext context, AsyncSnapshot snapshot) {
                                    if (snapshot.hasData == false) return Container();
                                    else if (snapshot.hasError) return Container();
                                    else return Visibility(
                                        visible: snapshot.data,
                                        child: CircleAvatar(
                                          radius: 15,
                                          backgroundColor: coffeeDarkBrown,
                                          child: IconButton(
                                            onPressed: () {
                                              setState(() {
                                                commentservice
                                                    .deleteComment(
                                                    comments[position].id)
                                                    .then((value) {
                                                  allcomments();
                                                  allrecomments(widget.menuitem.id);
                                                  commentload();
                                                });
                                              });
                                            },
                                            padding: EdgeInsets.zero,
                                            icon: Icon(
                                              Icons.close,
                                              color: coffeeBrown,
                                            ),
                                            color: coffeeBrown,
                                          ),
                                        ),
                                      );
                                  }),
                              CircleAvatar(
                                radius: 15,
                                backgroundColor: coffeeDarkBrown,
                                child: IconButton(
                                  onPressed: () {
                                    setState(() {
                                      showrecomments[position] =
                                      !showrecomments[position];
                                    });
                                  },
                                  padding: EdgeInsets.zero,
                                  icon: Icon(
                                    showrecomments[position]
                                        ? Icons.keyboard_arrow_up
                                        : Icons.keyboard_arrow_down,
                                    color: coffeeBrown,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    Visibility(
                      visible: showrecomments[position],
                      child: showreply(position),
                    ),
                  ],
                ),
              );
            },
          ),
        );
      });
    }
    else {
      setState(() {
        commentlist = Container(height: 150,);
      });
    }
  }

  // 확장 버튼 클릭 시 답글 표시 토글 처리
  Widget showreply(int index) {
    ReComment reComment = ReComment(0, 0, 0, '');
    for (var data in recomments) {
      if (data.commentId == comments[index].id) {
        reComment = data;
      }
    }

    return Container(
      alignment: Alignment.topLeft,
      width: double.maxFinite,
      color: Color(0x22999999),
      margin: EdgeInsets.symmetric(vertical: 10),
      padding: EdgeInsets.symmetric(
          horizontal: 20, vertical: 10),
      child: Text(reComment.comment, style: textStyle15,),
    );
  }

  @override
  void initState() {
    allcomments();
    allrecomments(widget.menuitem.id);
  }

  @override
  Widget build(BuildContext context) {

    commentload();

    return Scaffold(
      body: ListView(
        shrinkWrap: true,
        children: [
          Container(
            margin: EdgeInsets.only(bottom: 20),
            padding: EdgeInsets.symmetric(vertical: 10),
            height: 200,
            color: menuBackground,
            child: Image.network(
              BaseUrl + imagepath + widget.menuitem.img,
              width: double.maxFinite,
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Container(
                  margin: EdgeInsets.all(20),
                  child: Text(
                    '${widget.menuitem.name}',
                    style: textStyle30,
                  )),
              Container(
                margin: EdgeInsets.symmetric(horizontal: 20),
                child: CircleAvatar(
                  backgroundColor: coffeeDarkBrown,
                  child: IconButton(onPressed: (){
                    allcomments(); allrecomments(widget.menuitem.id); commentload(); },
                      padding: EdgeInsets.zero,
                      icon: Icon(Icons.refresh, color: coffeeBrown, size: 30,)),
                ),
              ),
            ],
          ),
          Container(
            height: 100,
            margin: EdgeInsets.symmetric(horizontal: 20, vertical: 20),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      '가격',
                      style: textStyle20B,
                    ),
                    Text(
                      '${widget.menuitem.price}원',
                      style: textStyle20,
                    )
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      '수량',
                      style: textStyle20B,
                    ),
                    Container(
                      width: 100,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          InkWell(
                            onTap: () {
                              if (count > 1) {
                                setState(() {
                                  count--;
                                });
                              }
                            },
                            child: Image.asset(
                              "assets/minus.png",
                              width: 30,
                              color: coffeePointRed,
                            ),
                          ),
                          Text(
                            count.toString(),
                            style: textStyle20,
                          ),
                          InkWell(
                            onTap: () {
                              setState(() {
                                count++;
                              });
                            },
                            child: Image.asset(
                              "assets/add.png",
                              width: 30,
                              color: coffeePointRed,
                            ),
                          ),
                        ],
                      ),
                    )
                  ],
                ),
              ],
            ),
          ),
          Container(
            margin: EdgeInsets.all(10),
            padding: EdgeInsets.all(10),
            color: menuBackground,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  '평점 ${ratebar}점',
                  style: textStyle20,
                ),
                InkWell(
                  onTap: () {
                    showDialog(
                        context: context,
                        builder: (context) {
                          return AlertDialog(
                            content: Container(
                              height: 70,
                              child: Column(
                                children: [
                                  Text(
                                    '별점선택',
                                    style: textStyle20B,
                                  ),
                                  RatingBar.builder(
                                      initialRating: 3,
                                      minRating: 0.5,
                                      allowHalfRating: true,
                                      direction: Axis.horizontal,
                                      itemCount: 5,
                                      itemBuilder: (context, _) => Icon(
                                            Icons.star,
                                            color: coffeePointRed,
                                          ),
                                      onRatingUpdate: (rating) {
                                        setState(() {
                                          rate = rating;
                                        });
                                      }),
                                ],
                              ),
                            ),
                            actions: [
                              TextButton(
                                  onPressed: () {
                                    Navigator.pop(context);
                                  },
                                  child: Text(
                                    '취소',
                                    style: textStyle15,
                                  )),
                              TextButton(
                                  onPressed: () {
                                    Navigator.pop(context);
                                  },
                                  child: Text(
                                    '확인',
                                    style: textStyle15,
                                  )),
                            ],
                          );
                        });
                  },
                  child: RatingBarIndicator(
                    rating: ratebar,
                    direction: Axis.horizontal,
                    itemCount: 5,
                    itemBuilder: (context, _) => Icon(
                      Icons.star,
                      color: coffeePointRed,
                    ),
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              Container(
                padding: EdgeInsets.all(5),
                width: 300,
                height: 50,
                child: TextField(
                  controller: controller,
                  focusNode: focusmode,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      borderSide: BorderSide(width: 1, color: coffeeBrown),
                    ),
                  ),
                ),
              ),
              ElevatedButton(
                  onPressed: () {
                    setState(() {
                      var mention = controller.text;
                      if (mention == '') {
                        showToast('리뷰를 최소 10자 이상 입력해주세요.');
                      } else {
                        Future<SharedPreferences> preferences =
                            SharedPreferences.getInstance();
                        preferences.then((value) {
                          String? id = value.getString('id');
                          if (id != null) {
                            var userid = id;
                            Comment review = Comment(
                                mention, 0, widget.menuitem.id, rate, userid);
                            if (edited) {
                              review.setid(comments[index].id);
                              commentservice
                                  .updateComment(review)
                                  .then((result) {
                                if (result == 'true') {
                                  setState(() {
                                    edited = false;
                                    editmode[index] = false;
                                    index = -1;
                                    allcomments();
                                    allrecomments(widget.menuitem.id);
                                    commentload();
                                  });
                                } else {
                                  showToast('error');
                                }
                              });
                            } else {
                              commentservice.makeComment(review).then((result) {
                                if (result == 'true') {
                                  setState(() {
                                    allcomments();
                                    allrecomments(widget.menuitem.id);
                                    commentload();
                                  });
                                } else {
                                  showToast('error');
                                }
                              });
                            }
                            focusmode.unfocus();
                            controller.text = '';
                          }
                        });
                      }
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: menuBackground,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(30)),
                    ),
                    minimumSize: Size(100, 50),
                  ),
                  child: Text(
                    '등록',
                    style: textStyle15.apply(color: coffeeBrown),
                  )),
            ],
          ),
          commentlist,
          ElevatedButton(
              onPressed: () {
                showToast("상품이 장바구니에 담겼습니다.");
                var img = widget.menuitem.img;
                var id = widget.menuitem.id;
                var name = widget.menuitem.name;
                var price = widget.menuitem.price;
                var quantity = count;
                OrderDetailitem detailitem = OrderDetailitem(
                    img, id, name, price, quantity, price * quantity);
                Navigator.pop(context, detailitem);
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: coffeePointRed,
              ),
              child: Container(
                alignment: Alignment.center,
                child: Text(
                  '담기',
                  style: textStyle15.apply(color: Colors.white),
                ),
              ))
        ],
      ),
    );
  }
}
