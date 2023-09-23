// Purpose: DTO for ProductRequest object to be used in ProductController.java


package ca.gbc.productservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
}
