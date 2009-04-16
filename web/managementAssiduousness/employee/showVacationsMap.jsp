<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/ganttDiagrams.tld" prefix="gd" %>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.showVacationsMap" /></h2>

<logic:notEmpty name="ganttDiagram">
	<logic:present name="yearMonth">
		<logic:present name="employee">
			<bean:define id="month" name="yearMonth" property="month" />
			<bean:define id="year" name="yearMonth" property="year" />
			<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
			<bean:define id="employee" name="employee"/>
			<bean:define id="yearMonth" name="yearMonth"/>
			<%request.setAttribute("employee", employee);
			request.setAttribute("yearMonth", yearMonth);%>
			<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
				<jsp:param name="month" value="<%=month.toString() %>" />
				<jsp:param name="year" value="<%=year.toString() %>" />
				<jsp:param name="yearMonthSchema" value="choose.year" />
				<jsp:param name="method" value="showVacationsMap" />
			</jsp:include>
			<style type="text/css">
				.tcalendar {
					border-collapse: collapse;
					/*border: 1px solid #ccc;*/
				}
				.tcalendar th {
					border: 1px solid #ccc;
					overflow: hidden;
				}
				.tcalendar td {
					border: 1px solid #ccc;
				}
			
				.tcalendar th {
					text-align: center;
					background-color: #f5f5f5;
					background-color: #f5f5f5;
					padding: 3px 4px;
				}
				.tcalendar td {
					background-color: #fff;
					padding: 0;
				}
				.tcalendar td.padded {
					padding: 2px 6px;
					border: 1px solid #ccc;
				}
				td.padded { }
				.tdbar {
					background-color: #a3d1d9;
				}
				.emptytdbar {
					background-color: #eee;
				}
				tr.active td {
					background-color: #fefeea;
				}
				.color555 {
					color: #555;
				}
				tr.selected td {
					background-color: #fdfdde;
				}
				td.tcalendarlinks {
					padding: 0.5em 0;
					border-bottom: none;
					border-left: none;
					border-right: none;
				}
				td.tcalendarlinks span { color: #888; }
				td.tcalendarlinks span a { color: #888; }
			</style>
			<p>										
				<gd:ganttDiagram ganttDiagram="ganttDiagram" eventParameter="entryID" eventUrl="<%= "/personnelSection/viewEmployeeAssiduousness.do?method=showVacationsMapByMonth&amp;year=" + year + "&amp;employeeNumber=" + employeeNumber %>" showPeriod="false" showObservations="false" bundle="ASSIDUOUSNESS_RESOURCES"/>
			</p>
		</logic:present>
	</logic:present>
</logic:notEmpty>

<logic:notEmpty name="ganttDiagramByMonth">
	<logic:present name="yearMonth">
		<logic:present name="employee">
			<bean:define id="month" name="yearMonth" property="month" />
			<bean:define id="year" name="yearMonth" property="year" />
			<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		
			<bean:define id="employee" name="employee"/>
			<bean:define id="yearMonth" name="yearMonth"/>
			<%request.setAttribute("employee", employee);
			request.setAttribute("yearMonth", yearMonth);%>
			<jsp:include page="common/consultEmployeeAssiduousnessMenu.jsp">
				<jsp:param name="month" value="<%=month.toString() %>" />
				<jsp:param name="year" value="<%=year.toString() %>" />
				<jsp:param name="yearMonthSchema" value="choose.date" />
				<jsp:param name="method" value="showVacationsMapByMonth" />
			</jsp:include>
			<style type="text/css">
				.tcalendar {
					border-collapse: collapse;
					/*border: 1px solid #ccc;*/
				}
				.tcalendar th {
					border: 1px solid #ccc;
					overflow: hidden;
				}
				.tcalendar td {
					border: 1px solid #ccc;
				}

				.tcalendar th {
					text-align: center;
					background-color: #f5f5f5;
					background-color: #f5f5f5;
					padding: 3px 4px;
				}
				.tcalendar td {
					background-color: #fff;
					padding: 0;
				}
				.tcalendar td.padded {
					padding: 2px 6px;
					border: 1px solid #ccc;
				}
				td.padded { }
				.tdbar {
					background-color: #a3d1d9;
				}
				
				tr.active td {
					background-color: #fefeea;
				}
				.color555 {
					color: #555;
				}
				tr.selected td {
					background-color: #fdfdde;
				}
				td.tcalendarlinks {
					padding: 0.5em 0;
					border-bottom: none;
					border-left: none;
					border-right: none;
				}
				td.tcalendarlinks span { color: #888; }
				td.tcalendarlinks span a { color: #888; }
			</style>
			<p>										
				<gd:ganttDiagram ganttDiagram="ganttDiagramByMonth" eventParameter="entryMonthID" eventUrl="<%= "/personnelSection/viewEmployeeAssiduousness.do?method=showJustifications&amp;month=" + month + "&amp;year=" + year + "&amp;employeeNumber=" + employeeNumber %>" showPeriod="false" showObservations="false" bundle="ASSIDUOUSNESS_RESOURCES"/>
			</p>
		</logic:present>
	</logic:present>
</logic:notEmpty>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" property="message">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>