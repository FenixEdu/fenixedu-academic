<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error">
	<html:errors bundle="GEP_RESOURCES"/>
</span>
<html:form action="/searchCoursesInformation">
	<table class="listClasses" width="100%">
		<tr>
			<td>
				<p align="left"><bean:message key="label.gep.coursesInformation.chooseExecutionDegree"
				  							  bundle="GEP_RESOURCES"/>:</p>
			</td>
		</tr>
	</table>
	
	<br />
	<br />
	<bean:message key="label.gep.degree" bundle="GEP_RESOURCES"/>:
	<html:select property="executionDegreeId">
		<html:option key="label.selectAll" bundle="GEP_RESOURCES" value="all"/>
		<html:options collection="infoExecutionDegrees" labelProperty="infoDegreeCurricularPlan.infoDegree.nome" property="idInternal"/>
	</html:select>
	<br />
	<bean:message key="label.gep.basic" bundle="GEP_RESOURCES"/>:
	<html:checkbox property="basic" value="true"/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.show"
					  bundle="GEP_RESOURCES"/>
	</html:submit>
	<html:hidden property="method" value="doSearch"/>
</html:form>