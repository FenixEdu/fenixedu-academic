<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.ShiftType"%>
<html:xhtml/>

<h2><bean:message key="label.hours.load.total" bundle="SOP_RESOURCES"/></h2>

<logic:present name="infoExecutionCourse">
		
	<bean:define id="executionCourse" name="infoExecutionCourse" property="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse" toScope="request"/>
	<jsp:include page="contextExecutionCourse.jsp"/>
		
	<logic:notEmpty name="executionCourse" property="courseLoads">
		<fr:view name="executionCourse" property="courseLoads" schema="ExecutionCourseCourseLoadView">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 vamiddle thlight" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter"/>					            			
			</fr:layout>				
		</fr:view>
	</logic:notEmpty>

	<logic:present name="curricularCourses" >

		<h3 class="mtop15"><bean:message key="label.curricularCourses" /></h3>

			<logic:iterate id="curricularCourse" name="curricularCourses" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
				<p>
					<p><bean:message key="label.curricularCourse"/>: <strong><bean:write name="curricularCourse" property="name" /></strong></p>
					Curso :	<strong><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome" /></strong>
				</p>
								
				<table class="tstyle4 tdcenter thlight">
					<tr>
						<th><bean:message key="message.manager.theoreticalHours" /></th>
						<th><bean:message key="message.manager.theoPratHours" /></th>
						<th><bean:message key="message.manager.praticalHours" /></th>
						<th><bean:message key="message.manager.labHours" /></th>
						<th><bean:message key="message.manager.fieldWorkHours" /></th>
						<th><bean:message key="message.manager.problemsHours" /></th>
						<th><bean:message key="message.manager.seminaryHours" /></th>
						<th><bean:message key="message.manager.trainingPeriodHours" /></th>
						<th><bean:message key="message.manager.tutorialOrientationHours" /></th>
					</tr>
					<tr>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.TEORICA, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalTheoreticalHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalTheoreticalHours"/></font>								
							<%
								}			
							%>								
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.TEORICO_PRATICA, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalTheoPratHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalTheoPratHours"/></font>								
							<%
								}			
							%>		
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.PRATICA, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalPraticalHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalPraticalHours"/></font>								
							<%
								}			
							%>												
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.LABORATORIAL, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalLabHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalLabHours"/></font>								
							<%
								}			
							%>																					
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.FIELD_WORK, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalFieldWorkHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalFieldWorkHours"/></font>								
							<%
								}			
							%>																				
						</td>

						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.PROBLEMS, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalProblemsHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalProblemsHours"/></font>								
							<%
								}			
							%>																		
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.SEMINARY, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalSeminaryHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalSeminaryHours"/></font>								
							<%
								}			
							%>																				
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.TRAINING_PERIOD, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalTrainingPeriodHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalTrainingPeriodHours"/></font>								
							<%
								}			
							%>																				
						</td>
						<td>
							<%
								if(executionCourse.getEqualLoad(ShiftType.TUTORIAL_ORIENTATION, curricularCourse.getCurricularCourse())) {
							%>
								<font color="#008000"><bean:write name="curricularCourse" property="totalTutorialOrientationHours"/></font>
								
							<%
								} else { 							
							%>	
								<font color="red"><bean:write name="curricularCourse" property="totalTutorialOrientationHours"/></font>								
							<%
								}			
							%>																				
						</td>
					</tr>
				</table>

				<table class="tstyle4 tdcenter thlight">
					<tr>				
						<th>
							<bean:message key="label.manager.curricularCourseScope.branch"/>
						</th>
						<th>
							<bean:message key="label.manager.curricularCourseScope.curricularYear"/>
						</th>
						<th>
							Sem.
							<%--<bean:message key="label.manager.curricularCourseScope.curricularSemester"/> --%>
						</th>										
					</tr>		
					<%-- Scopes --%>
					<logic:iterate id="scope" name="curricularCourse" property="infoScopes" >
						<tr>
							<td>
								<logic:equal name="scope" property="infoBranch.name" value="" >
									<bean:message key="label.commonBranch" />
								</logic:equal>
								<logic:notEqual name="scope" property="infoBranch.name" value="" >
									<bean:write name="scope" property="infoBranch.name"/>
								</logic:notEqual>
							</td>
							<td>
								<bean:write name="scope" property="infoCurricularSemester.infoCurricularYear.year"/>
							</td>
							<td>
								<bean:write name="scope" property="infoCurricularSemester.semester"/>
							</td>															
						</tr>
					</logic:iterate>
				</table>
				
			</logic:iterate>				
	</logic:present>
</logic:present>

