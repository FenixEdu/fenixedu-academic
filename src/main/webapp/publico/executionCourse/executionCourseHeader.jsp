<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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
	<a href="${curricularCourse.degreeCurricularPlan.degree.site.fullPath}">
		<bean:write name="curricularCourse" property="degreeCurricularPlan.degree.sigla"/>
	</a>								
</logic:iterate>
</span>
</p>



