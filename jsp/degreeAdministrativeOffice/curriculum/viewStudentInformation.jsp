<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:include page="/student/curriculum/viewCurricularPlans_bd.jsp"/>


<html:link page="/viewStudentSchedule.do" paramId="userName" paramName="studentPerson" paramProperty="username">
	<bean:message key="link.student.schedule"/>
</html:link> 