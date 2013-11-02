<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<bean:define id="infoPerson" name="infoPerson" scope="request" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoPerson" property="nome"/><br />
	<bean:define id="teacherId" name="infoPerson" property="istUsername"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherId"/><br />
	<%--<b><bean:message key="label.executionYear" /> </b> <bean:write name="executionYear" property="year" /> <br />--%>
<%--	(<i><html:link page="/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1" paramId="teacherId" paramName="infoPerson" paramProperty="teacherId">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>) --%>

</p>
<div class="infoop2">
	<bean:message bundle="HTMLALT_RESOURCES" key="label.teacher.auth" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" />
</div>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>
<html:form action="/createProfessorship">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createProfessorship"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" />
	

	<h2>
		<span class="emphasis-box">1</span> <i><bean:message key="label.professorship.chooseExecutionPeriod"/></i>
	</h2>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId" onchange="this.form.page.value='1';this.form.method.value='showExecutionDegrees';this.form.submit();">
		<option></option>
		<html:options collection="executionPeriods" property="externalId" labelProperty="description"/>
	</html:select>
	<logic:present name="notAuth" >
	<span class="error"><bean:message bundle="HTMLALT_RESOURCES" key="label.not.auth.for.execution.semester" /></span >
	</logic:present>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	
	<logic:present name="executionDegrees">
		<h2>
			<span class="emphasis-box">2</span> <i><bean:message key="label.professorship.chooseExecutionDegree"/></i>
		</h2>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeId" property="executionDegreeId" onchange="this.form.page.value='2';this.form.method.value='showExecutionDegreeExecutionCourses';this.form.submit();">
			<option></option>
			<html:options collection="executionDegrees" labelProperty="label" property="value"/>
		</html:select>
		<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</logic:present>
	
	<logic:present name="executionCourses">
		<h2>
			<span class="emphasis-box">3</span> <i><bean:message key="label.professorship.chooseExecutionCourse"/></i>
		</h2>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionCourseId" property="executionCourseId">
			<option></option>		
			<html:options collection="executionCourses" labelProperty="nome" property="externalId"/>
		</html:select>
		<br />
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.responsibleFor" property="responsibleFor" value="true">
			<bean:message key="label.professorship.responsibleFor"/>
		</html:checkbox>
		<br />
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</logic:present>
</html:form>
