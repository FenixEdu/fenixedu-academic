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

<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.externalSupervisorsManagement"/></h2>

<div class="infoop2"><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="label.selectRegistrationAgreement.explanation"/></div>

<fr:form action="/manageExternalSupervision.do?method=showSupervisors">
	<fr:edit id="sessionBean" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision.ManageExternalSupervisionBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="registrationAgreement" layout="menu-select" key="label.selectRegistrationAgreement.registrationAgreement" required="true">
				<fr:property name="format" value="${description}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.lists.RegistrationAgreementTypeProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thmiddle thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.continue"/>
	</html:submit>
</fr:form>