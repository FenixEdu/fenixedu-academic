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
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.CurriculumEntry"%>
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
		<fr:property name="classes" value="tstyle2 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,"/>
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

<logic:equal name="registration" property="concluded" value="true">
</logic:equal>

<p class="mtop15 mbottom025"><strong>Visualizar Currículo:</strong></p>
<fr:edit id="bean" name="bean" schema="registration-select-execution-year" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight mtop025 thmiddle"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
</fr:edit>

<%
	final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
	request.setAttribute("studentCurriculum", studentCurriculum);

	final StudentCurricularPlan studentCurricularPlan = studentCurriculum.getStudentCurricularPlan(executionYear);
	request.setAttribute("studentCurricularPlan", studentCurricularPlan);

	final Collection<CurriculumEntry> curriculumEntries = studentCurriculum.getCurriculumEntries(executionYear);
	request.setAttribute("curriculumEntries", curriculumEntries);
	
	final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(executionYear);
	request.setAttribute("totalEctsCredits", totalEctsCredits);
	
	final double average = studentCurriculum.getRoundedAverage(executionYear, true);
	request.setAttribute("average", average);

	final int curricularYear = studentCurriculum.calculateCurricularYear(executionYear);
	request.setAttribute("curricularYear", curricularYear);

	final double sumPiCi = studentCurriculum.getSumPiCi(executionYear);
	request.setAttribute("sumPiCi", sumPiCi);

	final double sumPi = studentCurriculum.getSumPi(executionYear);
	request.setAttribute("sumPi", sumPi);
%>

<bean:size id="curricularEntriesCount" name="curriculumEntries"/>

<logic:equal name="curricularEntriesCount" value="0">
	<p class="mvert15">
		<em>
			<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</em>
	</p>	
</logic:equal>

<logic:greaterThan name="curricularEntriesCount" value="0">

	<div class="infoop3 mtop2">
		<logic:notEmpty name="executionYear">
			<p class="mvert025"><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert025">Os cálculos acima baseam-se nas informações na tabela em baixo.</p>
	
			<p class="mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
		
			<p class="mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
			<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
			<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b></p>
		</logic:notEmpty>
		<logic:empty name="executionYear">
			<bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="all.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<logic:equal name="registration" property="concluded" value="false">
				<p class="mvert025"><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert025">Os cálculos acima baseam-se nas informações na tabela em baixo.</p>
		
				<p class="mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="average"/></b></p>
			
				<p class="mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="totalEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="registration" property="degreeType.years"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b></p>
			</logic:equal>
			<logic:equal name="registration" property="concluded" value="true">
				<bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>

				<p class="mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="registration" property="average"/></p>
			</logic:equal>
		</logic:empty>
		<logic:notEmpty name="executionYear">
			<bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>.
		</logic:notEmpty>
	</div>


	<table class="tstyle4 thlight tdcenter mtop15">
		<tr>
			<th>Plano Curricular</th>
			<th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="average" bundle="STUDENT_RESOURCES"/></th>
			<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
		</tr>
		<tr>
			<td><bean:write name="studentCurricularPlan" property="name"/></td>
			<td><bean:write name="curricularEntriesCount"/></td>
			<td><bean:write name="totalEctsCredits"/></td>
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



		<table class="tstyle4">
			<tr>
				<th rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum.withBreak"/>
				</th>
				<th rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.type.of.aproval.withBreak"/>
				</th>
				<th rowspan="2" colspan="2">
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
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EnrolmentCurriculumEntry">
					<tr>
						<td><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>						
						<td colspan="2">
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="true">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
							</logic:equal>
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="false">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/>
							</logic:equal>
						</td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>
						<td class="acenter">
							<logic:empty name="curriculumEntry" property="weigthTimesClassification">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesClassification">
								<bean:write name="curriculumEntry" property="weigthTimesClassification"/>
							</logic:notEmpty>
						</td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EquivalanteCurriculumEntry">
					<bean:size id="numberEntries" name="curriculumEntry" property="entries"/>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries" indexId="index">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EnrolmentCurriculumEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td>
									<logic:equal name="simpleEntry" property="enrolment.extraCurricular" value="true">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
									</logic:equal>
									<logic:equal name="simpleEntry" property="enrolment.extraCurricular" value="false">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/>
									</logic:equal>
								</td>
								<td><bean:write name="simpleEntry" property="enrolment.curricularCourse.code"/></td>
								<td><bean:write name="simpleEntry" property="enrolment.curricularCourse.name"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="weigth"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>			
									<td rowspan="<%= numberEntries %>" class="acenter">
										<logic:empty name="curriculumEntry" property="weigthTimesClassification">
											-
										</logic:empty>
										<logic:notEmpty name="curriculumEntry" property="weigthTimesClassification">
											<bean:write name="curriculumEntry" property="weigthTimesClassification"/>
										</logic:notEmpty>
									</td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCredits"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
								</logic:equal>
							</tr>
						</logic:equal>
					</logic:iterate>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotNeedToEnrolCurriculumEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
								<td><bean:write name="simpleEntry" property="curricularCourse.code"/></td>
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
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.DismissalEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
								<td>
									<logic:present name="simpleEntry" property="curricularCourse">
										<bean:write name="simpleEntry" property="curricularCourse.code"/>
									</logic:present>
								</td>
								<td>
									<logic:present name="simpleEntry" property="curricularCourse">
										<bean:write name="simpleEntry" property="curricularCourse.name"/>
									</logic:present>
									<logic:present name="simpleEntry" property="curriculumGroup">
										<bean:message key="label.studentDismissal.group.credits.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES" /> (<bean:write name="curriculumEntry" property="curriculumGroup.name.content"/>)
									</logic:present>
								</td>
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
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotNeedToEnrolCurriculumEntry">
					<tr>
						<td><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td colspan="2">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/>
							<logic:equal name="curriculumEntry" property="ectsCredits" value="0">
								(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/>)
							</logic:equal>
						</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">
							<logic:equal name="curriculumEntry" property="ectsCredits" value="0">-</logic:equal>
							<logic:notEqual name="curriculumEntry" property="ectsCredits" value="0">
								<bean:write name="curriculumEntry" property="ectsCredits"/>
							</logic:notEqual>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.DismissalEntry">
					<tr>
						<td>
							<logic:present name="curriculumEntry" property="curricularCourse">
								<bean:write name="curriculumEntry" property="curricularCourse.code"/>
							</logic:present>
						</td>
						<td>
							<logic:present name="curriculumEntry" property="curricularCourse">
								<bean:write name="curriculumEntry" property="curricularCourse.name"/>
							</logic:present>
							<logic:present name="curriculumEntry" property="curriculumGroup">
								<bean:message key="label.studentDismissal.group.credits.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES" /> (<bean:write name="curriculumEntry" property="curriculumGroup.name.content"/>)
							</logic:present>
						</td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
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
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotInDegreeCurriculumCurriculumEntry">
					<tr>
						<td></td>
						<td></td>
						<td colspan="2">
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="true">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
							</logic:equal>
						</td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.grade"/></td>						
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigth"/></td>
						<td class="acenter">
							<logic:empty name="curriculumEntry" property="weigthTimesClassification">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesClassification">
								<bean:write name="curriculumEntry" property="weigthTimesClassification"/>
							</logic:notEmpty>
						</td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<tr>
				<td colspan="8" style="text-align: right;">
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

</logic:greaterThan>
