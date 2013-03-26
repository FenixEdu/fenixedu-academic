<%@page import="pt.ist.fenixWebFramework.Config"%>
<%@page import="pt.ist.fenixWebFramework.Config.CasConfig"%>
<%@page import="pt.ist.fenixWebFramework.FenixWebFramework"%>
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="section">
   
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
        <span class="permalink1">
        	(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)            
        </span>
    </h2>

 	<logic:notEmpty name="section" property="orderedSubSections">
		<fr:view name="section" property="orderedSubSections" layout="list">
		    <fr:layout>
		        <fr:property name="eachLayout" value="values"/>
		        <fr:property name="eachSchema" value="site.section.name"/>
		    </fr:layout>
		    <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${idInternal}&amp;" + context %>"/>
		</fr:view>
    </logic:notEmpty>
    
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
            
	<h3 class="mtop2">
        <a name="<%= "item" + item.getIdInternal() %>"></a>
        <fr:view name="item" property="name"/>
            <span class="permalink1">(<app:contentLink name="item"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h3>

    <p>
        <em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
        <%
			final Config c = FenixWebFramework.getConfig();
			final String serverName = request.getServerName();
			final CasConfig casConfig = c.getCasConfig(serverName);
			if (casConfig != null && casConfig.isCasEnabled()) {
			    final String schema = request.getScheme();
			    final String server = request.getServerName();
			    final int port = request.getServerPort();
		%>
				<a href="<%= "https://barra.ist.utl.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + schema + "://" + server + (port == 80 || port == 443 ? "" : ":" + port) + request.getContextPath() + section.getReversePath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
	            <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, section.getIdInternal()) %>">
    	            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
        	   </html:link>.
       	<%
			}
       	%>
    </p>
</logic:present>