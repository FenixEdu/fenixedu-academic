<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="label.generateFiles"/></h2>
<span class="error"><html:errors/></span>

<html:form action="/generateFiles">  
	<html:hidden property="method" value="generateGratuityFile" />
	<html:hidden property="page" value="1" />
	
	<bean:define id="fileToGenerate" name="file"/>
	<html:hidden property="file" value="<%=fileToGenerate.toString() %>" />
	<table>
		<tr>
			<td>
				<bean:message key="label.masterDegree.gratuity.executionYear"/>
			</td>
			<td>
				<html:select property="executionYear">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message key="label.manager.executionCourseManagement.select"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
			</td>
		</tr>	
	</table>
	<p />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.generateFiles.generate"/>
	</html:submit>
</html:form>