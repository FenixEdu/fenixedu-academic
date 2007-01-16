<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


<h3><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.choosePrinter"/></h3>
<logic:messagesPresent message="true">
	<html:messages bundle="DEGREE_OFFICE_RESOURCES" id="messages" message="true" >
		<p><span class="error0"><bean:write name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>
<br/>
<html:form action="/printMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printMarkSheets"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.markSheet" property="markSheet"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table>
		<tr>
			<td>
				<ul>
				<logic:iterate id="name"  name="printerNames">
					<li><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.printerName" property="printerName" value='<%= name.toString() %>'>
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
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DEGREE_OFFICE_RESOURCES" key="label.print"/></html:submit>
	
</html:form>
