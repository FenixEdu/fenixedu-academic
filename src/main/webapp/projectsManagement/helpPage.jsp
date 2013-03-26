<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="helpPage">
	<bean:define id="helpPage" value="<%=request.getAttribute("helpPage").toString()%>" />
	<center>
	<h2><bean:message key="<%="link."+helpPage%>" /></h2>
	</center>
	<bean:message key="<%="message."+helpPage%>" />

	<logic:present name="infoRubricList">
		<br />
		<br />
		<table class="rubric-table" align="center">
			<tr>
				<td class="report-hdr"><bean:message key="label.code" /></td>
				<td class="report-hdr"><bean:message key="label.description" /></td>
			</tr>
			<logic:iterate id="infoRubrica" name="infoRubricList" indexId="lineIndex">
				<tr>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>"><bean:write name="infoRubrica" property="code" /></td>
					<td class="<%= "report-td-" + (lineIndex.intValue() % 2) %>"><bean:write name="infoRubrica" property="description" /></td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>
</logic:present>
