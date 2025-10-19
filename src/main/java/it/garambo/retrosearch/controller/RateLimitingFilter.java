package it.garambo.retrosearch.controller;

import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class RateLimitingFilter implements Filter {

  private final Bucket rateLimitingBucket;

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (rateLimitingBucket.tryConsume(1)) {
      filterChain.doFilter(servletRequest, servletResponse);
    }

    log.info("Too many requests!");
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
    httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    httpResponse.getWriter().write("Too many requests.");
  }
}
