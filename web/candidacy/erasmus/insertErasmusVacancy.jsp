<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>

<p><bean:message key="message.erasmus.insertVacancy.instructions" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />


<fr:form action='<%="/caseHandling" + processName + ".do?method=executeInsertErasmusVacancy&processId=" + processId.toString() %>' >
	<fr:edit id="erasmus.vacancy.bean" name="erasmusVacancyBean" visible="false" />
	
	<p><strong><bean:message key="label.erasmus.insertVacancy.choose.university" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>:
	<fr:edit 	name="erasmusVacancyBean"
				id="erasmus.vacancies.country.and.university"
				schema="ErasmusVacancy.insert.choose.country.university">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,width40em"/>
		</fr:layout>
		
		<fr:destination name="chooseCountryPostback" path='<%= "/caseHandling" + processName + ".do?method=selectCountryForVacancyInsertion&processId="  + processId.toString() %>' /> 
	</fr:edit>
	
	
	<p><strong><bean:message key="label.eramsus.insertVacancy.choose.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit	name="erasmusVacancyBean"
				id="erasmus.vacancies.degree"
				schema="ErasmusVacancy.insert.choose.degree">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,width40em"/>
		</fr:layout>				
	</fr:edit>

</fr:form>

