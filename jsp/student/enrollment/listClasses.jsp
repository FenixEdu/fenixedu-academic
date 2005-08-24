<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<style>@import url(/ciapl/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->
<ul>
	<li class="navheader">Turmas</li>
	<logic:iterate id="infoClass" name="infoClassList">
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
					<span><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" /></span>	
				</logic:equal>
			</logic:present>			
			<logic:notPresent name="classId" >
				<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()  + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString() %>">
					<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />						
				</html:link>
			</logic:notPresent>						
		</li>
	</logic:iterate> 
	<br />
	<li><html:link page="/studentShiftEnrollmentManager.do?method=start"><bean:message key="button.back" /></html:link></li>
</ul>