<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:message key="label.coordinator.candidate" />
<br>
  	<ul>
        <li><html:link page="/candidateOperation.do?method=getCandidates&action=visualize&page=0"><bean:message key="link.coordinator.visualizeCandidate" /></html:link></li>
        <li><html:link page="/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0"><bean:message key="link.coordinator.approveCandidates" /></html:link></li>
		<li><html:link page="/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan" /></html:link></li>
	</ul>
<br />
<br />
<html:link page="/studentSection.do"><bean:message key="link.coordinator.student" /></html:link>