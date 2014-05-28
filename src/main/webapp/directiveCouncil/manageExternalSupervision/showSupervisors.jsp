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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>
<bean:define id="rpId" name="sessionBean" property="registrationProtocol.externalId"/>
<bean:define id="agreement" name="sessionBean" property="registrationAgreement.name"/>

<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.externalSupervisorsManagement"/></h2>

<p><html:link action="/manageExternalSupervision.do?method=prepareSelectAgreement">
	<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="link.back"/>
</html:link></p>

<p class="mtop15 mbottom05"><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="label.showSupervisors.agreement"/><strong><fr:view name="sessionBean" property="registrationAgreement.description"/></strong></p>

<logic:notEmpty name="sessionBean" property="supervisors">
	<fr:view name="sessionBean" property="supervisors">
		<fr:layout name="tabular">
			<fr:property name="sortBy" value="name,externalId"/>
			
			<fr:property name="linkFormat(delete)" value="<%="/manageExternalSupervision.do?method=deleteSupervisor&supervisorId=${externalId}&registrationProtocolId=" + rpId.toString()%>"/>
			<fr:property name="order(delete)" value="1" />
			<fr:property name="key(delete)" value="label.showSupervisors.supervisor.delete" />
			<fr:property name="bundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES" />
			<fr:property name="confirmationKey(delete)" value="label.showSupervisors.deleteSupervisor"/>
			<fr:property name="confirmationBundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES"/>
			<fr:property name="confirmationArgs(delete)" value="<%="${name},${istUsername}," + agreement.toString()%>"/>
			
			<fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
			<fr:property name="columnClasses" value=",,,tdclear tderror1" />
		</fr:layout>
		<fr:schema type="net.sourceforge.fenixedu.domain.Person" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="istUsername" key="label.showSupervisors.supervisor.istUsername" />
			<fr:slot name="name" key="label.showSupervisors.supervisor.name"/>
		</fr:schema>
	</fr:view>
</logic:notEmpty>

<logic:empty name="sessionBean" property="supervisors">
	<p class="mtop05"><em><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="label.showSupervisors.nonexistentSupervisors"/></em></p>
</logic:empty>

<div id="divAdicionar">
	<p class="mtop15 mbottom0"><strong><bean:message key="label.showSupervisors.addSupervisor" bundle="DIRECTIVE_COUNCIL_RESOURCES"/></strong></p>
	<fr:form action="/manageExternalSupervision.do?method=addSupervisor">
		<fr:edit name="sessionBean" id="sessionBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision.ManageExternalSupervisionBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
				<fr:slot name="newSupervisor" layout="autoComplete" key="label.showSupervisors.nameOrId" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
					<fr:property name="size" value="35" />
					<fr:property name="labelField" value="name" />
					<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
					<fr:property name="args" value="slot=name,size=20" />
					<fr:property name="minChars" value="3" />
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchPeopleByNameOrISTID" />
					<fr:property name="indicatorShown" value="true" />
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person" />
					<fr:property name="required" value="true"/>
				</fr:slot>
				<fr:destination name="invalid" path="/manageExternalSupervision.do?method=invalidAddSupervisor"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
			</fr:schema>
		</fr:edit>
		<html:submit>
			<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.add" />
		</html:submit>
	</fr:form>
</div>