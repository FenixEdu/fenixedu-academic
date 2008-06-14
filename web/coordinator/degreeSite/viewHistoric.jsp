<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionDegree" %>

<h2>
<bean:message key="label.historic" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.site" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.degree" />&nbsp;
</h2>

<logic:present name="executionDegrees">
	<logic:iterate id="executionDegree" name="executionDegrees" type="net.sourceforge.fenixedu.domain.ExecutionDegree">
		<p>
			<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;degreeID=" + executionDegree.getDegreeCurricularPlan().getDegree().getIdInternal() + "&amp;executionDegreeID=" + executionDegree.getIdInternal()%>" target="_blank">
				<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:write name="executionDegree" property="executionYear.year"/>
			</html:link>
		</p>
	</logic:iterate>
</logic:present>

<logic:notPresent name="executionDegrees">
	<p><em><bean:message key="error.coordinator.noHistoric" /></em></p>
</logic:notPresent>
