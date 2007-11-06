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

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"/>
<%
	final Registration registration = ((RegistrationSelectExecutionYearBean) bean).getRegistration();
	request.setAttribute("registration", registration);

	// average
	Curriculum curriculum = registration.getCurriculum();
	request.setAttribute("curriculum", curriculum);
	if (!curriculum.isEmpty()) {
		final BigDecimal sumPiCi = curriculum.getSumPiCi();
		request.setAttribute("sumPiCi", sumPiCi);

		final BigDecimal sumPi = curriculum.getSumPi();
		request.setAttribute("sumPi", sumPi);

		final BigDecimal average = curriculum.getAverage();
		request.setAttribute("average", average);
	}
	
	// curricular year
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	request.setAttribute("currentExecutionYear", currentExecutionYear);
	curriculum = registration.getCurriculum(currentExecutionYear);
	if (!curriculum.isEmpty()) {
		final BigDecimal sumEctsCredits = curriculum.getSumEctsCredits();
		request.setAttribute("sumEctsCredits", sumEctsCredits);
		
		final Integer curricularYear = curriculum.getCurricularYear();
		request.setAttribute("curricularYear", curricularYear);
	}
%>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<%-- TODO implement StudentCurriculum renderer --%>

<bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>

<logic:equal name="curricularEntriesCount" value="0">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:greaterThan name="curricularEntriesCount" value="0">

	<div class="infoop2 mvert2">
		<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

		<p class="mtop1 mbottom05"><strong><bean:message key="degree.average.is.current.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="average"/></b></p>
		<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
		<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>

		<p class="mtop1 mbottom05"><strong><bean:message key="curricular.year.in.begin.of.execution.year.info" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="currentExecutionYear" property="year"/></strong>.</p>
		<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
		<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
		<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
	</div>

	<fr:view name="curriculum"/>

</logic:greaterThan>
