<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<html:form action="/manageCreditsNotes">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editNote"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.noteType" property="noteType"/>
	
	<bean:define id="noteType" name="creditsNotesForm" property="noteType" type="java.lang.String" />

	<em><bean:message key="<%="label."+noteType %>" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></em>
	<h2><bean:message key="label.observations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<html:messages id="message" message="true">
		<span class="error"><!-- Error messages go here -->
			<bean:write name="message"/>
		</span>
	</html:messages>
	

<%
	try {
		final String noteTypeHelp = java.util.ResourceBundle.getBundle("resources.TeacherCreditsSheetResources", request.getLocale()).getString("label."+noteType+".help");
%>
		<div class="infoop2">	
			<%= noteTypeHelp %>			
		</div>
<%
	} catch (java.util.MissingResourceException ex) {

	}
%>

<p class="mtop1 mbottom05"><bean:message key="label.observations" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>:</p>
<html:textarea alt="<%= noteType %>" cols="60" rows="8" property="<%= noteType %>" styleClass="mbottom05"/>

	
<div class="mtop1">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
		<bean:message key="button.cancel"/>
	</html:submit>
</div>

</html:form>

