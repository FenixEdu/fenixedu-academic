<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="DIRECTIVE_COUNCIL">

	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<html:messages id="message" message="true" bundle="DEFAULT">
		<span class="error">
			<bean:write name="message"/>
		</span>
	</html:messages>
	
	<html:form action="/summariesControl">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listSummariesControl"/>
	
		<p><h2><bean:message key="link.summaries.control"/></h2></p>
				
		<p><html:select bundle="HTMLALT_RESOURCES" altKey="select.department" property="department" onchange="this.form.method.value='listExecutionPeriods';this.form.submit()">
				<html:option key="choose.department" value=""/>
				<html:options collection="departments" property="value" labelProperty="label"/>
			</html:select>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</p>
		
		<logic:notEmpty name="executionPeriods">
			<p><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod" onchange="this.form.submit()">
						<html:option key="choose.execution.period" value=""/>
						<html:options collection="executionPeriods" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</p>		
		</logic:notEmpty>
		
		<logic:present name="listElements">
			<br/>			
			<bean:define id="url" type="java.lang.String">/summariesControl.do?method=exportToExcel&department=<bean:write name="summariesControlForm" property="department"/>&executionPeriod=<bean:write name="summariesControlForm" property="executionPeriod"/></bean:define> 						
			<bean:define id="url2" type="java.lang.String">/summariesControl.do?method=exportToCSV&department=<bean:write name="summariesControlForm" property="department"/>&executionPeriod=<bean:write name="summariesControlForm" property="executionPeriod"/></bean:define> 						
			
			<p><html:link page="<%= url %>">
				<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
				<bean:message key="link.export.to.excel"/>						
			</html:link>
			
			&nbsp;&nbsp;&nbsp;
			
			<html:link page="<%= url2 %>">			
				<html:img border="0" src="<%= request.getContextPath() + "/images/icon_csv.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
				<bean:message key="link.export.to.csv"/>						
			</html:link></p>
			
			<br/>
			
			<%
				String sortCriteria = request.getParameter("sortBy");
			
				if (sortCriteria == null) {
				    sortCriteria = "teacherNumber=ascending";
				}
			%>
			
			<bean:define id="department" type="java.lang.String" name="department"/>
			<bean:define id="executionPeriod" type="java.lang.String" name="executionPeriod"/>
			
			<p><fr:view name="listElements" schema="summaries.control.list">
					<fr:layout name="tabular-sortable">
						<fr:property name="rowClasses" value="listClasses"/>
						<fr:property name="prefixes" value=",,,,,,,<strong>,,<strong>"/>
						<fr:property name="suffixes" value=",,,,,h,h,%</strong>,h,%</strong>"/>
	
						<fr:property name="sortUrl" value="<%= "/summariesControl.do?method=listSummariesControl&department=" + department + "&executionPeriod=" + executionPeriod %>"/>
						<fr:property name="sortParameter" value="sortBy"/>
						<fr:property name="sortBy" value="<%= sortCriteria %>"/>
					</fr:layout>
			</fr:view></p>		
			
		</logic:present>
					
	</html:form>
</logic:present>