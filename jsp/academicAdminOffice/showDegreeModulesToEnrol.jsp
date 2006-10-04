<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.student.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<br/>
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>

<fr:form action="/studentEnrolments.do?method=enrol">
	<fr:edit id="showDegreeModulesToEnrol"
			 name="studentEnrolmentBean"
			 layout="student-enrolments"/>
	<br/>
	<br/>	
	<html:submit><bean:message key="button.submit"/></html:submit>
</fr:form>
