<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<html:xhtml/>

<br/>
<h2><strong><bean:message key="link.create.external.person" bundle="MANAGER_RESOURCES"/></strong></h2>

<span class="error"><!-- Error messages go here --><html:errors/><br /></span>

<fr:form action="/externalPerson.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>

	<h2><strong><bean:message key="label.search.for.external.person" bundle="MANAGER_RESOURCES" /></strong></h2>
	<fr:edit id="anyPersonSearchBean" name="anyPersonSearchBean" schema="net.sourceforge.fenixedu.domain.Person.AnyPersonSearchBean" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>

<br/>
<br/>

<logic:equal name="anyPersonSearchBean" property="hasBeenSubmitted" value="true">
	<bean:define id="people" name="anyPersonSearchBean" property="search"/>
	<logic:iterate id="person" name="people">
		<bean:write name="person" property="name"/>
		<br/>
	</logic:iterate>
	<br/>
	<br/>
	<bean:define id="url" type="java.lang.String">/externalPerson.do?method=prepareCreate&amp;name=<bean:write name="anyPersonSearchBean" property="name"/>&amp;idDocumentType=<bean:write name="anyPersonSearchBean" property="idDocumentType"/>&amp;documentIdNumber=<bean:write name="anyPersonSearchBean" property="documentIdNumber"/></bean:define>
	<html:link action="<%= url %>">
		<bean:message key="link.create.external.person.because.does.not.exist" bundle="MANAGER_RESOURCES"/>
	</html:link>
</logic:equal>
