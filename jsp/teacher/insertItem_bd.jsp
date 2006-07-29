<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/html2xhtml.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/richtext.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/htmleditor.js"></script>

<span class="error"><html:errors/>
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>

<h2><bean:message key="message.insertItem" /></h2>
<br />

<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="itemsList" name="component" property="items"/>
	
<html:form action="/insertItem">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<logic:present name="verEditor">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.information" property="information" />
	</logic:present>
	
<table>
	<tr>
		<td>
			<bean:message key="message.itemName"/>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" /><span class="error"><html:errors property="name"/></span>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.sectionOrder"/>	
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.itemOrder" property="itemOrder" size="1">
				<html:option value="-1"><bean:message key="label.end"/></html:option>
				<html:options collection="itemsList" labelProperty="name" property="itemOrder" />
			
			</html:select>
			<span class="error"><html:errors property="itemOrder"/></span>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.itemUrgent"/>
		</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.urgent" property="urgent" size="1" >	
				<html:option key="label.no" value="false"></html:option>
				<html:option key="label.yes" value="true"></html:option>
			</html:select>
			<span class="error"><html:errors property="urgent"/></span>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan='2'>
			<logic:present name="naoVerEditor">
				<bean:message key="label.editor"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" disabled="true"/>
				&nbsp;
				<bean:message key="label.plain.text"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true"/>						
			</logic:present>	
			<logic:notPresent name="naoVerEditor">	
				<bean:message key="label.editor"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true" onclick="this.form.method.value='prepareInsertItem';this.form.page.value=0;this.form.information.value='';this.form.submit();"/>
				&nbsp;
				<bean:message key="label.plain.text"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" onclick="this.form.method.value='prepareInsertItem';this.form.page.value=0;this.form.submit();"/>					
			</logic:notPresent>
		</td>

	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.itemInformation"/>
		</td>
		<td>
			<logic:present name="verEditor">
				<script language="JavaScript" type="text/javascript"> 
				<!--
					initEditor();		
				//-->
				</script>
				
				<noscript>JavaScript must be enable to use this form <br/> </noscript>
				
				<script language="JavaScript" type="text/javascript"> 
				<!--
					writeTextEditor(300, 300, document.forms[0].information.value);		
				//-->
				</script>						
			</logic:present>	
			
			<logic:notPresent name="verEditor">	
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.information" rows="20" cols="80" property="information"/>
			</logic:notPresent>
			<span class="error"><html:errors property="information"/></span>
		</td>
	</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertItem" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="section" name="component" property="section"/>	
<bean:define id="sectionCode" name="section" property="idInternal"/>	
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentSectionCode" property="currentSectionCode" value="<%= sectionCode.toString() %>" />

<logic:present name="verEditor">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.information.value=update()">
		<bean:message key="button.save"/>
	</html:submit>
</logic:present>
<logic:notPresent name="verEditor">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
</logic:notPresent>

<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>	
<br/><br/>
<bean:message key="message.text.editor.requires"/>		
</html:form>
</logic:present>