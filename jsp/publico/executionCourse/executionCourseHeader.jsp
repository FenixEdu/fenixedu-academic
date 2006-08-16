<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h1>
	<bean:write name="executionCourse" property="nome"/>
</h1>
<h2 class="greytxt">
	<bean:write name="executionCourse" property="executionPeriod.semester" />
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year" />
</h2>
<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" length="1">
	<bean:define id="url" type="java.lang.String">/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>&amp;degreeID=<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
	<html:link action="<%= url %>">
		<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
	</html:link>
</logic:iterate>
<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" offset="1">
	,
	<bean:define id="url" type="java.lang.String">/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=<bean:write name="curricularCourse" property="idInternal"/>&amp;executionPeriodOID=<bean:write name="executionCourse" property="executionPeriod.idInternal"/>&amp;degreeID=<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.idInternal"/></bean:define>
	<html:link action="<%= url %>">
		<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
	</html:link>
</logic:iterate>
