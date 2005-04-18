<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<h2><bean:message key="title.insertAnnouncement"/></h2>

<span class="error"><html:errors/>
	<logic:present name="errors">
		<bean:write name="errors" filter="true" />
	</logic:present	>
</span>

<html:form action="/announcementManagementAction" focus="title" >
	<html:hidden property="page" value="1"/>
	
	<logic:present name="verEditor">
		<html:hidden property="information" />
	</logic:present>	

<strong><bean:message key="label.title" /></strong>
<br />
<html:textarea rows="2" cols="65" name="insertAnnouncementForm" property="title" >
</html:textarea>
<span class="error"><html:errors property="title"/></span>
<br />
<br />	
<strong><bean:message key="label.information" /></strong>
<br />
<br />

<bean:message key="label.editor"/>
<html:radio property="editor" value="true" onclick="this.form.method.value='prepareCreateAnnouncement';this.form.page.value=0;this.form.submit();"/>
&nbsp;
<bean:message key="label.plain.text"/>
<html:radio property="editor" value="false" onclick="this.form.method.value='prepareCreateAnnouncement';this.form.page.value=0;this.form.submit();"/>					

<br />
<br />

<logic:present name="verEditor">
	<script language="JavaScript" type="text/javascript"> 
		<!--
		initEditor();		
		//-->
		</script>
		
		<noscript>JavaScript must be enable to use this form <br> </noscript>
		
		<script language="JavaScript" type="text/javascript"> 
		<!--
		writeTextEditor(200, 200, document.forms[0].information.value);		
		//-->
	</script>
</logic:present>

<logic:notPresent name="verEditor">
	<html:textarea rows="10" cols="60" property="information" > </html:textarea>
</logic:notPresent>

<span class="error"><html:errors property="information"/></span>
<br />
<br />

<logic:present name="verEditor">
	<html:submit styleClass="inputbutton" onclick="this.form.information.value=update()"><bean:message key="button.save" /></html:submit>
</logic:present>
<logic:notPresent name="verEditor">
	<html:submit styleClass="inputbutton"><bean:message key="button.save" /></html:submit>
</logic:notPresent>

<html:reset styleClass="inputbutton"><bean:message key="label.clear" /></html:reset>

<html:hidden property="method" value="createAnnouncement" />
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

</html:form>