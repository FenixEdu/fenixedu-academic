<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<logic:present name="publico.infoRooms" >
<bean:define id="executionLabel" name="<%= PresentationConstants.EXECUTION_PERIOD %>" scope="request" />
  <h2><bean:message key="title.chooseRoom"/></h2>
  <br/>
  <h2><bean:write name="executionLabel" property="name"/> - <bean:write name="executionLabel"  property="infoExecutionYear.year" /></h2>
   <br/>
  <span class="error"><!-- Error messages go here --><html:errors /></span>	
		<html:form action="/viewRoomNew.do">
	<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD %>" property="<%= PresentationConstants.EXECUTION_PERIOD %>" value="<%=""+ pageContext.findAttribute("executionLabel") %>" />
	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.name" name="roomForm" property="name"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.building" name="roomForm" property="building"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.floor" name="roomForm" property="floor"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.type" name="roomForm" property="type"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.capacityNormal" name="roomForm" property="capacityNormal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.capacityExame" name="roomForm" property="capacityExame"/>

			<table border='0' cellpadding='10' cellspacing='1' width="60%">		
                <tr>
                    <th class="listClasses-header" width="10%">&nbsp;</th>
                    <th class="listClasses-header" width="15%">Nome</th>
                    <th class="listClasses-header" width="10%">Tipo</th>                    
                    <th class="listClasses-header">Capacidade Normal</th>

                </tr>
			
			<logic:iterate id="infoRoom" name="publico.infoRooms" indexId="infoRoomIndex" >
                <tr>
                    <td class="listClasses">
	                    <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.roomName" idName="infoRoom" property="roomName" value="nome"/>
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
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= infoExecutionPeriodCode.toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"  property="method" value="roomViewer" />	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</html:form>
	</logic:present>
	
	<logic:notPresent name="publico.infoRooms" >
		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.rooms"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>