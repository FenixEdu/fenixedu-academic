<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<html:xhtml/>

<h2>
	<bean:message key="link.coordinator.degreeSite.management"/>
</h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p><bean:message key="text.coordinator.degreeSite.editOK"/><br />
<logic:present name="<%= SessionConstants.MASTER_DEGREE %>">
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	
	<app:defineContentPath id="contextPathForUrl" name="infoExecutionDegree" property="infoDegreeCurricularPlan.degreeCurricularPlan.degree.site" toScope="request"/>
	<bean:define id="contextPathForUrl" name="contextPathForUrl" type="java.lang.String"/>

	<bean:define id="executionDegreeID" name="infoExecutionDegree" property="idInternal" />
	
	<%= net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.HAS_CONTEXT_PREFIX %>
	<html:link href="<%= request.getContextPath() + "/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeID=" + executionDegreeID + "&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=" + contextPathForUrl%>" target="_blank">
		<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:write name="infoExecutionDegree" property="executionDegree.executionYear.year"/>
	</html:link>

</logic:present>
