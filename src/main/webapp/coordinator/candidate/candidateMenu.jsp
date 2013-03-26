<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<bean:message key="label.coordinator.candidate" />
<br/>
<ul>
	<li><html:link
		page='<%= "/candidateOperation.do?method=getCandidates&action=visualize&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message key="link.coordinator.visualizeCandidate" />
	</html:link></li>
	
	<li><html:link
		page='<%= "/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message key="link.coordinator.approveCandidates" />
	</html:link></li>
	
	<li><html:link titleKey="link.masterDegree.administrativeOffice.makeStudyPlan.title" 
		page='<%= "/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&amp;page=0&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message 
			key="link.masterDegree.administrativeOffice.makeStudyPlan" />
	</html:link></li>
	
	<li><html:link titleKey="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu.title" 
		page='<%= "/printAllCandidatesList.do?method=prepare&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
		<bean:message
			key="link.masterDegree.candidateListFilter.printListAllCandidatesFilterMenu" />
	</html:link></li>
</ul>
