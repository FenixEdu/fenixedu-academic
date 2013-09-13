<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<html:xhtml/>

<h2>
	<bean:message key="link.coordinator.degreeSite.management"/>
</h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p><bean:message key="text.coordinator.degreeSite.editOK"/><br />
<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>">
	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
	
	<app:defineContentPath id="contextPathForUrl" name="infoExecutionDegree" property="infoDegreeCurricularPlan.degreeCurricularPlan.degree.site" toScope="request"/>
	<bean:define id="contextPathForUrl" name="contextPathForUrl" type="java.lang.String"/>

	<bean:define id="executionDegreeID" name="infoExecutionDegree" property="externalId" />
	
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %>
	<html:link href="<%= request.getContextPath() + "/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeID=" + executionDegreeID + "&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + contextPathForUrl%>" target="_blank">
		<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:write name="infoExecutionDegree" property="executionDegree.executionYear.year"/>
	</html:link>

</logic:present>
