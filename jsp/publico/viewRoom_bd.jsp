<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>

	<br/>
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.view.room"/> </b> </font></center>
	<br/>

	<logic:present name="publico.infoRoom" scope="session">

            <table align="center" cellspacing="10">
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.name"/>
                    </td>
                    <td align="left" height="40">
                        <bean:write name="publico.infoRoom" property="nome"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.building"/>
                    </td>
                    <td align="left" height="40">
                    	<bean:write name="publico.infoRoom" property="edificio"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.floor"/>
                    </td>
                    <td align="left" height="40">
                        <bean:write name="publico.infoRoom" property="piso"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.type"/>
                    </td>
                    <td align="left" height="40">
                        <bean:write name="publico.infoRoom" property="tipo"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.capacity.normal"/>
                    </td>
                    <td align="left" height="40">
                        <bean:write name="publico.infoRoom" property="capacidadeNormal"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" height="40">
                        <bean:message key="property.room.capacity.exame"/>
                    </td>
                    <td align="left" height="40">
                        <bean:write name="publico.infoRoom" property="capacidadeExame"/>
                    </td>
                </tr>
            </table>

		<br/>
		<center>
		<html:link page="/viewRoomOcupation.do">
			<bean:message key="link.room.occupation"/>
		</html:link>
		</center>

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