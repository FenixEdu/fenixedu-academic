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
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen" type="text/css" />
 
<style media="print"> 
 
div.xpto {
page-break-inside: avoid;
page-break-after: always;
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
