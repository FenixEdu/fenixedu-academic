<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%@page import="org.apache.struts.action.ActionMessages"%><html:xhtml/>

<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	

<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registrationConclusionBean" paramProperty="registration.externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>


<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="true">
	<br/>
	<div class="error0"><strong><bean:message  key="message.conclusion.process.already.performed" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></div>
	<br/>
</logic:equal>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>" >
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>
<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="illegal.access">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<logic:equal name="registrationConclusionBean" property="hasAccessToRegistrationConclusionProcess" value="true">

	<div style="float: right;">
		<bean:define id="personID" name="registrationConclusionBean" property="registration.student.person.externalId"/>
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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
	
	
	<%-- Credits in group not correct  --%> 		
	<h3 class="mtop1 mbottom05"><bean:message key="label.summary" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:iterate id="curriculumGroup" name="registrationConclusionBean" property="curriculumGroupsNotVerifyingStructure">
		<p>
			<span class="error0">O grupo <bean:write name="curriculumGroup" property="fullPath"/> tem <bean:write name="curriculumGroup" property="aprovedEctsCredits"/> créditos ECTS quando deveria ter <bean:write name="curriculumGroup" property="creditsConcluded"/> créditos ECTS</span>
		</p>
	</logic:iterate>
		
	<%-- Registration Not Concluded  --%> 
	<logic:equal name="registrationConclusionBean" property="concluded" value="false">
		<p>
			<span class="error0"><bean:message key="registration.not.concluded" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		</p>
		<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
			<%-- Conclusion Process For Cycle  --%>
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForCycle">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
			<%-- Conclusion Process For Registration  --%>
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForRegistration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
	</logic:equal>
	
	
	<%-- Registration Concluded  --%>
	<logic:equal name="registrationConclusionBean" property="concluded" value="true">
		
		<%-- Conclusion Processed  --%>
		<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="true">
			<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
			
				<%-- Conclusion Process For Cycle  --%>
				<div style="float: left;">
					<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
					<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForCycleWithConclusionProcessedInformation">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
							<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
						</fr:layout>
					</fr:view>
				</div>

				<div style="float: left; margin-left: 20px;">				
					<logic:equal name="registrationConclusionBean" property="canRepeatConclusionProcess" value="true">		
						
						<strong><bean:message  key="student.new.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>		
						<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewConclusionPreviewForCycle">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
								<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
							</fr:layout>
						</fr:view>
					</logic:equal>
				</div>

				<div style="clear: both;"></div>
			</logic:equal>

			<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
			
				<%-- Conclusion Process For Registration  --%>
				<div style="float: left;">
					<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
					<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForRegistrationWithConclusionProcessedInformation">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
							<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
						</fr:layout>
					</fr:view>
				</div>
				
				<div style="float: left; margin-left: 20px;">
					<logic:equal name="registrationConclusionBean" property="canRepeatConclusionProcess" value="true">		
						<strong><bean:message  key="student.new.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
						<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewConclusionPreviewForRegistration">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
								<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
							</fr:layout>
						</fr:view>
					</logic:equal>
				</div>
				
			</logic:equal>
			<div style="clear: both;"></div>

		</logic:equal>
		
		<%-- Conclusion Not Processed  --%>
		<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="false">
			<logic:iterate id="curriculumModule" name="registrationConclusionBean" property="curriculumModulesWithNoConlusionDate">
				<p>
					<span class="error0"><bean:write name="curriculumModule" property="fullPath"/> não tem data de conclusão, assegure-se que está concluído e todas as datas de avaliação estão inseridas no sistema.</span>
				</p>
			</logic:iterate>
			
			<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
				<%-- Conclusion Process For Cycle  --%>
				<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewConclusionPreviewForCycle">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
			<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
				<%-- Conclusion Process For Registration  --%>
				<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
				<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewConclusionPreviewForRegistration">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:equal>
	
		<p class="mtop05">
			<bean:define id="registrationId" name="registrationConclusionBean" property="registration.externalId" />		
			<logic:empty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:empty>
			<logic:notEmpty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<bean:define id="cycleCurriculumGroupId" name="registrationConclusionBean" property="cycleCurriculumGroup.externalId" />
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId + "&amp;cycleCurriculumGroupId=" + cycleCurriculumGroupId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:notEmpty>
		</p>
	</logic:equal>

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
					<th class=" scplancolcurricularcourse" rowspan="2" colspan="2">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum"/>
					</th>
					<th class=" scplancolgrade"colspan="3">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="degree.average"/>
					</th>
					<th class=" scplancolgrade">
						<bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</th>
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
					</td>
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

	
	<%-- Form used to concluded process or to repeat --%>		
	<logic:equal name="registrationConclusionBean" property="canBeConclusionProcessed" value="true">
		<fr:form action="/registration.do?method=doRegistrationConclusion">
		
			<fr:edit id="registrationConclusionBean" name="registrationConclusionBean" visible="false" />
			
			<strong><bean:message  key="student.registrationConclusionProcess.data" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
			<fr:edit id="registrationConclusionBean-manage" name="registrationConclusionBean">
				<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean">
					<fr:slot name="calculatedConclusionDate" readOnly="true">
						<fr:property name="classes" value="bold" />
					</fr:slot>
					<fr:slot name="enteredConclusionDate" layout="input-with-comment">
				        <fr:property name="bundle" value="APPLICATION_RESOURCES"/>
						<fr:property name="comment" value="label.registrationConclusionProcess.enteredConclusionDate.comment"/>
						<fr:property name="commentLocation" value="right" />
					</fr:slot>
					<fr:slot name="observations" key="label.anotation" bundle="ACADEMIC_OFFICE_RESOURCES">
					</fr:slot>

				</fr:schema>
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
			
			<p class="mtop15">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
				</html:submit>
			</p>
		
		</fr:form>
	</logic:equal>

</logic:equal>

<logic:equal name="registrationConclusionBean" property="hasAccessToRegistrationConclusionProcess" value="false">
	<p class="mtop15">
		<em class="error0"><bean:message key="error.not.authorized.to.registration.conclusion.process" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:equal>
