package model.service;

import model.dto.ProductCreateDto;
import model.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    ProductResponseDto findByUuid(String uuid);
    Boolean save(ProductCreateDto productCreateDto);
    Boolean deleteByUuid(String uuid);
}
