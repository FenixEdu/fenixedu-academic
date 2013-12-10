<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="title.teachers"/></h2>

<div class="infoop2">
	<bean:message key="label.teachers.explanation" />
	<p><bean:message key="label.teachers.specialTeacherWarning" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></p>
</div>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>	
</p>

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
		<th><bean:message key="label.istid" bundle="APPLICATION_RESOURCES" /></th>
		<th><bean:message key="label.name" /></th>
		<logic:equal name="isResponsible" value="true">
			<th><bean:message key="label.teacher.responsible" /></th>			
			<th><bean:message key="message.edit" /></th>    
		</logic:equal>
	</tr>	
	<logic:iterate id="professorship" name="listPersons">
	<bean:define id="person" name="professorship" property="person" />
	<tr>
		<td><bean:write name="person"  property="istUsername" /></td>
		<td><bean:write name="person" property="name" /></td>	
		<logic:equal name="isResponsible" value="true">
		<logic:equal name="professorship" property="responsibleFor" value="false">
			<td>
				<bean:message key="label.no.capitalized" />
			</td>
			<td>
				<bean:define id="teacherOID" name="professorship" property="externalId"/>		
				<html:link page="<%= "/teachersManagerDA.do?method=viewProfessorshipProperties&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;teacherOID=" + teacherOID %>">
					Detalhes
				</html:link>
			</td>
		</logic:equal>
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<td>
				<bean:message key="label.yes.capitalized" />
			</td>
			<td>
				<bean:message key="label.notAvailable" />
			</td>
		</logic:equal>
		</logic:equal>
	</tr>
	</logic:iterate>	
</table>
</logic:present>