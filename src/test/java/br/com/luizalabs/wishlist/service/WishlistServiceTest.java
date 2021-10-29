package br.com.luizalabs.wishlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.payload.CreateWishlistPayload;
import br.com.luizalabs.wishlist.domain.payload.UpdateWishlistPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.domain.search.WishlistSearchParams;
import br.com.luizalabs.wishlist.exception.WishlistAlreadyExistsException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.repository.WishlistRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WishlistServiceTest {

    @RegisterExtension
    static MockGenerator mockGenerator = MockGenerator.instance();
    private final ObjectId id = new ObjectId("61701da494bdec3eec35d8ff");
    @Mock
    WishlistRepository repository;
    @InjectMocks
    private WishlistService service;
    private CreateWishlistPayload createWishlistPayload;
    private UpdateWishlistPayload updateWishlistPayload;
    private Wishlist wishlist;

    @BeforeEach
    public void beforeEach() {
        createWishlistPayload = mockGenerator.generateFromJson("createPayload").as(CreateWishlistPayload.class);
        updateWishlistPayload = mockGenerator.generateFromJson("updatePayload").as(UpdateWishlistPayload.class);

        wishlist = mockGenerator.generateFromJson("wishlist").as(Wishlist.class);

        reset(repository);
    }

    @Test
    void createWithSuccess() {
        when(repository.existsByName(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(wishlist);

        assertResult(service.create(createWishlistPayload));

        verify(repository).existsByName(any());
        verify(repository).save(any());
    }

    @Test
    void createWithWishlistAlreadyExistsException() {
        when(repository.existsByName(any())).thenReturn(true);

        assertThrows(WishlistAlreadyExistsException.class, () -> service.create(createWishlistPayload));

        verify(repository).existsByName(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(wishlist));
        when(repository.save(any())).thenReturn(wishlist);

        assertResult(service.update(id, updateWishlistPayload));
        verify(repository).findById(id);
        verify(repository).save(any());
    }

    @Test
    void updateWithWishlistNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> service.update(id, updateWishlistPayload));

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(wishlist));

        assertResult(service.findById(id));

        verify(repository).findById(id);
    }

    @Test
    void FindByIdWithWishlistNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> service.findById(id));

        verify(repository).findById(id);
    }

    @Test
    void deleteWithSuccess() {
        when(repository.findById(id)).thenReturn(Optional.of(wishlist));

        final var captor = ArgumentCaptor.forClass(Wishlist.class);

        service.delete(id);

        verify(repository).findById(id);
        verify(repository).delete(captor.capture());

        assertEquals(captor.getValue().getId(), id);
    }

    @Test
    void deleteWithWishlistNotFoundException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> service.delete(id));

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void findAllWithSuccess() {
        Pageable page = PageRequest.of(0, 1);
        WishlistSearchParams params = new WishlistSearchParams();
        params.setName(wishlist.getName());

        Page<Wishlist> wishlistPage = new PageImpl<>(List.of(wishlist), page,
                Integer.MAX_VALUE);

        when(repository.findAll(any(), any(Pageable.class))).thenReturn(wishlistPage);
        var response = service.findAll(page, params);

        assertNotNull(response);
        assertResult(response.getContent().get(0));
        assertEquals(response.getContent().size(), page.getPageSize());
    }


    public void assertResult(WishlistResponse result) {
        assertNotNull(result);
        assertEquals(result.getId(), id.toHexString());
        assertEquals(result.getName(), wishlist.getName());
    }
}