<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="studentNumber" name="studentNumber" scope="request"/>
<bean:define id="degreeType" name="degreeType" scope="request"/>
<bean:define id="backLink" name="backLink" scope="request"/>

<bean:define id="linkMakeEnrollmentEquivalence">
	/selectStudentCurricularPlansToMakeEnrollmentEquivalence.do?method=showStudentCurricularPlans&backLink=<%= pageContext.findAttribute("backLink").toString() %>&degreeType=<%= pageContext.findAttribute("degreeType").toString() %>&studentNumber=<%= pageContext.findAttribute("studentNumber").toString() %>
</bean:define>

<bean:define id="linkDeleteEnrollmentEquivalence">
	/selectStudentCurricularPlansToDeleteEnrollmentEquivalence.do?method=showStudentCurricularPlans&backLink=<%= pageContext.findAttribute("backLink").toString() %>&degreeType=<%= pageContext.findAttribute("degreeType").toString() %>&studentNumber=<%= pageContext.findAttribute("studentNumber").toString() %>
</bean:define>

<%--
<bean:define id="linkCreateStudentCurricularPlan">
	/createStudentCurricularPlan.do?backLink=<%= pageContext.findAttribute("backLink").toString() %>&degreeType=<%= pageContext.findAttribute("degreeType").toString() %>&studentNumber=<%= pageContext.findAttribute("studentNumber").toString() %>
</bean:define>
--%>

<h2><bean:message key="tilte.enrollment.equivalence"/></h2>

<span class="error"><html:errors/></span>

<br/>

<ul>
	<li>
		<html:link page="<%= pageContext.findAttribute("linkMakeEnrollmentEquivalence").toString() %>" transaction="true">
			<bean:message key="link.enrollment.equivalence.make"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= pageContext.findAttribute("linkDeleteEnrollmentEquivalence").toString() %>" transaction="true">
			<bean:message key="link.enrollment.equivalence.delete"/>
		</html:link>
	</li>
<%--
	<li>
		<html:link page="<%= pageContext.findAttribute("linkCreateStudentCurricularPlan").toString() %>" transaction="true">
			<bean:message key="link.enrollment.equivalence.create.student.curricular.plan"/>
		</html:link>
	</li>
--%>
</ul>
