<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>

	<br/>
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.view.room"/> </b> </font></center>
	<br/>

	<logic:present name="publico.infoRoom" scope="session">

            <table cellspacing="0" cellpadding="0" width="100%">
                <tr class="timeTable_line">
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.name"/>: <bean:write name="publico.infoRoom" property="nome"/></b>
                    </td>
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.building"/>: <bean:write name="publico.infoRoom" property="edificio"/></b>
                    </td>
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.floor"/>: <bean:write name="publico.infoRoom" property="piso"/></b>
                    </td>
                </tr>
                <tr class="timeTable_line">
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.type"/>: <bean:write name="publico.infoRoom" property="tipo"/></b>
                    </td>
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.capacity.normal"/>: <bean:write name="publico.infoRoom" property="capacidadeNormal"/></b>
                    </td>
                    <td class="horariosHoras_first">
                        <b><bean:message key="property.room.capacity.exame"/>: <bean:write name="publico.infoRoom" property="capacidadeExame"/></b>
                    </td>
                </tr>
            </table>

		<br/>
		<br/>

	   	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.ROOM_TIMEBABLE %>"/> 

	</logic:present>

	<logic:notPresent name="publico.infoRoom" scope="session">
		<table align="center" border='1' cellpadding='10'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.room"/> </font>
				</td>
			</tr>
		</table>
	</logic:notPresent>