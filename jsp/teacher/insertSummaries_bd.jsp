<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><html:errors/></span>
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>


<html:form action="/summariesManager">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertSummary"/>
	<html:hidden property="objectCode"/>
	<table>
	
	<tr>
		<td><bean:message key="label.summaryDate"/><bean:message key="message.dateFormat"/>
			</td>
	</tr>
	<tr>
		<td><html:text property="summaryDate"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryHour"/><bean:message key="message.hourFormat"/>
			</td>
	</tr>
	<tr>
		<td><html:text property="summaryHour"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryType"/>
			</td>
	</tr>
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
		<td><html:text size="66" property="title"/></td>
	<tr/>
	<tr>
		<td><bean:message key="label.summaryText"/>
			</td>
	</tr>
	<tr>
		<td><html:textarea rows="7" cols="50" property="summaryText"/></td>
	<tr/>
	

</table>
<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
