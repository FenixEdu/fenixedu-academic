<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<ul>
	<logic:iterate id="infoClass" name="infoClassEnrollmentDetails" property="infoClassList">
		<li>
			<logic:present name="classId" >
				present
				<logic:notEqual name="infoClass" property="idInternal" value="<%= classId.toString() %>"> 
					<html:link page="<%= "/studentShiftEnrolmentManagerLoockup.do?method=proceedToShiftEnrolment&amp;studentId=" + request.getParameter("studentId").toString() + "&amp;classId=" + request.getParameter("classId").toString()%>">
						<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />			
					</html:link>
				<logic:notEqual>
				<logic:equal name="infoClass" property="idInternal" value="<%= classId.toString() %>"> 
					<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />			
				<logic:equal>
				
			<logic:present>			
			<logic:notPresent name="classId" >
				not present
				<html:link page="<%= "/studentShiftEnrolmentManagerLoockup.do?method=proceedToShiftEnrolment&amp;studentId=" + request.getParameter("studentId").toString()%>">
					<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />			
				</html:link>
			<logic:notPresent>						
		</li>
	</logic:iterate>
</ul>