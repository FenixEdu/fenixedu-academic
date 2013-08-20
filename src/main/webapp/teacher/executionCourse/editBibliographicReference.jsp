<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.bibliography" /></h2>

<div class="infoop2">
	<bean:message key="label.bibliography.explanation" />
</div>

<logic:present name="bibliographicReference">
	<blockquote>
		<html:form action="/editBibliographicReference">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editBibliographicReference"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<bean:define id="bibliographicReferenceID" type="java.lang.String" name="bibliographicReference" property="externalId"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.bibliographicReferenceID" property="bibliographicReferenceID" value="<%= bibliographicReferenceID.toString() %>"/>
			<bean:define id="executionCourseID" type="java.lang.String" name="executionCourse" property="externalId"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			
			<table class="tstyle5">
			<tr>
			<td><bean:message key="message.bibliographicReferenceTitle"/></td>
			<span class="error"><!-- Error messages go here --><html:errors property="title"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.title" property="title" cols="56" rows="4"/>
			</tr>

			<tr>
			<td><bean:message key="message.bibliographicReferenceAuthors"/></td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="authors"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.authors" property="authors" cols="56" rows="4"/>
			</td>
			</tr>

			<tr>
			<td><bean:message key="message.bibliographicReferenceReference"/>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="reference"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.reference" property="reference" cols="56" rows="2"/>
			</td>
			</tr>

			<tr>
			<td><bean:message key="message.bibliographicReferenceYear"/></td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="year"/></span>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/>
			</td>
			</tr>

			<tr>
			<td><bean:message key="message.bibliographicReferenceOptional"/></td>
			<td>
			<span class="error"><!-- Error messages go here --><html:errors property="optional"/></span>
			<html:select bundle="HTMLALT_RESOURCES" property="optional">
				<html:option key="option.bibliographicReference.optional" value="true"/>
				<html:option key="option.bibliographicReference.recommended" value="false"/>
			</html:select>
			</td>
			</tr>
			
			</table>

			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</p>
		</html:form>
	</blockquote>
</logic:present>