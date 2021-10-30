package br.com.luizalabs.wishlist.controller;

import static org.mockito.Mockito.reset;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.exception.handler.ExceptionHandlerController;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.helper.TestMessageSource;
import br.com.luizalabs.wishlist.service.WishlistService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    private static final String BASE_URL = "/wishlists/";
    @RegisterExtension
    static MockGenerator mockGenerator = MockGenerator.instance();
    private final ObjectId id = new ObjectId();
    @MockBean
    private WishlistService service;
    private MockMvc mockMvc;
    private Wishlist wishlist;
    private WishlistResponse wishlistResponse;

    @BeforeEach
    private void beforeEach() {

        wishlist = mockGenerator.generateFromJson("wishlist").as(Wishlist.class);
        wishlistResponse = new WishlistResponse(wishlist);

        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.standaloneSetup(new WishlistController(service))
                    .setControllerAdvice(new ExceptionHandlerController(new TestMessageSource("Product Test")))
                    .setMessageConverters(mockGenerator.getHttpMessageConverter())
                    .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                    .build();
        }

        reset(service);
    }
}