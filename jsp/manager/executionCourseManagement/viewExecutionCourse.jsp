<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

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
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="sigla" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="theoreticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.labHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="labHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.seminaryHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="seminaryHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.problemsHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="problemsHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.fieldWorkHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="fieldWorkHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.trainingPeriodHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="trainingPeriodHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.tutorialOrientationHours"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="tutorialOrientationHours" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="comment" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.available.grade.submission"/></b>
			</td>
			<td>
				<bean:write name="<%=SessionConstants.EXECUTION_COURSE%>" property="availableGradeSubmission" />
			</td>
		</tr>
	</table>

	<br />
	<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses"/></h2>
	<ul>
		<li>
			<html:link module="/manager" module="/manager" page="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear")  + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod")  + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") + "&amp;executionCourseName=" + executionCourseName.toString()%>">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.executionCourseManagement.associate"/>
			</html:link>
		</li>
	</ul>
	<table>
		<tr>	
			<td>	
				<b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCoursesList" /></b>
				<logic:present name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<logic:notEmpty name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
						<bean:define id="curricularCourses" name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses"/>
								
							<table width="100%" cellpadding="0" border="0">
								<tr>
									<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
									</th>
									<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
									</th>
									<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.underGraduate" />
									</th>
									<th class="listClasses-header">&nbsp;
									</th>
								</tr>
					
								<logic:iterate id="curricularCourse" name="curricularCourses" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
									<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal"/> 
									<tr>	 			
										<td class="listClasses" style="text-align:left"><bean:write name="curricularCourse" property="name"/>
										</td>
										<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
										</td>
										<td class="listClasses"><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
										</td>
										<td class="listClasses">
											&nbsp;
											<html:link module="/manager" module="/manager" page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId") + "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
												<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.manager.teachersManagement.dissociate"/>
											</html:link>&nbsp;
										</td>
					 				</tr>
					 			</logic:iterate>						
							</table>
					</logic:notEmpty>						
					<logic:empty name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
						<p><i><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
					</logic:empty>
				</logic:present>
				<logic:notPresent name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<p><i><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
				</logic:notPresent>	
			</td>
		</tr>
	</table>
</logic:present>