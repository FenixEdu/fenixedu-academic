<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.sendMailToStudents" bundle="DELEGATES_RESOURCES" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="DELEGATES_RESOURCES">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<p class="mtop2 mbottom05">
	<b><bean:message key="label.sendMailToStudents.chooseReceivers" bundle="DELEGATES_RESOURCES" /></b></p>
<p class="color888 mtop05 mbottom0">
	<bean:message key="label.sendMailToStudents.chooseReceivers.help" bundle="DELEGATES_RESOURCES" /></p>
<logic:present name="curricularCoursesList" >
	<p class="color888 mtop0 mbottom05">
		<bean:message key="label.sendMailToStudents.chooseCurricularCourses.help" bundle="DELEGATES_RESOURCES" /></p>
</logic:present>	

<p class="mtop05 mbottom05">
	<b><bean:message key="label.delegates.sendMailTo" bundle="DELEGATES_RESOURCES" /></b>
	<html:link page="/sendEmailToDelegateStudents.do?method=prepare">
		<bean:message key="link.sendToDelegateStudents" bundle="DELEGATES_RESOURCES"/>
	</html:link>,
	<html:link page="/sendEmailToDelegateStudents.do?method=prepareSendToStudentsFromSelectedCurricularCourses">
		<bean:message key="link.sendToStudentsFromCurricularCourses" bundle="DELEGATES_RESOURCES"/>
	</html:link>
</p>

<logic:present name="curricularCoursesList" >
	<logic:notEmpty name="curricularCoursesList">
		<fr:form action="/sendEmailToDelegateStudents.do?method=sendToStudentsFromSelectedCurricularCourses">
			<fr:view name="curricularCoursesList" layout="tabular" schema="delegates.sendEmail.showCurricularCourses.curricularCourseInfo">
				<fr:layout>
					<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05 mbottom05"/>
					<fr:property name="columnClasses" value=",width250px aleft nowrap,,,"/>
					<fr:property name="suffixes" value=",º ano,º sem"/>
					<fr:property name="checkable" value="true" />
					<fr:property name="checkboxName" value="selectedCurricularCourses" />
					<fr:property name="checkboxValue" value="curricularCourse.idInternal" />
					<fr:property name="selectAllShown" value="true" />
					<fr:property name="selectAllLocation" value="bottom" />
				</fr:layout>
				<fr:destination name="invalid" path="/sendEmailToDelegateStudents.do?method=prepareSendToStudentsFromSelectedCurricularCourses" />
			</fr:view>
			<div class="mtop1">
				<html:submit><bean:message key="button.continue" bundle="DELEGATES_RESOURCES"/></html:submit>
			</div>
		</fr:form>
	</logic:notEmpty>
</logic:present>




