<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="infoSummary" name="component" property="infoSummary"/>
<bean:define id="summaryCode" name="infoSummary" property="idInternal"/>
<span class="error"><html:errors/></span>
<html:form action="/summariesManager1">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="editSummary"/>
	<html:hidden property="objectCode"/>
	<html:hidden  property="summaryCode" value="<%= summaryCode.toString() %>"/>
	<table>
	
	<tr>
		<td><bean:message key="label.summaryDate"/><bean:message key="message.dateFormat"/>
			</td>
	</tr>
	<tr>
		<td><html:text name="infoSummary" property="summaryDateFormatted"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryHour"/><bean:message key="message.hourFormat"/>
			</td>
	</tr>
	<tr>
		<td><html:text name="infoSummary" property="summaryHourFormatted"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryType"/>
			</td>
	</tr>
	<bean:define id="summaryTypeValue" name="summaryTypeValue"/>
	<tr>
		<td><html:select property="summaryType">
			<html:options name="lessonTypeValues" labelName="lessonTypeNames"/>
		</html:select></td>
	<tr/>
	<tr>
		<td><bean:message key="label.title"/>
		</td>
	</tr>
	<tr>
		<td><html:text name="infoSummary" size="66" property="title"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryText"/>
			</td>
	</tr>
	<tr>
		<td><html:textarea name="infoSummary" rows="7" cols="50" property="summaryText"/></td>
	<tr/>
	

</table>
<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
