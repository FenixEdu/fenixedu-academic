<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<logic:present name="<%= SessionConstants.INFO_EXAMS_MAP_LIST%>">
	<logic:iterate id="infoExamsMap" name="<%= SessionConstants.INFO_EXAMS_MAP_LIST%>">
	<h2><bean:message key="title.view.room"/></h2>
	<br />
			<table width="100%">
                <tr>
                    <td class="listClasses-header">
                        <bean:message key="property.room.name"/>
                    </td>
					<td class="listClasses-header">
						<bean:message key="property.room.type"/>
					</td>
                    <td class="listClasses-header">
                        <bean:message key="property.room.building"/>
                    </td>
                    <td class="listClasses-header">
                        <bean:message key="property.room.floor"/>
                    </td>
					<td class="listClasses-header">
						<bean:message key="property.room.capacity.normal"/>
					</td>
					<td class="listClasses-header">
						<bean:message key="property.room.capacity.exame"/>
					</td>
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
		<br style="page-break-after:always;" />
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%= SessionConstants.INFO_EXAMS_MAP_LIST%>">
	<h2><bean:message key="title.view.room"/></h2>
	<br />
	<table align="center">
		<tr>
			<td>
				<span class="error"><bean:message key="message.public.notfound.room"/></span>
			</td>
		</tr>
	</table>
</logic:notPresent>