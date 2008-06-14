<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="fileToGenerate" name="file"/>
<p>
<b>
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.generating" />
	<logic:equal name="fileToGenerate" value="sibs">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.SIBS" />
	</logic:equal>
	<logic:equal name="fileToGenerate" value="letters">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.letters" />
	</logic:equal>
</b>
<p>
<html:form action="/generateFiles">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="generateGratuityFile" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.file" property="file" value="<%=fileToGenerate.toString() %>" />
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.executionYear"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.select"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.paymentEndDate"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.paymentEndDate" property="paymentEndDate" />
			</td>
		</tr>
	</table>
	<p />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.generateFiles.generate"/>
	</html:submit>
</html:form>