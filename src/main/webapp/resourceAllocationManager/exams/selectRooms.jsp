<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


<html:form action="/roomExamSearch">
		
<h2>Selecionar Salas</h2>

Preencher apenas os campos necessï¿?rios. <br/>
Se quiser ver todas as salas não preencha nenhum.
<%-- clique 
<html:link page="<%= "/roomExamSearch.do?method=search" %>">aqui</html:link>. 
--%>
<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID"/> 

<table class="tstyle5 thlight thright">
	<tr>
		<th><bean:message key="property.room.name"/></th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" maxlength="20" size="20" property="name"/></td>
	</tr>
	<tr>
		<th><bean:message key="property.room.building"/></th>
		<td>
		   	<html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building">
		       	<option value="" selected="selected"></option>           
			    <html:options collection="public.buildings" property="value" labelProperty="label"/>                  	             
		    </html:select>
		</td>
	</tr>
	<tr>
		<th><bean:message key="property.room.floor"/></th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" maxlength="2" size="2" property="floor"/></td>
	</tr>
	<tr>
		<th><bean:message key="property.room.type"/></th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type">
			    <html:options collection="public.types" property="value" labelProperty="label"/>                  	             
		    </html:select>
	</tr>
	<tr>
		<th><bean:message key="property.room.capacity.normal"/></th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.normal" maxlength="3" size="3" property="normal"/></td>
	</tr>
	<tr>
		<th><bean:message key="property.room.capacity.exame"/></th>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.exam" maxlength="3" size="3" property="exam"/></td>
	</tr>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>
</p>

<%--	
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>
--%>
</html:form>	