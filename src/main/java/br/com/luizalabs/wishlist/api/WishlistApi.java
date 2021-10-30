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

    @ApiOperation(value = "Create new Wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid payload value(s)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 409, message = "Wishlist already exists"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse addProduct(@ApiParam(required = true) @Valid AddProductPayload payload);

    @ApiOperation(value = "Find Wishlist by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = WishlistResponse.class),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse findById(@ApiParam(value = "Wishlist customer's id", required = true) @NotBlank String id);

    @ApiOperation(value = "Delete Wishlist by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse delete(@ApiParam(value = "Wishlist customer's id", required = true) @NotBlank String customerId, @NotBlank String productId);

    boolean hasProduct(@ApiParam(value = "Wishlist customer's id", required = true) @NotBlank String customerId,
            @ApiParam(value = "Product id to check if it is already on the wishlist", required = true) @NotBlank String productId);
}
