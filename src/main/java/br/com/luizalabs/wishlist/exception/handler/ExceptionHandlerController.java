package br.com.luizalabs.wishlist.exception.handler;

import br.com.luizalabs.wishlist.domain.response.ErrorResponse;
import br.com.luizalabs.wishlist.exception.ProductAlreadyOnWishListException;
import br.com.luizalabs.wishlist.exception.WishlistNotFoundException;
import br.com.luizalabs.wishlist.exception.WishlistTooBigException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerController {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public Collection<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    return exception.getBindingResult()
        .getAllErrors()
        .stream()
        .map(violation -> ErrorResponse.as(
                messageSource.getMessage(violation, LocaleContextHolder.getLocale()))
            .tag(simpleKey(violation)))
        .toList();
  }

  @ExceptionHandler(WishlistNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ErrorResponse handleWishlistNotFoundException(WishlistNotFoundException exception) {
    return exceptionMessage(exception);
  }

  @ExceptionHandler(WishlistTooBigException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleWishlistTooBigException(WishlistTooBigException exception) {
    return exceptionMessage(exception);
  }

  @ExceptionHandler(ProductAlreadyOnWishListException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT)
  public ErrorResponse handleProductAlreadyOnWishListException(
      ProductAlreadyOnWishListException exception) {
    return exceptionMessage(exception);
  }

  private ErrorResponse exceptionMessage(Throwable throwable, Object... params) {
    return ErrorResponse.as(
        message(throwable.getClass().getSimpleName().concat(".message"), params));
  }

  private String simpleKey(ObjectError violation) {
    return violation instanceof FieldError fieldError ? (fieldError).getField()
        : violation.getObjectName();
  }

  private String message(String code, Object... params) {
    return messageSource.getMessage(code, params, LocaleContextHolder.getLocale());
  }
}
