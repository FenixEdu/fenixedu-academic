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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.bibliography" /></h2>

<div class="infoop2">
	<bean:message key="label.bibliography.explanation" />
</div>

<logic:present name="executionCourse">
		<html:form action="/manageBibliographicReference">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBibliographicReference"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<bean:define id="executionCourseID" type="java.lang.String" name="executionCourse" property="externalId"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>

			<table class="tstyle5 tdtop">
			<tr>
			<td>
			<bean:message key="message.bibliographicReferenceTitle"/>
			</td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="title"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.title" property="title" cols="56" rows="4"/>
			</td>
			</tr>
			
			<tr>
			<td>
			<bean:message key="message.bibliographicReferenceAuthors"/>
			</td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="authors"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.authors" property="authors" cols="56" rows="4"/>
			</td>
			</tr>

			<tr>
			<td>
			<bean:message key="message.bibliographicReferenceReference"/>
			</td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="reference"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.reference" property="reference" cols="56" rows="2"/>
			</td>
			</tr>

			<tr>
			<td>
			<bean:message key="message.bibliographicReferenceYear"/>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="year"/></span>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/>
			</td>
			</tr>

			<tr>
			<td>
			<bean:message key="message.bibliographicReferenceOptional"/>
			<span class="error"><!-- Error messages go here --><html:errors property="optional"/></span>
			</td>
			<td>
			<html:select bundle="HTMLALT_RESOURCES" property="optional">
				<html:option key="option.bibliographicReference.optional" value="true"/>
				<html:option key="option.bibliographicReference.recommended" value="false"/>
			</html:select>
			</td>			
			</tr>
			</table>




			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
</logic:present>