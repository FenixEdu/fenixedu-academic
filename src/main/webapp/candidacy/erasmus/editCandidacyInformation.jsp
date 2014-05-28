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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<%-- <h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2> --%>

<h2>Edit Application Data</h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
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

<fr:form action='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?userAction=editCandidacy&processId=" + processId.toString() %>' id="erasmusCandidacyForm">

	<input type="hidden" id="methodId" name="method" value="executeEditCandidacyInformation"/>
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />


		<p class="mtop1 mbottom0"><strong>Date</strong></p>
		<fr:edit id="individualCandidacyProcessBean.candidacyDate" 
			 name="individualCandidacyProcessBean"
			 schema="MobilityApplicationProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright ulnomargin thmiddle"/>
		        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>'  />
		</fr:edit>

		<p class="mtop1 mbottom0"><strong><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.exchange.coordinator.edit" 
					property="mobilityStudentDataBean" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright ulnomargin thmiddle"/>
		        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		
		<p class="mtop1 mbottom0"><strong><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.current.study" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.current.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright ulnomargin"/>
		        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		
		<p class="mtop1 mbottom0"><strong><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.period.of.study"
					name="individualCandidacyProcessBean"
					schema="ErasmusIndividualCandidacyProcess.period.of.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright ulnomargin"/>
		        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<fr:edit		id="erasmusStudentDataBean.applyForSemester.edit"
					name="individualCandidacyProcessBean"
					property="mobilityStudentDataBean"
					schema="ErasmusStudentDataBean.applyForSemester.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
  		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:property name="requiredMessageShown" value="false" />
			</fr:layout>
		</fr:edit>
		
		
		<p><strong>Portuguese Language Course</strong></p>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.language.intensive.course"
					name="individualCandidacyProcessBean"
					property="mobilityStudentDataBean"
					schema="MobilityStudentData.languageCompetence.intensive.portuguese.course">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
		        <fr:property name="requiredMessageShown" value="false" />
			</fr:layout>
		</fr:edit>
		
		<p>
			<html:submit onclick="this.form.method.value='executeEditCandidacyInformation'; return true;"><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
			<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		</p>

</fr:form>
