<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><strong><bean:message key="label.reports.payments.by.year.and.month" bundle="MANAGER_RESOURCES"/></strong></h2>

<fr:form action="/sibsReports.do?method=reportByYearAndMonth">
	<fr:edit id="reportBean" name="reportBean" schema="SibsPaymentsReportBean.search" >
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="label.reports.generate" bundle="MANAGER_RESOURCES"/></html:submit>	
</fr:form>
