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
	<html:hidden property="teacherOID"/>
	<h2 class="infoop"><bean:write name="infoCreditsTeacher" property="infoTeacher.infoPerson.nome"/></h2>
	<bean:message key="label.tfc.students.number"/>&nbsp;&nbsp;<html:text property="tfcStudentsNumber" size="5"/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<bean:define id="link">
		/executionCourseShiftsPercentageManager.do?method=show&amp;teacherOID=<bean:write name="infoCreditsTeacher" property="infoTeacher.idInternal"/>
	</bean:define>
	<tiles:insert definition="teacher-professor-ships">
		<tiles:put name="title" value="label.professorships"/>
		<tiles:put name="link" value="<%= link %>"/>
	</tiles:insert>	
</html:form>
