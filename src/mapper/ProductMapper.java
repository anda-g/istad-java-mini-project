package mapper;

import model.dto.ProductCreateDto;
import model.dto.ProductResponseDto;
import model.entites.Product;

import java.util.UUID;

public class ProductMapper {
    public static ProductResponseDto mapProductToProductResponseDto(Product product) {
        return new ProductResponseDto(
                product.getUuid(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory()
        );
    }

    public static Product mapProductCreateDtoToProduct(ProductCreateDto productCreateDto) {
        return new Product(
                null,
                UUID.randomUUID().toString(),
                productCreateDto.name(),
                productCreateDto.price(),
                productCreateDto.quantity(),
                productCreateDto.category()
        );
    }
}
