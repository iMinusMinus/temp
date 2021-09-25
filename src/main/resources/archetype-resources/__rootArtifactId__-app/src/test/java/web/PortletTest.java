package ${package}.web;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.MimeResponse.Copy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
#if($configType.contains('@java'))
import javax.portlet.annotations.ActionMethod;
import javax.portlet.annotations.DestroyMethod;
import javax.portlet.annotations.EventDefinition;
import javax.portlet.annotations.EventMethod;
import javax.portlet.annotations.HeaderMethod;
import javax.portlet.annotations.InitMethod;
import javax.portlet.annotations.InitParameter;
import javax.portlet.annotations.LocaleString;
import javax.portlet.annotations.PortletApplication;
import javax.portlet.annotations.PortletConfiguration;
import javax.portlet.annotations.PortletConfigurations;
import javax.portlet.annotations.PortletQName;
import javax.portlet.annotations.Preference;
import javax.portlet.annotations.PublicRenderParameterDefinition;
import javax.portlet.annotations.RenderMethod;
import javax.portlet.annotations.RuntimeOption;
import javax.portlet.annotations.Supports;
import javax.portlet.annotations.ServeResourceMethod;
import javax.portlet.annotations.UserAttribute;
#else
import javax.portlet.GenericPortlet;
#end
import javax.xml.namespace.QName;

import lombok.extern.slf4j.Slf4j;

#if($configType.contains('@java'))
@PortletApplication(runtimeOptions = {@RuntimeOption(name = "javax.portlet.escapeXml", values = {"true"})},
        publicParams = {@PublicRenderParameterDefinition(identifier = "pub_id", qname = @PortletQName(namespaceURI = "", localPart = "pub_id"))},
        events = {
            @EventDefinition(qname = @PortletQName(namespaceURI = "http://www.iamwhatiam.ml/portals/EventPortlet", localPart = "Search")),
            @EventDefinition(qname = @PortletQName(namespaceURI = "", localPart = "track")),
        },
        defaultNamespaceURI = "https://www.java.net/", resourceBundle = "messages", userAttributes = {@UserAttribute(name = "tid")}
        )
@PortletConfigurations(value = {
        @PortletConfiguration(portletName = "test-portlet", title = {@LocaleString(value = "title of test portlet"), @LocaleString(locale = "zh", value = "porlet测试标题")},
                shortTitle = {@LocaleString(value = "portlet title")}, keywords = {@LocaleString(value = "portal"), @LocaleString(value = "portlet")},
                initParams = {@InitParameter(name = "provider", value = "google", description = {@LocaleString(value = "search provider", locale = "en")})},
                prefs = {@Preference(name = "locale", values = {"zh", "en"}, isReadOnly = true), @Preference(name = "topic", values = {})},
                publicParams = {"pub_id"}, supports = {@Supports(windowStates = {"normal", "minimized"})}, asyncSupported = true
        ),
})
#end
@Slf4j
public class PortletTest #if(!$configType.contains('@java'))extends GenericPortlet#{end} {

#if($configType.contains("@java"))
    private PortletContext portletContext;

    public PortletContext getPortletContext() {
        return portletContext;
    }
#end

#if(!$configType.contains('@java'))
    @Override
#{else}
    @InitMethod(value = "test-portlet")
#{end}
    public void init(PortletConfig config) throws PortletException {
#if($configType.contains("@java"))
        portletContext = config.getPortletContext();
#end
    }

    /**
     * performs processing that can cause a change to persistent data.
     */
#if(!$configType.contains('@java'))
    @Override
#{else}
    @ActionMethod(portletName = "test-portlet", actionName = "login",
        publishingEvents = {@PortletQName(namespaceURI = "http://www.iamwhatiam.ml/portals/EventPortlet", localPart = "Search")})
#{end}
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        // may be redirect
        response.getRenderParameters().setValue("action", request.getActionParameters().toString());
        response.setEvent(new QName("http://www.iamwhatiam.ml/portals/EventPortlet", "Search"), "{keywords: [\"java\", \"portal\", \"portlet\"]");
    }

     /**
      * generates markup for inclusion into the head section of a portal page and sets HTTP header information
      */
#if(!$configType.contains('@java'))
    @Override
#{else}
    @HeaderMethod(portletNames = {"test-portlet", "${portletName}"})
#{end}
    public void renderHeaders(HeaderRequest request, HeaderResponse response) throws PortletException, IOException {
        // https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.6.0.min.js
        response.getWriter().write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>");
    }

    /**
     * generates markup fragments for inclusion into a portal page body section.
     */
#if(!$configType.contains('@java'))
    @Override
#{else}
    @RenderMethod(portletNames = {"test-portlet"})
#{end}
    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.createRenderURL(Copy.ALL).getRenderParameters().setValue("name", "value");
        getPortletContext().getRequestDispatcher("/WEB-INF/jsp/portal.jsp").include(request, response);
    }

    /**
     * generates additional data related to the current render state.
     */
#if(!$configType.contains('@java'))
    @Override
#{else}
    @ServeResourceMethod(portletNames = {"test-portlet"}, asyncSupported = true)
#{end}
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        // may be create ActionURL, RednerURL, ResourceURL
        response.setContentType("application/json");
        response.getWriter().write("{key:\"value\"}");
    }

    /**
     *  processes an event that was fired by a portlet  or by the portlet container. Can cause a change to persistent data.
     */
#if(!$configType.contains('@java'))
    @Override
#{else}
    @EventMethod(portletName = "test-portlet",
        processingEvents = {@PortletQName(namespaceURI = "http://www.iamwhatiam.ml/portals/EventPortlet", localPart = "Search")},
        publishingEvents = {@PortletQName(namespaceURI = "", localPart = "track")})
#{end}
    public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException{
        Object event = request.getEvent().getValue();
        log.debug("process event, QName: {}, local part: {}, event: {}", request.getEvent().getQName(), request.getEvent().getName(), event);
        response.getRenderParameters().setValue("event", event.toString());
        // may be include other portlet

        response.setEvent("track", "{key:\"iMinusMinus\"}");
    }

#if(!$configType.contains('@java'))
    @Override
#{else}
    @DestroyMethod(value = "test-portlet")
#{end}
    public void destroy() {

    }
}