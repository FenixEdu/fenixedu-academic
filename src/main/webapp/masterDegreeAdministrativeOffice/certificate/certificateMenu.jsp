<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>

	<li class="navheader"><bean:message key="label.coordinator.student" /></li>
	    <li><html:link page="/chooseCertificateInfoAction.do?method=prepare&page=0"><bean:message key="link.certificate" /></html:link></li>
	    <li><html:link page="/chooseDeclarationInfoAction.do?method=prepare&page=0"><bean:message key="link.declarations" /></html:link></li>
	    <li><html:link page="/chooseFinalResultInfoAction.do?method=prepare&page=0"><bean:message key="link.finalResult" /></html:link></li>
	    <li><html:link page="/enrollment.do"><bean:message key="link.masterDegree.enrollment"/></html:link></li>    
		<li><html:link page="/seeStudentAndCurricularPlans.do?method=start"><bean:message key="link.masterDegree.administrativeOffice.seeStudentCurricularPlans"/></html:link></li>
	
	<br/>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>

	<br/>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>
</ul>
  	
   
