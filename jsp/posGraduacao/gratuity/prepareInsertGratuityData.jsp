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
		<html:hidden property="executionYear"/>
		<html:hidden property="page" value="1"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" class="infoselected">
					<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>
					<b><bean:message key="label.masterDegree.gratuity.executionYear" />:</b>&nbsp;<bean:write name="executionYearLabel" /><br />
				</td>
			</tr>
		</table>
		<br />
	</logic:present>
	
	<table>
		<logic:notPresent name="showNextSelects">
			<tr>
				<td>
					<bean:message key="label.masterDegree.gratuity.executionYear"/>
				</td>
				<td>
					<html:select property="executionYear" onchange="document.insertGratuityDataForm.method.value='prepareInsertGratuityDataChooseDegree';document.insertGratuityDataForm.submit();">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.choose.one"/>
						</html:option>
						<html:optionsCollection name="executionYears"/>
					</html:select>
				</td>
			</tr>
		</logic:notPresent>
		<logic:present name="showNextSelects">
			<%--<tr>
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
			</tr>--%>
			<tr>
				<td>
					<bean:message key="label.qualification.degree"/>
				</td>
				<td>
					<html:select property="degree">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.choose.one"/>
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