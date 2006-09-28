<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<h2><strong><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<fr:form>
	<fr:edit id="showDegreeModulesToEnrol"
			 name="studentEnrolmentBean"
			 layout="student-enrolments"/>
			 
	<html:submit>asdasdadasdasdasda</html:submit>
</fr:form>
