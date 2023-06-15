package br.com.luizalabs.wishlist.domain.response;

import br.com.luizalabs.wishlist.domain.Wishlist;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponse {

  private String id;
  private String customerId;
  private List<String> products;

  public static WishlistResponse from(Wishlist entity) {
    return WishlistResponse.builder()
        .id(entity.getId() != null ? entity.getId().toHexString() : null)
        .customerId(entity.getCustomerId())
        .products(entity.getProducts())
        .build();
  }

}
