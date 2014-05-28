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

<h2><bean:message key="label.emailTemplates" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<logic:equal name="hasMobilityPrograms" value="true">
<fr:form action="/caseHandlingMobilityApplicationProcess.do">
	<fr:edit id="mobilityEmailTemplateBean" name="mobilityEmailTemplateBean" visible="false" />
	
	<fr:edit id="mobilityEmailTemplateBean-choose-type" name="mobilityEmailTemplateBean">	
	
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="type" layout="menu-postback">
				<fr:property name="destination" value="postback" />
				<fr:property name="format" value="${localizedName}" />
				<fr:property name="sortBy" value="localizedName"/>
			</fr:slot>
			<fr:slot name="mobilityProgram" layout="menu-select-postback">
				<fr:property name="destination" value="postback" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.MobilityProgramAllProvider" />
				<fr:property name="format" value="${name}" />
				<fr:property name="sortBy" value="name"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear error0" /> 
		</fr:layout>
		
		<fr:destination name="postback" path="<%= "/caseHandlingMobilityApplicationProcess.do?method=manageEmailTemplatesPostback&processId=" + processId %>" />
		
	</fr:edit>
	
</fr:form>
</logic:equal>

<logic:empty name="mobilityEmailTemplateBean" property="type">

	<p><em><bean:message key="message.choose.email.type.in.order.to.edit" bundle="ACADEMIC_OFFICE_RESOURCES" /> </em></p>

</logic:empty>

<logic:notEmpty name="mobilityEmailTemplateBean" property="type">
<logic:notEmpty name="mobilityEmailTemplateBean" property="mobilityProgram">
	<div class="mbottom3">
		<fr:form action="<%= "/caseHandlingMobilityApplicationProcess.do?method=previewEmailTemplate&processId=" + processId %>">
			<fr:edit id="mobilityEmailTemplateBean" name="mobilityEmailTemplateBean" visible="false" />
			
			<fr:edit id="mobilityEmailTemplateBean-edit" name="mobilityEmailTemplateBean">
				<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateBean" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="subject" required="true">
						<fr:property name="size" value="161" />
					</fr:slot>
					<fr:slot name="body" layout="longText" required="true">
						<fr:property name="rows" value="40" />
						<fr:property name="columns" value="120" />
					</fr:slot>
				</fr:schema>
	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					<fr:property name="columnClasses" value=",,tdclear error0" />
				</fr:layout>
			</fr:edit>
			
			<p><html:submit><bean:message key="label.preview" bundle="APPLICATION_RESOURCES" /></html:submit></p>
			
		</fr:form>
	</div>
	
	<div id="help_box">
		<bean:define id="typeName" name="mobilityEmailTemplateBean" property="type.fullQualifiedName" />
		
		<bean:message key="<%= "message.instructions." + typeName %>" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</div>
</logic:notEmpty>
</logic:notEmpty>
