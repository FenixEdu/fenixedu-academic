<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
  	</tr>
</table>
<h2><bean:message key="title.editTurno"/></h2>
<br />
<span class="error"><html:errors/></span>

<jsp:include page="editShift.jsp"/>

<br />
<br />
Aulas
<br />
	<logic:present name="shift" property="infoLessons">
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="property.weekday"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.time.start"/>
				</td>
		        <td class="listClasses-header">
		        	<bean:message key="property.time.end"/>
	    	    </td>
				<td class="listClasses-header">
					<bean:message key="property.room"/>
				</td>
				<td class="listClasses-header">
		        	<bean:message key="property.capacity"/>
		        </td>
				<td class="listClasses-header">
		        </td>
				<td class="listClasses-header">
		        </td>
			</tr>
			<logic:iterate id="lesson" name="shift" property="infoLessons">
				<tr align="center">
					<td class="listClasses">
						<bean:write name="lesson" property="diaSemana"/>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td class="listClasses">
						<bean:write name="lesson" property="infoSala.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="lesson" property="infoSala.capacidadeNormal"/>
					</td>
					<td class="listClasses">
					</td>
					<td class="listClasses">
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="shift" property="infoLessons">
		<span class="error">
			<bean:message key="message.shift.lessons.none"/>
		</span>
	</logic:notPresent>

<br />
Listar aqui as turmas a que pertence.