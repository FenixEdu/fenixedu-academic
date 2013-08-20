<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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
