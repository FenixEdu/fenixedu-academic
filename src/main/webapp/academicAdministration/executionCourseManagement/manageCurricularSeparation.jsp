<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants" %>
<%@page import="org.fenixedu.academic.domain.AcademicProgram"%>
<%@page import="org.fenixedu.academic.domain.ExecutionDegree"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<h3><bean:message bundle="MANAGER_RESOURCES" key="title.manager.executionCourseManagement.manageCurricularSeparation"/></h3>

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
<logic:messagesPresent message="true" property="info">
	<p>
		<span class="infoop4">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="info">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<bean:define id="degreeAndYearParameter" value="" /> <%-- bean redefined ahead --%>
<bean:define id="notLinkedCoursesParameter" value="" /> <%-- bean redefined ahead --%>
<logic:notEqual name="sessionBean" property="chooseNotLinked" value="true">
	<bean:define id="curricularYearName">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= pageContext.findAttribute("curYear") + ".ordinal.short" %>"/>
		<bean:message bundle="ENUMERATION_RESOURCES" key="YEAR" />
	</bean:define>
	<bean:define id="degreeAndYearParameter"
		value="<%="&amp;originExecutionDegreeId=" + pageContext.findAttribute("originExecutionDegreeId")
				+ "&amp;curricularYearId=" + pageContext.findAttribute("curricularYearId") %>" />
	<bean:define id="notLinkedCoursesParameter" value="&amp;executionCoursesNotLinked=null" />
</logic:notEqual>
<logic:equal name="sessionBean" property="chooseNotLinked" value="true">
	<bean:define id="curricularYearName" value=""/>
	<bean:define id="notLinkedCoursesParameter" value="&amp;executionCoursesNotLinked=true" />
</logic:equal>

<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="externalId"/>
	<bean:define id="executionPeriodName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.executionPeriod.qualifiedName"/>
	<bean:define id="executionPeriodId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.externalId"/>
	<bean:define id="coursePeriodDegreeCurYearParameter"
		value="<%="&amp;executionCourseId=" + executionCourseId.toString()
				+ "&amp;executionPeriodId=" + executionPeriodId.toString()
				+ degreeAndYearParameter.toString() %>" />
	
	<p><bean:write name="executionPeriodName"/> &nbsp;&gt;&nbsp;
	<logic:present name="originExecutionDegreeName">
		<logic:notEmpty name="originExecutionDegreeName">
			<b><bean:write name="originExecutionDegreeName"/></b> &nbsp;&gt;&nbsp;
			<bean:write name="curricularYearName"/> &nbsp;&gt;&nbsp;
		</logic:notEmpty>
	</logic:present>		
 	<bean:write name="executionCourseName"/></p>

	<ul>
		<li>
			<html:link module="/academicAdministration"
					   page="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan"
					   		+ coursePeriodDegreeCurYearParameter.toString()
							+ "&amp;executionCourseName=" + executionCourseName.toString()
							+ notLinkedCoursesParameter.toString()%>">
				<bean:message bundle="MANAGER_RESOURCES" key="link.executionCourseManagement.curricular.associate"/>
			</html:link>
			<logic:equal name="<%=PresentationConstants.EXECUTION_COURSE%>" property="executionCourse.splittable" value="true" >
			<logic:present name="sessionBean" property="executionDegree">
			<bean:define id="degree" name="sessionBean" property="executionDegree.degree"/>
			<academic:allowed operation="MANAGE_EXECUTION_COURSES_ADV" permission="ACADEMIC_PLANNING_EXECUTIONS" program="<%= (AcademicProgram) degree %>">
			|
			<html:link module="/academicAdministration"
					   page="<%="/seperateExecutionCourse.do?method=prepareTransfer" 
							+ coursePeriodDegreeCurYearParameter.toString()%>">
				<bean:message bundle="MANAGER_RESOURCES" key="link.executionCourseManagement.curricular.transfer"/>
			</html:link>
			|
			<html:link module="/academicAdministration"
				   	page="<%="/seperateExecutionCourse.do?method=prepareSeparate"
							+ coursePeriodDegreeCurYearParameter.toString()%>">
				<bean:message bundle="MANAGER_RESOURCES" key="link.executionCourseManagement.curricular.split"/>
			</html:link>
			</academic:allowed>
			</logic:present>
			</logic:equal>
		</li>
	</ul>
	<table>
		<tr>	
			<td>	
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCoursesList" /></h3>
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
								<logic:present name="sessionBean" property="executionDegree">
								<bean:define id="degree" name="sessionBean" property="executionDegree.degree"/>
								<academic:allowed operation="MANAGE_EXECUTION_COURSES_ADV" permission="ACADEMIC_PLANNING_EXECUTIONS" program="<%= (AcademicProgram) degree %>">
								<th class="listClasses-header">
									&nbsp;
								</th>
								</academic:allowed>
								</logic:present>
								</logic:equal>
							</tr>

							<logic:iterate id="curricularCourse" name="curricularCourses" type="org.fenixedu.academic.dto.InfoCurricularCourse">
								<bean:define id="curricularCourseId" name="curricularCourse" property="externalId"/>
								<tr>	 			
									<td class="listClasses" style="text-align:left">
										<bean:write name="curricularCourse" property="name"/>
									</td>
									<td class="listClasses">
										<bean:write name="curricularCourse" property="acronym"/>
									</td>
									<td class="listClasses">
										<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
									</td>
									<logic:equal name="<%=PresentationConstants.EXECUTION_COURSE%>" property="canRemoveCurricularCourses" value="true">
									<logic:present name="sessionBean" property="executionDegree">
									<bean:define id="degree" name="sessionBean" property="executionDegree.degree"/>
									<academic:allowed operation="MANAGE_EXECUTION_COURSES_ADV" permission="ACADEMIC_PLANNING_EXECUTIONS" program="<%= (AcademicProgram) degree %>">
									<td class="listClasses">
										&nbsp;
										<bean:define id="dissociateConfirm">
												return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.dissociate.confirm"/>')
										</bean:define>
										<html:link module="/academicAdministration" onclick="<%= dissociateConfirm %>"
												   page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse"
												   		+ "&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId")
												   		+ coursePeriodDegreeCurYearParameter.toString()
														+ notLinkedCoursesParameter.toString()%>">
											<bean:message bundle="MANAGER_RESOURCES" key="button.manager.teachersManagement.dissociate"/>
										</html:link>
										&nbsp;
									</td>
									</academic:allowed>
									</logic:present>
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
		<tr>
			<td colspan="4">
				<br/>
				<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
					<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
					<html:submit>
						<bean:message bundle="MANAGER_RESOURCES" key="label.return"/>
					</html:submit>
				</fr:form>
			</td>
		</tr>
	</table>
</logic:present>
