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
	
	<logic:present name="registrationConclusionBean" property="registration.ingression">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="registrationConclusionBean" property="registration.ingression">
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
			<logic:iterate id="curriculumModule" name="registrationConclusionBean" property="curriculumModulesWithNoConlusionDate">
				<p>
					<span class="error0"><bean:write name="curriculumModule" property="fullPath"/> não tem data de conclusão, assegure-se que está concluído e todas as datas de avaliação estão inseridas no sistema.</span>
				</p>
			</logic:iterate>
			
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
			<table class="scplan">
				<tr class="scplangroup">
					<td class=" scplancolcurricularcourse" rowspan="2" colspan="2">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum"/>
					</th>
					<td class=" scplancolgrade"colspan="3">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="degree.average"/>
					</th>
					<td class=" scplancolgrade">
						<bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</td>
				</tr>
				<tr class="scplangroup">
					<td class=" scplancolgrade">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>
					</td>
					<td class=" scplancolgrade">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/>
					</td>
					<td class=" scplancolgrade" style="width: 100px;">
						(<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/> x <bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>)
					</td>
					<td  class=" scplancolgrade" style="width: 100px;">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.credits"/>
					</th>
				</tr>
				<logic:iterate id="curriculumEntry" name="curriculumEntries">
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotInDegreeCurriculumCurriculumEntry">
						<tr class="scplanenrollment">
							<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
							<td class=" scplancolcurricularcourse"><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
							<td class=" scplancolgrade"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>						
							<td class=" scplancolweight"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
							<td class=" scplancolweight">
								<logic:empty name="curriculumEntry" property="weigthTimesGrade">
									-
								</logic:empty>
								<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
									<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
								</logic:notEmpty>
							</td>
							<td class=" scplancolects">
								<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
									-
								</logic:empty>
								<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
									<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
								</logic:notEmpty>
							</td>
						</tr>
					</logic:equal>
				</logic:iterate>				
				<logic:iterate id="curriculumEntry" name="curriculumEntries">
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.GivenCreditsEntry">
						<tr class="scplanenrollment">
							<td class="acenter">-</td>
							<td class=" scplancolcurricularcourse"><bean:message bundle="APPLICATION_RESOURCES" key="label.givenCredits"/></td>
							<td class=" scplancolgrade">-</td>						
							<td class=" scplancolweight">-</td>
							<td class=" scplancolweight">-</td>
							<td class=" scplancolects">
								<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
									-
								</logic:empty>
								<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
									<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
								</logic:notEmpty>
							</td>
						</tr>
					</logic:equal>
				</logic:iterate>				
				<tr class="scplanenrollment">
					<td colspan="3" style="text-align: right;">
						Somatórios
					</td>
					<td class=" scplancolweight">
						<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPi"/>
					</td>
					<td class=" scplancolweight">
						<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPiCi"/>
					</td>
					<td class=" scplancolects">
						<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumEctsCredits"/>
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
