<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<h2><bean:message key="title.search.result"/></h2>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>Crit&eacute;rios:</p>
			<strong>
<!--
				<bean:write name="<%=SessionConstants.EXECUTION_PERIOD%>" property="name" scope="request"/>
				-
				<bean:write name="<%=SessionConstants.EXECUTION_PERIOD%>" property="infoExecutionYear.year" scope="request"/>
				<br/>
-->
				Entre <bean:write name="startDate" scope="request"/> e 
				<bean:write name="endDate" scope="request"/> 
				<br/>
				<bean:write name="weekDay" scope="request"/>,
				das <bean:write name="intervalStart" scope="request"/>
				�s <bean:write name="intervalEnd" scope="request"/>
				<br/>
				Capacidade normal m�nima de <bean:write name="minimumCapacity" scope="request"/> lugares.
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
