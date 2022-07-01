package fr.lba.sbh.commons;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class NotRetryableServerException extends HttpServerErrorException {

    private static final long serialVersionUID = -6295871319577919217L;

    public NotRetryableServerException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset) {
        super(statusCode, statusText, responseBody, responseCharset);
    }

    public NotRetryableServerException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody,
            Charset responseCharset) {
        super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
    }

    public NotRetryableServerException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public NotRetryableServerException(HttpStatus statusCode) {
        super(statusCode);
    }

    @Override
    public String getMessage() {
        return getResponseBodyAsString();
    }

}
