package model.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private String uuid;
    private String name;
    private Double price;
    private Integer quantity;
    private Category category;
}
