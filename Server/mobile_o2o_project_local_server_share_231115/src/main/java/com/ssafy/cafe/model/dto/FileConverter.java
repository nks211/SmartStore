package com.ssafy.cafe.model.dto;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileConverter {

	public static UploadFile storeFile(MultipartFile file, String folder) throws IOException{
		if(file.isEmpty()) return null;
		String originName = file.getOriginalFilename();
		String storeName = UUID.randomUUID()+"."+extractExt(originName);
//		File saveFile = new File("C:/Users/SSAFY/Desktop/finalProject/finalproject/Server/mobile_o2o_project_local_server_share_231115/src/main/resources/static/imgs"+folder, storeName);
		File saveFile = new File("C:/temp/imgs"+folder, storeName);
//		file.transferTo();
		FileCopyUtils.copy(file.getBytes(), saveFile);
		return new UploadFile(originName, storeName);
	}
	
	private static String extractExt(String origin) {
        int pos = origin.lastIndexOf(".");
        return origin.substring(pos + 1);
    }
}
