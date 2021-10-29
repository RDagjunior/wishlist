package br.com.luizalabs.wishlist.domain.payload;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateWishlistPayload {

    @ApiModelProperty(value = "Wishlist's customerId.", required = true)
    @Size(max = 120, message = "{Wishlist.customerId.size}")
    @NotBlank(message = "{Wishlist.customerId.notBlank}")
    private String customerId;

}
