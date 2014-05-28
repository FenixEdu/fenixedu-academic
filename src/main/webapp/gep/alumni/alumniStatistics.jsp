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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<!-- gep/alumni/alumniStatistics.jsp -->
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/highstock/highstock.js">
</script>
</head>
<script type="text/javascript">
	chartData = eval("<%= request.getAttribute("chartData") %>");
	Highcharts.setOptions({ global: {
			useUTC: false
		}
	});
	jQuery(function() {
	    // Create the chart    
	    window.chart = new Highcharts.StockChart({
	        chart: {
	            renderTo: 'chart'
	        },
	        
	        plotOptions: {
	            series: {
	                marker: {
	                    enabled: true    
	                },
	                dataLabels : {
	                    enabled :true,
	                    formatter: function(){
	                        if(this.series.index === 1)
	                            return null;
	                        else
	                            return this.y
	                    }
	                },
	                
	            }
	        },
	        
	        rangeSelector: {
	            selected: 1
	        },
	        
	        title: {
	            text: 'nº registos / dia'
	        },
	        
	        xAxis: {
	            maxZoom: 14 * 24 * 3600000 // fourteen days
	        },
	        
	        yAxis: {
	            title: {
	                text: 'Nº de Alumni'
	            }
	        },
	        
	        series: [{
	            name: 'Nº de Alumni',
	            data: chartData
	        }]
	    });
	});
</script>

<h2><bean:message key="title.alumni.statistics" bundle="GEP_RESOURCES" /></h2>


<table class="tstyle2">
	<tr>
		<td class="aleft"><bean:message key="label.alumni.total.alumni.fenix" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics1" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.total.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics2" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.new.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics3" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.registered.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics4" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.job.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics5" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.formation.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics6" /></td>
	</tr>
</table>

<div id="chart" style="height: 500px"></div>

<p><strong><bean:message key="title.alumni.report.file.generated" bundle="GEP_RESOURCES" /></strong></p>

<logic:empty name="doneJobs">
	<p><em><bean:message key="message.alumni.report.file.generated.empty" bundle="GEP_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="doneJobs">
	<fr:view name="doneJobs">
		<fr:schema type="net.sourceforge.fenixedu.domain.alumni.AlumniReportFile" bundle="GEP_RESOURCES">
			<fr:slot name="filename" key="label.alumni.report.file.filename" />
			<fr:slot name="requestDate" key="label.alumni.report.file.request.date" />
			<fr:slot name="jobStartTime" key="label.alumni.report.file.job.start.time" />
			<fr:slot name="jobEndTime" key="label.alumni.report.file.job.end.time" />
		</fr:schema>
		
		<fr:layout name="tabular">
	   		<fr:property name="classes" value="tstyle1 mtop05" />
	    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />
		
			<fr:link label="label.alumni.report.file.view,GEP_RESOURCES" name="view" link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module="" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p><strong><bean:message key="title.alumni.report.file.not.generated" bundle="GEP_RESOURCES" /></strong></p>

<logic:empty name="undoneJobs">
	<p><em><bean:message key="message.alumni.report.file.not.generated.empty" bundle="GEP_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="undoneJobs">
	<fr:view name="undoneJobs">
		<fr:schema type="net.sourceforge.fenixedu.domain.alumni.AlumniReportFile" bundle="GEP_RESOURCES">
			<fr:slot name="requestDate" key="label.alumni.report.file.request.date" />
			<fr:slot name="jobStartTime" key="label.alumni.report.file.job.start.time" />
			<fr:slot name="jobEndTime" key="label.alumni.report.file.job.end.time" />
			<fr:slot name="isNotDoneAndCancelled" key="label.alumni.report.file.job.cancelled" />
		</fr:schema>
		
		<fr:layout name="tabular">
	   		<fr:property name="classes" value="tstyle1 mtop05" />
	    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />

			<fr:link label="label.alumni.report.file.cancel" name="cancel" link="/alumni.do?method=cancel&jobId=${externalId}" condition="isNotDoneAndNotCancelled"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:equal value="true" name="canRequestReport">
<p>Gerar lista de alumni's em formato Excel:</p>
<ul>
	<li>
		<html:link page="/alumni.do?method=generateRegisteredAlumniPartialReport">
			<bean:message key="link.alumni.partial.reports" bundle="GEP_RESOURCES"/>
		</html:link>
		<br/> Lista de alumni que (pelo menos) concluiram o passo inicial do processo público de registo.
	</li>
	
	<li class="mtop1">
		<html:link page="/alumni.do?method=generateRegisteredAlumniFullReport">
			<bean:message key="link.alumni.full.reports" bundle="GEP_RESOURCES"/>
		</html:link>
		<br/>Lista de todos os alumni que na parte pública ou privada iniciaram a actualização da informação pessoal e/ou informação profissional e/ou formação pós-graduada.
	</li>

	<li class="mtop1">
		<html:link page="/alumni.do?method=generateAllAlumni">
			<bean:message key="link.alumni.full.reports.all" bundle="GEP_RESOURCES"/>
		</html:link>
		<br/>Lista de todos os alumni.
	</li>

</ul>
</logic:equal>
