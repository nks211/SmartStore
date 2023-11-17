import 'package:flutter/material.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:smart_store_flutter_starter/dto/OrderDetailitem.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

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

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      body: ListView(
        shrinkWrap: true,
        children: [
          Container(
            margin: EdgeInsets.only(bottom: 20),
            padding: EdgeInsets.symmetric(vertical: 10),
            height: 200,
            color: menuBackground,
            child: Image.asset('assets/${widget.menuitem.img}', width: double.maxFinite,),
          ),
          Container(
              margin: EdgeInsets.all(20),
              child: Text('${widget.menuitem.name}', style: textStyle30,)
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
                    Text('가격', style: textStyle20B,),
                    Text('${widget.menuitem.price}원', style: textStyle20,)
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('수량', style: textStyle20B,),
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
                            child: Image.asset("assets/minus.png", width: 30, color: coffeePointRed,),
                          ),
                          Text(count.toString(), style: textStyle20,),
                          InkWell(
                            onTap: () {
                              setState(() {
                                count++;
                              });
                            },
                            child: Image.asset("assets/add.png", width: 30, color: coffeePointRed,),
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
                Text('평점 ${rate}점', style: textStyle20,),
                InkWell(
                  onTap: () {
                    showDialog(context: context,
                        builder: (context) {
                          return AlertDialog(
                            content: Container(
                              height: 70,
                              child: Column(
                                children: [
                                  Text('별점선택', style: textStyle20B,),
                                  RatingBar.builder(
                                      initialRating: 3,
                                      minRating: 0.5,
                                      allowHalfRating: true,
                                      direction: Axis.horizontal,
                                      itemCount: 5,
                                      itemBuilder: (context, _) => Icon(
                                        Icons.star, color: coffeePointRed,
                                      ),
                                      onRatingUpdate: (rating) {
                                        setState(() {
                                          rate = rating;
                                        });
                                      }
                                  ),
                                ],
                              ),
                            ),
                            actions: [
                              TextButton(
                                  onPressed: (){
                                    Navigator.pop(context);
                                  },
                                  child: Text('취소', style: textStyle15,)
                              ),
                              TextButton(
                                  onPressed: (){
                                    setState(() {
                                      ratebar = rate;
                                    });
                                    Navigator.pop(context);
                                  },
                                  child: Text('확인', style: textStyle15,)
                              ),
                            ],
                          );
                    });
                  },
                  child: RatingBarIndicator(
                    rating: ratebar,
                    direction: Axis.horizontal,
                    itemCount: 5,
                    itemBuilder: (context, _) => Icon(Icons.star, color: coffeePointRed,),
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
                  onPressed: (){
                    setState(() {
                      if (index < 0) {
                        setState(() {
                          comment.insert(0, controller.text);
                        });
                      }
                      else {
                        setState(() {
                          comment[index] = controller.text;
                          edited = false;
                          index = -1;
                        });
                      }
                      focusmode.unfocus();
                      controller.text = '';
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: menuBackground,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(30)),
                    ),
                    minimumSize: Size(100, 50),
                  ),
                  child: Text('등록', style: textStyle15.apply(color: coffeeBrown),)
              ),
            ],
          ),
          SizedBox(
            height: 150,
            child: ListView.builder(
              shrinkWrap: true,
              itemCount: comment.length,
              itemBuilder: (BuildContext context, int position){
                return Container(
                  margin: EdgeInsets.all(10),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(comment[position], style: textStyle15,),
                      Container(
                        width: 70,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            CircleAvatar(
                              radius: 15,
                              backgroundColor: coffeeDarkBrown,
                              child: IconButton(
                                onPressed: () {
                                  setState(() {
                                    edited = !edited;
                                  });
                                  if (edited) {
                                    setState(() {
                                      index = position;
                                    });
                                    focusmode.requestFocus();
                                  }
                                  else {
                                    focusmode.unfocus();
                                  }
                                },
                                padding: EdgeInsets.zero,
                                icon: Icon(edited? Icons.undo : Icons.edit, color: coffeeBrown,),
                              ),
                            ),
                            CircleAvatar(
                              radius: 15,
                              backgroundColor: coffeeDarkBrown,
                              child: IconButton(
                                onPressed: () {
                                  setState(() {
                                    comment.removeAt(position);
                                  });
                                },
                                padding: EdgeInsets.zero,
                                icon: Icon(Icons.close, color: coffeeBrown,),
                                color: coffeeBrown,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
          ),
          ElevatedButton(
              onPressed: () {
                showToast("상품이 장바구니에 담겼습니다.");
                var img = widget.menuitem.img;
                var id = widget.menuitem.id;
                var name = widget.menuitem.name;
                var price = widget.menuitem.price;
                var quantity = count;
                OrderDetailitem detailitem = OrderDetailitem(img, id, name, price, quantity, price * quantity);
                Navigator.pop(context, detailitem);
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: coffeePointRed,
              ),
              child: Container(
                alignment: Alignment.center,
                child: Text('담기', style: textStyle15.apply(color: Colors.white),),
              )
          )
        ],
      ),
    );
  }
}
