<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors /></span>
<h2><bean:message key="title.masterDegree.administrativeOffice.marksManagement" /></h2>
<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />

<%-- don't forget to take above parameters - executionYear, degree, curricularCourse and scopeCode - 
	to actions to maintain jsp state --%>
<ul>
    <li>
		<html:link page="<%= "/changeMarkDispatchAction?method=prepareChangeMark&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;curricularCourseCode=" + pageContext.findAttribute("curricularCourseCode") %>">
    		<bean:message key="link.masterDegree.administrativeOffice.marksView" />
		</html:link>
	</li>
    <li><html:link page="<%="/marksSubmission.do?method=prepare&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;scopeCode=" + pageContext.findAttribute("scopeCode") %>">
    		<bean:message key="link.masterDegree.administrativeOffice.marksSubmission" />
    	</html:link>
    </li>
    <li>
		<html:link page="<%="/marksConfirmation.do?method=prepareMarksConfirmation&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;scopeCode=" + pageContext.findAttribute("scopeCode") %>">
    		<bean:message key="link.masterDegree.administrativeOffice.marksConfirmation" />
		</html:link>
	</li>
     <li>
		<html:link page="<%= "/changeMarkDispatchAction.do?method=prepareChangeMark&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;curricularCourseCode=" + pageContext.findAttribute("scopeCode") %>">
    		<bean:message key="link.masterDegree.administrativeOffice.changeMark" /> 
		</html:link>
	</li>
</ul>
     
     