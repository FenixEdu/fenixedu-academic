<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>


<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.unitSite.analytics" bundle="SITE_RESOURCES"/>
</h2>

<div class="infoop2">
    <p class="mvert0">
  		<app:defineContentPath id="siteURL" name="site" toScope="request"/>
		<bean:define id="url" name="siteURL" type="java.lang.String"/>
    	<bean:message key="message.site.analytics" bundle="SITE_RESOURCES" arg0="<%= RequestUtils.absoluteURL(request, url).toString() %>"/>
   	</p>
</div>

<logic:present name="codeAccepted">
	<p>
		<span class="success0">
			<bean:message key="message.site.analytics.code.accepted" bundle="SITE_RESOURCES"/>.
		</span>
	</p>
</logic:present>

<logic:present name="codeRemoved">
	<p>
		<span class="success0">
			<bean:message key="message.site.analytics.code.removed" bundle="SITE_RESOURCES"/>.
		</span>
	</p>
</logic:present>

<fr:form action="<%= String.format("%s?method=analytics&amp;%s", actionName, context) %>">
	<fr:edit id="siteAnalytics" name="site" schema="unitSite.analytics.code">
		<fr:layout>
		    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
		    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>
	
	<html:submit>
		<bean:message key="button.submit"/>
	</html:submit>
</fr:form>
