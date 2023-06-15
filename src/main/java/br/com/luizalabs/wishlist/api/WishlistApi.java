package br.com.luizalabs.wishlist.api;

import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@Tag(name = "Wishlist Api")
public interface WishlistApi {

  @Operation(summary = "Add a product to a customer's wishlist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Product added to the wishlist"),
      @ApiResponse(responseCode = "400", description = "Invalid payload value(s) or wishlist with the maximum accepted number of products"),
      @ApiResponse(responseCode = "409", description = "Product already exists on this wishlist"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  WishlistResponse addProduct(@Parameter(required = true) @Valid AddProductPayload payload);

  @Operation(summary = "Find a customer's wishlist by his id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ok"),
      @ApiResponse(responseCode = "400", description = "Invalid id value"),
      @ApiResponse(responseCode = "404", description = "Wishlist not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  WishlistResponse findById(
      @Parameter(description = "Wishlist customer's id", required = true) String customerId);

  @Operation(summary = "Delete a product from a customer's wishlist by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Product deleted from wishlist"),
      @ApiResponse(responseCode = "404", description = "Wishlist not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  WishlistResponse removeProductFromCustomerWishlist(
      @Parameter(description = "Wishlist customer's id", required = true) String customerId,
      @NotBlank(message = "{AddProductPayload.productId.notBlank}") String productId);

  @Operation(summary = "Verify if a product is already on that customer's wishlist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "True if the product is already on that customers wishlist, otherwise false"),
      @ApiResponse(responseCode = "404", description = "Wishlist not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  boolean hasProduct(
      @Parameter(description = "Wishlist customer's id", required = true) @NotBlank(message = "{AddProductPayload.customerId.notBlank}") String customerId,
      @Parameter(description = "Product id to check if it is already on the wishlist", required = true) @NotBlank String productId);
}

