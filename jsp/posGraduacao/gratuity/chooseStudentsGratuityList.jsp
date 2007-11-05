<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<html:form action="/studentsGratuityList" >
<logic:notPresent name="showNextSelects">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareChooseDegree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
</logic:notPresent>
<logic:present name="showNextSelects">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="studentsGratuityList"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	
	<div class="infoop2">
		<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>
		<bean:message key="label.masterDegree.gratuity.executionYear" />: <bean:write name="executionYearLabel" />
	</div>

</logic:present>

<table class="tstyle5 thlight thright thmiddle">
		<logic:notPresent name="showNextSelects">
		<tr>
			<th>
				<bean:message key="label.masterDegree.gratuity.executionYear"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear" onchange="this.form.value='prepareChooseDegree';this.form.submit();">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message key="label.choose.one"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		</logic:notPresent>
		<logic:present name="showNextSelects">			
			<tr>
				<th>
					<bean:message key="label.qualification.degree"/>:
				</th>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degree" property="degree">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.choose.one"/>
						</html:option>
						<html:option value="all" key="label.masterDegree.gratuity.all">
							<bean:message key="label.masterDegree.gratuity.all"/>
						</html:option>
						<html:optionsCollection name="<%=SessionConstants.DEGREES%>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.masterDegree.gratuity.specializationArea"/>:
				</th>
				<td>
					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.specialization" property="specialization">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.choose.one"/>
						</html:option>
						<html:option value="all" key="label.masterDegree.gratuity.all">
							<bean:message key="label.gratuitySituationType.all"/>
						</html:option>
						<html:options collection="values" property="value" labelProperty="label"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.masterDegree.gratuity.situation"/>:
				</th>
				<td>
					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType" bundle="ENUMERATION_RESOURCES"/>	            
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.situation" property="situation">
						<html:option key="dropDown.Default" value=""/>
						<html:option value="all">
							<bean:message key="label.gratuitySituationType.all"/>
						</html:option>
						<html:options collection="values" property="value" labelProperty="label"/>
					</html:select>
				</td>
			</tr>
		</logic:present>		
</table>	

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.masterDegree.gratuity.list"/>
	</html:submit>				
</p>

</html:form>	