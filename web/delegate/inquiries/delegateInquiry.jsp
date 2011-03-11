<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>



body.survey {
font-size: 12px;
line-height: 15px;
font-family: Arial;
background: #eee;
text-align: center;
margin: 40px 20px 80px 20px;
}
body.survey h1 {
font-size: 22px;
line-height: 30px;
margin: 15px 0;
}
body.survey h2 {
font-size: 17px;
line-height: 30px;
margin: 40px 0 10px 0;
}
body.survey p {
margin: 10px 0 5px 0;
}

#page {
margin: 20px auto;
text-align: left;
padding: 20px 30px;
width: 900px;
background: #fff;
border: 1px solid #ddd;
/*
-moz-border-radius: 4px;
border-radius: 4px;
*/
}

/* ---------------------------
      STRUCTURAL TABLE 
--------------------------- */

table.structural {
border-collapse: collapse;
}
table.structural tr td {
padding: 0;
vertical-align: top;
}

/* ---------------------------
      TABLE GRAPH 
--------------------------- */

div.graph {
margin: 15px 0px 30px 0px;
}

table.graph, table.graph-2col {
color: #555;
}

table.graph {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
}
table.graph td {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 5px !important;
text-align: center;
	vertical-align: middle !important;
}
table.graph tr.thead th {
border-bottom: 2px solid #ccc;
}
table.graph tr th:first-child {
padding: 5px 0px 5px 0px;
}
table.graph tr.thead th {
vertical-align: bottom;
text-align: center;
color: #555;
text-transform: uppercase;
font-size: 9px;
padding: 5px 5px;
background: #f5f5f5;
}
/*
table.graph tr th {
width: 300px;
}
table.graph tr.thead th {
width: 55px;
}
*/
table.graph tr.thead th.first {
width: 300px;
}

table.graph tr td.x1, table tr td.x2, table tr td.x3, table tr td.x4 {
background: #f5f5f5;
width: 55px;
}
table.graph tr td.x1 {
border-right: 1px solid #ccc;
}

/* specific */

table.general-results  {
width: 100%;
}

table.general-results td {
width: 30px;
height: 32px;
}

table.teacher-results tr td {
width: 30px;
}
table.teacher-results tr th {
width: auto;
padding-left: 10px !important;
}

div.workload-left, div.workload-right   {
float: left;
width: 435px;
margin-top: 30px;
}
div.workload-left   {
padding-right: 30px;
}
div.workload-right table td { 
text-align: left; /* fixes IE text alignment issue*/
}


tr.sub th {
padding-left: 20px !important;
}


div.result-audit {
margin: 20px 0 -20px 0;
}
div.result-audit span {
background: #555;
background: #C04439;
padding: 5px 10px;
/* border: 1px solid #ddd; */
color: #fff;
font-weight: bold;
}



tr.result-audit th span, tr.result-analysis th span {
/*
color: #c04439;
color: #b83326;
*/
font-weight: bold;
}
tr.result-audit, tr.result-analysis {
background: #f5f5f5;
}


/* ---------------------------
      TABLE GRAPH 2COL
--------------------------- */

table.graph-2col {
border-collapse: collapse;
margin: 5px 0 5px 0;
/* width: 1000px; */
}
table.graph-2col th {
text-align: left;
border-top: none;
border-bottom: 1px solid #ccc;
padding: 5px 0px;
font-weight: normal;
/* width: 380px; */
}
table.graph-2col td {
text-align: right !important;
border-top: none;
border-bottom: 1px solid #ccc !important;
padding: 5px 5px !important;
text-align: center;
}

/* ---------------------------
      INSIDE TABLE 
--------------------------- */

table.graph table {
width: 500px;
border-collapse: collapse;
}
table.graph table tr td {
border: none;
padding: 0 !important;
}
table.graph table tr td div {
}

/* ---------------------------
      GRAPH BARS 
--------------------------- */

div.graph-bar-horz {
height: 21px;
-moz-border-radius: 3px;
border-radius: 3px;
float: left;
background: #3573a5;
}
div.graph-bar-horz-number {
float: left;
padding-top: 2px;
padding-left: 6px;
}

/* right-aligned bars */

table.bar-right div {
float: right;
text-align: right;
}

table.bar-right div.graph-bar-horz-number {
padding-right: 10px;
}


div.bar-yellow, div.bar-red, div.bar-green, div.bar-blue, div.bar-purple, div.bar-grey  {
width: 30px;
height: 19px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.bar-yellow { background: #DDB75B; }
div.bar-red { background: #C04439; }
div.bar-green { background: #478F47; }
div.bar-blue { background: #3574A5; }
div.bar-purple { background: #743E8C; }
div.bar-grey { background: #888888; }


div.first-bar {
-moz-border-radius-topleft: 3px;
-moz-border-radius-bottomleft: 3px;
border-top-left-radius: 3px;
border-bottom-left-radius: 3px;
}
div.last-bar {
-moz-border-radius-topright: 3px;
-moz-border-radius-bottomright: 3px;
border-top-right-radius: 3px;
border-bottom-right-radius: 3px;
}


div.graph-bar-16-1,
div.graph-bar-16-2,
div.graph-bar-16-3,
div.graph-bar-16-4,
div.graph-bar-16-5,
div.graph-bar-16-6,
div.graph-bar-19-1,
div.graph-bar-19-2,
div.graph-bar-19-3,
div.graph-bar-19-4,
div.graph-bar-19-5,
div.graph-bar-19-6,
div.graph-bar-19-7,
div.graph-bar-19-8,
div.graph-bar-19-9,
div.graph-bar-grey {
height: 18px;
/*
-moz-border-radius: 3px;
border-radius: 3px;
*/
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
}
div.graph-bar-yellow {
background: #e9a73d;
}
div.graph-bar-red {
background: #ce423d;
}
div.graph-bar-green {
background: #2d994a;
}
div.graph-bar-blue {
background: #006ca2;
}
div.graph-bar-grey {
background: #eee;
color: #888;
font-weight: normal;
-moz-border-radius: 3px;
border-radius: 3px;
}



.neutral div.graph-bar-19-1, .neutral div.graph-bar-16-1 { background: #528FBD; } /* red */ 
.neutral div.graph-bar-19-2 { background: #4C87B8; } /* red */
.neutral div.graph-bar-19-3, .neutral div.graph-bar-16-2 { background: #457EB2; } /* red */
.neutral div.graph-bar-19-4, .neutral div.graph-bar-16-3 { background: #3F76AC; } /* yellow */
.neutral div.graph-bar-19-5, .neutral div.graph-bar-16-4 { background: #396DA7; } /* green */
.neutral div.graph-bar-19-6 { background: #3265A1; } /* green */
.neutral div.graph-bar-19-7, .neutral div.graph-bar-16-5 { background: #2C5D9C; } /* green */
.neutral div.graph-bar-19-8 { background: #255495; } /* green */
.neutral div.graph-bar-19-9, .neutral div.graph-bar-16-6 { background: #204D91; } /* green */

table.neutral table {
border-collapse: separate !important;
}

.classification div.graph-bar-19-1, .classification div.graph-bar-16-1 { background: #c04439; } /* red */ 
.classification div.graph-bar-19-2 { background: #ca623a; } /* red */
.classification div.graph-bar-19-3, .classification div.graph-bar-16-2 { background: #cc7d3f; } /* red */
.classification div.graph-bar-19-4, .classification div.graph-bar-16-3 { background: #ddb75b; } /* yellow */
.classification div.graph-bar-19-5, .classification div.graph-bar-16-4 { background: #91a749; } /* green */
.classification div.graph-bar-19-6 { background: #74a14e; } /* green */
.classification div.graph-bar-19-7, .classification div.graph-bar-16-5 { background: #5c9b4e; } /* green */
.classification div.graph-bar-19-8 { background: #478f47; } /* green */
.classification div.graph-bar-19-9, .classification div.graph-bar-16-6 { background: #438a43; } /* green */

span.legend-bar {
padding: 0 3px;
font-size: 8px;
}


span.legend-bar-16-1,
span.legend-bar-16-2,
span.legend-bar-16-3,
span.legend-bar-16-4,
span.legend-bar-16-5,
span.legend-bar-16-6,
span.legend-bar-19-1,
span.legend-bar-19-2,
span.legend-bar-19-3,
span.legend-bar-19-4,
span.legend-bar-19-5,
span.legend-bar-19-6,
span.legend-bar-19-7,
span.legend-bar-19-8,
span.legend-bar-19-9 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 2px 4px;
font-size: 6px;
font-weight: bold;
}


table.neutral span.legend-bar-19-1, table.neutral span.legend-bar-16-1 { background: #528FBD; }
table.neutral span.legend-bar-19-2 { background: #4C87B8; }
table.neutral span.legend-bar-19-3, table.neutral span.legend-bar-16-2 { background: #457EB2; }
table.neutral span.legend-bar-19-4, table.neutral span.legend-bar-16-3 { background: #3F76AC; }
table.neutral span.legend-bar-19-5, table.neutral span.legend-bar-16-4 { background: #396DA7; }
table.neutral span.legend-bar-19-6 { background: #3265A1; }
table.neutral span.legend-bar-19-7, table.neutral span.legend-bar-16-5 { background: #2C5D9C; }
table.neutral span.legend-bar-19-8 { background: #255495; }
table.neutral span.legend-bar-19-9, table.neutral span.legend-bar-16-6 { background: #204D91; }


table.classification span.legend-bar-19-1, table.classification span.legend-bar-16-1 { background: #c04439; } /* red */ 
table.classification span.legend-bar-19-2 { background: #ca623a; } /* red */
table.classification span.legend-bar-19-3, table.classification span.legend-bar-16-2 { background: #cc7d3f; } /* red */
table.classification span.legend-bar-19-4, table.classification span.legend-bar-16-3 { background: #ddb75b; } /* yellow */
table.classification span.legend-bar-19-5, table.classification span.legend-bar-16-4 { background: #91a749; } /* green */
table.classification span.legend-bar-19-6 { background: #74a14e; } /* green */
table.classification span.legend-bar-19-7, table.classification span.legend-bar-16-5 { background: #5c9b4e; } /* green */
table.classification span.legend-bar-19-8 { background: #478f47; } /* green */
table.classification span.legend-bar-19-9, table.classification span.legend-bar-16-6 { background: #438a43; } /* green */


ul.legend-general {
list-style: none;
padding: 0;
margin: 10px 0;
color: #555;
}
ul.legend-general li {
/*display: inline;*/
padding-right: 10px;
padding: 2px 0;
}

ul.legend-general-teacher {
list-style: none;
padding: 0;
margin: 10px 0;
color: #555;
}
ul.legend-general-teacher li {
display: inline;
padding: 2px 0;
padding-right: 5px;
}


span.legend-bar-1,
span.legend-bar-2,
span.legend-bar-3,
span.legend-bar-4,
span.legend-bar-5 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 2px 5px 0px 5px;
font-size: 8px;
font-weight: bold;
}


span.legend-bar-1 { background: #3574A5; }
span.legend-bar-2 { background: #478F47; }
span.legend-bar-3 { background: #DDB75B; }
span.legend-bar-4 { background: #C04439; }
span.legend-bar-5 { background: #743E8C; }


/* ---------------------------
      SUMMARY
--------------------------- */

div.summary table th {
border: none;
padding: 3px 0;
width: 200px;
}
div.summary table td {
text-align: left;
border: none;
padding: 3px 0;
}


/* ---------------------------
      TOOLTIPS
--------------------------- */

a {
color: #105c93;
}
a.help, a.helpleft {
position: relative;
text-decoration: none;
border: none !important;
width: 20px;
text-transform: none;
font-size: 12px;
font-weight: normal;
}
a.help span, a.helpleft span { display: none; }
a.help:hover, a.helpleft:hover {
z-index: 100;
border: 1px solic transparent;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a.helpleft:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 20px;
left: 20px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
}
a.helpleft[class]:hover span {
right: 20px;
}

/* ---------------------------
      CHARTS
--------------------------- */

div.chart {
clear:both;
/* min-width: 600px; */
background: #eee;
padding: 10px;
/*
-moz-border-radius: 3px;
border-radius: 3px;
*/
}


table.graph tr td div.chart {
clear:both;
/* min-width: 600px; */
background: none;
padding: 0;
}

/* ---------------------------
      REPORTS
--------------------------- */


div#report div.graph {
width: 900px;
}

span.link {
text-decoration: none;
color: #105c93;
border-bottom: 1px solid #97b7ce;
cursor: pointer;
}


div#report div.bar-yellow,
div#report div.bar-red,
div#report div.bar-green,
div#report div.bar-blue,
div#report div.bar-purple,
div#report div.bar-grey {
margin: 3px 0;
padding: 0 3px;
display: inline;
background: #fff;
}
div#report div.bar-yellow div,
div#report div.bar-red div,
div#report div.bar-green div,
div#report div.bar-blue div,
div#report div.bar-purple div,
div#report div.bar-grey div {
padding: 2px 9px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
margin: auto;
display: inline;
}

div#report div.bar-yellow div { background: #DDB75B; }
div#report div.bar-red div { background: #C04439; }
div#report div.bar-green div { background: #478F47; }
div#report div.bar-blue div { background: #3574A5; }
div#report div.bar-purple div { background: #743E8C; }
div#report div.bar-grey div { background: #888888; }


td.comment {
background: #f5f5f5;
text-align: left !important;
}
td.comment div {
width: auto;
padding: 0 5px 10px 5px;
}


div.workload-left div.graph, div.workload-right div.graph {
width: auto !important;
}

/*
Delegate Inquiry Specific Questions
*/

.question {
border-collapse: collapse;
margin: 10px 0;
}
.question th {
padding: 5px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
vertical-align: top;
font-weight: bold;
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
}

</style> 

<script src="<%= request.getContextPath() + "/javaScript/inquiries/jquery.min.js" %>" type="text/javascript" ></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/hideButtons.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/check.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/checkall.js" %>"></script>

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<em>Portal do Delegado</em>
<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>
<h3 class="mtop15">
<span class="highlight1"><bean:write name="executionCourse" property="name"/></span> - 
<bean:write name="executionDegree" property="degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)
</h3>
<p>
Por baixo de cada pergunta que teve um resultado "Inadequado" ou "a melhorar" está um espaço para colocares o motivo que te levou e aos teus colegas a assinalar o problema. 
Sê o mais objectivo possível. O teu contributo é indispensável para os problemas serem resolvidos!
</p>
<div id="report">
<fr:form action="/delegateInquiry.do?method=saveChanges">
	<fr:edit id="delegateInquiryBean" name="delegateInquiryBean" visible="false"/>
	
	<!-- Curricular Inquiry Results -->
	<bean:define id="toogleFunctions" value=""/>
	<h3 class="separator2 mtop25">
		<span style="font-weight: normal;">
			<bean:message key="title.inquiry.resultsUC" bundle="DELEGATES_RESOURCES"/>
		</span>
	</h3>
	<logic:iterate indexId="iter" id="blockResult" name="delegateInquiryBean" property="curricularBlockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
		<bean:define id="toogleFunctions">
			<bean:write name="toogleFunctions" filter="false"/>
			<%= "$('#block" + (Integer.valueOf(iter)+(int)1) + "').click(function() { $('#block" + (Integer.valueOf(iter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
		</bean:define>
		<h4 class="mtop15">
			<logic:notEmpty name="blockResult" property="blockResultClassification">
				<div class="<%= "bar-" + blockResult.getBlockResultClassification().name().toLowerCase() %>"><div>&nbsp;</div></div>
			</logic:notEmpty>
			<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
			<bean:define id="expand" value=""/>
			<logic:notEqual value="true" name="blockResult" property="mandatoryComments">
				<span style="font-weight: normal;">| 
					<span id="<%= "block" + (Integer.valueOf(iter)+(int)1) %>" class="link">
						<bean:message bundle="DELEGATES_RESOURCES" key="link.inquiry.showResults"/>
					</span>
				</span>
				<bean:define id="expand" value="display: none;"/>
			</logic:notEqual>
		</h4>
		<div id="<%= "block" + (Integer.valueOf(iter)+(int)1) + "-content"%>" style="<%= expand %>"> 
			<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
				<fr:edit id="<%= "group" + iter + groupIter %>" name="groupResult" layout="inquiry-group-resume-input"/>
			</logic:iterate>
		</div>
	</logic:iterate>
	
	<!-- Teachers Inquiry Results -->
	<bean:define id="teacherToogleFunctions" value=""/>
	<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="delegateInquiryBean" property="teachersResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<bean:write name="teacherShiftTypeResult" property="professorship.person.name"/> / 
				<bean:message name="teacherShiftTypeResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>
			</span>
		</h3>
		<logic:iterate indexId="iter" id="blockResult" name="teacherShiftTypeResult" property="blockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
			<bean:define id="teacherToogleFunctions">
				<bean:write name="teacherToogleFunctions" filter="false"/>
				<%= "$('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "').click(function()" 
					+ "{ $('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
			</bean:define>
			<h4 class="mtop15" style="clear: right">
				<logic:notEmpty name="blockResult" property="blockResultClassification">
					<div class="<%= "bar-" + blockResult.getBlockResultClassification().name().toLowerCase() %>"><div>&nbsp;</div></div>
				</logic:notEmpty>
				<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
				<bean:define id="expand" value=""/>
				<logic:notEqual value="true" name="blockResult" property="mandatoryComments">
					<span style="font-weight: normal;">| 
						<span id="<%= "teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType()  + (Integer.valueOf(iter)+(int)1) %>" class="link">
							Mostrar resultados
						</span>
					</span>
					<bean:define id="expand" value="display: none;"/>
				</logic:notEqual>
			</h4>
			<div id="<%= "teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "-content"%>" style="<%= expand %>"> 
				<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
					<fr:edit id="<%= "teacherGroup" + teacherIter + iter + groupIter %>" name="groupResult" layout="inquiry-group-resume-input"/>
				</logic:iterate>
			</div>
		</logic:iterate>
	</logic:iterate>
	
	<!-- Delegate Inquiry -->	
	<logic:iterate id="inquiryBlockDTO" name="delegateInquiryBean" property="delegateInquiryBlocks">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			</span>
		</h3>					
		<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups"indexId="index">					
			<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
		</logic:iterate>
	</logic:iterate>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.saveInquiry" bundle="DELEGATES_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>
</div>

<bean:define id="scriptToogleFunctions">
	<script>
	<bean:write name="toogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptToogleFunctions" filter="false"/>

<bean:define id="scriptTeacherToogleFunctions">
	<script>
	<bean:write name="teacherToogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptTeacherToogleFunctions" filter="false"/>