<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
		</p>
	</html:messages>
	
	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registrationConclusionBean" paramProperty="registration.idInternal">
				<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registrationConclusionBean" property="registration.student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registrationConclusionBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	<logic:present name="registrationConclusionBean" property="registration.ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="registrationConclusionBean" property="registration.ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
	<h3 class="mtop1 mbottom05"><bean:message key="label.summary" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:iterate id="curriculumGroup" name="registrationConclusionBean" property="curriculumGroupsNotVerifyingStructure">
		<p>
			<span class="error0">O grupo <bean:write name="curriculumGroup" property="fullPath"/> tem <bean:write name="curriculumGroup" property="aprovedEctsCredits"/> créditos ECTS quando deveria ter <bean:write name="curriculumGroup" property="creditsConcluded"/> créditos ECTS</span>
		</p>
	</logic:iterate>

	<logic:equal name="registrationConclusionBean" property="concluded" value="false">
		<p>
			<span class="error0"><bean:message key="registration.not.concluded" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		</p>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForCycle">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForRegistration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="concluded" value="true">
		<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="true">
			<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForCycleWithConclusionProcessedInformation">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
			<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForRegistrationWithConclusionProcessedInformation">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="false">
			<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForCycle">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
			<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForRegistration">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:equal>
	
		<p class="mtop05">
			<bean:define id="registrationId" name="registrationConclusionBean" property="registration.idInternal" />		
			<logic:empty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:empty>
			<logic:notEmpty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<bean:define id="cycleCurriculumGroupId" name="registrationConclusionBean" property="cycleCurriculumGroup.idInternal" />
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId + "&amp;cycleCurriculumGroupId=" + cycleCurriculumGroupId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:notEmpty>
		</p>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="false">
		<h3 class="mtop15 mbottom05"><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

		<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="true">
			<p>
				<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
					<fr:layout>
						<fr:property name="visibleCurricularYearEntries" value="false" />
					</fr:layout>
				</fr:view>
			</p>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="false">
			<bean:define id="curriculumEntries" name="registrationConclusionBean" property="curriculumForConclusion.curriculumEntries"/>
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
						<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPi"/>
					</td>
					<td class="acenter">
						<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPiCi"/>
					</td>
				</tr>
			</table>
		</logic:equal>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="canBeConclusionProcessed" value="true">
		<fr:form action="/registration.do?method=doRegistrationConclusion">
		
			<fr:edit id="registrationConclusionBean" name="registrationConclusionBean" visible="false" />
			
			<p class="mtop15">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
				</html:submit>
			</p>
		
		</fr:form>
	</logic:equal>

</logic:present>
