<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<br/>

<span class="error"><html:errors/></span>

<html:form action="/chooseDegreesForExecutionCourseMerge" >
	    
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseDegreesAndExecutionPeriod"/>
	<br/>
	<strong>Escolha o Período Execução</strong>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId">
		<logic:iterate id="executionPeriod" name="executionPeriods">
			<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>
			<html:option value="<%= executionPeriodId.toString() %>">
				<bean:write name="executionPeriod" property="name"/>-<bean:write name="executionPeriod" property="infoExecutionYear.year"/>
			</html:option>
		</logic:iterate>
	</html:select>
	
	<br/>
	<br/>
	<table>
	<tr>
	<td>
	<strong>Escolha a Licenciatura de Origem</strong>
	<br/>
	<br/>
	<table>
	<logic:iterate id="degree" name="sourceDegrees">
		<tr>
			<td class="listClasses">
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sourceDegreeId" property="sourceDegreeId" idName="degree" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="degree" property="sigla"/>
			</td>
			<td class="listClasses" style="text-align:left">
			<bean:write name="degree" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td>
	<td>
	
	<strong>Escolha a Licenciatura de Destino</strong>
	<br/>
	<br/>
	<table>
	<logic:iterate id="degree" name="destinationDegrees">
		<tr>
			<td class="listClasses">
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.destinationDegreeId" property="destinationDegreeId" idName="degree" value="idInternal"/> 
			</td>
			<td class="listClasses">
			<bean:write name="degree" property="sigla"/>
			</td>
			<td class="listClasses" style="text-align:left">
			<bean:write name="degree" property="nome"/>
			</td>
		</tr>
	</logic:iterate>
	</table>
	</td></tr></table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>