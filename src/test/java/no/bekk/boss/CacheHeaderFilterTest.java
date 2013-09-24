package no.bekk.boss;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static no.bekk.boss.CacheHeaderFilter.CACHE_CONTROL;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CacheHeaderFilterTest {

    private CacheHeaderFilter filter;

    @Mock
    private FilterConfig filterConfigMock;

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    @Mock
    private FilterChain mockFilterChain;

    @BeforeClass
    public static void setUpClass() {
        DateTimeUtils.setCurrentMillisFixed(0);
    }

    @Before
    public void setUp() {
        this.filter = new CacheHeaderFilter();
    }

    @Test(expected = ServletException.class)
    public void shouldFailIfConfigurationIsMissing() throws ServletException {
        this.filter.init(filterConfigMock);
    }

    @Test
    public void shouldSetCorrectHttpHeaders() throws ServletException, IOException {
        configureCacheHeaders();
        this.filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        verify(mockHttpServletResponse).setHeader("Cache-Control", "public, max-age=600");
        verify(mockHttpServletResponse).setDateHeader("Expires", 600 * 1000);
        verify(mockFilterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    private void configureCacheHeaders() throws ServletException {
        when(filterConfigMock.getInitParameter(CACHE_CONTROL)).thenReturn("public");
        when(filterConfigMock.getInitParameter(CacheHeaderFilter.TTL)).thenReturn("600");
        this.filter.init(filterConfigMock);
    }

}
