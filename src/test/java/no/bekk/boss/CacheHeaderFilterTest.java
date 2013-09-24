package no.bekk.boss;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static no.bekk.boss.CacheHeaderFilter.CACHE_CONTROL;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

@RunWith(MockitoJUnitRunner.class)
public class CacheHeaderFilterTest {

    private CacheHeaderFilter filter;


    @Mock
    private FilterConfig filterConfigMock;

    @Before
    public void setUp() {
        this.filter = new CacheHeaderFilter();
    }

    @Test(expected = ServletException.class)
    public void shouldFailIfConfigurationIsMissing() throws ServletException {
       this.filter.init(filterConfigMock);
    }

    @Test
    public void shouldNotFailIfConfigurationIsOK() throws ServletException {
        when(filterConfigMock.getInitParameter(CACHE_CONTROL)).thenReturn("public");
        when(filterConfigMock.getInitParameter(CacheHeaderFilter.TTL)).thenReturn("600");
        this.filter.init(filterConfigMock);
    }



}
