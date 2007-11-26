<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="DIRECTIVE_COUNCIL,DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="DIRECTIVE_COUNCIL" /></em>
	<h2><bean:message key="link.summaries.control"/></h2>
		
	<p><em><!-- Error messages go here --><html:errors /></em></p>
	<html:messages id="message" message="true" bundle="DEFAULT">
		<p>
			<em><!-- Error messages go here -->
				<bean:write name="message"/>
			</em>
		</p>
	</html:messages>
	
	<html:form action="/summariesControl">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listSummariesControl"/>
	
		<table class="tstyle5 mvert05">
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.department" property="department" onchange="this.form.method.value='listExecutionPeriods';this.form.submit()">
					<html:option key="choose.department" value=""/>
					<html:options collection="departments" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		
			<logic:notEmpty name="executionPeriods">
				<tr>
					<td>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod" onchange="this.form.submit()">
								<html:option key="choose.execution.period" value=""/>
								<html:options collection="executionPeriods" property="value" labelProperty="label"/>
						</html:select>
						<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
					</td>
				</tr>		
			</logic:notEmpty>
		</table>		
		
		
		<logic:present name="listElements">

			<bean:define id="url" type="java.lang.String">/summariesControl.do?method=exportToExcel&department=<bean:write name="summariesControlForm" property="department"/>&executionPeriod=<bean:write name="summariesControlForm" property="executionPeriod"/></bean:define> 						
			<bean:define id="url2" type="java.lang.String">/summariesControl.do?method=exportToCSV&department=<bean:write name="summariesControlForm" property="department"/>&executionPeriod=<bean:write name="summariesControlForm" property="executionPeriod"/></bean:define> 						
			
			<p class="mvert15">
			<html:link page="<%= url %>">
				<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
				<bean:message key="link.export.to.excel"/>						
			</html:link>
			
			&nbsp;
			
			<html:link page="<%= url2 %>">			
				<html:img border="0" src="<%= request.getContextPath() + "/images/icon_csv.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
				<bean:message key="link.export.to.csv"/>						
			</html:link>
			</p>
			

			
			<%
				String sortCriteria = request.getParameter("sortBy");
			
				if (sortCriteria == null) {
				    sortCriteria = "teacherNumber=ascending";
				}
			%>
			
			<bean:define id="department" type="java.lang.String" name="department"/>
			<bean:define id="executionPeriod" type="java.lang.String" name="executionPeriod"/>
			
			<p>
				<fr:view name="listElements" schema="summaries.control.list">
					<fr:layout name="tabular-sortable">
						<fr:property name="classes" value="tstyle4 thsmalltxt mtop05"/>
						<fr:property name="columnClasses" value=",acenter,acenter,,smalltxt,aright,aright,bold aright, aright,bold aright"/>
						<fr:property name="suffixes" value=",,,,,h,h,%,h,%"/>
						<fr:property name="sortUrl" value="<%= "/summariesControl.do?method=listSummariesControl&department=" + department + "&executionPeriod=" + executionPeriod %>"/>
						<fr:property name="sortParameter" value="sortBy"/>
						<fr:property name="sortBy" value="<%= sortCriteria %>"/>
					</fr:layout>
				</fr:view>
			</p>		
			
		</logic:present>
					
	</html:form>
</logic:present>