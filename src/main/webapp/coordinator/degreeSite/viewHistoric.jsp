<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionDegree" %>

<html:xhtml/>

<h2>
<bean:message key="label.historic" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.site" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.degree" />&nbsp;
</h2>

<logic:present name="executionDegrees">
	<logic:iterate id="executionDegree" name="executionDegrees" type="net.sourceforge.fenixedu.domain.ExecutionDegree">
		<p>
			<app:defineContentPath id="contextPathForUrl" name="executionDegree" property="degreeCurricularPlan.degree.site" toScope="request"/>
			<bean:define id="contextPathForUrl" name="contextPathForUrl" type="java.lang.String"/>

			<bean:define id="executionDegreeID" name="executionDegree" property="externalId" />
			
			<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %>
			<html:link href="<%= request.getContextPath() + "/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeID=" + executionDegreeID + "&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + contextPathForUrl%>" target="_blank">
				<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:write name="executionDegree" property="executionYear.year"/>
			</html:link>
		</p>
	</logic:iterate>
</logic:present>

<logic:notPresent name="executionDegrees">
	<p><em><bean:message key="error.coordinator.noHistoric" /></em></p>
</logic:notPresent>
