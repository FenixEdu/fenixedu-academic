<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />

<logic:present name="infoStudentShiftEnrolment" property="allowedClasses">
	Turma :<br>
	<logic:iterate id="cl" name="infoStudentShiftEnrolment" property="allowedClasses">
		<html:link page="/studentShiftEnrolmentManager.do?method=showAvailableShifts" paramId="class" paramName="cl" paramProperty="nome" >
			<bean:write name="cl" property="nome"/><br>
		</html:link>
	</logic:iterate>
	<br>
</logic:present>