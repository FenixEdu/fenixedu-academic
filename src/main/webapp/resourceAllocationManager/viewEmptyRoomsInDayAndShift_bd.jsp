<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="title.search.result.roomsWithoutExams"/></h2>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>Crit&eacute;rios:</p>
			<strong>
				<bean:write name="dateAndTime" scope="request"/>
			</strong>
		</td>
	</tr>
</table>
<br />
<table width="98%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <th class="listClasses-header">
                        <bean:message key="property.room.name"/>
                    </th>
					<th class="listClasses-header">
						<bean:message key="property.room.type"/>
					</th>
                    <th class="listClasses-header">
                        <bean:message key="property.room.building"/>
                    </th>
                    <th class="listClasses-header">
                        <bean:message key="property.room.floor"/>
                    </th>
					<th class="listClasses-header">
						<bean:message key="property.room.capacity.normal"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="property.room.capacity.exame"/>
					</th>
                </tr>
	<logic:iterate id="infoRoom" name="<%= PresentationConstants.INFO_EMPTY_ROOMS_KEY %>" scope="request">
                <tr>
					<td class="listClasses">
						<bean:write name="infoRoom" property="nome"/>
					</td>
                    <td class="listClasses">
                        <bean:write name="infoRoom" property="tipo"/>
                    </td>
					<td class="listClasses">
						<bean:write name="infoRoom" property="edificio"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoRoom" property="piso"/>
					</td>
                    <td class="listClasses">
                         <bean:write name="infoRoom" property="capacidadeNormal"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="infoRoom" property="capacidadeExame"/>
                    </td>
                </tr>
	</logic:iterate>
</table>