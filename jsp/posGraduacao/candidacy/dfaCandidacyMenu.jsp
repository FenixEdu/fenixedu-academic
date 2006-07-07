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
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
</ul>
<ul style="margin-left: 2em; margin-top: 1em; margin-bottom: 1em;">
	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacyManagement" bundle="ADMIN_OFFICE_RESOURCES"/></li>
	<li><html:link page="/dfaCandidacy.do?method=prepareCreateCandidacy"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.createCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></html:link></li>
	<li><html:link page="/dfaCandidacy.do?method=prepareChooseCandidacy"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.viewCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></html:link></li>
    <li><html:link page="/listDFACandidacy.do?method=prepareListCandidacies"><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.listCandidacies" bundle="ADMIN_OFFICE_RESOURCES"/></html:link></li>
</ul>    
<ul>
    <li><html:link page="/payments.do?method=prepareSearchPerson"><bean:message key="link.masterDegree.administrativeOffice.payments" /></html:link></li>
</ul>
	   
  	