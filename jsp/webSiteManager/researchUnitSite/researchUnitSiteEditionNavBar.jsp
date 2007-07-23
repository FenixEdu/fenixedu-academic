<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="../commons/siteMenu.jsp"/>

<ul>
	<li class="navheader"><bean:message key="link.site.researchUnit" bundle="WEBSITEMANAGER_RESOURCES"/></li>
    <li>
	    <html:link page="<%= String.format("%s?method=managePeople&amp;%s", actionName, context) %>">
            <bean:message key="link.site.contract" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
</ul>
