<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>


<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.choosePrinter"/></h3>
<logic:messagesPresent message="true">
	<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true" >
		<p><span class="error0"><bean:write name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>
<br/>
<html:form action="/printMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printMarkSheets"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.markSheet" property="markSheet"/>
	<html:hidden bundle="HTMLALT_RESOURCES" property="ecID"/>
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
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print"/></html:submit>
	
</html:form>
