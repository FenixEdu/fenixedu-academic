<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<html:errors/>
<html:form action="/creditsManagement">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="processForm"/>
	<bean:message key="label.tfc.students.number"/>&nbsp;&nbsp;<html:text property="tfcStudentsNumber" size="5"/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</html:form>
<tiles:insert definition="teacher-professor-ships">
	<tiles:put name="title" value="label.professorships"/>
	<tiles:put name="link" value="/executionCourseShiftsPercentageManager.do?method=show"/>
</tiles:insert>