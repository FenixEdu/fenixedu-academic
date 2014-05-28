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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<logic:equal name="canEdit" value="true">
	<logic:present name="infoCurriculum">
		<h2><str:upperCase><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></str:upperCase>&nbsp;<str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></h2>
		<html:form action="/degreeCurricularPlanManagement">
			<bean:define id="infoCurriculumId" name="infoCurriculum" property="externalId"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editCurriculum"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoCurriculumCode" property="infoCurriculumCode" value="<%=infoCurriculumId.toString()%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoExecutionDegreeCode" property="infoExecutionDegreeCode" value="<%=pageContext.findAttribute("infoExecutionDegreeCode").toString()%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoCurricularCourseCode" property="infoCurricularCourseCode" value="<%=pageContext.findAttribute("infoCurricularCourseCode").toString()%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%=pageContext.findAttribute("degreeCurricularPlanID").toString()%>"/>
			<table>
				<tr>
					<td>
						<table>
							<tr>
								<td width="75%">
										<bean:define id="curricularCourseCode" name="infoCurriculum" property="infoCurricularCourse.externalId"/>
										(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") +"&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode %>">
											<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
										</html:link>)
										<br />
										<bean:define id="curricularCourseName" name="infoCurriculum" property="infoCurricularCourse.name"/>
										(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") +"&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;infoCurricularCourseName=" + curricularCourseName%>">
											<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></font>
										</html:link>)
									<br />
								</td>								
								<td width="5%">
									<br />
								</td>
								<td width="20%">
									<%-- <html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;language=English"%>"><bean:message key="label.coordinator.degreeCurricularPlan.curriculum.english"/></html:link> --%>
									<br />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			
				<tr>
					<td><br />
						<b><bean:message key="label.generalObjectives"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives" property="generalObjectives" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.operacionalObjectives"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives" property="operacionalObjectives" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.program"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.program" property="program" cols="80" rows="6"/></td>
				</tr>
<%--				<tr>
					<td><br />
						<b><bean:message key="label.generalObjectives.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn" property="generalObjectivesEn" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.operacionalObjectives.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn" property="operacionalObjectivesEn" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.program.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.programEn" property="programEn" cols="80" rows="6"/><br /></td>
				</tr>
--%>				
				<logic:notEmpty name="infoCurriculum" property="lastModificationDate">
					<tr>
						<td>
							<br />
							<bean:define id="lastModificationDate" name="infoCurriculum" property="lastModificationDate" type="java.util.Date"/>
							<b><bean:message key="label.coordinator.degreeSite.lastModificationDate"/>
							<%= Data.format2DayMonthYear(lastModificationDate)%><b>		
							<br />
						</td>
					</tr>
				</logic:notEmpty>
			</table>
			<br />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
			</html:submit> 
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
			</html:reset>  
		</html:form>
	</logic:present>
</logic:equal>