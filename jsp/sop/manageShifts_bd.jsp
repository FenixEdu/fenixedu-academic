<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.List"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
     	</td>
    </tr>
</table>
<br />
<h2><bean:message key="title.manage.turnos"/></h2>
<br />
<span class="error"><html:errors/></span>

<jsp:include page="createShift.jsp"/>

<br />
<br />
<br />
<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="property.executionCourse"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="property.shift.name"/>
			</td>
	        <td class="listClasses-header">
	        	<bean:message key="property.shift.type"/>
	        </td>
			<td class="listClasses-header">
				<bean:message key="property.lessons"/>
			</td>
			<td class="listClasses-header">
	        	<bean:message key="property.shift.capacity"/>
	        </td>
		</tr>
		<logic:iterate id="infoShift" name="<%= SessionConstants.SHIFTS %>">
			<tr align="center">
				<td class="listClasses">
					<bean:write name="infoShift" property="infoDisciplinaExecucao.sigla"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoShift" property="nome"/>
				</td>
              	<td class="listClasses">
              		<bean:write name="infoShift" property="tipo"/>
              	</td>
              	<td class="listClasses">
					<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
						<bean:write name="infoLesson" property="diaSemana"/> 
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="inicio.timeInMillis"/>
						</dt:format> -
						<dt:format pattern="HH:mm">
							<bean:write name="infoLesson" property="fim.timeInMillis"/>
						</dt:format>
						<bean:write name="infoLesson" property="infoSala.nome"/>;
			        </logic:iterate>      		
              	</td> 
              	<td class="listClasses">
              		<bean:write name="infoShift" property="lotacao"/>
              	</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.SHIFTS %>" scope="request">
	<span class="error"><bean:message key="errors.shifts.none"/></span>	
</logic:notPresent>
