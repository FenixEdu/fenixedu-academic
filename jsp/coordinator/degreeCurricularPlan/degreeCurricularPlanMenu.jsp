<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
</br>
</br>
<hr></hr>
<h2><center><bean:message key="link.coordinator.degreeCurricularPlan.management"/></center></h2>

<p>
<ul>
	<li>
		<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
		    <bean:message key="link.coordinator.degreeCurricular.viewActive" /></html:link>
		    <br/>
			<br/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showCurricularCoursesHistory&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
		    <bean:message key="link.coordinator.degreeCurricular.viewHistory" /></html:link>
		    <br/>
			<br/>
		</html:link>
	</li>
</ul>
</p>
