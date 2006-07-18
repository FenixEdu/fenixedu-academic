<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="executionCourseID">
<ul>
	<li class="navheader">Visualizar:</li>
	<li><span><bean:message key="label.curricular.course.name"/>: <strong><bean:write name="infoExecutionCourse" property="nome"/></strong></span></li>
	<li>
	<bean:define id="link"><bean:message key="link.shift.enrolement.edit"/></bean:define>
	<html:link page="<%="/studentShiftEnrollmentManagerLoockup.do?method=" + link + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()%>"><bean:message key="link.student.seeAllClasses" /></html:link>
	</li>
</ul>
</logic:present>

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
					<logic:present name="executionCourseID">
						<bean:define id="executionCourseID" name="executionCourseID"/>
						<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()
						 + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString() + "&amp;executionCourseID=" + executionCourseID.toString() %>">
							<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />		
						</html:link>
					</logic:present>
					<logic:notPresent name="executionCourseID">
						<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString() + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString()%>">
							<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />		
						</html:link>
					</logic:notPresent>					
				</logic:notEqual>		
						
				<logic:equal name="infoClassId" value="<%= classIdSelected.toString() %>"> 
					<span><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" /></span>	
				</logic:equal>
			</logic:present>
						
			<logic:notPresent name="classId" >
				<logic:present name="executionCourseID">
					<bean:define id="executionCourseID" name="executionCourseID"/>
					<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()
					  + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString() + "&amp;executionCourseID=" + executionCourseID.toString() %>">
						<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />						
					</html:link>
				</logic:present>
				<logic:notPresent name="executionCourseID">
					<html:link page="<%= "/studentShiftEnrollmentManagerLoockup.do?method=" + classSelected + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()
					  + "&amp;classId=" + pageContext.findAttribute("infoClassId").toString() %>">
						<bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome" />						
					</html:link>
				</logic:notPresent>
			</logic:notPresent>						
		</li>
	</logic:iterate> 
	<br />
	<li><html:link page="/studentShiftEnrollmentManager.do?method=start"><bean:message key="button.back" /></html:link></li>
</ul>