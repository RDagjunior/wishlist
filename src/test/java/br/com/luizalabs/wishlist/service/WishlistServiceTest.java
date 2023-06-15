package br.com.luizalabs.wishlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.exception.ProductAlreadyOnWishListException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.exception.WishlistTooBigException;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.repository.WishlistRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WishlistServiceTest {

  @RegisterExtension
  static MockGenerator mockGenerator = MockGenerator.instance();
  private final String customerId = "customerId";
  private final String productId = "productId1";
  @Mock
  WishlistRepository repository;
  @InjectMocks
  private WishlistService service;
  private Wishlist wishlist;
  private AddProductPayload productPayload;


  @BeforeEach
  public void beforeEach() {
    productPayload = mockGenerator.generateFromJson("addProductPayload")
        .as(AddProductPayload.class);
    wishlist = mockGenerator.generateFromJson("wishlist").as(Wishlist.class);

    reset(repository);
  }

  @Test
  void addProductInNotExistingWishlistWithSuccess() {
    when(repository.save(any())).thenReturn(wishlist);
    when(repository.existsByCustomerId(productPayload.getCustomerId())).thenReturn(false);

    assertResult(service.addProduct(productPayload), 2);

    verify(repository).existsByCustomerId(any());
    verify(repository).save(any());
  }

  @Test
  void addProductInExistingWishlistWithSuccess() {
    when(repository.save(any())).thenReturn(wishlist);
    when(repository.existsByCustomerId(productPayload.getCustomerId())).thenReturn(true);
    when(repository.findByCustomerId(productPayload.getCustomerId())).thenReturn(
        Optional.of(wishlist));

    assertResult(service.addProduct(productPayload), 3);

    verify(repository).existsByCustomerId(any());
    verify(repository).save(any());
  }

  @Test
  void addProductThatAlreadyExistsInExistingWishlistAndExpectProductAlreadyOnWishListException() {
    when(repository.existsByCustomerId(productPayload.getCustomerId())).thenReturn(true);
    when(repository.findByCustomerId(productPayload.getCustomerId())).thenReturn(
        Optional.of(wishlist));

    productPayload.setProductId("productId1");
    assertThrows(ProductAlreadyOnWishListException.class, () -> service.addProduct(productPayload));

    verify(repository).existsByCustomerId(any());
    verify(repository, never()).save(any());
  }

  @Test
  void addProductInExistingWishlistAndExpectWishlistTooBigException() {
    when(repository.existsByCustomerId(productPayload.getCustomerId())).thenReturn(true);
    when(repository.findByCustomerId(productPayload.getCustomerId())).thenReturn(
        Optional.of(wishlist));
    for (int i = 0; i < 18; i++) {
      wishlist.getProducts().add("product" + i);
    }
    assertThrows(WishlistTooBigException.class, () -> service.addProduct(productPayload));

    verify(repository).existsByCustomerId(any());
    verify(repository, never()).save(any());
  }

  @Test
  void findWishlistByCustomerIdWithSuccess() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

    assertResult(service.findById(customerId), 2);
  }

  @Test
  void findNonExistentWishlistByCustomerIdAndExpectWishlistNotFoundException() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.empty());

    assertThrows(WishlistNotFoundException.class, () -> service.findById(customerId));
  }

  @Test
  void removeExistingProductFromCustomerWishlistWithSuccess() {
    when(repository.save(any())).thenReturn(wishlist);
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

    assertResult(service.removeProductFromCustomerWishlist(customerId, productId), 1);
    verify(repository).save(any());
  }

  @Test
  void removeNonExistingProductFromCustomerWishlistWithSuccess() {
    when(repository.save(any())).thenReturn(wishlist);
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

    assertResult(service.removeProductFromCustomerWishlist(customerId, "product"), 2);
    verify(repository).save(any());
  }

  @Test
  void removeProductFromCustomerNotFoundWishlistAndExpectWishlistNotFoundException() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.empty());

    assertThrows(WishlistNotFoundException.class,
        () -> service.removeProductFromCustomerWishlist(customerId, productId));

    verify(repository, never()).save(any());
  }

  @Test
  void hasProductWithExistingProductOnCustomerWishList() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

    assertTrue(service.hasProduct(customerId, productId));

    verify(repository, never()).save(any());
  }

  @Test
  void hasProductWithNonExistingProductOnCustomerWishList() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.of(wishlist));

    assertFalse(service.hasProduct(customerId, "product"));

    verify(repository, never()).save(any());
  }

  @Test
  void hasProductWithOnCustomerWithoutWishlistAndExpectWishlistNotFoundException() {
    when(repository.findByCustomerId(customerId)).thenReturn(Optional.empty());

    assertThrows(WishlistNotFoundException.class, () -> service.hasProduct(customerId, productId));

    verify(repository, never()).save(any());
  }

  public void assertResult(WishlistResponse result, int expectedListSize) {
    assertNotNull(result);
    assertEquals(wishlist.getCustomerId(), result.getCustomerId());
    assertEquals(wishlist.getProducts(), result.getProducts());
    assertEquals(expectedListSize, result.getProducts().size());

  }
}