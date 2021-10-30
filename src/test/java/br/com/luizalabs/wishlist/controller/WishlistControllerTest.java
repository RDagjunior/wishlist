package br.com.luizalabs.wishlist.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.payload.AddProductPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.exception.ProductAlreadyOnWishListException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.exception.WishlistTooBigException;
import br.com.luizalabs.wishlist.exception.handler.ExceptionHandlerController;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.helper.TestMessageSource;
import br.com.luizalabs.wishlist.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    private static final String BASE_URL = "/customer-wishlists/";
    @RegisterExtension
    static MockGenerator mockGenerator = MockGenerator.instance();
    private final String customerId = "customerId";
    private final String productId = "productId";

    @MockBean
    private WishlistService service;
    private MockMvc mockMvc;
    private Wishlist wishlist;
    private WishlistResponse wishlistResponse;
    private AddProductPayload productPayload;

    @BeforeEach
    private void beforeEach() {
        productPayload = mockGenerator.generateFromJson("addProductPayload").as(AddProductPayload.class);
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

    @Test
    void createWithSuccessAndExpectsStatusCode201() throws Exception {
        when(service.addProduct(productPayload)).thenReturn(wishlistResponse);

        assertResult(mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(productPayload)))
                .andExpect(status().isCreated()));

        verify(service).addProduct(any());
    }

    @Test
    void createWithWishlistTooBigExceptionAndExpectsStatusCode400() throws Exception {
        when(service.addProduct(productPayload)).thenThrow(WishlistTooBigException.class);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(productPayload)))
                .andExpect(status().isBadRequest());

        verify(service).addProduct(any());
    }

    @Test
    void createWithInvalidPayloadAndExpectsStatusCode400() throws Exception {
        productPayload.setProductId(null);
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(productPayload)))
                .andExpect(status().isBadRequest());

        verify(service, never()).addProduct(any());
    }

    @Test
    void createWithProductAlreadyOnWishListExceptionAndExpectsStatusCode409() throws Exception {
        when(service.addProduct(productPayload)).thenThrow(ProductAlreadyOnWishListException.class);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(productPayload)))
                .andExpect(status().isConflict());

        verify(service).addProduct(any());
    }

    @Test
    void CreateWithInvalidPayloadAndExpectStatusCode400() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{aa:bb}"))
                .andExpect(status().isBadRequest());

        verify(service, never()).addProduct(any());
    }

    @Test
    void findByCustomerIdWithSuccessAndExpectStatusCode200() throws Exception {
        when(service.findById(customerId)).thenReturn(wishlistResponse);

        assertResult(
                mockMvc.perform(
                                get(BASE_URL.concat(customerId)).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()));

        verify(service).findById(customerId);
    }

    @Test
    void findByIdWithWishlistNotFoundExceptionAndExpectStatusCode404() throws Exception {
        when(service.findById(customerId)).thenThrow(WishlistNotFoundException.class);

        mockMvc.perform(
                        get(BASE_URL.concat(customerId)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).findById(customerId);
    }

    @Test
    void removeProductFromCustomerWishlistWithSuccessAndExpectStatusCode200() throws Exception {
        when(service.removeProductFromCustomerWishlist(customerId, productId)).thenReturn(wishlistResponse);

        assertResult(mockMvc.perform(
                        delete(BASE_URL.concat(customerId))
                                .queryParam("productId", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()));

        verify(service).removeProductFromCustomerWishlist(customerId, productId);
    }

    @Test
    void removeProductFromCustomerWishlistWithWishlistNotFoundExceptionAndExpectStatusCode404() throws Exception {
        doThrow(new WishlistNotFoundException()).when(service).removeProductFromCustomerWishlist(customerId, productId);

        mockMvc.perform(
                        delete(BASE_URL.concat(customerId))
                                .queryParam("productId", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).removeProductFromCustomerWishlist(customerId, productId);
    }

    @Test
    void hasProductWithSuccessAndExpectStatusCode200() throws Exception {
        when(service.hasProduct(customerId, productId)).thenReturn(true);

        mockMvc.perform(
                        get(BASE_URL.concat(customerId).concat("/existsProduct/").concat(productId))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).hasProduct(customerId, productId);
    }

    @Test
    void hasProductWithWishlistNotFoundExceptionAndExpectStatusCode404() throws Exception {
        doThrow(new WishlistNotFoundException()).when(service).hasProduct(customerId, productId);

        mockMvc.perform(
                        get(BASE_URL.concat(customerId).concat("/existsProduct/").concat(productId))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).hasProduct(customerId, productId);
    }


    private void assertResult(ResultActions resultActions) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId", is(wishlistResponse.getCustomerId())));

        for (int i = 0; i < wishlistResponse.getProducts().size(); i++) {
            resultActions.andExpect(jsonPath("$.products[" + i + "]", is(wishlistResponse.getProducts().get(i))));

        }
    }
}