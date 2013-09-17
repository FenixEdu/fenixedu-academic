<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
    <li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>

 	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.listing" /></li>
	<li><html:link page="/listMasterDegrees.do?method=chooseDegreeFromList&jspTitle=title.studentListByDegree&page=0"><bean:message key="link.studentListByDegree" /></html:link></li>
	<li><html:link page="/listCourseStudents.do?method=chooseDegreeFromList&jspTitle=title.studentListByCourse&page=0"><bean:message key="link.studentListByCourse" /></html:link></li>
	<li><html:link page="/listMasterDegreeThesis.do?method=prepare"><bean:message key="link.masterDegreeThesisList" /></html:link></li>
	<li><html:link page="/listConcludedMasterDegreeProofs.do?method=prepare"><bean:message key="link.concludedMasterDegreeProofList" bundle="ADMIN_OFFICE_RESOURCES"/></html:link></li>	
	<br/>
    <li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/> </html:link></li>
</ul>

