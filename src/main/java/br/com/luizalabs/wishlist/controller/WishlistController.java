package br.com.luizalabs.wishlist.controller;

import br.com.luizalabs.wishlist.api.WishlistApi;
import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.service.WishlistService;
import br.com.luizalabs.wishlist.stopwatch.Stopwatch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-wishlists")
public class WishlistController implements WishlistApi {

  private final WishlistService service;

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  @Stopwatch
  public WishlistResponse addProduct(@RequestBody AddProductPayload payload) {
    return service.addProduct(payload);
  }

  @Override
  @GetMapping("/{customerId}")
  @ResponseStatus(code = HttpStatus.OK)
  @Stopwatch
  public WishlistResponse findById(@PathVariable String customerId) {
    return service.findById(customerId);
  }

  @Override
  @DeleteMapping("/{customerId}")
  @ResponseStatus(code = HttpStatus.OK)
  @Stopwatch
  public WishlistResponse removeProductFromCustomerWishlist(@PathVariable String customerId,
      @RequestParam String productId) {
    return service.removeProductFromCustomerWishlist(customerId, productId);
  }

  @Override
  @GetMapping("/{customerId}/existsProduct/{productId}")
  @ResponseStatus(code = HttpStatus.OK)
  @Stopwatch
  public boolean hasProduct(@PathVariable String customerId, @PathVariable String productId) {
    return service.hasProduct(customerId, productId);
  }
}
