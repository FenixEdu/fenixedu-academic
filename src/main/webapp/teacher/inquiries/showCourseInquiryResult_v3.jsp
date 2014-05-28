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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xml:lang="pt-PT" xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT"> 
<head> 
<title>QUC</title> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/jquery.min.js"></script> 
<script type="text/javascript">jQuery.noConflict();</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/highcharts.js"></script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/inquiries/exporting.js"></script> 
<script type="text/javascript">var example = 'bar-basic', theme = 'default';</script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/jquery/scripts.js"></script> 
<script type="text/javascript">Highcharts.theme = { colors: [] }; var highchartsOptions = Highcharts.getOptions();</script> 
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
}
 
</style>

<bean:define id="resultsTitle" name="answersResultsSummaryBean" property="inquiryGroupQuestion.inquiryQuestionHeader.title"/>
<bean:define id="totalAnswersLabel" name="totalAnswers" property="inquiryQuestion.label"/>
<bean:define id="totalAnswersNumber" name="totalAnswers" property="questionResult.value"/>

<logic:equal value="true" name="executionCourse" property="availableForInquiries">
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
	     marginRight: 30,
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

</script> 
</bean:define>
</logic:equal>

<bean:define id="workLoadTitle" name="workLoadSummaryBean" property="inquiryGroupQuestion.inquiryQuestionHeader.title"/>

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
			<logic:iterate indexId="iter" id="questionResult" name="workLoadSummaryBean" property="questionsResults">
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

<logic:equal value="true" name="executionCourse" property="availableForInquiries">
	<bean:write name="answerResultsJS" filter="false"/>
</logic:equal>
<bean:write name="workloadJS" filter="false"/>
<bean:write name="ucEvaluationsJS" filter="false"/> 

</head>

<body class="survey-results">  
 
<div id="page"> 
 
<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<p>
	<em style="float: left;"><bean:write name="executionPeriod" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/>
		 - <bean:write name="executionPeriod" property="executionYear.year"/></em>
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
								<b><bean:message key="label.inquiry.toAudit" bundle="INQUIRIES_RESOURCES"/></b>
								<a href="#" class="helpleft">[?]
									<span><bean:message key="label.tooltip.audit" bundle="INQUIRIES_RESOURCES"/></span>
								</a>
							</th> 
						</tr>
					</logic:equal>			
					<logic:equal value="YELLOW" name="auditResult">
						<tr class="result-analysis">
							<td></td>
							<th>
								<b><bean:message key="label.inquiry.inObservation" bundle="INQUIRIES_RESOURCES"/></b>
								<a href="#" class="helpleft">[?]
									<span><bean:message key="label.tooltip.audit" bundle="INQUIRIES_RESOURCES"/></span>
								</a>
							</th>
						</tr>
					</logic:equal>
				</table> 	
			</logic:present>
			<table> 
				<tr> 
					<td> 
						<p style="margin-top: 15px;"><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</p> 
						<ul class="legend-general" style="margin-top: 0px;"> 
							<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li>  
						</ul> 
					</td> 
					<td style="padding-left: 30px;"> 
						<p style="margin-top: 15px;"><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</p> 
						<ul class="legend-general" style="margin-top: 0px;"> 
							<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
							<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li>
						</ul> 
					</td> 
				</tr> 
			</table>
		</td> 
		<td style="width: 500px; vertical-align: bottom;">
			<style>				
				p.nonresponses span, p.inquiry-available span { padding: 0 0 0 0; color: #555; }
				p.nonresponses, p.inquiry-available { line-height: 20px; margin-bottom: 0; }
				p.nonresponses { margin-bottom: 0; }
				p.inquiry-available { margin-top: 0;}			
			</style> 
			<logic:equal value="true" name="executionCourse" property="availableForInquiries">
				<div class="chart" style="margin-top: 5px;"> 
					<div id="pie1" class="highcharts-container" style="height: 225px; width: 480px;"></div> 
				</div>				
				<p class="nonresponses">
					<span>Opção "Não responder":</span>
					<bean:size id="size" name="nonAnswersResultsSummaryBean" property="questionsResults"/>
					<logic:iterate indexId="index" length="length" id="questionResult" name="nonAnswersResultsSummaryBean" property="questionsResults">
						<bean:define id="questionLabel" name="questionResult" property="inquiryQuestion.label"/>				
						<bean:define id="questionValue" name="questionResult" property="presentationValue"/>
						<logic:notEmpty name="questionValue">
							<logic:notEqual value="0" name="questionValue">
								<bean:define id="labelValue">
					            	<%= index+1 != size ? "<span>" + questionLabel.toString() + " " + questionValue + "% - </span>" 
					            	: "<span>" + questionLabel.toString() + " " + questionValue + "% </span>" %>		            	
					            </bean:define>
					            <bean:write name="labelValue" filter="false"/>
				            </logic:notEqual>
			            </logic:notEmpty>
	            	</logic:iterate>				
				</p>
			</logic:equal>
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
				<th style="width: 240px;">
					<bean:write name="ucData" property="inquiryQuestion.label"/>
					<logic:notEmpty name="ucData" property="inquiryQuestion.toolTip">
						<a href="#" class="helpleft">[?]
							<span><bean:write name="ucData" property="inquiryQuestion.toolTip"/></span>
						</a>	
					</logic:notEmpty>
				</th>
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

<bean:define id="teachersNumber" value="0"/>

<style>
	h2 em {
	 font-weight: normal;
	 font-size: 12px;
	}
</style>
<logic:iterate indexId="iter" id="blockResult" name="blockResultsSummaryBeans" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
	<div id="report">
		<h2>
			<logic:notEmpty name="blockResult" property="blockResultClassification">
				<div class="<%= "bar-" + blockResult.getBlockResultClassification().toString().toLowerCase() %>"><div>&nbsp;</div></div>
			</logic:notEmpty>
			<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
			<logic:equal value="0" name="iter">
				<a href="#" class="helpleft">[?]
					<span> 
						<b>ECTS Previsto:</b> carga de trabalho prevista para a UC, nomeadamente, distribuída pelas várias componentes.<br/> 
						<b>ECTS Estimado:</b> componentes da carga de trabalho média estimada pelos alunos em 1ª inscrição que obtiveram aprovação. 
					</span>
				</a>
			</logic:equal>
			<logic:equal name="hasNotRelevantData" value="true">
				<em><bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></em>		
			</logic:equal>
		</h2>
	</div>
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
	<bean:define id="teachersNumber" value="<%= String.valueOf(iter + 2) %>"/>	
	<logic:equal name="hasNotRelevantData" value="false">
		<logic:iterate id="groupResult" name="blockResult" property="groupsResults">
			<fr:view name="groupResult" />
		</logic:iterate>
	</logic:equal>
</logic:iterate>

<logic:notEmpty name="teachersSummaryBeans">
	<h2><bean:write name="teachersNumber"/>. <bean:message key="title.inquiry.teachingStaff" bundle="INQUIRIES_RESOURCES"/></h2>
	<table class="graph teacher-results">
		<logic:iterate id="teacherResult" name="teachersSummaryBeans" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGeneralResultBean">
			<bean:define id="colorCode"><%= teacherResult.getInquiryResult().getResultClassification().name().toLowerCase() %></bean:define>
			<tr>
				<td><div class="<%= "bar-" + colorCode %>">&nbsp;</div></td>
				<th>
					<bean:message name="teacherResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/> - 
					<bean:write name="teacherResult" property="professorship.person.name"/>
					<bean:write name="teacherResult" property="teacherId"/> - 
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