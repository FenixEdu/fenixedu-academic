<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>

<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request" />

<%
	java.util.Hashtable params = new java.util.Hashtable();
	InfoStudent infoStudent = (InfoStudent) student;
	params.put("degreeType", infoStudent.getDegreeType().toString());
	params.put("studentNumber", infoStudent.getNumber());
	params.put("method", "getStudentForCreateMasterDegreeThesis");
	pageContext.setAttribute("parameters", params, PageContext.PAGE_SCOPE);
%>
  
<ul>
    <li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
	<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
	<li><html:link page="/studentSection.do"><bean:message key="label.coordinator.student" /></html:link></li>

	<li class="navheader"><bean:message key="link.masterDegree.administrativeOffice.thesis.title" /></li>
    <li><html:link page="/createMasterDegreeThesis.do" name="parameters"><bean:message key="link.masterDegree.administrativeOffice.thesis.create" /></html:link></li>
    <li><html:link page="/thesisSection.do"><bean:message key="link.masterDegree.administrativeOffice.thesis.changeStudent"/></html:link></li>
	
	<br/>
	<li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/externalPersonSection.do"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.title" /></html:link></li>
    <li><html:link page="/marksManagement.do?method=prepareChooseMasterDegree"><bean:message key="link.masterDegree.administrativeOffice.marksManagement" /></html:link></li>
    <li><html:link page="/listingSection.do"><bean:message key="link.masterDegree.administrativeOffice.listing" /></html:link></li>    
</ul>
	   
  	
