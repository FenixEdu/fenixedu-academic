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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>

<%-- <h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2> --%>

<h2><bean:message key="erasmus.label.edit.degree.and.courses" bundle="CANDIDATE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="onlyAllowedDegreeEnrolment" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.onlyAllowedDegreeEnrolment"/>

<fr:form action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&amp;processId=%s", processId.toString()) %>'>

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<p><strong><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
				name="individualCandidacyProcessBean"
				schema="ErasmusIndividualCandidacyProcess.university.edit" 
				property="mobilityStudentDataBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="chooseCountryPostback" path="<%= "/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&method=chooseCountry&amp;processId=" + processId.toString() %>"/>
		<fr:destination name="chooseUniversityPostback" path="<%= "/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&method=chooseUniversity&amp;processId=" + processId.toString() %>"/>
	</fr:edit>
</fr:form>

<logic:empty name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity">
	<p><em><bean:message key="message.erasmus.select.university" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity">
<fr:form action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&amp;processId=%s", processId.toString()) %>' id="thisForm">

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />


	<p>
		<logic:notEqual name="onlyAllowedDegreeEnrolment" value="true">
			<strong><bean:message key="label.eramsus.candidacy.choosen.subjectsAndDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
		</logic:notEqual>
		<logic:equal name="onlyAllowedDegreeEnrolment" value="true">
			<strong><bean:message key="label.erasmus.candidacy.choose.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
		</logic:equal>
		<strong><bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	</p>

	<bean:define id="universityName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity.nameI18n.content" type="String"/> 
	
	<p>
		<em><bean:message key="message.erasmus.for.chosen.university.must.select.majority.of.courses" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= universityName %>"/></em>
	</p>

	<fr:view name="individualCandidacyProcessBean" schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight thright"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>
		
	<logic:notEqual name="onlyAllowedDegreeEnrolment" value="true">
		<div class="mtop3">		
			<p><em><bean:message key="message.erasmus.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
			<fr:edit id="degree.course.information.bean.edit" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thright"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:destination name="chooseDegreePostback" path='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&method=chooseDegree&amp;processId=%s", processId) %>' />
				</fr:layout>
			</fr:edit>
				
			
			<p><html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;">+ <bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit></p>
			
			<table class="tstyle2 thlight thcenter">
			<tr>
				<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
				<th><!-- just in case --></th>
			</tr>
			<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
				<bean:define id="curricularCourseId" name="course" property="externalId" />
			<tr>
				<td>
					<fr:view name="course" property="nameI18N">
					</fr:view>
				</td>
				<td>
					<fr:view name="course" property="degree.nameI18N" /> - 
					<fr:view name="course" property="degree.sigla" />
				</td>		
				<td>
					<a onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
				</td>
			</tr>
			</logic:iterate>
			</table>
		
			<p>
				<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>:
				<fr:view	name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
			</p>
			
		</div>
	</logic:notEqual>
	
	<logic:equal name="onlyAllowedDegreeEnrolment" value="true">
		<div class="mtop3">		
			<fr:edit id="mobility.individual.application" name="mobilityIndividualApplicationProcessBean">
				<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
					<fr:slot name="degree" key="label.mobility.degree" layout="menu-select-postback">
						<fr:property name="format" value="${presentationName}" />
						<fr:property name="destination" value="chooseDegreePostback"/>
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.DegreesForExecutionYearProviderForMobilityIndividualApplicationProcess" />		
					</fr:slot>
				</fr:schema>
				
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:destination name="chooseDegreePostback" path="/caseHandlingMobilityIndividualApplicationProcess.do?method=chooseDegreeForMobility" />
				</fr:layout>
			</fr:edit>
		</div>
	</logic:equal>
	<p>
		<html:submit onclick="this.form.method.value='executeEditDegreeAndCoursesInformation'; return true;"><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>
</logic:notEmpty>
