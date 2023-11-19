package com.ssafy.cafe.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.cafe.model.dto.FileConverter;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.UploadFile;
import com.ssafy.cafe.model.service.ProductService;

import io.swagger.annotations.ApiOperation;



@RestController
@RequestMapping("/rest/product")
@CrossOrigin("*")
public class ProductRestController {
	
	
	private static final Logger log = LoggerFactory.getLogger(ProductRestController.class);

	
    @Autowired
    ProductService pService;
    
    @PostMapping
    @ApiOperation(value = "상품을 추가한다")
    public boolean addProduct(
    		@RequestPart("file") MultipartFile file, 
    		@RequestPart("name") String name,
    		@RequestPart("type") String type,
    		@RequestPart("price") String price
    		) {
    	try {
			UploadFile ufile = FileConverter.storeFile(file, "/menu/");
			Product tmp = new Product(name, type, Integer.parseInt(price));
			tmp.setImg(ufile.getStoreImgName());
			tmp.setOriginImgName(ufile.getOriginImgName());
			pService.add(tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return true;
    	
    }
    
    @PutMapping("/includeImg")
    @ApiOperation(value="이미지를 포함한 상품 정보를 업데이트 한다.", response = Boolean.class)
    public Boolean updateProduct(
    		@RequestPart("file") MultipartFile file, 
    		@RequestPart("id") String id,
    		@RequestPart("name") String name,
    		@RequestPart("type") String type,
    		@RequestPart("price") String price,
    		@RequestPart("isSalable") String isSalable
    		) {
    	
    	try {
			UploadFile ufile = FileConverter.storeFile(file, "/menu/");
			boolean boolSalable = true;
			if(isSalable.equals("0")) {
				boolSalable = false;
			}
			Product tmp = new Product(Integer.parseInt(id), name, type, Integer.parseInt(price), boolSalable);
			tmp.setImg(ufile.getStoreImgName());
			tmp.setOriginImgName(ufile.getOriginImgName());
			pService.updateProduct(tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return true;
    }
    
    
    @PutMapping
    @ApiOperation(value="이미지를 제외한 상품 정보를 업데이트한다.", response = Boolean.class)
    public Boolean updateProduct(@RequestBody Product product) {
    	pService.updateProduct(product);
    	return true;
    }
    
    
    @GetMapping()
    @ApiOperation(value="전체 상품의 목록을 반환한다.", response = List.class)
    public ResponseEntity<List<Product>> getProductList(){
        return new ResponseEntity<List<Product>>(pService.getProductList(), HttpStatus.OK);
    }
    
    @GetMapping("/{productId}")
    @ApiOperation(value="{productId}에 해당하는 상품의 정보를 comment와 함께 반환한다."
            + "이 기능은 상품의 comment를 조회할 때 사용된다.", response = List.class)
    public List<Map<String, Object>> getProductWithComments(@PathVariable Integer productId){
        return pService.selectWithComment(productId);
    }
    
    
    
}
