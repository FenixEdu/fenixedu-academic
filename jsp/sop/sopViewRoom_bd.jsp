<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<span class="error"><html:errors/></span>  
<h2><bean:message key="title.view.room"/></h2>
<br />
<html:form action="/viewRoom">
	<table border="0" cellspacing="0" cellpadding="0">
<!--
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
		    <td nowrap="nowrap"><jsp:include page="selectExecutionPeriodList.jsp"/></td>
			<td width="10"></td>
			<td>
				<bean:define id="infoRoomOID" name="<%= SessionConstants.ROOM%>" property="idInternal" scope="request"/>
				<html:hidden property="method" value="chooseForViewRoom"/>
				<html:hidden property="<%= SessionConstants.ROOM_OID%>" value="<%=infoRoomOID.toString()%>"/>
				<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />				
				<html:hidden property="page" value="1"/>
			    <html:submit styleClass="inputbutton"><bean:message key="label.choose"/>
          		</html:submit>
			</td>
		</tr>
-->
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.week"/>:</td>
		    <td nowrap="nowrap">
		        <html:select property="indexWeek" size="1">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
			</td>
			<td width="10"></td>
			<td>
				<bean:define id="infoRoomOID" name="<%= SessionConstants.ROOM%>" property="idInternal" scope="request"/>
				<html:hidden property="method" value="execute"/>
				<html:hidden property="<%= SessionConstants.ROOM_OID%>" value="<%=infoRoomOID.toString()%>"/>
				<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />				
				<html:hidden property="page" value="1"/>
			    <html:submit styleClass="inputbutton"><bean:message key="label.choose"/>
          		</html:submit>
			</td>
		</tr>
	</table>
</html:form> 
<br />
<logic:present name="<%= SessionConstants.ROOM%>" scope="request">
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
						<bean:write name="<%= SessionConstants.ROOM%>" property="nome"/>
					</td>
                    <td class="listClasses">
                        <bean:write name="<%= SessionConstants.ROOM%>" property="tipo"/>
                    </td>
					<td class="listClasses">
						<bean:write name="<%= SessionConstants.ROOM%>" property="edificio"/>
					</td>
					<td class="listClasses">
						<bean:write name="<%= SessionConstants.ROOM%>" property="piso"/>
					</td>
                    <td class="listClasses">
                         <bean:write name="<%= SessionConstants.ROOM%>" property="capacidadeNormal"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="<%= SessionConstants.ROOM%>" property="capacidadeExame"/>
                    </td>
                </tr>
            </table>
	<br />
	<br />	
	<div align="center">
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"
		/>
	</div>
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.ROOM%>" scope="request">
		<table align="center">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>