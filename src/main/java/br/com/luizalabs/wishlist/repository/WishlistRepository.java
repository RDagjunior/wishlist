package br.com.luizalabs.wishlist.repository;

import br.com.luizalabs.wishlist.domain.Wishlist;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, ObjectId> {

  boolean existsByCustomerId(String customerId);

  Optional<Wishlist> findByCustomerId(String customerId);

}

