<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="idInternal"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
	<br/>
	<ul>
		<li class="navheader">
			<bean:message key="label.coordinator.studentInformation"/>
		</li>
		<li>
			<html:link page="<%= "/viewStudentCurriculum.do?method=prepareView&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.curriculum" /></html:link>
		</li>
		<li>
			<html:link page="<%= "/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.enrollment" /></html:link>
		</li>		
	</ul>
</logic:present>
