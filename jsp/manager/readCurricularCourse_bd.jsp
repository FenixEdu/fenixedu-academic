<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<table>
	<tr>
		<logic:present name="infoCurricularCourse">
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<bean:define id="curricularCourseName" name="infoCurricularCourse" property="name"/>
				<h2><b><bean:write name="curricularCourseName"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/editCurricularCourse.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>"><bean:message key="label.manager.edit.curricularCourse"/></html:link></li>
	<li><html:link page="<%="/insertCurricularCourseScope.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>"><bean:message key="label.manager.insert.curricularCourseScope"/></html:link></li>
	<li><html:link page="<%="/readExecutionPeriodToAssociateExecutionCoursesAction.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="name" paramName="curricularCourseName"><bean:message key="label.manager.associate.executionCourses"/></html:link></li>
	<li><html:link page="<%="/editCurriculum.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>"><bean:message key="label.manager.edit.curriculum"/></html:link></li>
</ul>

<br>
<span class="error"><html:errors/></span>
<br>

<logic:present name="basic">
		<h2><font color="#0066CC"><bean:message key="label.manager.curricularCourse.message.basic"/></font></h2>
</logic:present>

<logic:notPresent name="basic">
		<h2><font color="#0066CC"><bean:message key="label.manager.curricularCourse.message.non.basic"/></font></h2>
</logic:notPresent>

<table width="70%" cellpadding="0" border="0">
	<tr>
		<td class="listClasses-header"><bean:message key="message.manager.theoreticalHours" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.praticalHours" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.theoPratHours" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.labHours" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.maxIncrementNac" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.minIncrementNac" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.credits" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.ectsCredits" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.weight" />
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.enrollmentWeigth"/>
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.mandatoryEnrollment"/>
		</td>
		<td class="listClasses-header"><bean:message key="message.manager.curricular.course.enrollmentAllowed"/>
		</td>
	</tr>
	<tr>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="theoreticalHours"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="praticalHours"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="theoPratHours"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="labHours"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="maximumValueForAcumulatedEnrollments"/>		
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="minimumValueForAcumulatedEnrollments"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="credits"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="ectsCredits"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="weigth"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="enrollmentWeigth"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="mandatoryEnrollment"/>
		</td>
		<td class="listClasses">
			<bean:write name="infoCurricularCourse" property="enrollmentAllowed"/>
		</td>
</table>
<br>
	
<h3><bean:message key="label.manager.executionCourses"/></h3>

<logic:empty name="executionCoursesList">
	<i><bean:message key="label.manager.executionCourses.nonExisting"/></i>
</logic:empty>
	
<logic:present name="executionCoursesList" scope="request">
	<logic:notEmpty name="executionCoursesList">
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.executionCourse.site" />
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			
			<logic:iterate id="executionCourse" name="executionCoursesList">
				<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
				<tr>	 			
					<td class="listClasses"><bean:write name="executionCourse" property="nome"/>
					</td>
					<td class="listClasses"><bean:write name="executionCourse" property="sigla"/>
					</td>
					<td class="listClasses"><bean:write name="infoExecutionPeriod" property="name"/> - <bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
					</td>
					<td class="listClasses">
						<bean:define id="hasSite" name="executionCourse" property="hasSite"/>
				
						<logic:equal name="hasSite" value="true">
							<bean:message key="label.manager.yes"/>
						</logic:equal>
						<logic:notEqual name="hasSite" value="true">
							<bean:message key="label.manager.no"/>
						</logic:notEqual>
					</td>
					<td class="listClasses">
						<bean:define id="deleteConfirm">
							return confirm('<bean:message key="message.confirm.remove.execution.course"/>')
	  					</bean:define>
						<%--<html:link page="<%="/dissociateExecutionCourse.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal" onclick='<%= deleteConfirm.toString() %>'>
							<bean:message key="label.manager.dissociate.execution.course"/>
						</html:link> /--%>
						<html:link page="<%="/readTeacherInCharge.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key="label.manager.edit.execution.course.teachers"/>
						</html:link>
						<logic:equal name="hasSite" value="false">
							/
							<html:link page="<%="/createSite.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
								<bean:message key="label.manager.create.site"/>
							</html:link>
						</logic:equal>
					</td>
 				</tr>
 			</logic:iterate>						
		</table>
	</logic:notEmpty>	 	
</logic:present>

<h3><bean:message key="label.manager.curricularCourseScopes"/></h3>

<logic:empty name="curricularCourseScopesList">
	<i><bean:message key="label.manager.curricularCourseScopes.nonExisting"/></i>
	<br /><br />
</logic:empty>

<logic:present name="curricularCourseScopesList" scope="request">
	<logic:notEmpty name="curricularCourseScopesList">
		<html:form action="/deleteCurricularCourseScope" method="get">
			<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
			<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
			<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
				<bean:define id="onclick">
					return confirm('<bean:message key="message.confirm.delete.curricular.course.scope"/>')
 				</bean:define>
				<table width="70%" cellpadding="0" border="0">
					<tr>
						<td class="listClasses-header">
						</td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" />
						</td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" />
						</td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" />
						</td>
						<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.beginDate" />
						</td>
						<td class="listClasses-header">
						</td>
					</tr>
					<logic:iterate id="curricularCourseScope" name="curricularCourseScopesList">
						<bean:define id="infoBranch" name="curricularCourseScope" property="infoBranch"/>
						<bean:define id="infoCurricularSemester" name="curricularCourseScope" property="infoCurricularSemester"/>
						<bean:define id="infoCurricularYear" name="infoCurricularSemester" property="infoCurricularYear"/>
					<tr>
						<td class="listClasses">
							<html:radio property="curricularCourseScopeId" idName="curricularCourseScope" value="idInternal" />		
						</td>
						<td class="listClasses"><bean:write name="infoCurricularYear" property="year"/>
						</td>
						<td class="listClasses"><bean:write name="infoCurricularSemester" property="semester"/>
						</td>	 			
						<logic:empty name="infoBranch" property="code">
							<td class="listClasses">&nbsp;</td>										
						</logic:empty>
						<logic:notEmpty name="infoBranch" property="code">
							<td class="listClasses"><bean:write name="infoBranch" property="code"/>
							</td>										
						</logic:notEmpty>
						<td class="listClasses">
							<bean:define id="beginDate" name="curricularCourseScope" property="beginDate" type="java.util.Calendar"/>
							<%=beginDate.get(Calendar.DAY_OF_MONTH)%>/<%=String.valueOf(beginDate.get(Calendar.MONTH) + 1)%>/<%=beginDate.get(Calendar.YEAR)%>
						</td>
						<td class="listClasses">
							<html:link page="<%="/editCurricularCourseScope.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
								<bean:message key="label.manager.edit.curricularCourseScope"/>
							</html:link> /<br />
							<html:link page="<%="/insertCurricularCourseScopeFromAnother.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
								<bean:message key="label.manager.insert.curricularCourseScope.fromAnother"/>
							</html:link> /<br />
							<html:link page="<%="/endCurricularCourseScope.do?method=prepareEnd&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal">
								<bean:message key="label.manager.end.curricular.course.scope"/>
							</html:link>
						</td>
 					</tr>
 				</logic:iterate>			
			</table>
			<html:submit onclick='<%=onclick.toString() %>'><bean:message key="label.manager.delete.selected.curricularCourseScopes"/></html:submit>
		</html:form>
	</logic:notEmpty>	 	
</logic:present>
<html:link page="<%="/readAllCurricularCourseScopes.do?degreeId=" + pageContext.findAttribute("degreeId") + "&amp;degreeCurricularPlanId=" + pageContext.findAttribute("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId")%>">
	<bean:message key="label.manager.viewAllcurricularCourseScopes"/>
</html:link>	