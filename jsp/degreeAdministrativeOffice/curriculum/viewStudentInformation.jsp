<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:link page="/viewStudentSchedule.do" paramId="userName" paramName="registration" paramProperty="person.username" target="_blank">
	<bean:message key="link.student.schedule"/>
</html:link> 
<br/><br/>
<jsp:include page="/student/curriculum/displayStudentCurriculum_bd.jsp"/>