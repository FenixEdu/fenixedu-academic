<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page trimDirectiveWhitespaces="true" %>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>

<div id="container" style="min-width: 310px; height: 150px; margin: 0 auto"></div>

<script type="text/javascript">
<!--

Highcharts.chart('container', {
	exporting: { enabled: false },
    chart: {
        type: 'bar'
    },
    title: {
        text: ''
    },
    xAxis: {
        categories: ['']
    },
    yAxis: {
        min: 0,
        title: {
            text: ''
        }
    },
    legend: {
        reversed: true
    },
    plotOptions: {
        series: {
            stacking: 'normal'
        }
    },
    series: [{
        name: '<spring:message code="label.overdue" text="Overdue"/>',
        data: [${stats.debtOverdue}],
        color: '#bf4848'
    }, {
    	name: '<spring:message code="label.debt" text="Debt"/>',
        data: [${stats.debt}],
        color: '#ffec49'
    }, {
    	name: '<spring:message code="label.unused" text="Unused"/>',
        data: [${stats.unused}],
        color: '#ff49ff'
    }, {
    	name: '<spring:message code="label.payed.overdue" text="Payed Overdue"/>',
        data: [${stats.payedOverdue}],
        color: '#4572A7'
    }, {
    	name: '<spring:message code="label.payed" text="Payed"/>',
        data: [${stats.payed}],
        color: '#46a843'
    }]
});
//-->
</script>
