<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page language="java" %>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.AverageType"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"%>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<bean:define id="registrationCurriculumBean" name="registrationCurriculumBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean"/>
	<%
		final Registration registration = registrationCurriculumBean.getRegistration();
		request.setAttribute("registration", registration);
		final ExecutionYear executionYear = registrationCurriculumBean.getExecutionYear();
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
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	<logic:present name="registration" property="ingressionEnum">
	
	<h3 class="mbottom1"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	
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
	<fr:form action="/registration.do?method=viewRegistrationCurriculum">
		<fr:edit id="registrationCurriculumBean" 
			name="registrationCurriculumBean"
			visible="false"/>
		<fr:edit id="registrationCurriculumBean-executionYear" name="registrationCurriculumBean" schema="registration-select-execution-year" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop025 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
		</html:submit>
	</fr:form>
	
	<%
		final ICurriculum curriculum = registrationCurriculumBean.getCurriculum(executionYear);
		request.setAttribute("curriculum", curriculum);	
	
		if (!curriculum.isEmpty()) {
			request.setAttribute("sumPiCi", curriculum.getSumPiCi());
			request.setAttribute("sumPi", curriculum.getSumPi());
			request.setAttribute("weightedAverage", curriculum.getAverage());
			request.setAttribute("sumEctsCredits", curriculum.getSumEctsCredits());
			request.setAttribute("curricularYear", curriculum.getCurricularYear());
			request.setAttribute("totalCurricularYears", curriculum.getTotalCurricularYears());
		}
	%>
	
	<logic:equal name="curriculum" property="empty" value="true">
		<p class="mvert15">
			<em>
				<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</em>
		</p>	
	</logic:equal>
	
	<logic:equal name="curriculum" property="empty" value="false">
	
		<div class="infoop2 mtop2">
			<logic:notEmpty name="executionYear">
				<p class="mvert05"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/><bean:message key="begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="executionYear" property="year"/>.</p>
				<br/>
				<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
	
				<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
				<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
				<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="totalCurricularYears"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
			</logic:notEmpty>
	
			<logic:empty name="executionYear">
				<p class="warning0"><strong>Atenção:</strong><br/> <bean:message key="following.info.refers.to" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="all.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
				<br/>
	
				<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
					<p class="mvert05"><strong><bean:message key="final.degree.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		
					<p class="mtop1 mbottom05"><strong><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="pleft1 mvert05"><bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <b class="highlight1"><bean:write name="registrationCurriculumBean" property="finalAverage"/></b></p>
					<p class="mtop1 mbottom05"><strong><bean:message key="conclusion.date" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="pleft1 mvert05"><b class="highlight1"><bean:write name="registrationCurriculumBean" property="conclusionDate"/></b></p>
				</logic:equal>
				<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
					<logic:equal name="registration" property="concluded" value="true">
						<p class="mvert05"><span class="error0"><strong><bean:message key="missing.final.average.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></span></p>
					</logic:equal>

					<p class="mvert05"><strong><bean:message key="legal.value.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="mvert05"><strong><bean:message key="rules.info" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		
					<p class="mtop1 mbottom05"><strong>Cálculo da <bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="pleft1 mvert05"><bean:message key="degree.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
					<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="average.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
					<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="degree.average.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:write name="sumPiCi"/> / <bean:write name="sumPi"/> = <b class="highlight1"><bean:write name="weightedAverage"/></b></p>
		
					<p class="mtop1 mbottom05"><strong>Cálculo do <bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
					<p class="pleft1 mvert05"><bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <b class="highlight1"><bean:write name="curricularYear"/></b></p>
					<p class="pleft1 mvert05"><bean:message key="rule" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.rule" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
					<p class="pleft1 mvert05"><bean:message key="result" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:message key="curricular.year.abbreviation" bundle="ACADEMIC_OFFICE_RESOURCES"/> = <bean:message key="minimum" bundle="ACADEMIC_OFFICE_RESOURCES"/> (<bean:message key="int" bundle="ACADEMIC_OFFICE_RESOURCES"/> ( (<bean:write name="sumEctsCredits"/> + 24) / 60 + 1) ; <bean:write name="totalCurricularYears"/>) = <b class="highlight1"><bean:write name="curricularYear"/></b>;</p>
				</logic:equal>
			</logic:empty>
		</div>
	
		<table class="tstyle4 thlight tdcenter mtop15">
			<tr>
				<th><bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<th><bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> Ponderada</th>
				<logic:notEqual name="curriculum" property="studentCurricularPlan.averageType" value="WEIGHTED">
					<th><bean:message key="average" bundle="STUDENT_RESOURCES"/> Simples</th>
				</logic:notEqual>
				<th><bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/></th>
			</tr>
			<tr>
				<bean:size id="curricularEntriesCount" name="curriculum" property="curriculumEntries"/>
				<td><bean:write name="curricularEntriesCount"/></td>
				<td><bean:write name="sumEctsCredits"/></td>
				<logic:notEmpty name="executionYear">
					<td><bean:write name="weightedAverage"/></td>
					<logic:notEqual name="curriculum" property="studentCurricularPlan.averageType" value="WEIGHTED">
						<%
							curriculum.setAverageType(AverageType.SIMPLE);
							request.setAttribute("simpleAverage", curriculum.getAverage());
						%>
						<td><bean:write name="simpleAverage"/></td>
					</logic:notEqual>
					<td><bean:write name="curricularYear"/></td>
				</logic:notEmpty>
				<logic:empty name="executionYear">
					<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="false">
						<td><bean:write name="weightedAverage"/></td>
						<logic:notEqual name="curriculum" property="studentCurricularPlan.averageType" value="WEIGHTED">
							<%
								curriculum.setAverageType(AverageType.SIMPLE);
								request.setAttribute("simpleAverage", curriculum.getAverage());
							%>
							<td><bean:write name="simpleAverage"/></td>
						</logic:notEqual>
					<td><bean:write name="curricularYear"/></td>
					</logic:equal>
					<logic:equal name="registrationCurriculumBean" property="conclusionProcessed" value="true">
						<td><bean:write name="registrationCurriculumBean" property="finalAverage"/></td>
						<td>-</td>
					</logic:equal>			
				</logic:empty>
			</tr>
		</table>
	
		<logic:equal name="curriculum" property="studentCurricularPlan.boxStructure" value="true">
	<%--
			<p>
			<fr:view name="registration">
				<fr:layout name="registration-curriculum">
					<fr:property name="executionYear" value="<%=executionYear == null ? "" : executionYear.getYear()%>" />
				</fr:layout>
			</fr:view>
			</p>
	--%>
			<p>
				<fr:view name="curriculum"/>
			</p>
		</logic:equal>
		<logic:equal name="curriculum" property="studentCurricularPlan.boxStructure" value="false">
			<bean:define id="curriculumEntries" name="curriculum" property="curriculumEntries"/>
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
							<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>
							<td class="acenter">-</td>
							<td class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
							<td class="acenter">
								<logic:empty name="curriculumEntry" property="weigthTimesGrade">
									-
								</logic:empty>
								<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
									<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
								</logic:notEmpty>
							</td>
							<td class="acenter">-</td>
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
									<td class="acenter"><bean:write name="simpleEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>
									<td class="acenter"><bean:write name="simpleEntry" property="weigthForCurriculum"/></td>
									<logic:equal name="index" value="0">
										<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>			
										<td rowspan="<%= numberEntries %>" class="acenter">
											<logic:empty name="curriculumEntry" property="weigthTimesGrade">
												-
											</logic:empty>
											<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
												<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
											</logic:notEmpty>
										</td>
									</logic:equal>
									<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
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
									<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
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
									<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
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
								<logic:equal name="curriculumEntry" property="ectsCreditsForCurriculum" value="0">
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
						</tr>
					</logic:equal>
				</logic:iterate>
				<logic:iterate id="curriculumEntry" name="curriculumEntries">
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.CreditsInScientificAreaCurriculumEntry">
						<tr>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.credits.in.scientific.area"/></td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
						</tr>
					</logic:equal>
				</logic:iterate>
				<logic:iterate id="curriculumEntry" name="curriculumEntries">
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.CreditsInAnySecundaryAreaCurriculumEntry">
						<tr>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.credits.in.any.secondary.area"/></td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
							<td class="acenter">-</td>
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
							<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>						
							<td class="acenter">-</td>
							<td class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
							<td class="acenter">
								<logic:empty name="curriculumEntry" property="weigthTimesGrade">
									-
								</logic:empty>
								<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
									<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
								</logic:notEmpty>
							</td>
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
				</tr>
			</table>
		</logic:equal>	
		
	</logic:equal>

</logic:present>
