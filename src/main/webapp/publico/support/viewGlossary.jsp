<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="title.support.glossary"/></h2>

<br />
<logic:present name="infoGlossaryEntries">
	<table width="90%" cellspacing="0" cellpadding="0" style="border: 1px solid #333;">
		<logic:iterate id="infoGlossaryEntry" name="infoGlossaryEntries">
			<tr>
				<td colspan="3" style="background: #ccc; padding: 10px 0 0 10px;">
					<strong><bean:write name="infoGlossaryEntry" property="term"/>:</strong>
				</td>
			</tr>
			<tr>
				<td colspan="3" style="background: #fff; padding: 10px 0 0 10px;">
					<bean:write name="infoGlossaryEntry" property="definition"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>