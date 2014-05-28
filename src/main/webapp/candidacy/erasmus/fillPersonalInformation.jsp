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
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.selectPerson" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 3</strong>: <bean:message key="label.erasmus.candidacy.educational.background" bundle="ACADEMIC_OFFICE_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 4</strong>: <bean:message key="label.erasmus.candidacy.degrees.and.subjects" bundle="ACADEMIC_OFFICE_RESOURCES" /> </span>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="candidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%= "/caseHandling" + processName + ".do?parentProcessId=" + parentProcessId.toString() %>'>
 	<html:hidden property="method" value="searchPersonForCandidacy" />

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
		<logic:notEmpty name="individualCandidacyProcessBean" property="personBean">
			<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="APPLICATION_RESOURCES"/>:</h3>
			<fr:edit id="candidacyProcess.personalDataBean" 
					 name="individualCandidacyProcessBean" property="personBean"
					 schema="ErasmusIndividualCandidacyProcess.personalDataBean">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=fillPersonalInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:edit>
			<html:submit onclick="this.form.method.value='fillCandidacyInformation';return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:notEmpty>
	</logic:notEmpty>

	<html:cancel onclick="this.form.method.value='listProcesses';return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>

<br/>
<em><bean:message key="renderers.validator.required.mark.explanation" bundle="RENDERER_RESOURCES" /></em>
