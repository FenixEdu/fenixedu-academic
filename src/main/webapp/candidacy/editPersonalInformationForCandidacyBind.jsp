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
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>

 	<html:hidden property="method" value="bindPerson" />
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<p><bean:message key="message.ensure.personal.data" bundle="CANDIDATE_RESOURCES"/></p>
	
	<logic:notEmpty name="individualCandidacyProcessBean" property="choosePersonBean.person">
		<p><strong><bean:message key="label.person.personal.data" bundle="CANDIDATE_RESOURCES"/></strong></p>
		<fr:view name="individualCandidacyProcessBean" property="choosePersonBean.person" schema="CandidacyProcess.person.view">
			<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:notEmpty name="individualCandidacyProcessBean" property="personBean">
		<p><strong><bean:message key="label.candidate.personal.data" bundle="CANDIDATE_RESOURCES"/></strong></p>
		
		<logic:equal name="individualCandidacyProcessBean" property="choosePersonBean.employee" value="false">
		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean" property="personBean"
			schema="CandidacyProcess.personalDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=executeEditCandidacyPersonalInformationForBindInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		</logic:equal>

		<logic:equal name="individualCandidacyProcessBean" property="choosePersonBean.employee" value="true">
		
		<em><bean:message key="message.chosen.person.is.employee" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></em> 
		
		<fr:view name="individualCandidacyProcessBean" property="personBean"
			schema="CandidacyProcess.personalDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
		</logic:equal>
		
		<logic:notEmpty name="individualCandidacyProcessBean" property="choosePersonBean.person">
			<html:submit><bean:message key="button.join" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:notEmpty>
		<logic:empty name="individualCandidacyProcessBean" property="choosePersonBean.person">
			<html:submit><bean:message key="button.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:empty>
		<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</logic:notEmpty>
</fr:form>
