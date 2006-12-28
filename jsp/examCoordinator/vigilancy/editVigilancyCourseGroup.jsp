<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editVigilancyCourseGroup"/></h2>
<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/examCoordinator/vigilancy/checkall.js"></script>

<ul>
	<li><html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<strong><fr:view name="bean" property="selectedVigilantGroup.name"/>:</strong>

<logic:notEmpty name="coursesUnableToAdd">
<div class="warning0">
<bean:message bundle="VIGILANCY_RESOURCES" key="vigilancy.error.cannotAddExecutionCourseToGroup"/>
<fr:view name="coursesUnableToAdd">
	<fr:layout>
		<fr:property name="eachLayout" value="values"/>
		<fr:property name="eachSchema" value="presentExecutionCourse"/>

		<fr:property name="classes" value="mbottom05"/>
	</fr:layout>
</fr:view>
</div>
</logic:notEmpty>

<div id="executionCoursesInGroup">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.executionCourseGroup"/></strong></p>

<fr:form id="removeExecutionCourseForm" action="/vigilancy/vigilancyCourseGroupManagement.do?method=removeExecutionCoursesFromGroup">
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeExecutionCourseForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.removeCourse"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a class="switchInline" href="javascript:uncheckall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
<fr:edit name="bean" id="removeExecutionCourses" schema="removeExecutionCourses" 
action="vigilancy/vigilancyCourseGroupManagement.do?method=removeExecutionCoursesFromGroup"
nested="true">
	<fr:layout>
	<fr:property name="displayLabel" value="false"/>

	<fr:property name="classes" value="mvert0"/>
	</fr:layout>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeExecutionCourseForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.removeCourse"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a class="switchInline" href="javascript:uncheckall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<div id="possibleAdditions">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.courses"/></strong></p>

<fr:form id="selectedUnit" action="/vigilancy/vigilancyCourseGroupManagement.do?method=selectUnit">
<fr:edit id="selectUnit" name="bean" schema="selectUnitInVigilancyGroup" nested="true">
<fr:destination name="postback" path="/vigilancy/vigilancyCourseGroupManagement.do?method=selectUnit"/>
		<fr:layout>
			<fr:property name="classes" value="mtop15"/>
		</fr:layout>
</fr:edit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<fr:form id="addExecutionCourseForm" action="/vigilancy/vigilancyCourseGroupManagement.do?method=addExecutionCourseToGroup">
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addExecutionCourseForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addCourse"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
<fr:edit name="bean" id="addExecutionCourses" schema="addExecutionCourses" 
			action="vigilancy/vigilancyCourseGroupManagement.do?method=addExecutionCourseToGroup">
			<fr:layout>
			<fr:property name="displayLabel" value="false"/>

			<fr:property name="classes" value="mvert0"/>
			</fr:layout>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addExecutionCourseForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addCourse"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<p class="mtop15"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addExternalCourse"/></strong></p>

<div class="switchInline">
<fr:form id="addExternalExecutionCourse" action="/vigilancy/vigilancyCourseGroupManagement.do?method=addExternalCourse">
<fr:edit name="bean" id="addExternalCourse" schema="addExternalCourse">
			<fr:layout>
			<fr:property name="displayLabel" value="false"/>
			</fr:layout>
	<fr:destination name="invalid" path="/vigilancy/vigilancyCourseGroupManagement.do?method=addExternalCourse"/>
</fr:edit>
<p class="mtop0">
	<a href="javascript:document.getElementById('addExternalExecutionCourse').submit()"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></a>
</p>
</fr:form>
</div>

 
<div class="switchNone">
<fr:form id="addExternalExecutionCourse" action="/vigilancy/vigilancyCourseGroupManagement.do?method=addExternalCourse">
<fr:edit name="bean" id="addExternalCourse-withoutjs" schema="addExternalCourse-withoutjs">
			<fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
	<fr:destination name="invalid" path="/vigilancy/vigilancyCourseGroupManagement.do?method=addExternalCourse"/>
</fr:edit>
<p class="mtop0">
	<html:submit><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>