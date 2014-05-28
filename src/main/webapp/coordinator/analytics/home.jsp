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


<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.YearViewBean"%>
<%@page
	import="net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup"%>
<%@page
	import="net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule"%>
<%@page import="java.util.Set"%>


<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<link href="../javaScript/sviz/sviz.css" rel="stylesheet" type="text/css" />

<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears" /></h2>

<form action="<%= request.getContextPath() %>/coordinator/analytics.do" method="POST" id="show-execution-year-form">
	<input type="hidden" value="showHome" name="method" />
	<input type="hidden" value="<bean:write name='degreeCurricularPlanID' />" name="degreeCurricularPlanID" />
	<select id="executionYearSelectBox" name="executionYear">
	</select>
</form>

<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.allCurricularYears" /></h2>

<div id="progress-bar" style="float: left">
</div>

<div id="legenda" style="float: right">
</div>

<h2 style="float: left; display: block; width: 100%"><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByCurricularYears" /></h2>

<div style="width: 100%; margin-top: 20px; display: block; float: left" id="curricular-years-placeholder">
</div>

<h2 style="float: left; display: block; width: 100%"><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByCurricularCourse" /></h2>

<div style="width: 100%; margin-top: 20px; display: block; float: left">
	<select id="curricular-course-selector">
		<option disabled><bean:message bundle="COORDINATOR_RESOURCES" key="label.select.curricular.course" /></option>
	</select>
</div>

<div style="margin-top: 20px; float: left" id="curricular-course-placeholder">
</div>

<div style="margin-top: 40px; float: right" id="curricular-course-histogram">
</div>



<script type="text/javascript" src="../javaScript/sviz/d3.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/qtip.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/i18next.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/sviz.min.js"></script>

<script type="text/javascript">
SViz.init({ "lang": "<%= I18N.getLocale().getLanguage() %>", "localesBasePath": '<%= request.getContextPath() + "/javaScript/sviz" %>' });

var executionYears = <bean:write name="executionYears" filter="false" />;
var data = <bean:write name="currentExecutionYear" filter="false" />;

$.each(executionYears, function(idx, executionYearJson) {
	$("#executionYearSelectBox").append("<option value=\""+executionYearJson.id+"\">"+executionYearJson.name+"</option>");
});

$("option", "#executionYearSelectBox").each(function(idx, option) {
	if($(option).html() === data.name) {
		$(option).attr("selected", "selected");
		return false; //to skip the remaining...
	}
});

$("#executionYearSelectBox").change(function(e) {
	$("#show-execution-year-form").submit();
	
});

var progressData = {
	calculated: {
		flunked: data.flunked,
		approved: data.approved,
		notEvaluated: data.notEvaluated,
		attending: data.attending
}};

var entries = [];

$.each(data["curricular-years"], function(idx, el) {
	el.text = el.year+"º Ano";
	el.description = "Média: "+el.average+"<br>Total: "+el.total;
	entries.push(el);
});

entries.sort(function(a,b) {
	return a.year - b.year;
});

var curricularYearsData = {
    "domain": ["attending", "approved", "not-evaluated", "flunked" ],
    "entries": entries
};


$.each(data["curricular-courses"], function(idx, year) {
	$("#curricular-course-selector").append("<option disabled>"+year.year+"º Ano</option>")
	$.each(year.entries, function(ccIdx, el) {
		$("#curricular-course-selector").append("<option data-year-id='"+idx+"' data-cc-id='"+ccIdx+"'>"+el.name+"</option>");
	});
});


$("#curricular-course-selector").change(function(e) {
	var optionSelected = $(this).find("option:selected");
	var yearId = optionSelected.data("year-id");
	var ccId = optionSelected.data("cc-id");
	$("#curricular-course-placeholder").empty();
	$("#curricular-course-histogram").empty();
	var selectedData = data["curricular-courses"][yearId].entries[ccId];
	selectedData.text = selectedData.acronym;
	selectedData.description = selectedData.name+"<br/>Média: "+selectedData.average;
	SViz.loadViz("showCourses", { "domain": ["attending", "approved", "not-evaluated", "flunked" ], entries: [selectedData] }, "#curricular-course-placeholder", { innerRadius: 100, radius: 150, showLegend: false });
	
	var histogramData =  {
		"minGrade": 10,	
		"maxGrade": 20,
		"minRequiredGrade": 9.5,
		"ranks": ["NA", "RE"],
		"grades": selectedData.grades
	};
	
	console.log(histogramData);
	SViz.loadViz("showHistogram", histogramData, "#curricular-course-histogram", { legend: false, details: false, table: false , showMean: false, showMinGrade: false});
});


SViz.loadViz("showStackedBar", progressData, "#progress-bar", { width: "600", height: "100", legendSelector: "#legenda" });
SViz.loadViz("showCourses", curricularYearsData, "#curricular-years-placeholder", { showLegend: false });
</script>