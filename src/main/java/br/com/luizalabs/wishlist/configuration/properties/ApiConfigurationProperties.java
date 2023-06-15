package br.com.luizalabs.wishlist.configuration.properties;

import io.swagger.v3.oas.models.info.Contact;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("luizalabs.api-docs")
public class ApiConfigurationProperties {

  private String apiName;
  private String description;
  private String version;
  private String termsOfServiceUrl;
  private ContactConfiguration contact = new ContactConfiguration();
  private String license;
  private String licenseUrl;

  public Contact getContact() {
    return !contact.isEmpty()
        ? new Contact().name(contact.getName()).email(contact.getEmail()).url(contact.getUrl())
        : null;
  }

  @Data
  public static class ContactConfiguration {

    private String name;
    private String url;
    private String email;

    public boolean isEmpty() {
      return StringUtils.isBlank(name) && StringUtils.isBlank(url) && StringUtils.isBlank(email);
    }
  }

}
