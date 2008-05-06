<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.candidacy.dfa.periodsManagement" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>

<fr:view name="executionDegrees" schema="DFAPeriodsManagement.ExecutionDegree.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="linkFormat(editCandidacyPeriod)" value="/dfaPeriodsManagement.do?method=prepareEditCandidacyPeriod&executionDegreeId=${idInternal}"/>
		<fr:property name="key(editCandidacyPeriod)" value="link.candidacy.dfa.editCandidacyPeriod"/>
		<fr:property name="bundle(editCandidacyPeriod)" value="ADMIN_OFFICE_RESOURCES" />
		<fr:property name="linkFormat(editRegistrationPeriod)" value="/dfaPeriodsManagement.do?method=prepareEditRegistrationPeriod&executionDegreeId=${idInternal}"/>
		<fr:property name="key(editRegistrationPeriod)" value="link.candidacy.dfa.editRegistrationPeriod"/>
		<fr:property name="bundle(editRegistrationPeriod)" value="ADMIN_OFFICE_RESOURCES" />
	</fr:layout>
</fr:view>
