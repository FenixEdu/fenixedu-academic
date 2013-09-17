<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<jsp:include page="/i18n.jsp"/>

<h1 class="mtop0 mbottom03 cnone">
	<bean:write name="executionCourse" property="nome"/>
	<span class="greytxt" style="font-size: 11px;">
		(<bean:write name="executionCourse" property="executionPeriod.semester" />
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />)
	</span>
</h1>

<!--
<p class="greytxt" style="">
	<bean:write name="executionCourse" property="executionPeriod.semester" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />
</p>
-->

<p style="margin-top: 0;">
<span>
<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" indexId="i">
	<logic:notEqual name="i" value="0">,</logic:notEqual>				
	<app:contentLink name="curricularCourse" property="degreeCurricularPlan.degree.site">
		<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
	</app:contentLink>								
</logic:iterate>
</span>
</p>



