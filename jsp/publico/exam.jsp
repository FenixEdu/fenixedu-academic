<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>  
<%@ page import="Util.EvaluationType" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="evaluations" name="component" property="infoEvaluations" />
<bean:define id="hasPublishedMarks">false</bean:define>

<h2><bean:message key="title.evaluation" /></h2>

<logic:iterate id="evaluation" name="evaluations">
	<logic:present name="evaluation" property="publishmentMessage">
		<bean:define id="hasPublishedMarks">true</bean:define>
	</logic:present>	
</logic:iterate>


<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th><bean:message key="label.evaluationType"/></th>
		<th><bean:message key="label.season"/></th>
		<th><bean:message key="label.day"/></th>
		<th><bean:message key="label.beginning"/></th>	
		<logic:equal name="hasPublishedMarks" value="true">
			<th><bean:message key="label.publishedMarks"/></th>
		</logic:equal>
	</tr>	
	<logic:iterate id="evaluation" name="evaluations">
	<tr>
		<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
			<td><bean:write name="evaluation" property="evaluationType"/></td>
			<td><bean:write name="evaluation" property="season"/></td>	
			<td><bean:write name="evaluation" property="date"/></td>
			<td><bean:write name="evaluation" property="beginningHour"/></td>
		</logic:equal>
		<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.ONLINE_TEST_STRING %>">
			<td><bean:write name="evaluation" property="infoDistributedTest.title"/></td>
			<td>&nbsp;</td>		
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</logic:equal>
		<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">
			<td><bean:write name="evaluation" property="evaluationType"/></td>				
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</logic:equal>
		<logic:equal name="hasPublishedMarks" value="true">
			<logic:present name="evaluation" property="publishmentMessage">
				<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>
				<td>
					<html:link page="<%= "/viewPublishedMarks.do?method=viewPublishedMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + evaluationCode %>">
						<bean:message key="link.view" />
					</html:link>
				</td>
			</logic:present>
			<logic:notPresent name="evaluation" property="publishmentMessage">
				<td>&nbsp;</td>
			</logic:notPresent>
		</logic:equal>	
	</tr>
</logic:iterate>   
</table>