<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.teacherPortal" /></em>
<h2><bean:message key="link.evaluation" /></h2>

<logic:present name="executionCourse">
	<bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
</logic:present>
<logic:notPresent name="executionCourse">
	<bean:define id="executionCourseID" name="executionCourseID" />
</logic:notPresent>

<p><bean:message key="label.evaluationIntro"/></p>