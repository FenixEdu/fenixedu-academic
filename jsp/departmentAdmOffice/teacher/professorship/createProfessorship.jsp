<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:define id="infoTeacher" name="infoTeacher" scope="request" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/><br />
	(<i><html:link page="/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)

</p>
<%--
<h3>
	<logic:present name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.edit"/>
	</logic:present>
	<logic:notPresent name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.create"/>			
	</logic:notPresent>
</h3>
--%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>
<html:form action="/createProfessorship">
	<html:hidden property="teacherNumber"/>
	<html:hidden property="method" value="createProfessorship"/>	

	<h2>
		<span class="emphasis-box">1</span> <i><bean:message key="label.professorship.chooseExecutionDegree"/></i>
	</h2>
	<html:select property="executionDegreeId" onchange="this.form.method.value='showExecutionDegreeExecutionCourses';this.form.submit();">
		<html:options collection="executionDegrees" labelProperty="infoDegreeCurricularPlan.infoDegree.nome" property="idInternal"/>
	</html:select>

	
	<logic:present name="executionCourses">
		<h2>
			<span class="emphasis-box">2</span> <i><bean:message key="label.professorship.chooseExecutionCourse"/></i>
		</h2>
		<html:select property="executionCourseId">
			<html:options collection="executionCourses" labelProperty="nome" property="idInternal"/>
		</html:select>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</logic:present>
</html:form>
