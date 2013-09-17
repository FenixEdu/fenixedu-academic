<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="java.util.ArrayList" %>

	<br/>
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.room.occupation"/> </b> </font></center>
	<br/>

	<logic:present name="publico.semester" scope="request">
	<html:form action="/viewRoomOcupation.do">

		<table align="center" border="5" cellpadding='20' cellspacing='10'>
			<bean:define id="i" value="i_index" />
                <tr>
                    <td align="left" height="40">
                        <html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
                        	<bean:message key="message.choose.semester"/>
                        	<br/>
                            <html:options collection="publico.semester" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
		</table>

		<br/>
		<center>
		<html:submit>
			<bean:message key="label.choose"/>
		</html:submit>
		</center>

	</html:form>
	</logic:present>

	<logic:notPresent name="publico.semester" scope="request">
		<table align="center" border='1' cellpadding='10'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.rooms"/> </font>
				</td>
			</tr>
		</table>
	</logic:notPresent>
