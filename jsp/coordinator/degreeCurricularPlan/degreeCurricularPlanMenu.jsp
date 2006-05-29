<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session" type="InfoExecutionDegree" />
	<logic:equal name="infoExecutionDegree" property="bolonha" value="false">
		<br/>
		<hr/>
		<ul>
			<li class="navheader">
				<bean:message key="link.coordinator.degreeCurricularPlan.management"/>
			</li>
			<li>
				<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
				    <bean:message key="link.coordinator.degreeCurricular.viewActive" />
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showCurricularCoursesHistory&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
				    <bean:message key="link.coordinator.degreeCurricular.viewHistory" />
				</html:link>
			</li>
		</ul>
	</logic:equal>
</logic:present>
