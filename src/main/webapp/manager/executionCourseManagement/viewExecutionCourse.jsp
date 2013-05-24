<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="idInternal"/>

	<bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			&gt; <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	&gt; <bean:write name="executionCourseName"/>
 	
 	<br /><br />
 	
	<table>			
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nome" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="sigla" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="comment" />
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.available.grade.submission"/></b>
			</td>
			<td>
				<bean:write name="<%=PresentationConstants.EXECUTION_COURSE%>" property="availableGradeSubmission" />
			</td>
		</tr>
	</table>
	
	<logic:notEmpty name="<%=PresentationConstants.EXECUTION_COURSE%>" property="courseLoads">
		<fr:view name="<%=PresentationConstants.EXECUTION_COURSE%>" property="courseLoads" schema="ExecutionCourseCourseLoadView">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 vamiddle thlight" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter"/>					            			
			</fr:layout>				
		</fr:view>
	</logic:notEmpty>

<%-- BEGIN information needed to build link for returning to edit --%>
<bean:define id="executionPeriodQName" name="sessionBean" property="executionPeriod.qualifiedName" />
<bean:define id="linkGetRequestBigMessage" value="" />
<logic:equal name="sessionBean" property="chooseNotLinked" value="false">
	<bean:define id="executionDegreePName" name="sessionBean" property="executionDegree.presentationName"/>
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + pageContext.findAttribute("executionPeriodId")
				+ "&executionDegree=" + executionDegreePName.toString() + "~" + pageContext.findAttribute("executionDegreeId")
				+ "&curYear=" + pageContext.findAttribute("curYearName") + "~" + pageContext.findAttribute("curYearId")
				+ "&executionCoursesNotLinked=null"%>" />
</logic:equal>
<logic:equal name="sessionBean" property="chooseNotLinked" value="true">
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + executionPeriodQName.toString() + "~" + pageContext.findAttribute("executionPeriodId")
		+ "&executionDegree=null~null"
		+ "&curYear=null~null"
		+ "&executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") %>" />
</logic:equal>
<%-- END information needed to build link for returning to edit --%>
	
	<br />
	<table>
		<tr>
			<td>
				<fr:form action="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=" + executionCourseId.toString() + linkGetRequestBigMessage.toString() %>">
					<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
					<html:submit>
						<bean:message bundle="MANAGER_RESOURCES" key="label.edit"/>
					</html:submit>
				</fr:form>
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

	<br />
	<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses"/></h2>
	<ul>
		<li>
			<html:link module="/manager" module="/manager"
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