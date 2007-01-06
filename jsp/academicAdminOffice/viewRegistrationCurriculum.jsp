<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="java.util.Collection"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
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
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
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
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<logic:equal name="registration" property="concluded" value="true">
</logic:equal>

<fr:edit id="bean" name="bean" schema="registration-select-execution-year" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:edit>


<%
	final StudentCurriculum studentCurriculum = (executionYear == null) ? new StudentCurriculum(registration) : new StudentCurriculum(registration, executionYear);
	request.setAttribute("studentCurriculum", studentCurriculum);

	final StudentCurricularPlan studentCurricularPlan = studentCurriculum.getStudentCurricularPlan();
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	final Collection<StudentCurriculum.Entry> curriculumEntries = studentCurriculum.getCurriculumEntries();
	request.setAttribute("curriculumEntries", curriculumEntries);
	
	final double totalEctsCredits = studentCurriculum.getTotalEctsCredits();
	request.setAttribute("totalEctsCredits", totalEctsCredits);
	
	final double average = studentCurriculum.getRoundedAverage(true);
	request.setAttribute("average", average);

	final int curricularYear = studentCurriculum.getCurricularYear();
	request.setAttribute("curricularYear", curricularYear);

	final double sumPiCi = studentCurriculum.getSumPiCi();
	request.setAttribute("sumPiCi", sumPiCi);

	final double sumPi = studentCurriculum.getSumPi();
	request.setAttribute("sumPi", sumPi);
%>

<table class="tstyle4 thright thlight">
	<logic:notEmpty name="executionYear">
		<tr>
			<th>
				Ano Lectivo
			</th>
			<td>
				A informação constante nesta página refere-se ao <span class="error0"><b>início do ano lectivo <bean:write name="executionYear" property="year"/></b></span>
			</td>
		</tr>
	</logic:notEmpty>
	<logic:empty name="executionYear">
		<tr>
			<th>
				Nota
			</th>
			<td>
				<span class="warning0">
					A informação constante nesta página refere-se a toda a informação curricular do aluno.
				</span>			
			</td>
		</tr>
	</logic:empty>
	<tr>
		<th>
			Plano Curricular
		</th>
		<td>
			<bean:write name="studentCurricularPlan" property="name"/>
		</td>
	</tr>
<%-- 
		<tr>
			<%
				final int enrolmentsCount = registration.getEnrolments(executionYear).size();
				request.setAttribute("enrolmentsCount", enrolmentsCount);
			%>
			<th>
				<bean:message key="label.numberEnroledCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>
			</th>
			<td>
				<bean:write name="enrolmentsCount"/>
			</td>
		</tr>
--%>
	<tr>
		<th>
			<bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</th>
		<td>
			<bean:size id="curricularEntriesCount" name="curriculumEntries"/>
			<bean:write name="curricularEntriesCount"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</th>
		<td>
			<bean:write name="totalEctsCredits"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="average" bundle="STUDENT_RESOURCES"/>
		</th>
		<td>
			<bean:write name="average"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/>
		</th>
		<td>
			<bean:write name="curricularYear"/>
		</td>
	</tr>
</table>


<logic:equal name="curricularEntriesCount" value="0">
	<p class="mvert15">
		<span class="warning0"><em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em></span>
	</p>	
</logic:equal>

<logic:greaterThan name="curricularEntriesCount" value="0">
	<p>
		<span class="warning0">
			As regras seguintes constam do livro <em>Regulamentos dos Cursos de 1º e 2º Ciclo, 2006/2007</em> do Instituto Superior Técnico.
		</span>

		<table class="tstyle4">
			<tr>
				<th colspan="2">
					Média de Curso
				</th>
			</tr>	
			<tr>
				<th>
					Regra
				</th>
				<td>
					MC = Somatório (Peso x Classificação) das Unidades Curriculares / Somatório (Peso) das Unidades Curriculares
				</td>
			</tr>	
			<tr>
				<th>
					Resultado
				</th>
				<td>
					MC = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <bean:write name="average"/>
				</td>
			</tr>	
		</table>

		<table class="tstyle4">
			<tr>
				<th colspan="2">
					Ano Curricular
				</th>
			</tr>	
			<tr>
				<th>
					Regra
				</th>
				<td>
					AC = mínimo (inteiro ( (créditos ECTS aprovados + 24) / 60 + 1) ; Nº de anos do curso)
				</td>
			</tr>	
			<tr>
				<th>
					Resultado
				</th>
				<td>
					AC = mínimo (inteiro ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; 5) = 4;
				</td>
			</tr>	
		</table>
	</p>

	<p>
		<span class="warning0">
			Os cálculos acima baseam-se nas seguintes informações:
		</span>

		<table class="tstyle4">
			<tr>
				<th rowspan="3">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum"/>
				</th>
				<th rowspan="3" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.type.of.aproval"/>
				</th>
				<th rowspan="3">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.aproved"/>
				</th>
				<th colspan="4">
					Média de Curso
				</th>
				<th colspan="2">
					Ano Curricular
				</th>
			</tr>
			<tr>
				<th>
					Classificação
				</th>
				<th colspan="2">
					Peso
				</th>
				<th style="width: 70px;">
					(Peso x Classificação)
				</th>
				<th colspan="2">
					ECTS
				</th>
			</tr>
			<tr>
				<th colspan="2">
					Discriminado
				</th>
				<th colspan="2">
					Total
				</th>
				<th>
					Discriminado
				</th>
				<th>
					Total
				</th>
			</tr>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EnrolmentEntry">
					<tr>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigthTimesClassification"/></td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$ExtraCurricularEnrolmentEntry">
					<tr>
						<td></td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>						
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigthTimesClassification"/></td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$NotNeedToEnrolEntry">
					<tr>
						<td></td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EquivalentEnrolmentEntry">
					<bean:size id="numberEntries" name="curriculumEntry" property="entries"/>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries" indexId="index">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EnrolmentEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/></td>
								<td><bean:write name="simpleEntry" property="enrolment.curricularCourse.name"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="weigth"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>			
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="weigthTimesClassification"/></td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCredits"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
								</logic:equal>
							</tr>
						</logic:equal>
					</logic:iterate>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$NotNeedToEnrolEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
								<td><bean:write name="simpleEntry" property="curricularCourse.name"/></td>
								<td class="acenter">-</td>
								<td class="acenter">-</td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCredits"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
								</logic:equal>
							</tr>
						</logic:equal>
					</logic:iterate>
				</logic:equal>
			</logic:iterate>
			<tr>
				<td colspan="6" style="text-align: right;">
					Somatórios
				</td>
				<td class="acenter">
					<bean:write name="sumPi"/>
				</td>
				<td class="acenter">
					<bean:write name="sumPiCi"/>
				</td>
				<td></td>
				<td>
					<bean:write name="totalEctsCredits"/>
				</td>
			</tr>
		</table>
	</p>

</logic:greaterThan>
