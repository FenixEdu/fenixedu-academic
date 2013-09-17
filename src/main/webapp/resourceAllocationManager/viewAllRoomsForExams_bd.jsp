<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<logic:present name="<%= PresentationConstants.INFO_EXAMS_MAP_LIST%>">
	<logic:iterate id="infoExamsMap" name="<%= PresentationConstants.INFO_EXAMS_MAP_LIST%>">
	<h2 class="break-before"><bean:message key="title.view.room"/></h2>
	<br />
			<table width="100%">
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
                <tr>
					<td class="listClasses">
						<bean:write name="infoExamsMap" property="infoRoom.nome"/>
					</td>
                    <td class="listClasses">
                        <bean:write name="infoExamsMap" property="infoRoom.tipo"/>
                    </td>
					<td class="listClasses">
						<bean:write name="infoExamsMap" property="infoRoom.edificio"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoExamsMap" property="infoRoom.piso"/>
					</td>
                    <td class="listClasses">
                         <bean:write name="infoExamsMap" property="infoRoom.capacidadeNormal"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="infoExamsMap" property="infoRoom.capacidadeExame"/>
                    </td>
                </tr>
            </table>
		<br />
		<br />	
		<app:generateExamsMap name="infoExamsMap" user="sop"/>
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%= PresentationConstants.INFO_EXAMS_MAP_LIST%>">
	<h2><bean:message key="title.view.room"/></h2>
	<br />
	<table align="center">
		<tr>
			<td>
				<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
			</td>
		</tr>
	</table>
</logic:notPresent>