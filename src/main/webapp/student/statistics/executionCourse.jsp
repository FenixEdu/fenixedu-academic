<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>

<html:xhtml/>

<link href="../javaScript/sviz/sviz.css" rel="stylesheet" media="screen" type="text/css" />

<script type="text/javascript">
	$.noConflict();
</script>
<script type="text/javascript" src="../javaScript/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/d3.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/qtip.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/i18next.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/sviz.js"></script>

<h2><bean:write name="executionCourse" property="name" /> (<bean:write name="executionCourse" property="executionYear.name" />)</h2>

<select id="evaluation-select">
</select>

<div id="chart-select" style="display: inline-block">
	<input type="radio" name="chart-type" value="HISTOGRAM"> <bean:message key="label.student.statistics.histogram" bundle="STUDENT_RESOURCES" />
	<input type="radio" name="chart-type" checked="true" value="SUNBURST"> <bean:message key="label.student.statistics.sunburst" bundle="STUDENT_RESOURCES" />	
</div>

<div id="visualization" style="margin-top: 20px; margin-bottom: 10px"></div>

<h3><bean:message key="label.student.statistics.all.execution.courses.overtime" bundle="STUDENT_RESOURCES" /></h3>
<div id="visualization-overall-years"></div>

<script type="text/javascript">

	var data = <bean:write name="executionCourseStatistics" filter="false" />;
	var overtime = <bean:write name="curricularCourseOvertimeStatistics" filter="false" />;		
	
	$.each(data.evaluations, function(i, evaluation) {
		data.evaluations[i].ranks = ["NA","RE"];
		data.evaluations[i].minGrade = +data.evaluations[i].minGrade;
		data.evaluations[i].maxGrade = +data.evaluations[i].maxGrade;		
		data.evaluations[i].student = data.student;
		$("#evaluation-select").append("<option class=\"evaluation-option\" value=\""+i+"\">"+evaluation.name+"</option>");
	});

	SViz.init({ lang: "<%= Language.getLanguage() %>", localesBasePath: "../javaScript/sviz" });	
	var currentChart = SViz.loadViz("showEvaluationSunburst", data.evaluations[0], "#visualization");
	SViz.loadViz("showCourseOvertime", overtime, "#visualization-overall-years");
	
	$("#evaluation-select").change(function() {
		currentChart.update(data.evaluations[$(this).val()]);
	});
	
	$("input[name=chart-type]").change(function() {
		var chart = $(this).val();
		$("#visualization").empty();
		var dataId = $("#evaluation-select").val();
		if(chart === "HISTOGRAM") {
			currentChart = SViz.loadViz("showHistogram", data.evaluations[dataId], "#visualization", { "xAxisLabel": false, showMinGrade: false, "yAxisLabel": false, tableColumns: ["id", "name", "grade"] });		
		} else {
			currentChart = SViz.loadViz("showEvaluationSunburst", data.evaluations[dataId], "#visualization");
		}
	});
	
</script>
