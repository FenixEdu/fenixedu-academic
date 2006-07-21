<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="link.masterDegree.gratuity.insert"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/insertGratuityDataDA">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareInsertGratuityData" />
	<logic:present name="showNextSelects">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		
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
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear" onchange="this.form.method.value='prepareInsertGratuityDataChooseDegree';this.form.submit();">
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
					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.specializationArea" property="specializationArea">
						<html:option key="dropDown.Default" value=""/>
                		<html:options collection="values" property="value" labelProperty="label"/>
					</html:select>
				</td>
			</tr>--%>
			<tr>
				<td>
					<bean:message key="label.qualification.degree"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degree" property="degree">
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
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.masterDegree.gratuity.continue"/>
		</html:submit>
	</logic:present>
</html:form>