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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message key="label.showStudents" bundle="DELEGATES_RESOURCES" /></h2>


<bean:define id="year" name="currentExecutionYear" property="domainObject.year"/>
<fr:form action="/viewStudents.do?method=chooseExecutionYear">
	<fr:edit schema="choose.execution.year.domain.object" name="currentExecutionYear" id="chooseExecutionYear" layout="tabular">
		<fr:destination name="postBackChooseExecutionYear" path="/viewStudents.do?method=chooseExecutionYear"/>
	</fr:edit>
</fr:form>

<logic:present name="studentsList" >
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.studentsList" bundle="DELEGATES_RESOURCES" /></b></p>
	
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.delegates.studentsList.show" bundle="DELEGATES_RESOURCES" /></b>
		<html:link page='<%= "/viewStudents.do?method=prepareShowStudentsByCurricularCourse&amp;year=" + year%>'>
			<bean:message key="link.showStudentsByCurricularCourse" bundle="DELEGATES_RESOURCES"/>
		</html:link>,
		<span class="highlight1"><bean:message key="link.showStudents" bundle="DELEGATES_RESOURCES"/></span>
	</p>
			
	<logic:notEmpty name="studentsList">
		<fr:view name="studentsList" schema="delegates.showStudents.studentInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05"/>
				<fr:property name="columnClasses" value=",width300px nowrap aleft,nowrap aleft,nowrap aleft,nowrap aleft"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentsList">
		<p class="mtop2"><span class="error0">
			<b><bean:message key="error.delegates.showStudents.notFoundStudents" bundle="DELEGATES_RESOURCES" /></b></span></p>
	</logic:empty>
</logic:present>

<logic:present name="curricularCoursesList" >
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.curricularCoursesList" bundle="DELEGATES_RESOURCES" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.delegates.curricularCoursesList.help" bundle="DELEGATES_RESOURCES" /></p>
	
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.delegates.studentsList.show" bundle="DELEGATES_RESOURCES" /></b>
			<span class="highlight1"><bean:message key="link.showStudentsByCurricularCourse" bundle="DELEGATES_RESOURCES"/></span>
		,
		<html:link page='<%= "/viewStudents.do?method=showStudents&amp;year=" + year %>'>
			<bean:message key="link.showStudents" bundle="DELEGATES_RESOURCES"/>
		</html:link>
	</p>

	<logic:notEmpty name="curricularCoursesList">
		<fr:view name="curricularCoursesList" schema="delegates.showCurricularCourses.curricularCourseInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight tdcenter mtop05"/>
				<fr:property name="columnClasses" value="width250px aleft nowrap,,,"/>
				<fr:property name="suffixes" value=",º ano,º sem"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="curricularCoursesList">
		<p class="mtop2"><span class="error0">
			<b><bean:message key="error.delegates.showStudents.notFoundCurricularCourses" bundle="DELEGATES_RESOURCES" /></b></span></p>
	</logic:empty>
</logic:present>

<logic:present name="selectedCurricularCourseBean" >
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.selectedCurricularCourse" bundle="DELEGATES_RESOURCES" /></b></p>
		
	<fr:view name="selectedCurricularCourseBean" schema="delegates.showStudents.selectedCurricularCourse">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
			<fr:property name="columnClasses" value=",width200px nowrap aleft,nowrap aleft,nowrap aleft,nowrap aleft"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.studentsFromSelectedCurricularCourse" bundle="DELEGATES_RESOURCES" /></b>
	</p>
	<logic:notEmpty name="selectedCurricularCourseBean" property="enrolledStudents">
		<fr:view name="selectedCurricularCourseBean" property="enrolledStudents" schema="delegates.showStudents.studentInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value=",width200px nowrap aleft,nowrap aleft,nowrap aleft,nowrap aleft"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="selectedCurricularCourseBean" property="enrolledStudents">
		<p class="mtop1">
			<em><bean:message key="error.delegates.showStudents.notFoundStudents" bundle="DELEGATES_RESOURCES" /></em>
		</p>
	</logic:empty>
</logic:present>