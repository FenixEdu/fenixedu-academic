<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<ul>
	<logic:iterate id="infoClass" name="infoClassEnrollmentDetails" property="infoClassList">
		<bean:define id="infoClassId" name="infoClass" property="idInternal"/>
		<bean:define id="infoClassName" name="infoClass" property="nome"/>
		<bean:define id="classSelected">
			<bean:message key="label.class"/>
		</bean:define>
		<li>
			<logic:present name="classId" >
				<bean:define id="classIdSelected" name="classId" />
				<logic:notEqual name="infoClassId" value="<%= classIdSelected.toString() %>">
					<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString() + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString()%>">
						<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />		
					</html:link>
				</logic:notEqual>
				<logic:equal name="infoClassId" value="<%= classIdSelected.toString() %>"> 
					<b><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" /></b>			
				</logic:equal>
			</logic:present>			
			<logic:notPresent name="classId" >
				<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()  + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString() %>">
					<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />						
				</html:link>
			</logic:notPresent>						
		</li>
	</logic:iterate> 
	
	<p>
	<li>
		<html:link href="<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>">
			<bean:message key="label.Terminar" />
		</html:link>	
	</li>
	</p>
</ul>