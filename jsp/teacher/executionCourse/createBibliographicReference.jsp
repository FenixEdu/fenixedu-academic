<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
			<html:hidden property="method" value="createBibliographicReference"/>
			<html:hidden property="page" value="1"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:message key="message.bibliographicReferenceTitle"/>
			</h4>
			<span class="error"><html:errors property="title"/></span>
			<html:textarea property="title" cols="56" rows="4"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceAuthors"/>
			</h4>
			<span class="error"><html:errors property="authors"/></span>
			<html:textarea property="authors" cols="56" rows="4"/>
			<h4>
				<bean:message key="message.bibliographicReferenceReference"/>
			</h4>
			<span class="error"><html:errors property="reference"/></span>
			<html:textarea property="reference" cols="56" rows="2"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceYear"/>
			</h4>
			<span class="error"><html:errors property="year"/></span>
			<html:text property="year"/>
			<br/>
			<h4>
				<bean:message key="message.bibliographicReferenceOptional"/>
			</h4>
			<span class="error"><html:errors property="optional"/></span>
			<html:select property="optional">
				<html:option key="option.bibliographicReference.optional" value="true"/>
				<html:option key="option.bibliographicReference.recommended" value="false"/>
			</html:select>
			<br/>
			<br/>
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>