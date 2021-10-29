package br.com.luizalabs.wishlist.controller;

import br.com.luizalabs.wishlist.api.WishlistApi;
import br.com.luizalabs.wishlist.domain.payload.CreateWishlistPayload;
import br.com.luizalabs.wishlist.domain.payload.UpdateWishlistPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.domain.search.WishlistSearchParams;
import br.com.luizalabs.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
public class WishlistController implements WishlistApi {

    private final WishlistService service;

    @Override
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public WishlistResponse create(@RequestBody CreateWishlistPayload payload) {
        return service.create(payload);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public WishlistResponse update(@PathVariable ObjectId id, @RequestBody UpdateWishlistPayload payload) {
        return service.update(id, payload);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public WishlistResponse findById(@PathVariable ObjectId id) {
        return service.findById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable ObjectId id) {
        service.delete(id);
    }

    @Override
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public Page<WishlistResponse> findAll(Pageable pageable, WishlistSearchParams search) {
        return service.findAll(pageable, search);
    }
}
