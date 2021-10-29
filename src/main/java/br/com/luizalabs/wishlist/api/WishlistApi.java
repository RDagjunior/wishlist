package br.com.luizalabs.wishlist.api;


import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import br.com.luizalabs.wishlist.domain.payload.CreateWishlistPayload;
import br.com.luizalabs.wishlist.domain.payload.UpdateWishlistPayload;
import br.com.luizalabs.wishlist.domain.response.ErrorResponse;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.domain.search.WishlistSearchParams;
import br.com.luizalabs.wishlist.swagger.resource.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import springfox.documentation.annotations.ApiIgnore;

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
    WishlistResponse create(@ApiParam(required = true) @Valid CreateWishlistPayload payload);

    @ApiOperation(value = "Update wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Invalid id value or payload value(s)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse update(@ApiParam(value = "Wishlist hexadecimal id", required = true) ObjectId id,
            @ApiParam(required = true) @Valid UpdateWishlistPayload payload);

    @ApiOperation(value = "Find Wishlist by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = WishlistResponse.class),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    WishlistResponse findById(@ApiParam(value = "Wishlist hexadecimal id", required = true) ObjectId id);

    @ApiOperation(value = "Delete Wishlist by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Wishlist not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    void delete(@ApiParam(value = "Wishlist hexadecimal id", required = true) ObjectId id);

    @ApiOperation(value = "Finds all Wishlists", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of Wishlists returned with success", response = WishlistResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid parameter value was sent", response = ErrorResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "An unexpected error occurred", response = ErrorResponse.class)})
    @ApiPageable
    Page<WishlistResponse> findAll(@ApiIgnore @PageableDefault(direction = Sort.Direction.DESC, sort = "id") Pageable pageable, WishlistSearchParams search);
}
