<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/searchTeachersInformation">
	<table class="listClasses" width="100%">
		<tr>
			<td>
				<p align="left"><bean:message key="label.gep.teachersInformation.chooseExecutionDegree"
			  			  					  bundle="GEP_RESOURCES"/>:</p>
			</td>
		</tr>
	</table>
	<br />
	<br />
	<html:select property="executionDegreeId">
		<option></option>
		<html:options collection="infoExecutionDegrees" labelProperty="infoDegreeCurricularPlan.infoDegree.nome" property="idInternal"/>
	</html:select>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.show"
					  bundle="GEP_RESOURCES"/>
	</html:submit>
	<html:hidden property="method" value="doSearch"/>
</html:form>