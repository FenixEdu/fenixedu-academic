<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
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

SViz.init({ "lang": "<%= Language.getLanguage() %>", "localesBasePath": '<%= request.getContextPath() + "/javaScript/sviz" %>' });
SViz.loadViz("showStudentProgress", data, "#visualization");
SViz.loadViz("showCurricularCoursesOvertime", overtime, "#overtime-visualization");

</script>