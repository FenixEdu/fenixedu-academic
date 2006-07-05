<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
    <li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
    <li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
</ul>
<ul style="margin-left: 2em; margin-top: 1em; margin-bottom: 1em;">
	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.listing" /></li>
	<li><html:link page="/listMasterDegrees.do?method=chooseDegreeFromList&jspTitle=title.studentListByDegree&page=0"><bean:message key="link.studentListByDegree" /></html:link></li>
	<li><html:link page="/listCourseStudents.do?method=chooseDegreeFromList&jspTitle=title.studentListByCourse&page=0"><bean:message key="link.studentListByCourse" /></html:link></li>
	<li><html:link page="/listMasterDegreeThesis.do?method=prepare"><bean:message key="link.masterDegreeThesisList" /></html:link></li>
</ul>
<ul>
    <li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/> </html:link></li>
	<li><html:link page="/payments.do?method=prepareSearchPerson"><bean:message key="link.masterDegree.administrativeOffice.payments" /></html:link></li>
</ul>
