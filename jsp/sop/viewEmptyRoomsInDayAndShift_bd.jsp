<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.search.result.roomsWithoutExams"/></h2>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>Crit&eacute;rios:</p>
			<strong>
				<bean:write name="dateAndTime" scope="request"/>
			</strong>
		</td>
	</tr>
</table>
<br />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<logic:iterate id="infoRoom" name="<%= SessionConstants.INFO_EMPTY_ROOMS_KEY %>" scope="request">
			<bean:write name="infoRoom" property="nome"/>;&nbsp;
		</logic:iterate>
		</td>
	</tr>
</table>
