<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<h2><bean:message key="title.view.room"/></h2>
<br />
<html:form action="/chooseExecutionPeriod">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
		    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
		    <td nowrap="nowrap"><jsp:include page="selectExecutionPeriodList.jsp"/></td>
			<td width="10"></td>
			<td>
				<html:hidden property="method" value="chooseForViewRoom"/>
				<html:hidden property="page" value="1"/>
			    <html:submit styleClass="inputbutton"><bean:message key="label.choose"/>
          		</html:submit>
			</td>
		</tr>
	</table>
</html:form> 
<br />
<logic:present name="publico.infoRoom" scope="request">
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
						<bean:write name="publico.infoRoom" property="nome"/>
					</td>
                    <td class="listClasses">
                        <bean:write name="publico.infoRoom" property="tipo"/>
                    </td>
					<td class="listClasses">
						<bean:write name="publico.infoRoom" property="edificio"/>
					</td>
					<td class="listClasses">
						<bean:write name="publico.infoRoom" property="piso"/>
					</td>
                    <td class="listClasses">
                         <bean:write name="publico.infoRoom" property="capacidadeNormal"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="publico.infoRoom" property="capacidadeExame"/>
                    </td>
                </tr>
            </table>
	<br />
	<br />	
	<div align="center"><app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"/></div>
	</logic:present>
	<logic:notPresent name="publico.infoRoom" scope="request">
		<table align="center">
			<tr>
				<td>
					<span class="error"><bean:message key="message.public.notfound.room"/></span>
				</td>
			</tr>
		</table>
	</logic:notPresent>