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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<html:xhtml/>

<link href="${pageContext.request.contextPath}/javaScript/sviz/sviz.css" rel="stylesheet" type="text/css" />

<h2><bean:message key="label.student.statistics.global.view" bundle="STUDENT_RESOURCES" /></h2>

<h3><bean:message key="label.student.statistics.progress" bundle="STUDENT_RESOURCES" /></h3>
<div id="visualization"></div>


<h3><bean:message key="label.student.statistics.percentage.approvals.overtime" bundle="STUDENT_RESOURCES" /></h3>
<div id="overtime-visualization"></div>

<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/sviz/d3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/sviz/qtip.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/sviz/i18next.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/sviz/sviz.min.js"></script>

<script type="text/javascript">
var data = <bean:write name="progress" filter="false" />;

var overtime = <bean:write name="curricularCoursesOvertime" filter="false" />;

SViz.init({ "lang": "<%= I18N.getLocale().getLanguage() %>", "localesBasePath": '<%= request.getContextPath() + "/javaScript/sviz" %>' });
SViz.loadViz("showStudentProgress", data, "#visualization");
SViz.loadViz("showCurricularCoursesOvertime", overtime, "#overtime-visualization");

</script>