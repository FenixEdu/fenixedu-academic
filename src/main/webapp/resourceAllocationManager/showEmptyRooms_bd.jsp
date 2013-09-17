<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<h2><bean:message key="title.search.result"/></h2>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>Crit&eacute;rios:</p>
			<strong>
<!--
				<bean:write name="<%=PresentationConstants.EXECUTION_PERIOD%>" property="name" scope="request"/>
				-
				<bean:write name="<%=PresentationConstants.EXECUTION_PERIOD%>" property="infoExecutionYear.year" scope="request"/>
				<br/>
-->
				Entre <bean:write name="startDate" scope="request"/> e 
				<bean:write name="endDate" scope="request"/> 
				<br/>
				<bean:write name="weekDay" scope="request"/>,
				das <bean:write name="intervalStart" scope="request"/>
				às <bean:write name="intervalEnd" scope="request"/>
				<br/>
				<logic:notEmpty name="minimumCapacity">
					Capacidade normal mínima de <bean:write name="minimumCapacity" scope="request"/> lugares.
				</logic:notEmpty>
			</strong>
		</td>
	</tr>
</table>
<br />
<table cellpadding="0" border="0" width="100%">
	 <tr>
		<th class="listClasses-header"><bean:message key="property.room.name"/></th>
		<th class="listClasses-header"><bean:message key="property.room.type"/></th>
		<th class="listClasses-header"><bean:message key="property.room.building"/></th>
		<th class="listClasses-header"><bean:message key="property.room.capacity.normal"/></th>
		<th class="listClasses-header"><bean:message key="property.room.capacity.exame"/></th>
	</tr>		
<logic:iterate id="infoRoom" name="roomList" scope="request">
	<tr>
		<td class="listClasses"><jsp:getProperty name="infoRoom" property="nome"/></td> 
	  	<td class="listClasses"><jsp:getProperty name="infoRoom" property="tipo"/></td> 
	  	<td class="listClasses"><jsp:getProperty name="infoRoom" property="edificio"/></td> 
		<td class="listClasses"><jsp:getProperty name="infoRoom" property="capacidadeNormal"/></td> 
		<td class="listClasses"><jsp:getProperty name="infoRoom" property="capacidadeExame"/></td> 
	</tr>
</logic:iterate>
</table>
