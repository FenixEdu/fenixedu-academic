<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
<html:xhtml/>

<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<%-- Foto --%>
<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<%-- Registration Details --%>
<logic:notPresent name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>
<logic:present name="registration" property="ingressionEnum">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<%-- Registration Average and Curricular Year calculations --%>
<logic:notPresent role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<logic:equal name="registration" property="degreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree" value="true">
		<%
			final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
			request.setAttribute("studentCurriculum", studentCurriculum);
		
			final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(null);
			request.setAttribute("totalEctsCredits", totalEctsCredits);
			
			final double average = studentCurriculum.getRoundedAverage(null, true);
			request.setAttribute("average", average);
		
			final int curricularYear = studentCurriculum.calculateCurricularYear(null);
			request.setAttribute("curricularYear", curricularYear);
		
			final double sumPiCi = studentCurriculum.getSumPiCi(null);
			request.setAttribute("sumPiCi", sumPiCi);
		
			final double sumPi = studentCurriculum.getSumPi(null);
			request.setAttribute("sumPi", sumPi);
		%>
	
		<p class="mtop2">
			<span class="warning0">
				<bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</span>
		</p>
		<table class="tstyle4 thright thlight mtop0">
			<tr>
				<th><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:write name="average"/></td>
			</tr>	
			<tr>
				<th><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
			</tr>	
			<tr>
				<th><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <bean:write name="average"/>
				</td>
			</tr>	
		</table>
		<table class="tstyle4 thright thlight mtop0">
			<tr>
				<th><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td><bean:write name="curricularYear"/></td>
			</tr>	
			<tr>
				<th><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</td>
			</tr>	
			<tr>
				<th><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<td>
					<bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <bean:write name="curricularYear"/>;
				</td>
			</tr>	
		</table>
	</logic:equal>
</logic:notPresent>


<%-- Choose Student Curricular Plan form --%>
<html:form action="<%="/viewCurriculum.do?method=prepare&registrationOID=" + registration.getIdInternal()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<table class="tstyle4 thright thlight mtop2">
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
			<th><bean:message key="organize.by" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="groups" onclick='this.form.submit();'/><bean:message key="groups" bundle="BOLONHA_MANAGER_RESOURCES"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.organizedBy" property="organizedBy" value="executionYears" onclick='this.form.submit();'/><bean:message key="label.execution.year" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>
	</table>
</html:form>


<%-- Show Student Curricular Plans --%>
<logic:empty name="selectedStudentCurricularPlans">
	<p>
		<span class="warning0">
			<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</p>
</logic:empty>
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
				<fr:property name="initialWidth" value="65em"/>
				<fr:property name="widthDecreasePerLevel" value="1"/>
				<fr:property name="tablesClasses" value="showinfo3 mvert0"/>
				<%-- tableClasses--%>
				<fr:property name="groupRowClasses" value="bgcolor2"/>
				<%-- groupHeaderRowClasses--%>
				<fr:property name="groupNameClasses" value="aleft"/>
				<%-- groupHeaderClasses--%>
				<fr:property name="enrolmentClasses" value="width05em acenter 0, width05em acenter 1, width2em acenter 2, aleft 3, width7em acenter 4, width6em acenter 5, width5em acenter 6, width1em acenter 7, width1em acenter 8, width1em aright 9, width1em aright 10, width1em aright 11"/>
				<%-- enrolmentColumnClasses--%>
				<fr:property name="enrolmentStateSelectionType" value="<%=enrolmentStateSelectionType.toString()%>"/>
			</fr:layout>
		</fr:view>

	</logic:iterate>
</logic:notEmpty>
