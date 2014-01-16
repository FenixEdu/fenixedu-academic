<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>

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
        <span class="permalink1">(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h2>

    <bean:define id="sectionId" name="section" property="externalId"/>
    <p>
       <em><bean:message key="message.section.view.mustLogin" bundle="SITE_RESOURCES"/></em>
		<%
		    if (org.fenixedu.bennu.core.util.CoreConfiguration.casConfig().isCasEnabled()) {
							    final String schema = request.getScheme();
							    final String server = request.getServerName();
							    final int port = request.getServerPort();
		%>
				<a href="<%= "https://barra.tecnico.ulisboa.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + schema + "://" + server + (port == 80 || port == 443 ? "" : ":" + port) + request.getContextPath() + section.getReversePath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
       			<html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, sectionId) %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</html:link>.
       	<%
			}
       	%>
    </p>
    <bean:message key="label.permittedGroup" bundle="SITE_RESOURCES"/>

	<logic:present name="section" property="availabilityPolicy">
		<logic:present name="section" property="availabilityPolicy.targetGroup">
			<logic:present name="section" property="availabilityPolicy.targetGroup.name">
				<fr:view name="section" property="availabilityPolicy.targetGroup.name">
				</fr:view>
			</logic:present>
		</logic:present>
	</logic:present>
	<logic:notPresent name="section" property="availabilityPolicy">
		<bean:message key="link.section.no.availability.policy" bundle="SITE_RESOURCES"/>
	</logic:notPresent>

</logic:present>
