package model.service;

import mapper.ProductMapper;
import model.dto.ProductCreateDto;
import model.dto.ProductResponseDto;
import model.entites.Product;
import model.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository = new ProductRepository();
    @Override
    public List<ProductResponseDto> findAll() {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        if (products != null) {
            products.forEach(product -> {
                productResponseDtoList.add(ProductMapper.mapProductToProductResponseDto(product));
            });
            return productResponseDtoList;
        }
        return null;
    }

    @Override
    public ProductResponseDto findByUuid(String uuid) {
        Product product = productRepository.findByUuid(uuid);
        if (product != null) {
            return ProductMapper.mapProductToProductResponseDto(product);
        }
        return null;
    }

    @Override
    public Boolean save(ProductCreateDto productCreateDto) {
        return productRepository.save(
                ProductMapper.mapProductCreateDtoToProduct(productCreateDto)
        );
    }

    @Override
    public Boolean deleteByUuid(String uuid) {
        return productRepository.deleteByUuid(uuid);
    }
}
