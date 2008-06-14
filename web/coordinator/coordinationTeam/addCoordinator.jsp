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

<table class="tstyle5 thlight">
	<tr>
		<td><bean:message key="label.teacherNumber"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber" /></td>
	</tr>
</table>

<p>
	<html:submit><bean:message key="label.submit"/></html:submit>
</p>


</html:form>




