<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<html:xhtml/>

<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<html:form action="/viewCurriculum.do?method=getStudentCP">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<table class="tstyle5 thlight thright thmiddle mbottom0">
		<tr>
			<th><bean:message bundle="APPLICATION_RESOURCES" key="label.person.name" /></th>
			<td>
				<bean:write name="registration" property="person.name"/>
			</td>
		</tr>

<%-- 
		<tr>
			<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/>:</th>
			<td>
				<bean:write name="registration" property="curricularYear"/>
			</td>
		</tr>

		<tr>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/>:</th>
			<td>
				<bean:write name="registration" property="average"/>
			</td>
		</tr>
--%> 
		<tr>
			<th><bean:message key="label.studentCurricularPlan.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="studentCPID" onchange='this.form.submit();'> 
					<html:options collection="scpsLabelValueBeanList" property="value" labelProperty="label" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="select" onchange='this.form.submit();' >
					<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>

		<tr>
			<th>Organizar por:</th>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="groups" onclick='this.form.submit();'/><bean:message key="groups" bundle="BOLONHA_MANAGER_RESOURCES"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="executionYears" onclick='this.form.submit();'/><bean:message key="label.execution.year" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>

	</table>
	
	<logic:notEmpty name="selectedStudentCurricularPlans">

		<bean:define id="organizedBy" name="organizedBy" scope="request" type="java.lang.String" />
		<bean:define id="enrolmentStateSelectionType" name="enrolmentStateSelectionType" scope="request" type="java.lang.Integer" />	
		<logic:iterate id="studentCurricularPlan" name="selectedStudentCurricularPlans" indexId="index">
			
			<logic:greaterThan name="index" value="0">
				<hr style="margin-bottom: 3em; margin-top: 3em;">
			</logic:greaterThan>

			<bean:define id="dateFormated">
				<dt:format pattern="dd.MM.yyyy">
					<bean:write name="studentCurricularPlan" property="startDate.time"/>
				</dt:format>
			</bean:define>

			<div class="mvert2 mtop0">
				<p>
					<strong>
						<bean:message key="label.number" />: 
					</strong> 
					<bean:write name="studentCurricularPlan" property="registration.number"/>				
				</p>
				<p>
					<strong>
						<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: 
					</strong> 
					<bean:message bundle="ENUMERATION_RESOURCES" name="studentCurricularPlan" property="degreeType.name"/>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/> 
					<bean:write name="studentCurricularPlan" property="degree.name"/>,
					<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/>
					<logic:present name="studentCurricularPlan" property="specialization">
						- <bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
					</logic:present>
				</p>
				<logic:present name="studentCurricularPlan" property="branch">
					<p>
						<strong>
							Grupo: 
						</strong> 
						<bean:write name="studentCurricularPlan" property="branch.name"/>
					</p>
				</logic:present>
				<p>
					<strong>
						<bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: 
					</strong> 
					<bean:write name="dateFormated"/>
				</p>
			</div>

			<fr:view name="studentCurricularPlan">
				<fr:layout>
					<fr:property name="organizedBy" value="<%=organizedBy%>"/>
					<fr:property name="initialWidth" value="50"/>
					<fr:property name="widthDecreasePerLevel" value="1"/>
					<fr:property name="tablesClasses" value="showinfo3 mvert0"/>
					<%-- tableClasses--%>
					<fr:property name="groupRowClasses" value="bgcolor2"/>
					<%-- groupHeaderRowClasses--%>
					<fr:property name="groupNameClasses" value="aleft"/>
					<%-- groupHeaderClasses--%>
					<fr:property name="enrolmentClasses" value="smalltxt acenter width6em,smalltxt acenter width5em,smalltxt acenter width7em,smalltxt acenter width5em,aright width6em"/>
					<%-- enrolmentColumnClasses--%>
					<fr:property name="enrolmentStateSelectionType" value="<%=enrolmentStateSelectionType.toString()%>"/>
				</fr:layout>
			</fr:view>

		</logic:iterate>
	</logic:notEmpty>
	 	
	<logic:empty name="selectedStudentCurricularPlans">
		<p>
			<span class="warning0">
				<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
			</span>
		</p>
	</logic:empty>

</html:form>
