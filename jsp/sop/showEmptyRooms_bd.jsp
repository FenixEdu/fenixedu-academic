<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
    <h2><bean:message key="title.search.result"/></h2>
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
						<jsp:getProperty name="infoRoom" property="capacidadeExame"/>
					</td> 
					<td class="listClasses">
						<jsp:getProperty name="infoRoom" property="capacidadeNormal"/>
					</td> 
					
				</tr>
			</logic:iterate>
		
		
</table>
