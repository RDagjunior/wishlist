package br.com.luizalabs.wishlist.domain.payload;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductPayload {

    @ApiModelProperty(value = "Id of the customer that own the Wishlist.", required = true)
    @Size(max = 120, message = "{AddProductPayload.customerId.size}")
    @NotBlank(message = "{AddProductPayload.customerId.notBlank}")
    private String customerId;


    @ApiModelProperty(value = "Id of a product that will be added to the Wishlist", required = true)
    @NotBlank(message = "{AddProductPayload.productId.notBlank}")
    private String productId;
}