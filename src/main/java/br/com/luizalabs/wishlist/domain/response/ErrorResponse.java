package br.com.luizalabs.wishlist.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = -6431868610599519417L;

	private String error;

	private String errorCode;

	private String errorDescription;

	public ErrorResponse code(String code) {
		this.errorCode = code;
		return this;
	}

	public ErrorResponse tag(String tag) {
		this.error = tag;
		return this;
	}

	public ErrorResponse description(String description) {
		this.errorDescription = description;
		return this;
	}

	public static ErrorResponse as(String description) {
		return new ErrorResponse().description(description);
	}
}

