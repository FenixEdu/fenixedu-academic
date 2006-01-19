<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<span class="error"><html:errors/></span>

<html:form action="/summariesControl">

	<html:hidden property="method" value="listSummariesControl"/>

	<p><h2><bean:message key="link.summaries.control"/></h2></p>
			
	<p><html:select property="department" onchange="this.form.method.value='listExecutionPeriods';this.form.submit()">
			<html:option key="choose.department" value=""/>
			<html:options collection="departments" property="value" labelProperty="label"/>
	</html:select></p>
	
	<logic:notEmpty name="executionPeriods">
		<p><html:select property="executionPeriod" onchange="this.form.submit()">
					<html:option key="choose.execution.period" value=""/>
					<html:options collection="executionPeriods" property="value" labelProperty="label"/>
		</html:select></p>		
	</logic:notEmpty>
	
	<br/>
	
	<logic:present name="listElements">		
		<p><fr:view name="listElements" schema="summaries.control.list">
				<fr:layout name="tabular">
					<fr:property name="rowClasses" value="listClasses"/>
					<fr:property name="prefixes" value=",,,,,,,<strong>"/>
					<fr:property name="suffixes" value=",,,,h,h,h,%</strong>"/>
				</fr:layout>
		</fr:view></p>	
	</logic:present>	
				
</html:form>