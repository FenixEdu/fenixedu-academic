<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<h2><bean:message key="title.addCoordinator"/></h2>

<html:form action="/addCoordinator">

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	
<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoExecutionDegreeId" property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="AddCoordinator" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

<p><html:link action="<%= "/viewCoordinationTeam.do?method=viewTeam&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() + "&infoExecutionDegreeId=" + infoExecutionDegreeId.toString() %>">
	<bean:message bundle="COORDINATOR_RESOURCES" key="link.back"/>
</html:link></p>

<table class="tstyle5 thlight">
	<tr>
		<td><bean:message key="label.istId" bundle="COORDINATOR_RESOURCES"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="newCoordinatorIstUsername" /></td>
	</tr>
</table>

<style>
	.subtitled_description{
		margin: 5em;
		margin-top: -1em;
		margin-bottom: 2em;
	}
</style>
<p class="subtitled_description">
	<bean:message key="label.istIdUsageDescription" bundle="COORDINATOR_RESOURCES"/>
</p>

<p>
	<html:submit><bean:message key="label.add"/></html:submit>
</p>


</html:form>




