<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<p class="mtop2"><h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2></p>
<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
	<logic:messagesPresent message="true" property="successCourse">
		<ul>
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="successCourse">
				<li><span class="success2"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>


<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="idInternal"/>
	<bean:define id="executionPeriodName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.name"/>
	<bean:define id="executionPeriodId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.idInternal"/>
	
	<p><b><bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			&gt; <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	&gt; <bean:write name="executionCourseName"/></b></p>
 	<table>
		<html:form action="/editExecutionCourse" focus="name">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="updateExecutionCourse"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= executionCourseId.toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear"/>				
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>

		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="30" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="5" property="code" />
			</td>
		</tr>							
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment" rows="3" cols="45" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.entry.phase"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="entryPhase">
					<html:optionsCollection name="entryPhases"/>
				</html:select>
			</td>
		</tr>						
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.available.grade.submission"/>
			</td>
			<td>
				<html:checkbox property="availableGradeSubmission" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
				</html:submit>
				</html:form>
			</td>
			<td>
				<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
					<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
					<html:submit>
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.backToCourseList"/>
					</html:submit>
				</fr:form>
			</td>
		</tr>
	</table>
	<p class="mtop2"><h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses"/></h2></p>
	<ul>
		<li>
			<html:link module="/manager"
					   page="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan&amp;executionCourseId="
							+ pageContext.findAttribute("executionCourseId")
							+ "&amp;executionDegree=" + pageContext.findAttribute("executionDegree")
							+ "&amp;curYear=" + pageContext.findAttribute("curYear")
							+ "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod")
							+ "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")
							+ "&amp;executionCourseName=" + executionCourseName.toString()%>">
				<bean:message bundle="MANAGER_RESOURCES" key="link.manager.executionCourseManagement.associate"/>
			</html:link>
		</li>
	</ul>
	<table>
		<tr>	
			<td>	
				<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCoursesList" /></b>
				<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<logic:notEmpty name="<%=PresentationConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses"> 
						<bean:define id="curricularCourses" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses"/>
								
							<table width="100%" cellpadding="0" border="0">
								<tr>
									<th class="listClasses-header">
										<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
									</th>
									<th class="listClasses-header">
										<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
									</th>
									<th class="listClasses-header">
										<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.degreeCurricularPlan" />
									</th>
									<logic:equal name="<%=PresentationConstants.EXECUTION_COURSE%>" property="canRemoveCurricularCourses" value="true">
									<th class="listClasses-header">
										&nbsp;
									</th>
									<th class="listClasses-header">
										&nbsp;
									</th>
									</logic:equal>
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
										<logic:equal name="<%=PresentationConstants.EXECUTION_COURSE%>" property="canRemoveCurricularCourses" value="true">
										<td class="listClasses">
											&nbsp;
											<bean:define id="dissociateConfirm">
												return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.dissociate.confirm"/>')
											</bean:define>
											<html:link module="/manager" onclick="<%= dissociateConfirm %>"
													   page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse&amp;curricularCourseId="
															+ pageContext.findAttribute("curricularCourseId")
															+ "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId")
															+ "&amp;executionDegree=" + pageContext.findAttribute("executionDegree")
															+ "&amp;curYear=" + pageContext.findAttribute("curYear")
															+ "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod")
															+ "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked")%>">
												<bean:message bundle="MANAGER_RESOURCES" key="button.manager.teachersManagement.dissociate"/>
											</html:link>
											&nbsp;
										</td>
										<td class="listClasses">
											&nbsp;
											<html:link module="/manager"
													   page="<%="/editExecutionCourseTransferCurricularCourses.do?method=prepareTransferCurricularCourse&amp;curricularCourseId="
															+ pageContext.findAttribute("curricularCourseId")
															+ "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId")
															+ "&amp;executionDegree=" + pageContext.findAttribute("executionDegree")
															+ "&amp;curYear=" + pageContext.findAttribute("curYear")
															+ "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriod")
															+ "&amp;executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") %>">
												<bean:message bundle="MANAGER_RESOURCES" key="link.transferCurricularCourse"/>
											</html:link>
											&nbsp;
										</td>
										</logic:equal>
					 				</tr>
					 			</logic:iterate>						
							</table>
					</logic:notEmpty>						
					<logic:empty name="<%=PresentationConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
						<p><i><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
					</logic:empty>
				</logic:present>
				<logic:notPresent name="<%=PresentationConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses">
					<p><i><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
				</logic:notPresent>	
			</td>
		</tr>
	</table>
	
</logic:present>
