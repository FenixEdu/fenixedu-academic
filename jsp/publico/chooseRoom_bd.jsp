<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<h2><bean:message key="title.chooseRoom"/></h2>
<br/>
	<logic:present name="publico.infoRooms" scope="session">
	
			
		<html:form action="/viewRoom.do">
		<bean:define id="exePeriodName" name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" property="name" type="java.lang.String" />
		<bean:define id="exeYear" name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" property="infoExecutionYear"/>		
		<bean:define id="exeYearName" name="exeYear" property="year"/>			
		<table border='0' cellpadding='10' cellspacing='0'>		
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="i_index">
			<bean:define id="i" value="i_index" />
                <tr>
                    <td>
	                    <html:radio  property="index" value="<%= String.valueOf(i_index) %>" />
                    </td>
                    <td>
	                    <bean:write name="infoRoom" property="nome"/>
                    </td>
                </tr>
			</logic:iterate>
		</table>
		<br/>
		<html:hidden  property="ePName" value="<%= pageContext.findAttribute("exePeriodName").toString() %>" />
		<html:hidden  property="eYName" value="<%= pageContext.findAttribute("exeYearName").toString() %>" /> 
		<html:submit styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</html:form>
	</logic:present>
	<logic:notPresent name="publico.infoRooms" scope="session">
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.rooms"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>