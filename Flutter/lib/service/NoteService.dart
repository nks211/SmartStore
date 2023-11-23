import 'dart:convert';

import 'package:smart_store/util/common.dart';
import 'package:http/http.dart' as http;

import '../dto/Note.dart';

class NoteService {

  Future<String> sendNote(Note note) async {
    final String url = BaseUrl + 'rest/note';
    var response = await http.post(Uri.parse(url),
      headers: {
        "content-type": "application/json",
      },
      body: jsonEncode(note.toJson()),
    );
    return response.body;
  }

}