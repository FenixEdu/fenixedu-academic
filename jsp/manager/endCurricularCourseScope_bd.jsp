<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table>
	<tr>
		<logic:present name="infoCurricularCourseScope">
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<bean:define id="curricularCourse" name="infoCurricularCourseScope" property="infoCurricularCourse" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse"/>
				<h2><b><bean:write name="curricularCourse" property="name" /></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>			
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.end.curricularCourseScope" /></h2>
<br />
<span class="error"><html:errors/></span>
<table>
<html:form action="/endCurricularCourseScope">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="end"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseScopeId" property="curricularCourseScopeId" value="<%= request.getParameter("curricularCourseScopeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginDate" property="beginDate"/>
	<table>	
		<tr>
			<td><bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.scope.beginDate"/></td>
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
			<td><bean:message bundle="MANAGER_RESOURCES" key="message.manager.curricular.course.scope.endDate"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endDate" size="10" maxlength="10" property="endDate" /></td>
		</tr>
	</table>
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>				
</html:form>