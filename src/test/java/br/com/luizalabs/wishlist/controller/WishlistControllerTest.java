package br.com.luizalabs.wishlist.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.luizalabs.wishlist.domain.Wishlist;
import br.com.luizalabs.wishlist.domain.payload.CreateWishlistPayload;
import br.com.luizalabs.wishlist.domain.payload.UpdateWishlistPayload;
import br.com.luizalabs.wishlist.domain.response.WishlistResponse;
import br.com.luizalabs.wishlist.domain.search.WishlistSearchParams;
import br.com.luizalabs.wishlist.exception.WishlistAlreadyExistsException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.exception.handler.ExceptionHandlerController;
import br.com.luizalabs.wishlist.helper.MockGenerator;
import br.com.luizalabs.wishlist.helper.TestMessageSource;
import br.com.luizalabs.wishlist.service.WishlistService;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    private CreateWishlistPayload createWishlistPayload;
    private UpdateWishlistPayload updateWishlistPayload;
    private MockMvc mockMvc;
    private Wishlist wishlist;
    private WishlistResponse wishlistResponse;

    @BeforeEach
    private void beforeEach() {
        createWishlistPayload = mockGenerator.generateFromJson("createPayload").as(CreateWishlistPayload.class);
        updateWishlistPayload = mockGenerator.generateFromJson("updatePayload").as(UpdateWishlistPayload.class);

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
    void createWithSuccessStatusCode201() throws Exception {
        when(service.create(createWishlistPayload)).thenReturn(wishlistResponse);

        assertResult(mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(createWishlistPayload)))
                .andExpect(status().isCreated()));

        verify(service).create(any());
    }

    @Test
    void createWithErrorWishlistAlreadyExistsExceptionStatusCode409() throws Exception {
        when(service.create(createWishlistPayload)).thenThrow(new WishlistAlreadyExistsException());

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(mockGenerator.asString(createWishlistPayload)))
                .andExpect(status().isConflict());

        verify(service).create(any());
    }

    @Test
    void testCreateWithInvalidPayloadStatusCode400() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("{aa:bb}"))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    void testUpdateWithSuccessStatusCode202() throws Exception {
        when(service.update(id, updateWishlistPayload)).thenReturn(wishlistResponse);

        assertResult(
                mockMvc.perform(
                                put(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON).content(mockGenerator.asString(
                                        updateWishlistPayload)))
                        .andExpect(status().isAccepted()));

        verify(service).update(id, updateWishlistPayload);
    }

    @Test
    void testUpdateWithErrorWishlistNotFoundExceptionStatusCode404() throws Exception {
        when(service.update(id, updateWishlistPayload)).thenThrow(new WishlistNotFoundException());

        mockMvc.perform(
                        put(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON).content(mockGenerator.asString(updateWishlistPayload)))
                .andExpect(status().isNotFound());

        verify(service).update(id, updateWishlistPayload);
    }

    @Test
    void findByIdWithSuccessStatusCode200() throws Exception {
        when(service.findById(id)).thenReturn(wishlistResponse);

        assertResult(
                mockMvc.perform(
                                get(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()));

        verify(service).findById(id);
    }

    @Test
    void findByIdWithErrorWishlistNotFoundExceptionStatusCode404() throws Exception {
        when(service.findById(id)).thenThrow(new WishlistNotFoundException());

        mockMvc.perform(
                        get(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).findById(id);
    }

    @Test
    void findByIdWithErrorInvalidIdStatusCode400() throws Exception {
        mockMvc.perform(
                        get(BASE_URL.concat("123456")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).findById(id);
    }

    @Test
    void deleteWithSuccessStatusCode202() throws Exception {
        doNothing().when(service).delete(id);

        mockMvc.perform(
                        delete(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(service).delete(id);
    }

    @Test
    void deleteWithErrorWishlistNotFoundExceptionStatusCode404() throws Exception {
        doThrow(new WishlistNotFoundException()).when(service).delete(id);

        mockMvc.perform(
                        delete(BASE_URL.concat(id.toString())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).delete(id);
    }

    @Test
    void deleteWithErrorInvalidIdStatusCode400() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("wrongId")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).delete(any());
    }

    @Test
    void findAllWithSuccessStatusCode200() throws Exception {
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<WishlistResponse> page = new PageImpl<>(List.of(new WishlistResponse(wishlist)), pageable, Integer.MAX_VALUE);

        given(service.findAll(any(Pageable.class), any(WishlistSearchParams.class))).willReturn(page);

        final WishlistSearchParams params = WishlistSearchParams.builder()
                .customerId("Test")
                .build();

        mockMvc.perform(get(BASE_URL)
                        .queryParam("customerId", params.getCustomerId())
                        .queryParam("page", String.valueOf(page.getNumber()))
                        .queryParam("size", String.valueOf(page.getSize())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(wishlistResponse.getId())))
                .andExpect(jsonPath("$.content[0].customerId", is(wishlistResponse.getCustomerId())))
                .andExpect(jsonPath("$.numberOfElements", is(1)));

        final ArgumentCaptor<WishlistSearchParams> searchParamsCaptor = ArgumentCaptor.forClass(WishlistSearchParams.class);
        final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(service, Mockito.times(1)).findAll(pageableCaptor.capture(), searchParamsCaptor.capture());

        final WishlistSearchParams searchParam = searchParamsCaptor.getValue();
        assertEquals(params.getCustomerId(), searchParam.getCustomerId());

        final Pageable pageableParam = pageableCaptor.getValue();
        assertEquals(pageable.getPageNumber(), pageableParam.getPageNumber());
        assertEquals(pageable.getPageSize(), pageableParam.getPageSize());
    }


    private void assertResult(ResultActions resultActions) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(wishlistResponse.getId())))
                .andExpect(jsonPath("$.customerId", is(wishlistResponse.getCustomerId())));
    }
}