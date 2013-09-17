<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="externalId"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
	<br/>
	<ul>
		<li class="navheader">
			<bean:message key="label.coordinator.studentInformation"/>
		</li>
		<li>
			<html:link page="<%= "/viewStudentCurriculum.do?method=prepare&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.curriculum" /></html:link>
		</li>
	</ul>
</logic:present>
