<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<logic:present name="<%=SessionConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=SessionConstants.EXECUTION_COURSE%>" property="idInternal"/>
	<bean:define id="executionPeriodName" name="<%=SessionConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.name"/>
	<bean:define id="executionPeriodId" name="<%=SessionConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.idInternal"/>
	
	<bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			> <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	> <bean:write name="executionCourseName"/>
 	
	<html:form action="/editExecutionCourse" focus="name">
		<html:hidden property="method" value="updateExecutionCourse"/>
		<html:hidden property="executionCourseId" value="<%= executionCourseId.toString() %>" />
		<html:hidden property="executionPeriod"/>
		<html:hidden property="executionDegree"/>
		<html:hidden property="curYear"/>				
		<html:hidden property="executionCoursesNotLinked"/>
		<html:hidden property="page" value="3"/>
		<table>			
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
				</td>
				<td>
					<html:text size="30" property="name" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
				</td>
				<td>
					<html:text size="5" property="code" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
				</td>
				<td>
					<html:text size="5" property="theoreticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
				</td>
				<td>
					<html:text size="5" property="praticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
				</td>
				<td>
					<html:text size="5" property="theoPratHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
				</td>
				<td>
					<html:text size="5" property="labHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.seminaryHours"/>
				</td>
				<td>
					<html:text size="5" property="seminaryHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.problemsHours"/>
				</td>
				<td>
					<html:text size="5" property="problemsHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.fieldWorkHours"/>
				</td>
				<td>
					<html:text size="5" property="fieldWorkHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.trainingPeriodHours"/>
				</td>
				<td>
					<html:text size="5" property="trainingPeriodHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.tutorialOrientationHours"/>
				</td>
				<td>
					<html:text size="5" property="tutorialOrientationHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
				</td>
				<td>
					<html:textarea property="comment" rows="3" cols="45" />
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>
	</html:form>
	
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
									<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
									</td>
									<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
									</td>
									<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.underGraduate" />
									</td>
									<td class="listClasses-header">&nbsp;
									</td>
									<td class="listClasses-header">&nbsp;
									</td>
								</tr>
					
								<logic:iterate id="curricularCourse" name="curricularCourses" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
									<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal"/>
									<tr>	 			
										<td class="listClasses" style="text-align:left">
											<bean:write name="curricularCourse" property="name"/>
										</td>
										<td class="listClasses">
											<bean:write name="curricularCourse" property="code"/>
										</td>
										<td class="listClasses">
											<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
										</td>
										<td class="listClasses">
											&nbsp;
											<html:link module="/manager" module="/manager" page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId") + "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod") + "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
												<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.manager.teachersManagement.dissociate"/>
											</html:link>&nbsp;
										</td>
										<td class="listClasses">
											&nbsp;
											<html:link module="/manager" module="/manager" page="<%="/editExecutionCourseTransferCurricularCourses.do?method=prepareTransferCurricularCourse&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId") + "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegree=" + pageContext.findAttribute("executionDegree") + "&amp;curYear=" + pageContext.findAttribute("curYear") + "&amp;executionPeriodId=" + pageContext.findAttribute("executionPeriodId") %>">
												<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.transferCurricularCourse"/>
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