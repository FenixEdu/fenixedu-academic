<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.Curriculum"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"/>
<%
	final Registration registration = ((RegistrationSelectExecutionYearBean) bean).getRegistration();
	request.setAttribute("registration", registration);
	final ExecutionYear executionYear = ((RegistrationSelectExecutionYearBean) bean).getExecutionYear();
	request.setAttribute("executionYear", executionYear);
%>

<ul class="mtop2">
	<li>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ebf5ea; border-bottom: 1px solid #cce5c7; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop025"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<p class="mtop15 mbottom025"><strong>Visualizar Currículo:</strong></p>
<fr:edit id="bean" name="bean" schema="registration-select-execution-year" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop025 thmiddle"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
</fr:edit>

<%
	final Curriculum curriculum = registration.getCurriculum(executionYear);
	request.setAttribute("curriculum", curriculum);	

	if (!curriculum.isEmpty()) {
		final BigDecimal sumPiCi = curriculum.getSumPiCi();
		request.setAttribute("sumPiCi", sumPiCi);

		final BigDecimal sumPi = curriculum.getSumPi();
		request.setAttribute("sumPi", sumPi);

		final BigDecimal average = curriculum.getAverage();
		request.setAttribute("average", average);

		final BigDecimal sumEctsCredits = curriculum.getSumEctsCredits();
		request.setAttribute("sumEctsCredits", sumEctsCredits);
		
		final Integer curricularYear = curriculum.getCurricularYear();
		request.setAttribute("curricularYear", curricularYear);
	}
%>

<bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>

<logic:equal name="curricularEntriesCount" value="0">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:greaterThan name="curricularEntriesCount" value="0">

	<div class="infoop2 mtop2">
		<logic:notEmpty name="executionYear">
			<p class="mvert05"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/><bean:message key="begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>.</p>
			<br/>
			<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><strong><bean:message key="bolonha.special.case.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>			

			<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="average"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>

			<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
			<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
		</logic:notEmpty>

		<logic:empty name="executionYear">
			<p class="warning0"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="all.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>

			<logic:equal name="registration" property="concluded" value="true">
				<logic:equal name="registration" property="registrationConclusionProcessed" value="true">
					<p class="mvert05"><strong><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
					<p class="mtop1 mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="pleft1 mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <b class="highlight1"><bean:write name="registration" property="average"/></b></p>
				</logic:equal>
				<logic:equal name="registration" property="registrationConclusionProcessed" value="false">
					<p class="mvert05"><span class="error0"><strong><bean:message key="missing.final.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></span></p>
				</logic:equal>
			</logic:equal>

			<logic:equal name="registration" property="concluded" value="false">
				<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><strong><bean:message key="bolonha.special.case.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>			
	
				<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="average"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
			</logic:equal>

		</logic:empty>
	</div>

	<table class="tstyle4 thlight tdcenter mtop15">
		<tr>
			<th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/></th>
			<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
		</tr>
		<tr>
			<td><bean:write name="curricularEntriesCount"/></td>
			<td><bean:write name="sumEctsCredits"/></td>
			<logic:notEmpty name="executionYear">
				<td><bean:write name="average"/></td>
				<td><bean:write name="curricularYear"/></td>
			</logic:notEmpty>
			<logic:empty name="executionYear">
				<logic:equal name="registration" property="concluded" value="false">
					<td><bean:write name="average"/></td>
					<td><bean:write name="curricularYear"/></td>
				</logic:equal>
				<logic:equal name="registration" property="concluded" value="true">
					<td><bean:write name="registration" property="finalAverage"/></td>
					<td>-</td>
				</logic:equal>			
			</logic:empty>
		</tr>
	</table>

	<fr:view name="curriculum"/>
	
</logic:greaterThan>
