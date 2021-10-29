package br.com.luizalabs.wishlist.domain.payload;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateWishlistPayload {

    @ApiModelProperty(value = "Wishlist's customer.", required = true)
    @Size(max = 120, message = "{Wishlist.customerId.size}")
    @NotBlank(message = "{Wishlist.customerId.notBlank}")
    private String customerId;


    @ApiModelProperty(value = "Wishlist's list of products.", required = true)
    @NotNull(message = "{Wishlist.products.notNull}")
    private List<@NotBlank String> products;
}