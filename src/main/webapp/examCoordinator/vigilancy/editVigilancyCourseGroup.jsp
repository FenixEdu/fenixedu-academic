<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editVigilancyCourseGroup"/></h2>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

<ul>
	<li><html:link page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<h3 class="mtop15"><fr:view name="bean" property="selectedVigilantGroup.name"/></h3>

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

<p class="mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.executionCourseGroup"/></strong></p>

<fr:form id="removeExecutionCourseForm" action="/vigilancy/vigilancyCourseGroupManagement.do">
<html:hidden property="method" value="removeExecutionCoursesFromGroup"/>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeExecutionCourseForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.removeCourse"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a class="switchInline" href="javascript:uncheckall('removeExecutionCourseForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
<fr:edit name="bean" id="removeExecutionCourses" schema="removeExecutionCourses" 
action="vigilancy/vigilancyCourseGroupManagement.do"
nested="true">
	<html:hidden property="method" value="removeExecutionCoursesFromGroup"/>
	<fr:layout>
		<fr:property name="displayLabel" value="false"/>
		<fr:property name="classes" value="tstyle4 mvert05"/>
		<fr:property name="columnClasses" value="thclear,,tdclear"/>
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

<p class="mtop2 mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.courses"/></strong></p>

<fr:form id="selectedUnit" action="/vigilancy/vigilancyCourseGroupManagement.do?method=selectUnit">

<fr:edit id="selectUnit" name="bean" schema="selectUnitInVigilancyGroup" nested="true">
<fr:destination name="postback" path="/vigilancy/vigilancyCourseGroupManagement.do?method=selectUnit"/>
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mtop15 mvert05"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
</fr:edit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<fr:form id="addExecutionCourseForm" action="/vigilancy/vigilancyCourseGroupManagement.do">
<html:hidden property="method" value="addExecutionCourseToGroup"/>
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
				<fr:property name="classes" value="tstyle4 mvert05"/>
				<fr:property name="columnClasses" value="thclear,,tdclear"/>
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

<p class="mtop2 mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addExternalCourse"/></strong></p>

<div class="switchInline">
<fr:form id="addExternalExecutionCourse" action="/vigilancy/vigilancyCourseGroupManagement.do">
<html:hidden property="method" value="addExternalCourse"/>
<fr:edit name="bean" id="addExternalCourse" schema="addExternalCourse">
	<fr:layout>
		<fr:property name="displayLabel" value="false"/>
		<fr:property name="classes" value="tstyle5 mvert05"/>
		<fr:property name="columnClasses" value="thclear,,tdclear"/>
	</fr:layout>
	<fr:destination name="invalid" path="/vigilancy/vigilancyCourseGroupManagement.do?method=addExternalCourse"/>
</fr:edit>
<p class="mtop0">
	<a href="javascript:document.getElementById('addExternalExecutionCourse').submit()"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></a>
</p>
</fr:form>
</div>

 
<div class="switchNone">
<fr:form id="addExternalExecutionCourse" action="/vigilancy/vigilancyCourseGroupManagement.do">
<html:hidden property="method" value="addExternalCourse"/>

<fr:edit name="bean" id="addExternalCourse-withoutjs" schema="addExternalCourse-withoutjs">
	<fr:layout>
		<fr:property name="displayLabel" value="false"/>
		<fr:property name="classes" value="tstyle5 mvert05"/>
		<fr:property name="columnClasses" value="thclear,,tdclear"/>
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