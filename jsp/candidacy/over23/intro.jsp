<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></h2>

<ul>
	<li><html:link page="/caseHandlingOver23CandidacyProcess.do?method=listProcesses"><bean:message key="label.candidacy.over23.process" bundle="APPLICATION_RESOURCES"/></html:link></li>
	<li><html:link page="/caseHandlingOver23IndividualCandidacyProcess.do?method=listProcesses"><bean:message key="label.candidacy.over23.individualProcess" bundle="APPLICATION_RESOURCES"/></html:link></li>
</ul>
