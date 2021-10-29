package br.com.luizalabs.wishlist.domain.payload;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateWishlistPayload {

    @ApiModelProperty(value = "Wishlist's name.", required = true)
    @Size(max = 120, message = "{Wishlist.name.size}")
    @NotBlank(message = "{Wishlist.name.notBlank}")
    private String name;

}
