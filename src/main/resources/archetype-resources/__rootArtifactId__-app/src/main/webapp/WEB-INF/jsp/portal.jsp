<%-- #if($showComment)<%-- --%>
<%-- Portlet规范1.0：https://jcp.org/en/jsr/detail?id=168 --%>
<%-- Portlet规范2.0：https://jcp.org/en/jsr/detail?id=286 --%>
<%-- Portlet规范3.0：https://jcp.org/en/jsr/detail?id=362 --%>
<%-- #end<%-- --%>
<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%--<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>--%>
<%@ taglib uri="http://xmlns.jcp.org/portlet_3_0" prefix="portlet"%>
<%-- #if($showComment)<%-- --%>
<%-- define objects in page scope: portletRequest/portletResponse, portletConfig, portletSession, portletSessionScope, portletPreferences, portletPreferencesValues --%>
<%-- define objects in page scope and appropriate phase: actionRequest/actionResponse, eventRequest/eventResponse, renderRequest/renderResponse, resourceRequest/resourceResponse--%>
<%-- #end<%-- --%>
<portlet:defineObjects />
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="keywords" content="portal, portlet">
    <base href="<c:url value='/' />" ><%-- when context is ROOT, ${pageContext.request.contextPath} output '//' --%>
    <meta charset="UTF-8">
    <title>Portal</title>
    <!-- [if lt IE 9]>

    <![endif]-->
</head>
<body>
<header>
    <div id="logo">
        <a href="https://iamwhatiam.ml/">
            <img src="https://iamwhatiam.ml/logo.png">
        </a>
    </div>
    <div id="search-box-container" class="portlet">
        <portlet:resourceURL id="search" var="search">
            <portlet:property name="user" value="${portletSessionScope.user}" />
            <form method="get" name="<portlet:namespace />-search" action="${search}">
                <label for="<portlet:namespace />-search-box">Search Query</label>
                <input type="text" id="<portlet:namespace />-search-box">
                <button type="button" id="<portlet:namespace />-search-button-container">
                    <span></span>
                </button>
                <input id="<portlet:namespace />-search-button" type="submit" value="Search">
            </form>
        </portlet:resourceURL>
    </div>
    <div id="toolbar">
        <div id="login-or-logon">
            <portlet:actionURL id="<portlet:namespace />-login" name="login" var="j_security_check" windowState="normal" portletMode="edit">
                <portlet:param name="csrfToken" value="${csrfToken}" />
            <div id="<portlet:namespace />-login" class="popup portlet">
                <form method="POST" action="${j_security_check}">
                    <fieldset>
                        <legend>Login to Portal</legend>
                        <div>
                            <label for="<portlet:namespace />-j_username">User Name</label>
                            <input type="text" name="j_username" id="<portlet:namespace />-j_username"/>
                        </div>
                        <div>
                            <label for="<portlet:namespace />-j_password">Password</label>
                            <input type="password" name="j_password" id="<portlet:namespace />-j_password"/>
                        </div>
                        <div>
                            <label for="<portlet:namespace />-j_login"></label>
                            <input type="submit" value="Login" name="login" id="<portlet:namespace />-j_login"/>
                        </div>
                    </fieldset>
                </form>
            </div>
            </portlet:actionURL>
            <div id="logon" class="popup portlet">
                <form method="POST" action="<portlet:resourceURL id="logon" cacheability="false" />">
                </form>
            </div>

        </div>
        <div id="notifications" class="portlet">

        </div>
    </div>

    <div id="navigation">
        <ul>
            <c:forEach items="${menu}" var="menu">
                <li>
                        ${menu.title}
                    <c:if test="${not empty menu.children}">
                        <ul>
                            <c:forEach items="${menu.children}" var="secondaryMenu">
                                <li>${secondaryMenu.title}</li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
</header>
<div id="stream" class="portlet">
    <portlet:renderURL windowState="MAXIMIZED" portletMode="view">

    </portlet:renderURL>
</div>
<div id="choice" class="portlet">
    <portlet:renderURL windowState="normal" portletMode="view">

    </portlet:renderURL>
</div>
<footer>

</footer>
<noscript style="align: center; color: hsla(1,1,1,0.4);"><fmt:message key="JAVASCRIPT_DISABLED_WARNING" /></noscript>
</body>
</html>