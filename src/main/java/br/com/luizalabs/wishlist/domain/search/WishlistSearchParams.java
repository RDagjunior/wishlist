package br.com.luizalabs.wishlist.domain.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistSearchParams {

    @ApiModelProperty("Search by wishlist's customer id")
    private String customerId;
}
