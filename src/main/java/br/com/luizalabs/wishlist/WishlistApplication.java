package br.com.luizalabs.wishlist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class WishlistApplication {

  public static void main(String[] args) {
    SpringApplication.run(WishlistApplication.class, args);
  }

}