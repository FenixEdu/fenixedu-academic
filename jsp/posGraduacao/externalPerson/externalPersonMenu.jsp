<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
	<li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
	
	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></li>
    <li><html:link page="/insertExternalPerson.do?page=0&amp;method=prepare"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insert" /></html:link></li>
    <li><html:link page="/visualizeExternalPersons.do?page=0&amp;method=prepare"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.visualize" /></html:link></li>
	<li><html:link page="/findExternalPerson.do?page=0&amp;method=prepare"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.find" /></html:link></li>		
    <li><html:link page="/insertInstitution.do?method=prepare&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insertInstitution"/></html:link></li>
    <li><html:link page="/editInstitution.do?method=prepare&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.editInstitution"/></html:link></li>
	<br/>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
    <li><html:link page="/dfaCandidacySection.do"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/> </html:link></li>
	<li><html:link page="/payments.do?method=prepareSearchPerson"><bean:message key="link.masterDegree.administrativeOffice.payments" /></html:link></li>
</ul>	   
