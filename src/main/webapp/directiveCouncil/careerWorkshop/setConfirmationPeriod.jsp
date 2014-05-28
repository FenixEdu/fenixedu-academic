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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.manageCareerWorkshop" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<bean:define id="event" name="eventsBean" property="affectedEvent" type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" />
<h3>
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
</h3>

<p class="mtop2"><html:link action="/careerWorkshopApplication.do?method=prepare">
	<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="link.back" />
</html:link></p>

<h4>
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.subTitle" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="formattedBeginDate" />
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.subTitleSeparator" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="formattedEndDate" />
<%
	if (event.getRelatedInformation() != null) {
 %>
 	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.dashSeparator" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="relatedInformation" />
<%
     }
 %>
</h4>

<fr:form action="/careerWorkshopApplication.do?method=addConfirmationPeriod">
	<fr:edit name="eventsBean" id="eventsBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop.ManageCareerWorkshopApplicationsBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="confirmationStartDate" key="label.manageCareerWorkshop.confirmationStartDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="confirmationEndDate" key="label.manageCareerWorkshop.confirmationEndDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
			<fr:property name="columnClasses" value=",,,,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.confirm" />
	</html:submit>
</fr:form>