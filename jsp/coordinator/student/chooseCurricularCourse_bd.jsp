<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><html:errors/></span>
<br />
<logic:present name="curricularCourses">
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<br /><br />
	<bean:define id="path" value="/listStudentsByCourse" />
	<table>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:size id="scopesSize" name="curricularCourseElem" property="infoScopes"/>
		   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>

			<logic:equal name="scopesSize" value="1">
				<!-- CurricularCourseScope -->
				<logic:iterate id="scopeElem" name="curricularCourseElem" property="infoScopes">
				<tr>
					<td>
						<bean:define id="scopeCode" name="scopeElem" property="idInternal"/>
						<html:link page="<%= path + ".do?method=chooseCurricularCourse&amp;scopeCode=" + scopeCode + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") %>">
							<bean:write name="curricularCourseElem" property="name"/>
						</html:link>
					</td>
				</tr>
				</logic:iterate>						   	
			</logic:equal>



		   	<logic:greaterThan name="scopesSize" value="1">
				<tr>
					<td><bean:write name="curricularCourseElem" property="name"/></td>
				</tr>		
				   <!-- CurricularCourseScope -->
				<logic:iterate id="scopeElem" name="curricularCourseElem" property="infoScopes">
				<tr>
					<td>
						<bean:define id="scopeCode" name="scopeElem" property="idInternal"/>
						<bean:define id="ano" name="scopeElem" property="infoCurricularSemester.infoCurricularYear.year" />
						<bean:define id="semestre" name="scopeElem" property="infoCurricularSemester.semester" />
						&nbsp;&nbsp;&nbsp;&nbsp;					
						<html:link page="<%= path + ".do?method=chooseCurricularCourse&amp;scopeCode=" + scopeCode + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") %>">
							<logic:notEqual name="scopeElem" property="infoBranch.name" value="">
								<bean:message key="property.curricularCourse.branch" />
								<bean:write name="scopeElem" property="infoBranch.name"/>&nbsp;
							</logic:notEqual>						
							<bean:message key="label.year" arg0="<%= String.valueOf(ano) %>"/>
							<bean:message key="label.period" arg0="<%= String.valueOf(semestre) %>"/>
						</html:link>
					</td>
				</tr>
				</logic:iterate>						   	
	   		</logic:greaterThan>
	   		

		</logic:iterate>
	</table>
</logic:present>