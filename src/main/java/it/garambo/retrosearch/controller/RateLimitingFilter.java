package it.garambo.retrosearch.controller;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

  private final Bucket rateLimitingBucket;
  private final List<String> excludedPaths;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    if (rateLimitingBucket.tryConsume(1)) {
      filterChain.doFilter(request, response);
      return;
    }

    log.warn("Too many requests!");
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.getWriter().write("Too many requests.");
  }

  @Override
  protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
    return excludedPaths.stream().anyMatch(e -> pathMatcher.match(e, request.getServletPath()));
  }
}
