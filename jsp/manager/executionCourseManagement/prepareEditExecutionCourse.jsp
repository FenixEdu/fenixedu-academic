<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<p>
<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
</p>
<logic:present name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>">
	<table>
		<tr>	
			<td>		
				<logic:notEmpty name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.executionCourseName" />
							</th>
							<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.code" />
							</th>
							<th class="listClasses-header">&nbsp;
							</th>
							<th class="listClasses-header">&nbsp;
							</th>
						</tr>
			
						<logic:iterate id="executionCourse" name="<%=SessionConstants.EXECUTION_COURSE_LIST_KEY%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
							<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
							<tr>	 			
								<td class="listClasses" style="text-align:left"><bean:write name="executionCourse" property="nome"/>
								</td>
								<td class="listClasses"><bean:write name="executionCourse" property="sigla"/>
								</td>
								<td class="listClasses">
									<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/> 
									&nbsp;<html:link module="/manager" module="/manager" page="<%="/editExecutionCourse.do?method=editExecutionCourse&amp;executionCourseId=" + executionCourseId.toString() + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "~" + pageContext.findAttribute("executionPeriodId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegreeName") + "~" + pageContext.findAttribute("executionDegreeId") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
										<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit"/>
									</html:link>
									/
									<html:link module="/manager" module="/manager" page="<%="/editExecutionCourse.do?method=deleteExecutionCourse&amp;executionCourseId=" + executionCourseId.toString() + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "~" + pageContext.findAttribute("executionPeriodId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegreeName") + "~" + pageContext.findAttribute("executionDegreeId") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
										<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.delete"/>
									</html:link>&nbsp;
								</td>
								<td class="listClasses">
									<html:link module="/manager" module="/manager" page="<%="/seperateExecutionCourse.do?method=prepareTransfer&amp;executionCourseId=" + executionCourseId.toString() %>">
										<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.seperate.execution_course"/>
									</html:link>
								</td>
			 				</tr>
			 			</logic:iterate>						
					</table>
				</logic:notEmpty>	 	
			</td>
		</tr>
	</table>
</logic:present>