<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />

<style>

th.empty, td.empty {
background: none;
border-top: none;
border-bottom: none;
width: 10px !important;
padding: 0 !important;
}
 
th.col-course { }
th.col-bar { width: 70px; }
th.col-fill { width: 80px; }
th.col-actions { }
 
td.col-course { text-align: left; }
td.col-bar { }
td.col-fill { }
td.col-actions { white-space: nowrap; }
 
div.delegate-resume {
background: #ccc;    
padding: 3px;
margin: 15px 0;
}

div.delegate-resume table {
margin: 0;
}
 
div.bar-yellow, div.bar-red, div.bar-green, div.bar-blue, div.bar-purple, div.bar-grey {
margin: 2px 0;
}
 
td.col-bar div div {
padding: 2px 10px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
margin: auto;
display: inline;
}
 
div.bar-yellow div { background: #DDB75B; }
div.bar-red div { background: #C04439; }
div.bar-green div { background: #478F47; }
div.bar-blue div { background: #3574A5; }
div.bar-purple div { background: #743E8C; }
div.bar-grey div { background: #aaaaaa; }
 
ul.legend-general {
list-style: none;
padding: 0;
margin: 10px 0;
color: #444;
}
ul.legend-general li {
display: inline;
padding-right: 10px;
padding: 2px 10px 0 0;
}
span.legend-bar-1,
span.legend-bar-2,
span.legend-bar-3,
span.legend-bar-4,
span.legend-bar-5,
span.legend-bar-6 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 1px 4px 0px 4px;
font-size: 8px;
font-weight: bold;
}
span.legend-bar-1 { background: #3574A5; }
span.legend-bar-2 { background: #478F47; }
span.legend-bar-3 { background: #DDB75B; }
span.legend-bar-4 { background: #C04439; }
span.legend-bar-5 { background: #aaaaaa; }
span.legend-bar-6 { background: #743E8C; }

</style>

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> (<bean:write name="executionSemester" property="semester"/>º Semestre <bean:write name="executionSemester" property="executionYear.year"/>)</h3>

<p><bean:message key="message.teacher.inquiry" bundle="INQUIRIES_RESOURCES"/></p>

<html:link action=""><bean:message key="link.inquiry.fillIn" bundle="INQUIRIES_RESOURCES"/></html:link>

<logic:notEmpty name="teacherResults">
	<p class="mtop15"><strong><bean:message key="title.inquiry.teacher" bundle="INQUIRIES_RESOURCES"/></strong></p>
	<fr:view name="teacherResults">
		<fr:layout name="teacher-shiftType-inquiry-resume">
			<fr:property name="classes" value="delegate-resume"/>
			<fr:property name="style" value="max-width: 630px;"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="coursesResultResume">
	<p class="mtop15"><strong><bean:message key="title.inquiry.course" bundle="INQUIRIES_RESOURCES"/></strong></p>
	<fr:view name="coursesResultResume">
		<fr:layout name="course-degrees-inquiry-resume">
			<fr:property name="classes" value="delegate-resume"/>
			<fr:property name="style" value="max-width: 630px;"/>
			<fr:property name="showMandatoryQuestions" value="false"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><bean:message key="label.inquiry.mandatoryCommentsNumber" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>

<ul class="legend-general" style="margin-top: 0px;"> 
	<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
</ul> 