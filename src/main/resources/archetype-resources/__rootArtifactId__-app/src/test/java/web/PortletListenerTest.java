package ${package}.web;

import javax.portlet.ActionURL;
#if($configType.contains('@java'))
import javax.portlet.annotations.PortletListener;
#end
import javax.portlet.PortletURLGenerationListener;
import javax.portlet.RenderURL;
import javax.portlet.ResourceURL;

import lombok.extern.slf4j.Slf4j;

#if($configType.contains('@java'))
@PortletListener
#end
@Slf4j
public class PortletListenerTest implements PortletURLGenerationListener<RenderURL, ActionURL> {

    @Override
    public void filterActionURL(ActionURL actionURL) {
        log.debug("action URL parameters: {}", actionURL.getActionParameters());
    }

    @Override
    public void filterRenderURL(RenderURL renderURL) {
        log.debug("reder URL fragment identifier: {}", renderURL.getFragmentIdentifier());
    }

    @Override
    public void filterResourceURL(ResourceURL resourceURL) {
        log.debug("resource URL parameters: {}", resourceURL.getResourceParameters());
    }

}