<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
</html:messages>

<ul>
	<li>
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteInsertErasmusVacancy&amp;processId=" + processId.toString() %>'>
			<bean:message key="label.erasmus.erasmusVacancy.insert" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
</ul>


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<p><bean:message key="title.erasmus.erasmusVacancy.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<fr:view name="process" property="candidacyPeriod.erasmusVacancy" schema="ErasmusVacancy.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,width40em"/>
	        <fr:property name="sortBy" value="universityUnit.country.localizedName=asc,universityUnit.nameI18n=asc" />

			<fr:property name="linkFormat(delete)" value="<%= String.format("/caseHandlingErasmusCandidacyProcess.do?method=executeRemoveVacancy&amp;processId=%s&amp;vacancyExternalId=${externalId}", processId.toString()) %>" />
			<fr:property name="key(delete)" value="label.erasmus.vacancy.vacancy.removal" />
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
			<fr:property name="visibleIfNot(delete)" value="vacancyAssociatedToAnyCandidacy" />
			<fr:property name="confirmationKey(delete)" value="message.erasmus.vacancy.confirm.vacancy.removal" />
			<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
	        
		</fr:layout>		
</fr:view>