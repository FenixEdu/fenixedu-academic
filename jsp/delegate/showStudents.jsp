<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.showStudents" bundle="DELEGATES_RESOURCES" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<logic:present name="studentsList" >
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.studentsList" bundle="DELEGATES_RESOURCES" /></b></p>
	
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.delegates.studentsList.show" bundle="DELEGATES_RESOURCES" /></b>
		<html:link page="/viewStudents.do?method=prepareShowStudentsByCurricularCourse">
			<bean:message key="link.showStudentsByCurricularCourse" bundle="DELEGATES_RESOURCES"/>
		</html:link>,
		<span class="warning0"><bean:message key="link.showStudents" bundle="DELEGATES_RESOURCES"/></span>
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
			<span class="warning0"><bean:message key="link.showStudentsByCurricularCourse" bundle="DELEGATES_RESOURCES"/></span>
		,
		<html:link page="/viewStudents.do?method=showStudents">
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
		<b><bean:message key="label.delegates.studentsFromSelectedCurricularCourse" bundle="DELEGATES_RESOURCES" /></b></p>
	<logic:notEmpty name="selectedCurricularCourseBean" property="enrolledStudents">
		<fr:view name="selectedCurricularCourseBean" property="enrolledStudents" schema="delegates.showStudents.studentInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value=",width200px nowrap aleft,nowrap aleft,nowrap aleft,nowrap aleft"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="selectedCurricularCourseBean" property="enrolledStudents">
		<p class="mtop1"><span class="error0">
			<b><bean:message key="error.delegates.showStudents.notFoundStudents" bundle="DELEGATES_RESOURCES" /></b></span></p>
	</logic:empty>
</logic:present>