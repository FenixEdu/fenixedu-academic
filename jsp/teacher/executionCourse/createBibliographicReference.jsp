<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.bibliography" /></h2>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.bibliography.explanation" />
		</td>
	</tr>
</table>

<logic:present name="executionCourse">
	<blockquote>
		<html:form action="/createBibliographicReference">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBibliographicReference"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:message key="message.bibliographicReferenceTitle"/>
			</h4>
			<span class="error"><!-- Error messages go here --><html:errors property="title"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.title" property="title" cols="56" rows="4"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceAuthors"/>
			</h4>
			<span class="error"><!-- Error messages go here --><html:errors property="authors"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.authors" property="authors" cols="56" rows="4"/>
			<h4>
				<bean:message key="message.bibliographicReferenceReference"/>
			</h4>
			<span class="error"><!-- Error messages go here --><html:errors property="reference"/></span>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.reference" property="reference" cols="56" rows="2"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceYear"/>
			</h4>
			<span class="error"><!-- Error messages go here --><html:errors property="year"/></span>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceOptional"/>
			</h4>
			<span class="error"><!-- Error messages go here --><html:errors property="optional"/></span>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.optional" property="optional">
				<html:option key="option.bibliographicReference.optional" value="true"/>
				<html:option key="option.bibliographicReference.recommended" value="false"/>
			</html:select>
			<br/>
			<br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>