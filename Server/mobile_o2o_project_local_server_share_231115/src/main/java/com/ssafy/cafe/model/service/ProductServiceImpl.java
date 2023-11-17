package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.ProductDao;
import com.ssafy.cafe.model.dto.Product;

/**
 * @author taeshik.heo
 * @since 2021. 6. 23.
 */
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductDao pDao;

    @Override
    public List<Product> getProductList() {
        return pDao.selectAll();
    }

    @Override
    public List<Map<String, Object>> selectWithComment(Integer productId) {
        return pDao.selectWithComment(productId);
    }

	@Override
	public void add(Product product) {
		pDao.insert(product);
		
	}
    
}
