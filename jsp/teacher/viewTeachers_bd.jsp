<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.teachers"/></h2>

<div class="infoop5">
	<bean:message key="label.teachers.explanation" />
	<p><bean:message key="label.teachers.specialTeacherWarning" /></p>
</div>

<span class="error"><!-- Error messages go here --><html:errors /></span>	

<logic:present name="siteView">
<bean:define id="infoSiteTeachers" name="siteView" property="component"/>
<bean:define id="teachersList" name="infoSiteTeachers" property="infoTeachers"/>
<bean:define id="isResponsible" name="infoSiteTeachers" property="isResponsible"/>

<logic:equal name="isResponsible" value="true">
<ul class="mvert15">
	<li>
		<html:link page="<%= "/teacherManagerDA.do?method=prepareAssociateTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") %>"><bean:message key="link.addTeacher"/></html:link>
	</li>
</ul>
</logic:equal>

<table class="tstyle2 tdcenter">
	<tr>
		<th><bean:message key="label.teacherNumber" /></th>
		<th><bean:message key="label.name" /></th>			
		<logic:equal name="isResponsible" value="true">
			<th><bean:message key="message.edit" /></th>    
		</logic:equal>
	</tr>	
	<logic:iterate id="infoTeacher" name="teachersList">
	<tr>
		<td><bean:write name="infoTeacher"  property="teacherNumber" /></td>
		<td><bean:write name="infoTeacher" property="infoPerson.nome" /></td>	
		<logic:equal name="isResponsible" value="true">
			<bean:define id="teacherCode" name="infoTeacher" property="idInternal"/>		
			<td>
				<html:link page="<%= "/teachersManagerDA.do?method=removeTeacher&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;teacherCode=" + teacherCode %>">
					<bean:message key="link.removeTeacher"/>
				</html:link>
			</td>
		</logic:equal>
	</tr>
	</logic:iterate>	
</table>
</logic:present>