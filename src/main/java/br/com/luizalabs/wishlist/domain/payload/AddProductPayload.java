package br.com.luizalabs.wishlist.domain.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductPayload {

  @Schema(title = "Id of the customer that own the Wishlist.", required = true)
  @Size(max = 20, message = "{AddProductPayload.customerId.size}")
  @NotBlank(message = "{AddProductPayload.customerId.notBlank}")
  private String customerId;


  @Schema(title = "Id of a product that will be added to the Wishlist", required = true)
  @NotBlank(message = "{AddProductPayload.productId.notBlank}")
  private String productId;
}