<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>

<logic:empty name="curricularCourseScopesList">
	<bean:message key="label.manager.curricularCourses.nonExisting" />
</logic:empty>


<logic:notEmpty name="curricularCourseScopesList">		
		
	<logic:iterate id="infoCurricularCourseScope" name="curricularCourseScopesList" length="1">		
		<h2 align="left">
			<bean:message key="label.degreeCurricularPlan" />&nbsp;			
			<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.tipoCurso" />
			&nbsp;<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
		</h2>
	</logic:iterate>
	
	<table>
		<logic:iterate id="infoCurricularCourseScope" name="curricularCourseScopesList" length="1">		
			<bean:define id="currentSemester" name="infoCurricularCourseScope" property="infoCurricularSemester.semester" />					
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" /></td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" /></td>
				<td class="listClasses-header"><bean:message key="label.curricularCourse" /></td>
				<%--<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" /></td>--%>
			</tr>
		</logic:iterate>

		<logic:iterate id="infoCurricularCourseScope" name="curricularCourseScopesList" >
			<logic:equal name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString() %>" >
				<bean:define id="ccID" name="infoCurricularCourseScope" property="infoCurricularCourse.idInternal" />							
				<tr>
					<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" /></td>			
					<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester" /></td>
					<td>
						<html:link page="<%= "/viewSite.do?method=curricularCourse&executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;ccCode=" + pageContext.findAttribute("ccID").toString() %>">						
							<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<%--<td><bean:write name="infoCurricularCourseScope" property="infoBranch.name" /></td>--%>
				</tr>			
			</logic:equal>
			
			<logic:notEqual name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString() %>" >
				<bean:define id="currentSemester" name="infoCurricularCourseScope" property="infoCurricularSemester.semester" />								
				<p><p>
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" /></td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" /></td>
					<td class="listClasses-header"><bean:message key="label.curricularCourse" /></td>
					<%--<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" /></td>--%>
				</tr>

				<bean:define id="ccID" name="infoCurricularCourseScope" property="infoCurricularCourse.idInternal" />											
				<tr>
					<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" /></td>			
					<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester" /></td>
					<td>
						<html:link page="<%= "/viewSite.do?method=curricularCourse&executionPeriodOId=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;ccCode=" + pageContext.findAttribute("ccID").toString() %>">					
							<bean:write name="infoCurricularCourseScope" property="infoCurricularCourse.name" />
						</html:link>
					</td>
					<%--<td><bean:write name="infoCurricularCourseScope" property="infoBranch.name" /></td>--%>
				</tr>	
			</logic:notEqual>			
		</logic:iterate>
	</table>
</logic:notEmpty>	

