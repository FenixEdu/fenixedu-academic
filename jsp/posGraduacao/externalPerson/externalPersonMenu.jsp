<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>

  
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
    <li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>
	<li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></html:link></li>
</ul>
    <p><b><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></b></p>
<ul>
	<blockquote>
	    <li><html:link page="/insertExternalPerson.do?page=0&amp;method=prepare"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insert" /></html:link></li>
	    <li><html:link page=""><bean:message key="link.masterDegree.administrativeOffice.externalPersons.find" /></html:link></li>
	    <li><html:link page=""><bean:message key="link.masterDegree.administrativeOffice.externalPersons.visualize" /></html:link></li>
	    <li><html:link page="/insertWorkLocation.do?method=prepare&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insertWorkLocation"/></html:link></li>
	    <li><html:link page="/editWorkLocation.do?method=prepare&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.editWorkLocation"/></html:link></li>
	</blockquote>
</ul>
<ul>
    <li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear&jspTitle=title.masterDegree.administrativeOffice.marksManagement"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>    
</ul>
	   
  	