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

<fr:form action="/registeredDegreeCandidacies.do?method=view">
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" 
			type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.candidacy.registrations.RegisteredDegreeCandidaciesSelectionBean">
			<fr:slot name="executionYear" layout="menu-select" required="true" >
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="campus" layout="menu-select" required="true" >
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager.CampusProvider" />
			<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="entryPhase" required="true" />
			<fr:slot name="beginDate" />
			<fr:slot name="endDate" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror1" />
		</fr:layout>
		
		
	</fr:edit>
	<html:submit><bean:message key="label.search" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>
	
	<logic:empty name="studentCandidacies">
		<em><bean:message key="message.registeredDegreeCandidacies.result.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</logic:empty>	
	
	<logic:notEmpty name="studentCandidacies">
	
		<p>
			<html:link href="#" onclick="jQuery('#export-form').submit()">
				<bean:message bundle="APPLICATION_RESOURCES" key="link.export" />
			</html:link>
		</p>
		<p>
			<html:link href="#" onclick="jQuery('#export-form-for-residence').submit()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.registeredDegreeCandidacies.export.withApplyForResidence" />
			</html:link>
		</p>
		
		<fr:view name="studentCandidacies">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy">
				<fr:slot name="activeCandidacySituation.situationDate" key="label.registeredDegreeCandidacies.registrationDate" />
				<fr:slot name="executionDegree.degree.nameI18N" key="label.registeredDegreeCandidacies.degreeName" />
				<fr:slot name="registration.number" key="label.registeredDegreeCandidacies.studentNumber" />
				<fr:slot name="person.name" key="label.registeredDegreeCandidacies.studentName" />
				<fr:slot name="person.documentIdNumber" key="label.registeredDegreeCandidacies.documentIdNumber" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
		</fr:view>

		<fr:form id="export-form" action="/registeredDegreeCandidacies.do?method=export">
			<fr:edit id="bean" name="bean" visible="false" />
		</fr:form>
		<fr:form id="export-form-for-residence" action="/registeredDegreeCandidacies.do?method=exportWithApplyForResidence">
			<fr:edit id="bean" name="bean" visible="false" />
		</fr:form>

		<p>
			<html:link href="#" onclick="jQuery('#export-form').submit()">
				<bean:message bundle="APPLICATION_RESOURCES" key="link.export" />
			</html:link>
		</p>
		<p>
			<html:link href="#" onclick="jQuery('#export-form-for-residence').submit()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.registeredDegreeCandidacies.export.withApplyForResidence" />
			</html:link>
		</p>
	</logic:notEmpty>
