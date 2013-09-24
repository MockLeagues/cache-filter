package no.bekk.boss;

import javax.servlet.*;
import java.io.IOException;

public class CacheHeaderFilter implements Filter {

    public static final String CACHE_CONTROL = "cache-control";
    public static final String TTL = "ttl"; // Seconds

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cacheControl = filterConfig.getInitParameter(CACHE_CONTROL);
        String ttl = filterConfig.getInitParameter(TTL);

        if (cacheControl == null || ttl == null) {
            throw new ServletException("CacheHeaderFilter not configures correctly");
        }

        if (!cacheControl.equalsIgnoreCase("private") && !cacheControl.equalsIgnoreCase("public")) {
            throw new ServletException("Specify either private or public for " + CACHE_CONTROL);
        }

        try {
            Long.parseLong(ttl);
        } catch (NumberFormatException e) {
            throw new ServletException("Specify a valid long for " + TTL, e);
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


    }

    @Override
    public void destroy() {
    }
}
