<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<em><bean:message key="label.teacherPortal" /></em>
<h2><bean:message key="link.evaluation" /></h2>

<logic:present name="executionCourse">
	<bean:define id="executionCourseID" name="executionCourse" property="externalId" />
</logic:present>
<logic:notPresent name="executionCourse">
	<bean:define id="executionCourseID" name="executionCourseID" />
</logic:notPresent>

<p><bean:message key="label.evaluationIntro"/></p>