<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<logic:present name="<%=SessionConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=SessionConstants.EXECUTION_COURSE%>" property="idInternal"/>

	<bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			> <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	> <bean:write name="executionCourseName"/>
 	
 	<br /><br />
 	
	<table>			
		<tr>
			<td>
				<b><bean:message key="message.manager.execution.course.name"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.execution.course.code"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="sigla" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.theoreticalHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="theoreticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.praticalHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.theoPratHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.labHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="labHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="message.manager.execution.course.comment"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="comment" />
			</td>
		</tr>
	</table>

	<br />
	<h2><bean:message key="label.manager.curricularCourses"/></h2>
	<ul>
		<li>
			<html:link page="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear")  + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod")  + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") + "&amp;executionCourseName=" + executionCourseName.toString()%>">
				<bean:message key="link.manager.executionCourseManagement.associate"/>
			</html:link>
		</li>
	</ul>
	<table>
		<tr>	
			<td>	
				<b><bean:message key="label.manager.executionCourseManagement.curricularCoursesList" /></b>
				<logic:present name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<logic:notEmpty name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
						<bean:define id="curricularCourses" name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses"/>
								
							<table width="100%" cellpadding="0" border="0">
								<tr>
									<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.curricularCourse" />
									</td>
									<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.code" />
									</td>
									<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.underGraduate" />
									</td>
									<td class="listClasses-header">&nbsp;
									</td>
								</tr>
					
								<logic:iterate id="curricularCourse" name="curricularCourses" type="DataBeans.InfoCurricularCourse">
									<tr>	 			
										<td class="listClasses" style="text-align:left"><bean:write name="curricularCourse" property="name"/>
										</td>
										<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
										</td>
										<td class="listClasses"><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
										</td>
										<td class="listClasses">
										<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal"/> 
											&nbsp;<html:link page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId") + "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
												<bean:message key="button.manager.teachersManagement.dissociate"/>
											</html:link>&nbsp;
										</td>
					 				</tr>
					 			</logic:iterate>						
							</table>
					</logic:notEmpty>						
					<logic:empty name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
						<p><i><bean:message key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
					</logic:empty>
				</logic:present>
				<logic:notPresent name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<p><i><bean:message key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
				</logic:notPresent>	
			</td>
		</tr>
	</table>
</logic:present>