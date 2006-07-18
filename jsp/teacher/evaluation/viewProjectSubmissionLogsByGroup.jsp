<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:messages id="message" message="true">
	<span class="error"> <bean:write name="message" /> </span>
</html:messages>
<h2><bean:message
	key="label.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissionLogsByGroup.title" /></h2>

<fr:view name="project" schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight thright"/>
    </fr:layout>
</fr:view>

<br/>

<fr:view name="projectSubmissionLogs" schema="projectSubmissionLog.view-full">
	<fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2"/>
        <fr:property name="columnClasses" value=",,,acenter"/>
    </fr:layout>
</fr:view>






