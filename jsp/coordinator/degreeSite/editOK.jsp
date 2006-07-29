<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<h2><bean:message key="link.coordinator.degreeSite.management"/></h2>

<p><bean:message key="text.coordinator.degreeSite.editOK"/><br />
<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoDegreeID" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.idInternal"/>
	<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;degreeID=" + infoDegreeID.toString() %>" target="_blank">
		<bean:message key="link.coordinator.degreeSite.viewSite" /></p>
	</html:link>
</logic:present>