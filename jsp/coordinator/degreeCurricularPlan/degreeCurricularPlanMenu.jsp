<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

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
