<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table>
	<tr>
		<logic:present name="infoCurricularCourseScope">
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<bean:define id="curricularCourse" name="infoCurricularCourseScope" property="infoCurricularCourse" type="DataBeans.InfoCurricularCourse"/>
				<h2><b><bean:write name="curricularCourse" property="name" /></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>			
<h2><bean:message key="label.manager.end.curricularCourseScope" /></h2>
<br />
<span class="error"><html:errors/></span>
<table>
<html:form action="/endCurricularCourseScope">
	<html:hidden property="page" value="3"/>
	<html:hidden property="method" value="end"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="curricularCourseScopeId" value="<%= request.getParameter("curricularCourseScopeId") %>"/>
	<html:hidden property="beginDate"/>
	<table>	
		<tr>
			<td><bean:message key="message.manager.curricular.course.scope.beginDate"/></td>
			<td>
				<bean:write name="curricularCourseScopeForm" property="beginDate"/>
		<%--		 
				<dt:format pattern="dd/MM/yyyy">
					<bean:write name="infoCurricularCourseScope" property="beginDate.time"/>
				</dt:format>
		<bean:define id="curricularCourseScope"><%=((InfoCurricularCourseScope)pageContext.findAttribute("infoCurricularCourseScope"))%></bean:define>
				<% String.valueOf(Data.format2DayMonthYear(((InfoCurricularCourseScope)curricularCourseScope).getBeginDate().getTime(), "/")); %>--%>
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.manager.curricular.course.scope.endDate"/></td>
			<td><html:text size="10" maxlength="10" property="endDate" /></td>
		</tr>
	</table>
	<br />
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>				
</html:form>