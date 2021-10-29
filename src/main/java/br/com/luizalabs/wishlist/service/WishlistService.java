package br.com.luizalabs.wishlist.service;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.payload.CreateWishlistPayload;
import br.com.luizalabs.wishlist.domain.payload.UpdateWishlistPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.domain.search.WishlistSearchParams;
import br.com.luizalabs.wishlist.exception.WishlistAlreadyExistsException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistResponse create(CreateWishlistPayload payload) {
        log.info("Create Wishlist - Payload: {}", payload);
        if (repository.existsByCustomerId(payload.getCustomerId())) {
            throw new WishlistAlreadyExistsException();
        }
        return new WishlistResponse(repository.save(createModel(payload)));
    }


    public WishlistResponse update(ObjectId id, UpdateWishlistPayload payload) {
        log.info("Update Wishlist - Id: {} Payload: {}", id, payload);

        return repository.findById(id).map(wishlist -> repository.save(updateModel(payload, wishlist))
                ).map(WishlistResponse::new)
                .orElseThrow(WishlistNotFoundException::new);
    }

    public WishlistResponse findById(ObjectId id) {
        return new WishlistResponse(getWishlistById(id));
    }

    public void delete(ObjectId id) {
        log.info("Delete wishlist -  Id: {}", id);
        final var wishlist = getWishlistById(id);

        repository.delete(wishlist);
    }

    private Wishlist getWishlistById(ObjectId id) {
        return repository.findById(id).orElseThrow(WishlistNotFoundException::new);
    }

    public Page<WishlistResponse> findAll(Pageable pageable, WishlistSearchParams search) {
        return repository.findAll(example(search), pageable).map(WishlistResponse::new);
    }

    private Wishlist createModel(CreateWishlistPayload payload) {
        return Wishlist.builder()
                .customerId(payload.getCustomerId())
                .build();
    }

    private Wishlist updateModel(UpdateWishlistPayload payload, Wishlist model) {
        model.setCustomerId(payload.getCustomerId());
        return model;
    }

    private Wishlist filters(final WishlistSearchParams search) {
        return Wishlist.builder().customerId(search.getCustomerId())
                .build();
    }

    private Example<Wishlist> example(final WishlistSearchParams search) {
        return Example.of(filters(search));
    }

}
