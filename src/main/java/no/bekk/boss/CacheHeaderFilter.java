package no.bekk.boss;

import org.joda.time.DateTime;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Add cache headers to responses
 *
 * init-param configuration:
 *
 * cache-control : private|public
 * ttl : time to live in seconds
 */
public class CacheHeaderFilter implements Filter {

    public static final String CACHE_CONTROL = "cache-control";
    public static final String TTL = "ttl"; // Seconds

    private String cacheControl;
    private int ttl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String cacheControl = filterConfig.getInitParameter(CACHE_CONTROL);
        String ttl = filterConfig.getInitParameter(TTL);

        verifyConfiguration(cacheControl, ttl);

        configureCacheControl(cacheControl);

        configureTtl(ttl);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        DateTime dateTime = new DateTime();
        DateTime dateTimePlusTtl = dateTime.plusSeconds(this.ttl);

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setHeader("Cache-Control", this.cacheControl + ", max-age=" + this.ttl);
        res.setDateHeader("Expires", dateTimePlusTtl.getMillis());

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

    private void verifyConfiguration(String cacheControl, String ttl) throws ServletException {
        if (cacheControl == null || ttl == null) {
            throw new ServletException("CacheHeaderFilter not configured correctly - " +
                    "please specify cache-control (public or private) and ttl (seconds) filter init parameters");
        }
    }

    private void configureTtl(String ttl) throws ServletException {
        try {
            this.ttl = Integer.parseInt(ttl);
        } catch (NumberFormatException e) {
            throw new ServletException("Specify a valid long for " + TTL, e);
        }
    }

    private void configureCacheControl(String cacheControl) throws ServletException {
        if (!cacheControl.equalsIgnoreCase("private") && !cacheControl.equalsIgnoreCase("public")) {
            throw new ServletException("Specify either private or public for " + CACHE_CONTROL);
        }

        this.cacheControl = cacheControl;
    }

}
