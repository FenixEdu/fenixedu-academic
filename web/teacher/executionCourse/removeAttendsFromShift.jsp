<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<bean:define id="shiftID" name="shift" property="idInternal"/>
<bean:define id="executionCourseID" name="executionCourseID"/>

<logic:present name="removeAll">
	<p>Deseja remover <strong>todos os alunos</strong> do turno <strong><fr:view name="shift" property="nome"/></strong>?</p>
</logic:present>

<logic:notPresent name="removeAll">
	<p>Deseja remover o aluno <strong><fr:view name="registration" property="person.name"/></strong> do turno <strong><fr:view name="shift" property="nome"/></strong>?</p>
</logic:notPresent>

<div class="forminline">
	<logic:present name="removeAll">
		<fr:form action="<%="/manageExecutionCourse.do?method=removeAllAttendsFromShift&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:present>
	
	<logic:notPresent name="removeAll">
		<bean:define id="registrationID" name="registration" property="idInternal"/>
		<fr:form action="<%="/manageExecutionCourse.do?method=editShift&amp;registrationID=" + registrationID + "&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:notPresent>
	
	<fr:form action="<%="/manageExecutionCourse.do?method=editShift&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
	<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:cancel>
	</fr:form>
</div>