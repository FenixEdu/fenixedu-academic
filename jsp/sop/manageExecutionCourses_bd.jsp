<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>Critérios:</p>
    		<strong><jsp:include page="contextExecutionCourse.jsp"/></strong>
		</td>
	</tr>
</table>

<br />
<h2>Gestão de Disciplinas</h2>

<logic:present name="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>" scope="request">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.code"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.name"/>
			</td>
		</tr>
		<logic:iterate id="executionCourse" name="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>">
			<tr>
				<td class="listClasses">
					<bean:write name="executionCourse" property="sigla"/>
				</td>
				<td class="listClasses">
					<bean:write name="executionCourse" property="nome"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_INFOEXECUTIONDEGREE %>" scope="request">
	<span class="error"><bean:message key="message.sop.search.execution.course.none"/></span>
</logic:notPresent>