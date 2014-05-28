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
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:write name="process" property="displayName" /></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<h3 class="mtop15 mbottom025">Escolher Curso</h3>
<bean:define id="processId" name="process" property="externalId" />
<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>
	<html:hidden property="method" value="prepareExecuteIntroduceCandidacyResult" />
	<fr:edit name="individualCandidacyResultBean">
	<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacyResultBean" bundle="APPLICATION_RESOURCES">
		<fr:slot name="degree" layout="menu-select-postback" >
			<fr:property name="from" value="degrees"></fr:property>
			<fr:property name="format" value="${name}" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="individualCandidacyResultBean" property="degree">
<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeIntroduceCandidacyResult" />

	<fr:edit id="individualCandidacyResultBean" name="individualCandidacyResultBean" visible="false" />

	<logic:notEmpty name="individualCandidacyResultBean" property="candidacyProcess">
		<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.introduce.result" bundle="APPLICATION_RESOURCES"/></h3>
		
		<fr:edit id="individualCandidacyResultBean.manage"
			name="individualCandidacyResultBean"
			schema="DegreeTransferIndividualCandidacyResultBean.introduce.result.coordinator">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandling" + processName + ".do?method=executeIntroduceCandidacyResultInvalid&amp;processId=" + processId.toString() %>' />
		</fr:edit>
		
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</logic:notEmpty>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities';return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	
</fr:form>
</logic:present>
