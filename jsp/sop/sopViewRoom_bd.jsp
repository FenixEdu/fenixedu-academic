<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>  
<h2><bean:message key="title.view.room"/></h2>
<br />
<html:form action="/viewRoom">
	<bean:define id="infoRoomOID" name="<%= SessionConstants.ROOM%>" property="idInternal" scope="request"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
	<html:hidden alt="<%= SessionConstants.ROOM_OID%>" property="<%= SessionConstants.ROOM_OID%>" value="<%=infoRoomOID.toString()%>"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />				
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="label.manager.executionPeriod"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.selectedExecutionPeriodOID" property="selectedExecutionPeriodOID" size="1"
		        		onchange="this.form.indexWeek.value='0';this.form.submit();">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.week"/>:</td>
		    <td nowrap="nowrap">
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.indexWeek" property="indexWeek" size="1" onchange="this.form.submit();">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form> 
<br />
<logic:present name="<%= SessionConstants.ROOM%>" scope="request">
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