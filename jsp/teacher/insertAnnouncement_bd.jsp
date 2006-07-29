<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/html2xhtml.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/richtext.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/htmleditor.js"></script>

<br />
<h2><bean:message key="title.insertAnnouncement"/></h2>

<span class="error"><html:errors/>
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>

<html:form action="/announcementManagementAction" focus="title" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<logic:present name="verEditor">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.information" property="information" />
	</logic:present>	

<strong><bean:message key="label.title" /></strong>
<br />
<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.title" rows="2" cols="65" name="insertAnnouncementForm" property="title" >
</html:textarea>
<span class="error"><html:errors property="title"/></span>
<br />
<br />	
<strong><bean:message key="label.information" /></strong>
<br />
<br />

<logic:present name="naoVerEditor">
	<bean:message key="label.editor"/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" disabled="true"/>
	&nbsp;
	<bean:message key="label.plain.text"/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true"/>						
</logic:present>	
<logic:notPresent name="naoVerEditor">
	<bean:message key="label.editor"/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true" onclick="this.form.method.value='prepareCreateAnnouncement';this.form.page.value=0;this.form.submit();"/>
	&nbsp;
	<bean:message key="label.plain.text"/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" onclick="this.form.method.value='prepareCreateAnnouncement';this.form.page.value=0;this.form.submit();"/>						
</logic:notPresent>	

<br />
<br />

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
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.information" rows="20" cols="80" property="information" > </html:textarea>
</logic:notPresent>

<span class="error"><html:errors property="information"/></span>
<br />
<br />

<logic:present name="verEditor">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.information.value=update()"><bean:message key="button.save" /></html:submit>
</logic:present>
<logic:notPresent name="verEditor">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save" /></html:submit>
</logic:notPresent>

<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear" /></html:reset>
<br/><br/>
<bean:message key="message.text.editor.requires"/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createAnnouncement" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

</html:form>