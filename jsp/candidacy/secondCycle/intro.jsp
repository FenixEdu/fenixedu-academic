<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.secondCycle" bundle="APPLICATION_RESOURCES"/></h2>

<ul>
	<li><html:link page="/caseHandlingSecondCycleCandidacyProcess.do?method=listProcesses"><bean:message key="label.candidacy.secondCycle.process" bundle="APPLICATION_RESOURCES"/></html:link></li>
	<li><html:link page="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=listProcesses"><bean:message key="label.candidacy.secondCycle.individualProcess" bundle="APPLICATION_RESOURCES"/></html:link></li>
</ul>
