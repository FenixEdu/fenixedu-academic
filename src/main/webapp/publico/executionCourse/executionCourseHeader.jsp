<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
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



