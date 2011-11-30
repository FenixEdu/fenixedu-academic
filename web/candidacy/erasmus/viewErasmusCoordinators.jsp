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
		<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=prepareExecuteAssignCoordinator&amp;processId=" + processId.toString() %>'>
			<bean:message key="label.erasmus.erasmus.coordinator.insert" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
</ul>


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<p><bean:message key="title.erasmus.erasmus.coordinators.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>


<logic:empty name="process" property="coordinators">
	<em>There's no coordinators assigned</em>
</logic:empty>

<logic:notEmpty name="process" property="coordinators">
	<fr:view name="process" property="coordinators" schema="ErasmusCoordinator.list">
				<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,width40em"/>
		        <fr:property name="sortBy" value="degree.presentationName=asc,teacher.person.name=asc" />
	
				<fr:property name="linkFormat(remove)" value="<%= String.format("/caseHandlingErasmusCandidacyProcess.do?method=executeRemoveTeacherFromCoordinators&amp;processId=%s&amp;erasmusCoordinatorExternalId=${externalId}", processId.toString()) %>" />
				<fr:property name="key(remove)" value="label.erasmus.coordinator.removal" />
				<fr:property name="bundle(remove)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="confirmationKey(remove)" value="message.erasmus.coordinator.removal.confirmation" />
				<fr:property name="confirmationBundle(remove)" value="ACADEMIC_OFFICE_RESOURCES" />
		        
			</fr:layout>		
	</fr:view>
</logic:notEmpty>
