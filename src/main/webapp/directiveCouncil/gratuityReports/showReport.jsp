<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(DIRECTIVE_COUNCIL)">


	<h2><bean:message bundle="APPLICATION_RESOURCES" key="label.directiveCouncil.gratuityReports" /></h2>
	
	<fr:hasMessages for="reportParameters" type="conversion">
		<p><span class="error0">			
			<fr:message for="reportParameters" show="message"/>
		</span></p>
	</fr:hasMessages>	
	
	<fr:form action="/gratuityReports.do?method=showReport">
		<fr:edit	id="reportParameters"
				 	name="reportParameters" 
					schema="GratuityReportParametersBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:destination name="invalid" path="/gratuityReports.do?method=showReportInvalid"/>
			</fr:layout>
		</fr:edit>
	
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>

	<br/>

	<fr:view name="report" schema="GratuityReport.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>


	<br/>

	<logic:equal name="reportParameters" property="detailed" value="true">	
		<fr:view name="report" property="entries" schema="GratuityReportEntry.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="sortBy" value="date=DESC" />
			</fr:layout>
		</fr:view>
	</logic:equal>


</logic:present>
