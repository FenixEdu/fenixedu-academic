<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<p>
<bean:write name="executionPeriodName"/>
<logic:present name="executionDegreeName">
	<logic:notEmpty name="executionDegreeName">
		> <bean:write name="executionDegreeName"/>
	</logic:notEmpty>
</logic:present>
</p>

<logic:present name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>">
	<table>
		<tr>	
			<td>		
				<logic:notEmpty name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<td class="listClasses-header"><bean:message key="label.manager.teachersManagement.executionCourseName" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
							</td>
							<td class="listClasses-header">&nbsp;
							</td>
						</tr>
			
						<logic:iterate id="executionCourse" name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>" type="DataBeans.InfoExecutionCourse">
							<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
							<tr>	 			
								<td class="listClasses" style="text-align:left"><bean:write name="executionCourse" property="nome"/>
								</td>
								<td class="listClasses"><bean:write name="executionCourse" property="sigla"/>
								</td>
								<td class="listClasses">
									<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/> 
									&nbsp;
									<html:link page="<%="/editExecutionCourse.do?method=editExecutionCourse&amp;executionCourseId=" + executionCourseId.toString() + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
										<bean:message key="label.manager.executionCourseManagement.edit"/>
									</html:link>
									/
									<html:link page="<%="/editExecutionCourse.do?method=deleteExecutionCourse&amp;executionCourseId=" + executionCourseId.toString() + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
										<bean:message key="label.manager.executionCourseManagement.delete"/>
									</html:link>&nbsp;
								</td>
			 				</tr>
			 			</logic:iterate>						
					</table>
				</logic:notEmpty>	 	
			</td>
		</tr>
	</table>
</logic:present>