<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.marksManagement" /></h2>
<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />

<%-- don't forget to take above parameters - executionYear, degree, curricularCourse and curricularCourseCode - 
	to actions to maintain jsp state --%>
<ul>
    <li>
    	<html:link page="">
    		<bean:message key="link.masterDegree.administrativeOffice.marksView" />
    	</html:link>
    </li>
    <li><html:link page="<%="/marksSubmission.do?method=prepare&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;curricularCourseCode=" + pageContext.findAttribute("curricularCourseCode") %>">
    		<bean:message key="link.masterDegree.administrativeOffice.marksSubmission" />
    	</html:link>
    </li>
    <li>
    	<html:link page="">
    		<bean:message key="link.masterDegree.administrativeOffice.marksConfirmation" />
    	</html:link>
    </li>
     <li>
     	<html:link page="">
     		<bean:message key="link.masterDegree.administrativeOffice.changeMark" /> 
    	</html:link>
    </li>
</ul>
     
     