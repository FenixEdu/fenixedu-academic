<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
	<li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>	
	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></li>
    <li><html:link page="/studentSituation.do?method=chooseStudent&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.gratuity.studentSituation" /></html:link></li>
    <li><html:link page="/readStudent.do?method=prepareReadStudent&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></html:link></li>		
    <li><html:link page="/insertGratuityDataDA.do?method=prepareInsertChooseExecutionYear"><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertGratuity"/></html:link></li>
	<li><html:link page="/editInsuranceValue.do?method=chooseExecutionYear"><bean:message key="link.masterDegree.administrativeOffice.gratuity.defineInsuranceValue"/></html:link></li>
    <li><html:link page="/fixSibsPaymentFileEntries.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.gratuity.fixConflicts"/></html:link></li>
    <li><html:link page="/studentsGratuityList.do?method=chooseExecutionYear&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></html:link></li>
    <li><html:link page="/listPayedInsurances.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.gratuity.listPayedInsurances"/></html:link></li>	    
	<br/>
	<li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>	
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
    <li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/> </html:link></li>
</ul>
	   
  	
