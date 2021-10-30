package br.com.luizalabs.wishlist.api;


import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = "Wishlist Api")
public interface WishlistApi {

    @ApiOperation(value = "Add a product to a customer's wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product added to the wishlist"),
            @ApiResponse(code = 400, message = "Invalid payload value(s) or wishlist with the maximum accepted number of products"),
            @ApiResponse(code = 409, message = "Product already exists on this wishlist"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse addProduct(@ApiParam(required = true) @Valid AddProductPayload payload);

    @ApiOperation(value = "Find a customer's wishlist by his id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = WishlistResponse.class),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse findById(
            @ApiParam(value = "Wishlist customer's id", required = true) String customerId);

    @ApiOperation(value = "Delete a product from a customer's wishlist by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product deleted from wishlist", response = WishlistResponse.class),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse removeProductFromCustomerWishlist(
            @ApiParam(value = "Wishlist customer's id", required = true) String customerId,
            @NotBlank(message = "{AddProductPayload.productId.notBlank}") String productId);

    @ApiOperation(value = "Verify if a product is already on that customer's wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "True if the product is already on that customers wishlist, otherwise false", response = boolean.class),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    boolean hasProduct(
            @ApiParam(value = "Wishlist customer's id", required = true) @NotBlank(message = "{AddProductPayload.customerId.notBlank}") String customerId,
            @ApiParam(value = "Product id to check if it is already on the wishlist", required = true) @NotBlank String productId);
}
