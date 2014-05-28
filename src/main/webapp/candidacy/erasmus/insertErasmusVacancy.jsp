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

<p><bean:message key="message.erasmus.insertVacancy.instructions" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<p>
<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteViewMobilityQuota&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
</p>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span><br/>
</html:messages>


<fr:form action='<%="/caseHandling" + processName + ".do?method=executeInsertMobilityQuota&processId=" + processId.toString() %>' >
	<fr:edit id="erasmus.vacancy.bean" name="erasmusVacancyBean" visible="false" />
	
	<p><strong><bean:message key="label.erasmus.insertVacancy.choose.university" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit 	name="erasmusVacancyBean"
				id="erasmus.vacancies.country.and.university"
				schema="ErasmusVacancy.insert.choose.country.university">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,width40em,tdclear error0"/>
		</fr:layout>
		
		<fr:destination name="chooseCountryPostback" path='<%= "/caseHandling" + processName + ".do?method=selectCountryForVacancyInsertion&processId="  + processId.toString() %>' /> 
	</fr:edit>
	
	
	<p><strong><bean:message key="label.eramsus.insertVacancy.choose.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit	name="erasmusVacancyBean"
				id="erasmus.vacancies.degree"
				schema="ErasmusVacancy.insert.choose.degree">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,width40em,tdclear error0"/>
		</fr:layout>				
	</fr:edit>
	
	<html:submit><bean:message key="label.insert" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>

