<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

  	<bean:message key="label.coordinator.candidate" /><br>
        &nbsp;&nbsp;- <html:link page="/candidateOperation.do?method=getCandidates&action=visualize&page=0"><bean:message key="link.coordinator.visualizeCandidate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0"><bean:message key="link.coordinator.approveCandidates" /></html:link><br>
	<br>
	
	<html:link page="/studentSection.do"><bean:message key="link.coordinator.student" />
    </html:link><br>
