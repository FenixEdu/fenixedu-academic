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
<h2><bean:message key="title.bind.person.to.candidacy" bundle="CANDIDATE_RESOURCES"/></h2>

<p><em><bean:message key="message.candidacy.not.bind.person.create.payment" bundle="CANDIDATE_RESOURCES"/></em></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.choosePersonBean.edit" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:form action='<%= "/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>

 	<html:hidden property="method" value="searchPersonForCandidacy" />
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="individualCandidacyProcessBean" property="candidacyProcess">
			<fr:view name="individualCandidacyProcessBean" property="choosePersonBean" schema="CandidacyProcess.choosePersonBean.view">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=prepareCreateNewProcessInvalid&amp;parentProcessId=" + parentProcessId.toString() %>' />
			</fr:view>
			
			<br/>
			<bean:message key="label.candidacy.similar.persons" bundle="APPLICATION_RESOURCES" />:
			<fr:edit id="individualCandidacyProcessBean.choosePersonBean.selectPerson"
				name="individualCandidacyProcessBean" property="choosePersonBean"
				schema="CandidacyProcess.choosePersonBean.selectPerson">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
			<bean:message key="label.candidacy.use.similar.person" bundle="APPLICATION_RESOURCES" />:
			<br/>
			<html:submit onclick="this.form.method.value='prepareEditPersonalInformationForBindWithSelectedPerson';return true;"><bean:message key="label.choose" bundle="APPLICATION_RESOURCES" /></html:submit>

			<br/>
			<br/>
			<bean:message key="label.candidacy.no.similar.person.to.use" bundle="APPLICATION_RESOURCES" />:
			<br/>
			<html:submit onclick="this.form.method.value='prepareEditPersonalInformationForBindWithNewPerson';return true;"><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		
	</logic:notEmpty>

	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>

<br/>
