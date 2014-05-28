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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll"/></h2>

<fr:edit name="chooseSCPBean" schema="choose.studentCurricularPlan" action="/showNotNeedToEnroll.do?method=prepare">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
	<fr:destination name="registrationPostBack" path="/showNotNeedToEnroll.do?method=prepare"/>
	<fr:destination name="studentCurricularPlanPostBack" path="/showNotNeedToEnroll.do?method=prepare"/>	
</fr:edit>

<html:messages message="true" id="messages">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
	</p>
</html:messages>

<html:form action="showNotNeedToEnroll">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertNotNeedToEnroll"/>
	
	<br/><br/>
	<logic:present name="infoStudentCurricularPlan">
		<html:link page="/showNotNeedToEnroll.do?method=prepareNotNeedToEnroll&amp;insert=true" paramId="scpID" paramName="infoStudentCurricularPlan" 
			paramProperty="externalId"><bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll.insert"/></html:link>
		<br/><br/>
		
		<bean:define id="infoDegreeCurricularPlan" name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan"/>
		<h3><bean:message bundle="DEGREE_ADM_OFFICE" key="title.student.notNeedToEnroll.current"/></h3>
		<table cellpadding=3>
			<tr>
				<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.student.notNeedToEnroll.curricularPlan"/></th>
				<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.curricular.course.name"/></th>
				<td class="listClasses-header"></td>
			</tr>
		
		<bean:define id="scpID" name="infoStudentCurricularPlan" property="externalId" />
		<logic:iterate id="infoNotNeedToEnroll" name="infoNotNeedToEnrollCurricularCourses">
			<tr>
				<td class="listClasses"><bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/></td>
				<td class="listClasses">
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.name"/> - 
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<html:link page="<%= "/showNotNeedToEnroll.do?method=deleteNotNeedToEnroll&amp;scpID=" + scpID %>"
					paramId="notNeedToEnrollID" paramName="infoNotNeedToEnroll" paramProperty="externalId">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll.delete"/></html:link>
				</td>
			</tr>
		</logic:iterate>
		</table>
		
		<logic:present name="insert">
			<bean:define id="infoStudentCurricularPlanID" name="infoStudentCurricularPlan" property="externalId" type="java.lang.String"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanID" property="studentCurricularPlanID" value="<%= infoStudentCurricularPlanID.toString() %>"/>			

			<h3><bean:message bundle="DEGREE_ADM_OFFICE" key="title.student.notNeedToEnroll.toInsert"/></h3>
			<table cellpadding=3>
				<tr>
					<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.student.notNeedToEnroll.curricularPlan"/></th>
					<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.curricular.course.name"/></th>
					<td class="listClasses-header"></td>
				</tr>
			<logic:iterate id="infoCurricularCourse" name="infoDegreeCurricularPlanCurricularCourses" indexId="index">
				<tr>
					<td class="listClasses"><bean:write name="infoDegreeCurricularPlan" property="name"/></td>
					<td class="listClasses">
						<bean:write name="infoCurricularCourse" property="name"/> - <bean:write name="infoCurricularCourse" property="code"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCoursesID" property="curricularCoursesID">
							<bean:write name="infoCurricularCourse" property="externalId"/> 
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" >
				<bean:message bundle="DEGREE_ADM_OFFICE" key="button.insert"/></html:submit>
		</logic:present>
	</logic:present>
	
</html:form>