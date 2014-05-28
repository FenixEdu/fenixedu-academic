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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.manageCareerWorkshop" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<fr:form action="/careerWorkshopApplication.do?method=addApplicationEvent">
	<fr:edit name="applicationsBean" id="applicationsBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop.ManageCareerWorkshopApplicationsBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="newEventStartDate" key="label.manageCareerWorkshop.startDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="newEventEndDate" key="label.manageCareerWorkshop.endDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="newEventInformation" key="label.manageCareerWorkshop.relatedInformation"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
				<fr:property name="columnClasses" value=",,,,tdclear tderror1" />
			</fr:layout>
		</fr:schema>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.add" />
	</html:submit>
</fr:form>

<p class="mtop25"></p>
<fr:view name="applicationsBean" property="careerWorkshopApplicationEvents">
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="beginDate=desc"/>
		
		<fr:property name="linkFormat(downloadApplications)" value="<%="/careerWorkshopApplication.do?method=downloadApplications&eventId=${externalId}"%>"/>
		<fr:property name="order(downloadApplications)" value="1" />
		<fr:property name="key(downloadApplications)" value="label.manageCareerWorkshop.downloadApplications" />
		<fr:property name="bundle(downloadApplications)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(setConfirmationPeriod)" value="<%="/careerWorkshopApplication.do?method=setConfirmationPeriod&eventId=${externalId}"%>"/>
		<fr:property name="order(setConfirmationPeriod)" value="2" />
		<fr:property name="key(setConfirmationPeriod)" value="label.manageCareerWorkshop.setConfirmationPeriod" />
		<fr:property name="bundle(setConfirmationPeriod)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(downloadConfirmations)" value="<%="/careerWorkshopApplication.do?method=downloadConfirmations&eventId=${externalId}"%>"/>
		<fr:property name="visibleIf(downloadConfirmations)" value="isConfirmationPeriodAttached"/>
		<fr:property name="order(downloadConfirmations)" value="3" />
		<fr:property name="key(downloadConfirmations)" value="label.manageCareerWorkshop.downloadConfirmations" />
		<fr:property name="bundle(downloadConfirmations)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(delete)" value="<%="/careerWorkshopApplication.do?method=deleteApplicationEvent&eventId=${externalId}"%>"/>
		<fr:property name="order(delete)" value="4" />
		<fr:property name="key(delete)" value="label.manageCareerWorkshop.cancelPeriod" />
		<fr:property name="bundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.manageCareerWorkshop.cancellationConfirmation"/>
		<fr:property name="confirmationBundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="<%="${formattedBeginDate},${formattedEndDate}"%>"/>
		<%--
		<fr:property name="linkFormat(purgeConfirmations)" value="<%="/careerWorkshopApplication.do?method=purgeConfirmations&eventId=${externalId}"%>"/>
		<fr:property name="visibleIf(purgeConfirmations)" value="isConfirmationPeriodAttached"/>
		<fr:property name="order(purgeConfirmations)" value="5" />
		<fr:property name="key(purgeConfirmations)" value="label.manageCareerWorkshop.purgeConfirmations" />
		<fr:property name="bundle(purgeConfirmations)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		--%>
		<fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
		<fr:property name="columnClasses" value=",,,,,,,,tdclear tderror1" />
	</fr:layout>
	<fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" bundle="DIRECTIVE_COUNCIL_RESOURCES">
		<fr:slot name="formattedBeginDate" key="label.manageCareerWorkshop.startDate" />
		<fr:slot name="formattedEndDate" key="label.manageCareerWorkshop.endDate"/>
		<fr:slot name="relatedInformation" key="label.manageCareerWorkshop.relatedInformation"/>
		<fr:slot name="numberOfApplications" key="label.managerCareerWorkshop.numberOfApplications"/>
		<fr:slot name="confirmationBeginDate" key="label.manageCareerWorkshop.confirmationStartDate"/>
		<fr:slot name="confirmationEndDate" key="label.manageCareerWorkshop.confirmationEndDate"/>
		<fr:slot name="numberOfConfirmations" key="label.managerCareerWorkshop.numberOfConfirmations"/>
	</fr:schema>
</fr:view>