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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement"/> - <bean:message bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement.subtitle.transferEnrollments"/></h2>
<br />

<jsp:include page="studentCurricularPlanHeader.jsp"/>
<br />

<html:form action="/studentsManagement" focus="number">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transferEnrollments"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType"/>

	<bean:define id="enrollmentIDsToTransfer" name="studentCurricularPlanForm" property="enrollmentIDsToTransfer" type="java.lang.String[]"/>
	<%
		for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
			final String enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
	%>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrollmentIDsToTransfer" property="enrollmentIDsToTransfer" value="<%= enrollmentIDToTransfer %>"/>
	<%
		}
	%>

	<logic:present name="infoStudentCurricularPlans">

		<br/>
		<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">
			<bean:define id="studentCurricularPlanId" name="infoStudentCurricularPlan" property="externalId" type="java.lang.String"/>

			<logic:equal name="studentCurricularPlanForm" property="selectedStudentCurricularPlanId" value="<%= studentCurricularPlanId.toString() %>">

			<table>
				<tr>
					<th colspan="2" rowspan="3" class="listClasses-header">
						<bean:message bundle="MANAGER_RESOURCES" key="label.studentCurricularPlan"/>
					</th>
					<td colspan="4" class="listClasses">
						<bean:define id="onChangeString" type="java.lang.String">this.form.method.value='changeStudentCurricularPlanState';this.form.selectedStudentCurricularPlanId.value=<%= studentCurricularPlanId.toString() %>;this.form.submit();</bean:define>

						<logic:present name="infoStudentCurricularPlan" property="startDate">
							:
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>
							</dt:format>
						</logic:present>						
					</td>
				</tr>
				<tr>
					<td  colspan="4" class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
						:
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							...
						</logic:notPresent>
						-
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							...
						</logic:notPresent>
					</td>
				</tr>
					<tr>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.executionYear"/>
						</th>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
						</th>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree"/>
						</th>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.course.code"/>
						</th>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.course.name"/>
						</th>
						<th class="listClasses-header">
							<bean:message bundle="MANAGER_RESOURCES" key="label.grade"/>
						</th>
					</tr>

				<logic:iterate id="enrolment" name="infoStudentCurricularPlan" property="studentCurricularPlan.enrolments">
					<bean:define id="enrollmentId" name="enrolment" property="externalId"/>
					<bean:define id="enrollmentIdString" type="java.lang.String"><bean:write name="enrollmentId"/></bean:define>

					<%
						boolean isSelected = false;
						for (int i = 0; i < enrollmentIDsToTransfer.length; i++) {
							final String enrollmentIDToTransfer = enrollmentIDsToTransfer[i];
							if (enrollmentIDToTransfer.equals(enrollmentIdString)) {
								isSelected = true;
							}
						}
						if (isSelected) {
					%>

					<tr>
						<td class="listClasses">
							<bean:write name="enrolment" property="executionPeriod.executionYear.year"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolment" property="executionPeriod.childOrder"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolment" property="curricularCourse.degreeCurricularPlan.degree.sigla"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolment" property="curricularCourse.code"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolment" property="curricularCourse.name"/>
						</td>
						<td class="listClasses">
							<bean:write name="enrolment" property="gradeValue"/>
						</td>
					</tr>

					<%
						}
					%>

				</logic:iterate>
			</table>
			<br />
			
			</logic:equal>
		</logic:iterate>

		<br />
		<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">
			<bean:define id="studentCurricularPlanId" name="infoStudentCurricularPlan" property="externalId" type="java.lang.String"/>

			<table>
				<tr>
					<th rowspan="3" class="listClasses-header">
						<bean:message bundle="MANAGER_RESOURCES" key="label.studentCurricularPlan"/>
					</th>
					<td class="listClasses">
						<bean:define id="onChangeString" type="java.lang.String">this.form.method.value='changeStudentCurricularPlanState';this.form.selectedStudentCurricularPlanId.value=<%= studentCurricularPlanId.toString() %>;this.form.submit();</bean:define>

						<logic:present name="infoStudentCurricularPlan" property="startDate">
							:
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>
							</dt:format>
						</logic:present>						
					</td>
					<td rowspan="3" class="listClasses">
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.selectedStudentCurricularPlanId" property="selectedStudentCurricularPlanId" value="<%= studentCurricularPlanId.toString() %>"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
						:
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">
							...
						</logic:notPresent>
						-
						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							<dt:format pattern="yyyy-MM-dd">
								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate.time"/>
							</dt:format>
						</logic:present>
						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
							...
						</logic:notPresent>
					</td>
				</tr>
			</table>
			
				<logic:iterate id="curriculumGroup" name="infoStudentCurricularPlan" property="studentCurricularPlan.root.allCurriculumGroups" type="org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup">
					<bean:write name="curriculumGroup" property="fullPath" />
					<html:radio property="selectedCurriculumGroupID" value="<%= curriculumGroup.getExternalId().toString() %>" />
					<br/>
				</logic:iterate>
			
			<br />
			
		</logic:iterate>
	</logic:present>

	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="submit" styleClass="inputbutton"/>

</html:form>