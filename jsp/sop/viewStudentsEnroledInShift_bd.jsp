<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<br />
<h2>Alunos Inscritos</h2>

<br />
<logic:present name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.number"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.mail"/>
				</td>
			</tr>
			<logic:iterate id="student" name="<%= SessionConstants.STUDENT_LIST %>">
				<tr align="center">
					<td class="listClasses">
						<bean:write name="student" property="number"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.email"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.STUDENT_LIST %>" scope="request">
	<span class="error"><bean:message key="errors.students.none.in.shift"/></span>	
</logic:notPresent>