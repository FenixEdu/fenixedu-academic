<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message bundle="MESSAGING_RESOURCES" key="label.viewForuns.title" /></h3>

<logic:present name="foruns">
<bean:define id="contextPrefix" name="contextPrefix" />
	<fr:view name="foruns" layout="tabular" schema="forum.view-with-name-description-and-creation-date">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
      		<fr:property name="columnClasses" value="listClasses,"/>
			<fr:property name="link(view)" value="<%= contextPrefix + "method=viewForum" %>"/>
			<fr:property name="param(view)" value="idInternal/forumId"/>
			<fr:property name="key(view)" value="messaging.viewForum.link"/>
			<fr:property name="bundle(view)" value="MESSAGING_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>