<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="link.masterDegree.gratuity.insert"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/insertGratuityDataDA">  
	<html:hidden property="method" value="prepareInsertGratuityData" />
	<logic:present name="showNextSelects">
		<html:hidden property="executionPeriod"/>
		<html:hidden property="executionPeriodName"/>
		<html:hidden property="page" value="1"/>
	</logic:present>
	<table>
		<tr>
			<td>
				<bean:message key="label.manager.executionCourseManagement.executionPeriod"/>
			</td>
			<td>
				<html:select property="executionPeriod" onchange="document.insertGratuityDataForm.method.value='prepareInsertGratuityDataChooseDegree';document.insertGratuityDataForm.submit();">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message key="label.manager.executionCourseManagement.select"/>
					</html:option>
					<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="showNextSelects">
			<tr>
				<td>
					<bean:message key="label.masterDegree.gratuity.specializationArea"/>
				</td>
				<td>
					<html:select property="specializationArea">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="specializations"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.qualification.degree"/>
				</td>
				<td>
					<html:select property="degree">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%=SessionConstants.DEGREES%>"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>
	<br />
	<logic:present name="showNextSelects">
		<html:submit styleClass="inputbutton">
			<bean:message key="button.masterDegree.gratuity.continue"/>
		</html:submit>
	</logic:present>
</html:form>