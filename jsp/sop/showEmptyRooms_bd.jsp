<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<h2><bean:message key="title.search.result"/></h2>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>Crit&eacute;rios:</p>
			<strong>
				<bean:write name="<%=SessionConstants.INFO_EXECUTION_PERIOD_KEY%>" property="name" scope="session"/>
				-
				<bean:write name="<%=SessionConstants.INFO_EXECUTION_PERIOD_KEY%>" property="infoExecutionYear.year" scope="session"/>
				<br/>
				<bean:write name="weekDay" scope="request"/>,
				das <bean:write name="intervalStart" scope="request"/>
				às <bean:write name="intervalEnd" scope="request"/>
				<br/>
				Capacidade normal mínima de <bean:write name="minimumCapacity" scope="request"/> lugares.
			</strong>
		</td>
	</tr>
</table>
<br/>
   	<table cellpadding="0" border="0" width="100%">
	 	<tr>
	
			<th>
				<bean:message key="property.room.name"/>
			</th>
			<th>
				<bean:message key="property.room.building"/>
			</th>
			<th>
				<bean:message key="property.room.type"/>
			</th>
			<th>
				<bean:message key="property.room.capacity.normal"/>
			</th>
			<th>
				<bean:message key="property.room.capacity.exame"/>
			</th>
		</tr>		
			<logic:iterate id="infoRoom" name="roomList" scope="request">
				<tr>
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="nome"/>
					</td> 
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="edificio"/>
					</td> 
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="tipo"/>
					</td> 
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="capacidadeNormal"/>
					</td> 
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="capacidadeExame"/>
					</td> 
					
				</tr>
			</logic:iterate>
		
		
</table>
