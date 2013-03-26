<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>
<script type="text/javascript">
function inverSelection(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = !(this.checked) })
}

function selectAll(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = true })
}

function selectNone(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = false })
}
</script>

<bean:define id="person" name="professorship" property="person" />

<h2><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions.options"/> <bean:write name="person" property="name" /></h2>

<bean:define id="teacherOID" name="professorship" property="externalId"/>
<ul>
<li><html:link page="<%= "/teacherManagerDA.do?method=viewTeachersByProfessorship&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
<bean:message key="button.back" bundle="APPLICATION_RESOURCES"/>
</html:link></li>
<li><html:link page="<%= "/teachersManagerDA.do?method=removeTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;teacherOID=" + teacherOID %>">
		<bean:message key="link.removeTeacher"/>
</html:link></li>
</ul>
<h3><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions"/>:</h3>

<html:link href="#" onclick="selectAll()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.all"/></html:link> | <html:link href="#" onclick="selectNone()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.none"/></html:link> | <html:link href="#" onclick="inverSelection()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.invert"/></html:link>
 
<fr:form action="<%= "/teacherManagerDA.do?method=viewTeachersByProfessorship&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	<fr:edit id="permissions" name="professorship" property="permissions" schema="professorship.view.properties">
   	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft tdleft thlight"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/> 
</fr:form>