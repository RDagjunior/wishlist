package br.com.luizalabs.wishlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.reset;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.repository.WishlistRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
    private Wishlist wishlist;

    @BeforeEach
    public void beforeEach() {

        wishlist = mockGenerator.generateFromJson("wishlist").as(Wishlist.class);

        reset(repository);
    }


    public void assertResult(WishlistResponse result) {
        assertNotNull(result);
        assertEquals(result.getId(), id.toHexString());
        assertEquals(result.getCustomerId(), wishlist.getCustomerId());
    }
}