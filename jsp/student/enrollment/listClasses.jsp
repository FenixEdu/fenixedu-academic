<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<ul>
	<logic:iterate id="infoClass" name="infoClassEnrollmentDetails" property="infoClassList">
		<bean:define id="classId" name="infoClass" property="idInternal"/>
		<li>
			<html:link page="<%= "/studentShiftEnrolmentManagerLoockup.do?method=proceedToShiftEnrolment&amp;studentId=" + request.getParameter("studentId").toString() + "&amp;classId=" + pageContext.findAttribute("classId").toString()%>">
				<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />			
			</html:link>
		</li>
	</logic:iterate>
</ul>