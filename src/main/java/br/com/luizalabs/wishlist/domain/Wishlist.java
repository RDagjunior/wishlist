package br.com.luizalabs.wishlist.domain;

import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wishlists")
public class Wishlist {

  @Id
  private ObjectId id;
  private String customerId;
  private List<String> products;
  @CreatedDate
  private OffsetDateTime createdAt;

  @LastModifiedDate
  private OffsetDateTime lastModifiedAt;


  public static Wishlist from(AddProductPayload payload) {
    return Wishlist.builder()
        .products(List.of(payload.getProductId()))
        .customerId(payload.getCustomerId())
        .build();
  }

}

