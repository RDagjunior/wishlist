package br.com.luizalabs.wishlist.domain.response;

import br.com.luizalabs.wishlist.domain.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WishlistResponse {

    @JsonIgnore
    private final Wishlist wishlist;

    @ApiModelProperty("Wishlist's customer id")
    public String getCustomerId() {
        return wishlist.getCustomerId();
    }

    @ApiModelProperty("Wishlist's products")
    public List<String> getProducts() {
        return wishlist.getProducts();
    }
}
