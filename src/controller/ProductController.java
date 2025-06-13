package controller;

import model.dto.ProductCreateDto;
import model.dto.ProductResponseDto;
import model.service.ProductServiceImpl;

import java.util.List;

public class ProductController {
    private final ProductServiceImpl productService = new ProductServiceImpl();

    public List<ProductResponseDto> getAllProducts() {
        return productService.findAll();
    }
    public ProductResponseDto getProductByUuid(String uuid){
        return productService.findByUuid(uuid);
    }
    public boolean insertNewProduct(ProductCreateDto productCreateDto) {
        return productService.save(productCreateDto);
    }
    public boolean deleteByUuid(String uuid) {
        return productService.deleteByUuid(uuid);
    }

    public void insertMultiProduct(Long numberOfProducts){
        productService.insertMultiProducts(numberOfProducts);
    }

    public List<ProductResponseDto> readMultiProduct(){
        return productService.readMultiProducts();
    }

    public void clearAllProducts(){
        productService.clearAllProducts();
    }
    public List<ProductResponseDto> searchByName(String name){
        return productService.searchByName(name);
    }
    public List<ProductResponseDto> filterByCategory(String category){
        return productService.filterByCategory(category);
    }
}
