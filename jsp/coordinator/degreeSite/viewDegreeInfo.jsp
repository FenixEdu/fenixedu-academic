<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.coordinator.degreeSite.edit"/></h2>

<html:form action="/degreeSiteManagement">
	<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
	<html:hidden property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
	
	<bean:define id="infoDegreeInfoId" name="infoDegreeInfoId"/>
	<html:hidden property="infoDegreeInfoId" value="<%=  infoDegreeInfoId.toString() %>"/>

	<html:hidden property="method" value="editDegreeInfomation" />

	<table>	
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.objectives"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="objectives" cols="80" rows="8"/></td>
				</tr>
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.history"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="history" cols="80" rows="8"/></td>
				</tr>		

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.professionalExits"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="professionalExits" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="additionalInfo" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="links" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.testIngression"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="testIngression" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.driftsInitial"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="driftsInitial" size="5"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.driftsFirst"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="driftsFirst" size="5"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.driftsSecond"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="driftsSecond" size="5"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.classifications"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="classifications" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.markMin"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="markMin" size="5"/></td>
				</tr>																	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.markMax"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="markMax" size="5"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.markAverage"/></strong></td>
				</tr>
				<tr>
					<td><html:text property="markAverage" size="5"/></td>
				</tr>																																															
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>                    		         	
	</html:submit>       
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>  
	
</html:form> 