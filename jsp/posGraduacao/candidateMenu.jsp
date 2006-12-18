<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li class="navheader"><bean:message key="label.masterDegree.administrativeOffice.candidate" /></li>
	<li><html:link page="/chooseExecutionYear.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.createCandidate" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToVisualizeCandidates.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeCandidateInformations" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToEditCandidates.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.editCandidateInformations" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToSelectCandidates.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.selectCandidates" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToCandidateStudyPlan.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan" /></html:link></li>
	<li><html:link page="/chooseExecutionYearToRegisterCandidate.do?method=chooseDegreeFromList&page=0"><bean:message key="link.masterDegree.administrativeOffice.candidateRegistration" /></html:link></li>
<br/>
	<li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
	<li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
	<li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
	<li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
	<li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
	<li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/></html:link></li>     
</ul> 