<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>

	<br/>
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.chooseRoom"/> </b> </font></center>
	<br/>

	<logic:present name="publico.infoRooms" scope="session">
	<html:form action="/viewRoom.do">

		<table align="center" border='1' cellpadding='10' cellspacing='1'>		
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="i_index">

			<bean:define id="i" value="i_index" />
                <tr>
                    <td align="right" height="40">
	                    <html:radio  property="index" value="<%= String.valueOf(i_index) %>" />
                    </td>
                    <td align="left" height="40">
	                    <bean:write name="infoRoom" property="nome"/>
                    </td>
                </tr>

			</logic:iterate>
		</table>

		<br/>
		<center>
		<html:submit>
			<bean:message key="label.choose"/>
		</html:submit>
		</center>

	</html:form>
	</logic:present>

	<logic:notPresent name="publico.infoRooms" scope="session">
		<table align="center" border='1' cellpadding='10'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.rooms"/> </font>
				</td>
			</tr>
		</table>
	</logic:notPresent>
