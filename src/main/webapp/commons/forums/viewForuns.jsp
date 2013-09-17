<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForuns.title" /></h3>

<logic:present name="foruns">
	<bean:define id="prefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="contextPrefix" value="<%= prefix + (prefix.contains("?") ? "&amp;" : "?") %>" type="java.lang.String"/>
	<fr:view name="foruns" layout="tabular" schema="forum.view-with-name-description-and-creation-date">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
      		<fr:property name="columnClasses" value="listClasses,"/>
			<fr:property name="link(view)" value="<%= contextPrefix + "method=viewForum" %>"/>
			<fr:property name="param(view)" value="externalId/forumId"/>
			<fr:property name="key(view)" value="messaging.viewForum.link"/>
			<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>