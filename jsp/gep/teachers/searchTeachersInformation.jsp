<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error">
	<html:errors bundle="GEP_RESOURCES"/>
</span>
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
	<table>
		<tr>
			<td>
				<bean:message key="label.gep.degree" bundle="GEP_RESOURCES"/>:
			</td>
			<td>
				<html:select property="executionDegreeId">
					<html:option key="label.selectAll" bundle="GEP_RESOURCES" value="all"/>
					<html:options collection="infoExecutionDegrees" labelProperty="label" property="value"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<br />
				<bean:message key="message.gep.choose.scientificArea" bundle="GEP_RESOURCES"/>:
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.gep.basic" bundle="GEP_RESOURCES"/>
			</td>
			<td>
				<html:radio property="basic" value="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.gep.non.basic" bundle="GEP_RESOURCES"/>
			</td>
			<td>
				<html:radio property="basic" value="false"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.selectAll" bundle="GEP_RESOURCES"/>
			</td>
			<td>
				<html:radio property="basic" value=""/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.show"
					  bundle="GEP_RESOURCES"/>
	</html:submit>
	<html:hidden property="method" value="doSearch"/>
</html:form>