<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.generateFiles"/></h2>
<span class="error"><html:errors/></span>


<html:form action="/generateFiles">  
	<html:hidden property="method" value="generateGratuityFile" />
	<bean:define id="fileToGenerate" name="file"/>
	<html:hidden property="file" value="<%=file.toString() %>" />
	<table>
		<tr>
			<td>
				<bean:message key="label.masterDegree.gratuity.executionYear"/>
			</td>
			<td>
				<html:select property="executionYear" onchange="document.insertGratuityDataForm.method.value='prepareInsertGratuityDataChooseDegree';document.insertGratuityDataForm.submit();">
					<html:option value="" key="label.manager.executionCourseManagement.select">
						<bean:message key="label.manager.executionCourseManagement.select"/>
					</html:option>
					<html:optionsCollection name="executionYears"/>
				</html:select>
			</td>
		</tr>
		
		<logic:equal name="fileToGenerate" value="sibs">
		
		
		</logic:equal>
	</table>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.generateFiles.generate"/>
	</html:submit>
</html:form>