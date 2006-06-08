<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<h3><bean:message key="label.choosePrinter"/></h3>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<p><span class="error0"><bean:write name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>
<br/>
<html:form action="/printMarkSheet.do">
	<html:hidden property="method" value="printMarkSheets"/>
	<html:hidden property="markSheet"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td>
				<ul>
				<logic:iterate id="name"  name="printerNames">
					<li><html:radio property="printerName" value='<%= name.toString() %>'>
						<bean:write name="name"/>
					</html:radio></li>
				</logic:iterate>
				</ul>
			</td>
			<logic:messagesPresent message="false">
				<td>
					<html:errors/>
				</td>									
			</logic:messagesPresent>
		</tr>
	</table>
	<br/>
	<html:cancel styleClass="inputbutton"><bean:message key="label.back"/></html:cancel>
	<html:submit styleClass="inputbutton"><bean:message key="label.print"/></html:submit>
	
</html:form>
