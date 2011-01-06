<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%-- 
<%@ page import="net.sourceforge.fenixedu.domain.accounting.report." %>
--%>

<html:xhtml />
<logic:present role="MANAGER">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.gratuity.reports" /></h2>

	<fr:form action="/gratuityReports.do?method=generateReport">
		<fr:edit id="gratuity.report.bean" name="gratuityReportBean" visible="false" />

		<%-- 		
		<fr:edit id="gratuity.report.bean.type" name="gratuityReportBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.accounting.reports.GratuityReportsDA$GratuityReportBean">
				<fr:slot name="type" key="label.gratuity.report.type" layout="menu-select-postback">
					<fr:property name="destination" value="postback" />
				</fr:slot>
			</fr:schema>
			
			<fr:destination name="postback" path="/gratuityReports.do?method=listReports" />
		</fr:edit>
		
		<logic:equal name="" property="" value="<%=  %>">
		</logic:equal>
		--%>
		
		<fr:edit id="gratuity.report.bean.execution.year" name="gratuityReportBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.accounting.reports.GratuityReportsDA$GratuityReportBean">
				<fr:slot name="executionYear" key="label.gratuity.report.executionYear" layout="menu-select" >
					<fr:property name="providerClass"
						value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
				<fr:property name="columnClasses" value=",acenter,aright,aright" />			
			</fr:layout>
		</fr:edit>
		
		<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
		
	</fr:form>
	
</logic:present>