<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exams" name="component" property="infoExams" />

<bean:define id="hasPublishedMarks">false</bean:define>
<logic:iterate id="exam" name="exams">
	<logic:notEmpty name="exam" property="publishmentMessage">
		<bean:define id="hasPublishedMarks">true</bean:define>
	</logic:notEmpty>	
</logic:iterate>

<table width="90%" align="center">
<tr>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>
	
	<logic:equal name="hasPublishedMarks" value="true">
		<td class="listClasses-header" ><bean:message key="label.publishedMarks"/></td>
	</logic:equal>
	
	
</tr>	
<logic:iterate id="exam" name="exams">
		
	<tr>
		<td class="listClasses"><bean:write name="exam" property="season"/></td>		
		<td class="listClasses"><bean:write name="exam" property="date"/></td>
		<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
		
		<logic:equal name="hasPublishedMarks" value="true">
			<logic:notEmpty name="exam" property="publishmentMessage">
				<bean:define id="examCode" name="exam" property="idInternal"/>
				<td class="listClasses" >
					<html:link page="<%= "/viewPublishedMarks.do?method=viewPublishedMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + examCode %>">
						<bean:message key="link.view" />
					</html:link>
				</td>
			</logic:notEmpty>
			<logic:empty name="exam" property="publishmentMessage">
				<td class="listClasses" ></td>
			</logic:empty>
		</logic:equal>	
	</tr>
	
</logic:iterate>
</table>