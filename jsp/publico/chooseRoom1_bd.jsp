<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="publico.infoRooms" >
<bean:define id="executionLabel" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request" />
  <h2><bean:message key="title.chooseRoom"/></h2>
  <br/>
  <h2><bean:write name="executionLabel" property="name"/> - <bean:write name="executionLabel"  property="infoExecutionYear.year" /></h2>
   <br/>
  <span class="error"><html:errors/></span>	
		<html:form action="/viewRoomNew.do">
	<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD %>" value="<%=""+ pageContext.findAttribute("executionLabel") %>" />
	
			<html:hidden name="roomForm" property="name"/>
			<html:hidden name="roomForm" property="building"/>
			<html:hidden name="roomForm" property="floor"/>
			<html:hidden name="roomForm" property="type"/>
			<html:hidden name="roomForm" property="capacityNormal"/>
			<html:hidden name="roomForm" property="capacityExame"/>

			<table border='0' cellpadding='10' cellspacing='1' width="60%">		
                <tr>
                    <td class="listClasses-header" width="10%">&nbsp;</td>
                    <td class="listClasses-header" width="15%">Nome</td>
                    <td class="listClasses-header" width="10%">Tipo</td>                    
                    <td class="listClasses-header">Capacidade Normal</td>

                </tr>
			
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="infoRoomIndex" >
                <tr>
                    <td class="listClasses">
	                    <html:radio idName="infoRoom" property="roomName" value="nome"/>
                    </td>
                    <td class="listClasses">
	                    <bean:write name="infoRoom" property="nome"/>
                    </td>
                    <td class="listClasses">
	                    <bean:write name="infoRoom" property="tipo"/>
                    </td>
                    
                    <td class="listClasses">
                    	<bean:write name="infoRoom" property="capacidadeNormal"/>
                    </td>
                </tr>
			</logic:iterate>
		</table>
		<br/>
		<bean:define id="infoExecutionPeriodCode" name="objectCode" />
		<html:hidden property="objectCode" value="<%= infoExecutionPeriodCode.toString() %>" />
		<html:hidden  property="method" value="roomViewer" />	
		<html:submit styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</html:form>
	</logic:present>
	
	<logic:notPresent name="publico.infoRooms" >
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.rooms"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>