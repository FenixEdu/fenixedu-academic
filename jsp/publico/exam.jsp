<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>  
<%@ page import="Util.EvaluationType" %>
a entrar
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="evaluations" name="component" property="infoEvaluations" />
<bean:define id="hasPublishedMarks">false</bean:define>
<logic:iterate id="evaluation" name="evaluations">
uma avaliacao
	<logic:notEmpty name="evaluation" property="publishmentMessage">
		<bean:define id="hasPublishedMarks">true</bean:define>
	</logic:notEmpty>	
</logic:iterate>
ja iterou uma vez a lista
<table width="90%" align="center">
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
	<logic:equal name="hasPublishedMarks" value="true">
	tem published mark
		<td class="listClasses-header" ><bean:message key="label.publishedMarks"/></td>
	</logic:equal>
</tr>	
<logic:iterate id="evaluation" name="evaluations">
a iterar a lista
	<logic:notEqual name="evaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">
	<tr>
		<td class="listClasses"><bean:write name="evaluation" property="season"/></td>		
		<td class="listClasses"><bean:write name="evaluation" property="date"/></td>
		<td class="listClasses"><bean:write name="evaluation" property="beginningHour"/></td>
		
		<logic:equal name="hasPublishedMarks" value="true">
			<logic:notEmpty name="evaluation" property="publishmentMessage">
				<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>
				<td class="listClasses" >
					<html:link page="<%= "/viewPublishedMarks.do?method=viewPublishedMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + evaluationCode %>">
						<bean:message key="link.view" />
					</html:link>
				</td>
			</logic:notEmpty>
			<logic:empty name="evaluation" property="publishmentMessage">
				<td class="listClasses" >&nbsp;</td>
			</logic:empty>
		</logic:equal>	
	</tr>
	</logic:notEqual>
</logic:iterate>   
</table>

<%-- add newEvaluation types here --%>