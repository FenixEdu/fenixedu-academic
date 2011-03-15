<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/jquery.min.js"></script> 
<script type="text/javascript">jQuery.noConflict();</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/highcharts.js"></script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/exporting.js"></script> 
<script type="text/javascript">var example = 'bar-basic', theme = 'default';</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/jquery/scripts.js"></script> 
<script type="text/javascript">Highcharts.theme = { colors: [] }; var highchartsOptions = Highcharts.getOptions();</script> 
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
}
 
</style> 

<style>

body.survey-results {
font-size: 12px;
line-height: 15px;
font-family: Arial;
background: #eee;
text-align: center;
margin: 40px 20px 80px 20px;
}
body.survey-results h1 {
font-size: 22px;
line-height: 30px;
margin: 15px 0;
}
body.survey-results h2 {
font-size: 17px;
line-height: 30px;
margin: 40px 0 10px 0;
}
body.survey-results h2 span {
line-height: 15px;
}
body.survey-results p {
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
background: #C04439;
padding: 5px 10px;
color: #fff;
}

tr.result-audit th span, tr.result-analysis th span {
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


.neutral div.graph-bar-19-1, .neutral div.graph-bar-16-1 { background: #92B7C6; }
.neutral div.graph-bar-19-2 { background: #7BA9C3; }
.neutral div.graph-bar-19-3, .neutral div.graph-bar-16-2 { background: #669BC0; }
.neutral div.graph-bar-19-4, .neutral div.graph-bar-16-3 { background: #5591BD; }
.neutral div.graph-bar-19-5, .neutral div.graph-bar-16-4 { background: #4983B5; }
.neutral div.graph-bar-19-6 { background: #4076AD; }
.neutral div.graph-bar-19-7, .neutral div.graph-bar-16-5 { background: #376AA5; }
.neutral div.graph-bar-19-8 { background: #2A5A9A; }
.neutral div.graph-bar-19-9, .neutral div.graph-bar-16-6 { background: #225093; }

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

span.legend-bar,
span.legend-bar-first,
span.legend-bar-last {
padding: 0 3px;
font-size: 8px;
}

span.legend-bar-first span.text { padding-right: 5px; }
span.legend-bar-last span.text { padding-left: 5px; }

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
font-size: 8px;
font-weight: bold;
color: #fff;
}


/*
#528FBD
#4C87B8
#457EB2
#3F76AC
#396DA7
#3265A1
#2C5D9C
#255495
#204D91
*/

/*
#92B7C6
#7BA9C3
#669BC0
#5591BD
#4983B5
#4076AD
#376AA5
#2A5A9A
#225093
*/



table.neutral span.legend-bar-19-1, table.neutral span.legend-bar-16-1 { background: #92B7C6; }
table.neutral span.legend-bar-19-2 { background: #7BA9C3; }
table.neutral span.legend-bar-19-3, table.neutral span.legend-bar-16-2 { background: #669BC0; }
table.neutral span.legend-bar-19-4, table.neutral span.legend-bar-16-3 { background: #5591BD; }
table.neutral span.legend-bar-19-5, table.neutral span.legend-bar-16-4 { background: #4983B5; }
table.neutral span.legend-bar-19-6 { background: #4076AD; }
table.neutral span.legend-bar-19-7, table.neutral span.legend-bar-16-5 { background: #376AA5; }
table.neutral span.legend-bar-19-8 { background: #2A5A9A; }
table.neutral span.legend-bar-19-9, table.neutral span.legend-bar-16-6 { background: #225093; }


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
span.legend-bar-5,
span.legend-bar-6 {
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
span.legend-bar-6 { background: #888888; }


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
div#report div.bar-purple {
margin: 3px 0;
padding: 0 3px;
display: inline;
background: #fff;
}
div#report div.bar-yellow div,
div#report div.bar-red div,
div#report div.bar-green div,
div#report div.bar-blue div,
div#report div.bar-purple div {
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

</style>

<bean:define id="resultsTitle" name="answersResultsSummaryBean" property="inquiryGroupQuestion.inquiryQuestionHeader.title"/>
<bean:define id="totalAnswersLabel" name="totalAnswers" property="inquiryQuestion.label"/>
<bean:define id="totalAnswersNumber" name="totalAnswers" property="questionResult.value"/>

<bean:define id="answerResultsJS">
<script type="text/javascript"> 
var chart;
jQuery(document).ready(function() {
   chart = new Highcharts.Chart({
	   colors: [
				'#999999', '#3B91B8', '#00518B'
	        ],
	  chart: {
	     renderTo: 'pie1',
	     marginRight: 20,
	     marginLeft: 20,
	     marginTop: 75,
	   	 marginBottom: 45
	  },
      title: {
         text: <%= "'" + resultsTitle.toString() + "'"%>
      },
      subtitle: {
          text: <%= "'" + totalAnswersLabel + ": " + totalAnswersNumber + "'" %>,
 		 y: 35
      },
      credits: {
          enabled: false
       },
 	  exporting: {
          enabled: false
       },
       plotArea: {
          shadow: null,
          borderWidth: null,
          backgroundColor: null
       },
       tooltip: {
          formatter: function() {
             return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
          }
       },
       plotOptions: {
          pie: {
             allowPointSelect: true,
             cursor: 'pointer',
             dataLabels: {
                enabled: true,
                color: Highcharts.theme.textColor || '#000000',
                connectorColor: Highcharts.theme.textColor || '#000000',
                formatter: function() {
                   return '<b>'+ this.point.name +'</b>: '+ this.y +' %';
                }
             }
          }
       },
       series: [{
         type: 'pie',
         name: 'Browser share',
         data: [
            <logic:iterate id="questionResult" name="answersResultsSummaryBean" property="questionsResults">
				<bean:define id="questionLabel" name="questionResult" property="inquiryQuestion.label"/>				
				<bean:define id="questionValue" name="questionResult" property="presentationValue"/>	  
				<logic:notEqual value="0" name="questionValue">          
	            	<%= "['" + questionLabel.toString() + "', " + questionValue + "],"%>
	            </logic:notEqual>  	            
            </logic:iterate>
         ]
      }]
   });
});

Highcharts.theme = {
   colors: ["#00518B", "#0072AA", "#000000"],
   chart: {
      backgroundColor: {
         linearGradient: [0, 0, 250, 500],
         stops: [
            [0, 'rgb(245, 245, 245)'],
            [1, 'rgb(245, 245, 245)']
         ]
      }
   }
};
var highchartsOptions = Highcharts.setOptions(Highcharts.theme)
</script> 
</bean:define>

<bean:define id="workLoadTitle" name="workLoadaSummaryBean" property="inquiryGroupQuestion.inquiryQuestionHeader.title"/>

<bean:define id="workloadJS">
<script type="text/javascript"> 
var chart;
jQuery(document).ready(function() {
   chart = new Highcharts.Chart({
      colors: [
		'#669BC0', '#4983B5', '#376AA5', '#225093'
      ],
      chart: {
         renderTo: 'graphic3',
         defaultSeriesType: 'bar'
      },
      title: {
         text: <%= "'" + workLoadTitle.toString() + "'" %>
      },
      credits: {
         enabled: false
      },
	  exporting: {
         enabled: false
      },
      xAxis: {
         categories: ['ECTS Previsto', 'ECTS Estimado']
      },
      yAxis: {
         min: 0,
         title: {
            text: ''
         }
      },
      legend: {
         backgroundColor: Highcharts.theme.legendBackgroundColorSolid || '#FAFAFA',
         reversed: true
      },
      tooltip: {
         formatter: function() {
            return ''+
                this.series.name +': '+ this.y +'';
         }
      },
      plotOptions: {
         series: {
            stacking: 'normal'
         }
      },
           series: [			
			<logic:iterate indexId="iter" id="questionResult" name="workLoadaSummaryBean" property="questionsResults">
				<bean:define id="questionLabel" name="questionResult" property="inquiryQuestion.label"/>
				<bean:define id="questionValue" name="questionResult" property="presentationValue"/>
				<bean:define id="espectedValue" value="0"/>
				<logic:equal name="iter" value="2">
					{ name: 'Trabalho Autónomo', data: [ <bean:write name="autonumousWorkEcts"/>, 0 ]},
					<bean:define id="espectedValue"><bean:write name="contactLoadEcts"/></bean:define>
				</logic:equal>  
	            <%= "{ name: '" + questionLabel.toString() + "', data: [" + espectedValue + "," + (questionValue.equals("") ? "0" : questionValue) + "]},"%>
			</logic:iterate>
			]
   });
});
</script> 
</bean:define> 

<bean:define id="ucEvaluationsTitle" name="ucEvaluationsGroupBean" property="inquiryGroupQuestion.inquiryBlock.inquiryQuestionHeader.title"/>

<bean:define id="ucEvaluationsJS">
<script type="text/javascript"> 
var chart;
jQuery(document).ready(function() {
   chart = new Highcharts.Chart({
      colors: [
         '#0072AA', '#00518B'
      ],
      chart: {
         renderTo: 'graphic1',
         defaultSeriesType: 'column'
      },
      title: {
         text: <%= "'" + ucEvaluationsTitle.toString() + "'" %>
      },
      credits: {
         enabled: false
      },
	  exporting: {
         enabled: false
      },
      xAxis: {
         categories: [
             <logic:iterate id="questionSummary" name="ucEvaluationsGroupBean" property="questionsResults">
             	<logic:iterate id="category" name="questionSummary" property="inquiryQuestion.inquiryQuestionHeader.scaleHeaders.scale">
             		<%= "'" + category + "'," %>
             	</logic:iterate>
             </logic:iterate>
         ]
      },
      yAxis: {
         min: 0,
         title: {
            text: ''
         }
      },
      legend: {
         layout: 'vertical',
         backgroundColor: Highcharts.theme.legendBackgroundColor || '#FAFAFA',
         align: 'left',
         verticalAlign: 'top',
         x: 50,
         y: 30,
         floating: true,
         shadow: true
      },
      tooltip: {
         formatter: function() {
            return ''+
               this.x + ': '+ this.y + '%';
         }
      },
      plotOptions: {
         column: {
            pointPadding: 0.2,
            borderWidth: 0
         }
      },
      series: [{
         name: 'Real',
         data: [ 0,
				<logic:iterate id="questionSummary" name="ucEvaluationsGroupBean" property="questionsResults">
					<logic:iterate id="inquiryResult" name="questionSummary" property="scaleValues" type="net.sourceforge.fenixedu.domain.inquiries.InquiryResult">
						<%= inquiryResult.getPresentationValue() + "," %>
					</logic:iterate>
				</logic:iterate>
               ]
      },
		{
         name: 'Alunos',
         data: [
				<logic:iterate id="inquiryResult" name="estimatedEvaluationBeanQuestion" property="scaleValues" type="net.sourceforge.fenixedu.domain.inquiries.InquiryResult">
					<%= inquiryResult.getPresentationValue() + "," %>
				</logic:iterate>
               ]
      }
	  ]
   });
});
</script>
</bean:define>

<bean:write name="answerResultsJS" filter="false"/>
<bean:write name="workloadJS" filter="false"/>
<bean:write name="ucEvaluationsJS" filter="false"/> 

</head>

<body class="survey-results">  
 
<div id="page"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em style="float: left;"><bean:write name="executionPeriod" property="semester"/>º Semestre - <bean:write name="executionPeriod" property="executionYear.year"/></em>
	<em style="float: right;">Data de produção dos resultados: <fr:view name="resultsDate" layout="no-time"/></em>
</p>

<div style="clear: both;"></div>
<h1>QUC - Resultados dos Inquéritos aos Alunos: <bean:write name="executionCourse" property="name"/></h1>

<p><bean:write name="executionDegree" property="degreeName"/></p>

<h2>Resultados gerais da UC e estatísticas de preenchimento</h2>
<table class="structural"> 
	<tr> 
		<td style="width: 400px; padding-right: 30px;"> 
			<p><strong>Resultados gerais</strong></p>
			<fr:view name="ucGroupResultsSummaryBean" layout="general-result-resume"/>		
			<logic:present name="auditResult">
				<table class="graph general-results" style="margin-top: 0;">
					<logic:equal value="RED" name="auditResult"> 				
						<tr class="result-audit"> 
							<td></td> 
							<th>
								<b>Sujeito a auditoria</b>
								<a href="" class="helpleft">[?]
									<span><bean:message key="label.tooltip.audit"/></span>
								</a>
							</th> 
						</tr>
					</logic:equal>			
					<logic:equal value="YELLOW" name="auditResult">
						<tr class="result-analysis">
							<td></td>
							<th>
								<b>Em análise</b>
								<a href="" class="helpleft">[?]
									<span><bean:message key="label.tooltip.audit"/></span>
								</a>
							</th>
						</tr>
					</logic:equal>
				</table> 	
			</logic:present>
			<table> 
				<tr> 
					<td> 
						<p style="margin-top: 15px;">Legenda:</p> 
						<ul class="legend-general" style="margin-top: 0px;"> 
							<li><span class="legend-bar-2">&nbsp;</span> Regular</li> 
							<li><span class="legend-bar-3">&nbsp;</span> A melhorar</li> 
							<li><span class="legend-bar-4">&nbsp;</span> Inadequado</li>
							<li><span class="legend-bar-6">&nbsp;</span> Sem representatividade</li>
						</ul> 
					</td> 
					<td style="padding-left: 30px;"> 
						<p style="margin-top: 15px;">Carga de trabalho:</p> 
						<ul class="legend-general" style="margin-top: 0px;"> 
							<li><span class="legend-bar-2">&nbsp;</span> De acordo com o previsto</li> 
							<li><span class="legend-bar-3">&nbsp;</span> Acima do previsto</li> 
							<li><span class="legend-bar-5">&nbsp;</span> Abaixo do previsto</li>
							<li><span class="legend-bar-6">&nbsp;</span> Sem representatividade</li>
						</ul> 
					</td> 
				</tr> 
			</table>
		</td> 
		<td style="width: 500px;"> 
			<div class="chart" style="margin-top: 5px;"> 
				<div id="pie1" class="highcharts-container" style="height: 225px; width: 480px;"></div> 
			</div>
			<style>				
				p.nonresponses span, p.inquiry-available span { padding: 0 0 0 0; color: #555; }
				p.nonresponses, p.inquiry-available { line-height: 20px; margin-bottom: 0; }
				p.nonresponses { margin-bottom: 0; }
				p.inquiry-available { margin-top: 0; }			
			</style>
			<p class="nonresponses">
				<span>Opção "Não responder":</span>
				<bean:size id="size" name="nonAnswersResultsSummaryBean" property="questionsResults"/>
				<logic:iterate indexId="index" length="length" id="questionResult" name="nonAnswersResultsSummaryBean" property="questionsResults">
					<bean:define id="questionLabel" name="questionResult" property="inquiryQuestion.label"/>				
					<bean:define id="questionValue" name="questionResult" property="presentationValue"/>
					<logic:notEqual value="0" name="questionValue">
						<bean:define id="labelValue">
			            	<%= index+1 != size ? "<span>" + questionLabel.toString() + " " + questionValue + "% - </span>" 
			            	: "<span>" + questionLabel.toString() + " " + questionValue + "% </span>" %>		            	
			            </bean:define>
			            <bean:write name="labelValue" filter="false"/>
		            </logic:notEqual>
            	</logic:iterate>				
			</p>
			<p class="inquiry-available">
				<span>
					Disponível para inquérito: 
					<logic:equal value="true" name="executionCourse" property="availableForInquiries">
						Sim
					</logic:equal>
					<logic:notEqual value="true" name="executionCourse" property="availableForInquiries">
						Não
					</logic:notEqual>
				</span>
			</p>
		</td> 
	</tr> 
</table> 

<bean:define id="ucGeneralData">
	<table class="graph-2col">
		<logic:iterate id="ucData" name="ucGeneralDataSummaryBean" property="questionsResults">
			<tr>
				<th style="width: 240px;"><bean:write name="ucData" property="inquiryQuestion.label"/></th>
				<td>
					<logic:notEmpty name="ucData" property="questionResult">
					<logic:equal value="PERCENTAGE" name="ucData" property="questionResult.resultType">
						<bean:write name="ucData" property="presentationValue"/>%
					</logic:equal>
					<logic:notEqual value="PERCENTAGE" name="ucData" property="questionResult.resultType">
						<bean:write name="ucData" property="presentationValue"/>
					</logic:notEqual>
					</logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
	</table>	
</bean:define> 

<logic:equal name="hasNotRelevantData" value="true">
	<h2>
		1. Acompanhamento da UC ao longo do semestre/carga de trabalho da UC
		<a href="" class="helpleft">[?]
			<span> 
				<b>ECTS Previsto:</b> carga de trabalho prevista para a UC, nomeadamente, distribuída pelas várias componentes.<br/> 
				<b>ECTS Estimado:</b> componentes da carga de trabalho média estimada pelos alunos em 1ª inscrição que obtiveram aprovação. 
			</span>
		</a>
	</h2>
	<div class="chart"> 
		<div id="graphic3" class="highcharts-container" style="height: 225px;"></div>
	</div>
	<h2>3. Métodos de avaliação da UC</h2>
	<bean:write name="ucGeneralData" filter="false"/>
	<div class="chart" style="margin: 20px 0 30px 0;">
		<div id="graphic1" class="highcharts-container" style="height: 225px;"></div>
	</div>
</logic:equal>

<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans">
	<h2>
		<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
		<logic:equal value="0" name="iter">
			<a href="" class="helpleft">[?]
				<span> 
					<b>ECTS Previsto:</b> carga de trabalho prevista para a UC, nomeadamente, distribuída pelas várias componentes.<br/> 
					<b>ECTS Estimado:</b> componentes da carga de trabalho média estimada pelos alunos em 1ª inscrição que obtiveram aprovação. 
				</span>
			</a>
		</logic:equal>
	</h2>
	<logic:equal value="0" name="iter">
		<div class="chart"> 
			<div id="graphic3" class="highcharts-container" style="height: 225px;"></div>
		</div>		
	</logic:equal>
	<logic:equal value="2" name="iter">
		<bean:write name="ucGeneralData" filter="false"/>
		<div class="chart" style="margin: 20px 0 30px 0;">
			<div id="graphic1" class="highcharts-container" style="height: 225px;"></div>
		</div>
	</logic:equal>
	<logic:iterate id="groupResult" name="blockResult" property="groupsResults">
		<fr:view name="groupResult" />
	</logic:iterate>
</logic:iterate>

<logic:notEmpty name="teachersSummaryBeans">
	<h2>5. Corpo docente</h2>
	<table class="graph teacher-results">
		<logic:iterate id="teacherResult" name="teachersSummaryBeans" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGeneralResultBean">
			<bean:define id="colorCode"><%= teacherResult.getInquiryResult().getResultClassification().name().toLowerCase() %></bean:define>
			<tr>
				<td><div class="<%= "bar-" + colorCode %>">&nbsp;</div></td>
				<th>
					<bean:message name="teacherResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/> - 
					<bean:write name="teacherResult" property="professorship.person.name"/>
					<bean:write name="teacherResult" property="teacherNumber"/> - 
					<bean:define id="professorshipOID" name="teacherResult" property="professorship.externalId"/>
					<bean:define id="shiftType" name="teacherResult" property="shiftType"/>
					<html:link page="<%= "/viewTeacherResults.do?professorshipOID=" + professorshipOID + "&shiftType=" + shiftType %>"
							 target="_blank" module="/publico">
						<bean:message bundle="INQUIRIES_RESOURCES" key="link.inquiries.showTeacherResults"/>
					</html:link>
				</th>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
</div>

</body>
</html>
