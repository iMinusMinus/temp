package ${package}.web;

import java.io.IOException;

import javax.portlet.annotations.PortletLifecycleFilter;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.HeaderFilter;
import javax.portlet.filter.HeaderFilterChain;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;
import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import lombok.extern.slf4j.Slf4j;

#if($configType.contains('@java'))
@PortletLifecycleFilter(portletNames = {"*"})
#end
@Slf4j
public class PortletFilterTest implements ActionFilter, EventFilter, HeaderFilter, RenderFilter, ResourceFilter {

    private String filterName;

    private String value;

    @Override
    public void init(FilterConfig filterConfig) throws PortletException {
        this.filterName = filterConfig.getFilterName();
        this.value = filterConfig.getInitParameter("sampleInitParameterKey");
        log.debug("init parameter keys: {}", filterConfig.getInitParameterNames());
    }

    @Override
    public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain) throws IOException, PortletException {
        log.debug("action parameters: {}", request.getActionParameters());
        chain.doFilter(request, response);
        log.debug("action render parameters: {}", response.getRenderParameterMap());
    }

    @Override
    public void doFilter(EventRequest request, EventResponse response, FilterChain chain) throws IOException, PortletException {
        log.debug("request event: {}", request.getEvent());
        chain.doFilter(request, response);
        log.debug("event render parameters: {}", response.getRenderParameterMap());
    }

    public void doFilter(HeaderRequest request, HeaderResponse response, HeaderFilterChain chain) throws IOException, PortletException {
        log.debug("header parameters: {}", request);
        chain.doFilter(request, response);
        log.debug("header property names: {}",response.getPropertyNames());
    }

    @Override
    public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
        log.debug("render preference: {}", request.getPreferences());
        chain.doFilter(request, response);
        log.debug("render property names: {}", response.getPropertyNames());
    }

    @Override
    public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException, PortletException {
        log.debug("resource parameters: {}", request.getResourceParameters());
        chain.doFilter(request, response);
        log.debug("resource property names: {}", response.getPropertyNames());
    }

    @Override
    public void destroy() {
        log.info("filter '{}' destroy", filterName);
    }

}