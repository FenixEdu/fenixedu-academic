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
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>


<fr:form action="/caseHandlingMobilityApplicationProcess.do">
	<fr:edit id="erasmus.vacancy.bean" name="erasmusVacancyBean" visible="false" />
	
	<fr:edit id="erasmusVacancyBean-edit" name="erasmusVacancyBean">
		
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusVacancyBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			
			<fr:slot name="mobilityProgram" layout="menu-select-postback">
				<fr:property name="destination" value="postback" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.MobilityProgramAllProvider" />
				<fr:property name="format" value="${name}" />
				<fr:property name="sortBy" value="name"/>
			</fr:slot>
			
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
		
		<fr:destination name="postback" path="<%= "/caseHandlingMobilityApplicationProcess.do?method=selectMobilityQuotaForQuotasManagementPostback&processId=" + processId %>" />
		
	</fr:edit>
</fr:form>


<logic:empty name="erasmusVacancyBean" property="mobilityProgram">
	<p><em><bean:message key="message.choose.mobility.program.to.view.quotas" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="erasmusVacancyBean" property="mobilityProgram">
	
	<bean:define id="mobilityProgramId" name="erasmusVacancyBean" property="mobilityProgram.externalId" />

	<ul>
		<li>
			<html:link action='<%= String.format("/caseHandlingMobilityApplicationProcess.do?method=prepareExecuteInsertMobilityQuota&amp;processId=%s&mobilityProgramId=%s", processId.toString(), mobilityProgramId) %>'>
				<bean:message key="label.erasmus.erasmusVacancy.insert" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</li>
	</ul>
	
	<logic:empty name="quotas">
		<p><em><bean:message key="message.mobility.quotas.empty.for.programme" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	</logic:empty>

	<logic:notEmpty name="quotas">
	<p><bean:message key="title.erasmus.erasmusVacancy.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
	
	<fr:view name="quotas">
	
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="mobilityAgreement.universityUnit.country.localizedName" key="label.erasmus.country" />
				<fr:slot name="mobilityAgreement.universityUnit.nameI18n" key="label.erasmus.university" />
				<fr:slot name="degree.presentationName" key="label.erasmus.degrees" />
				<fr:slot name="associatedToApplications" key="label.erasmus.vacancy.hasAssociatedCandidacies" />
			</fr:schema>
	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,width40em"/>
		        <fr:property name="sortBy" value="mobilityAgreement.universityUnit.country.localizedName=asc,mobilityAgreement.universityUnit.nameI18n=asc" />
	
				<fr:property name="linkFormat(delete)" value="<%= String.format("/caseHandlingMobilityApplicationProcess.do?method=executeRemoveVacancy&amp;processId=%s&amp;vacancyExternalId=${externalId}", processId.toString()) %>" />
				<fr:property name="key(delete)" value="label.erasmus.vacancy.vacancy.removal" />
				<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="visibleIfNot(delete)" value="associatedToApplications" />
				<fr:property name="confirmationKey(delete)" value="message.erasmus.vacancy.confirm.vacancy.removal" />
				<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
		        
			</fr:layout>		
	</fr:view>
	
	
	</logic:notEmpty>

</logic:notEmpty>
