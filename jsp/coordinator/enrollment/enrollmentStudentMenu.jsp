<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<bean:define id="infoExecutionDegreeID" name="infoExecutionDegree" property="idInternal"/>
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
	
	</br>
	</br>
	<hr></hr>
	<h2><center><bean:message key="label.coordinator.studentInformation"/></center></h2>
	
	<p>
	<ul>
		<li>
			<html:link forward="equivalence"><bean:message key="link.coordinator.equivalence"/></html:link>
			<br/>
			<br/>
		</li>
		<li>
			<html:link page="<%= "/viewStudentCurriculum.do?method=prepareView&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.curriculum" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>
		<li>
			<html:link page="<%= "/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent&amp;executionDegreeId=" + infoExecutionDegreeID.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
			    <bean:message key="link.student.enrollment" /></html:link>
			    <br/>
				<br/>
			</html:link>
		</li>		
	</ul>
	</p>
</logic:present>