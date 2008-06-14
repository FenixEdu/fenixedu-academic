<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="context" name="context" type="java.lang.String"/>

<h2>
	<bean:message key="title.library.thesis.export"/>
</h2>

<ul>
	<li>
		<html:link page="/theses/listConfirmed.do?method=prepare">
			<bean:message key="link.back"/>
		</html:link>
	</li>
</ul>

<p>
	<strong>
		<bean:message key="title.library.thesis.export.step1"/>
	</strong>
</p>

<fr:form action="<%= "/theses/exportTheses.do?method=generate&amp;" + context %>" target="_blank">
	<html:submit>
		<bean:message key="button.exportToXml"/>
	</html:submit>
</fr:form>

<p>
	<strong>
		<bean:message key="title.library.thesis.export.step2"/>
	</strong>
</p>

<logic:present name="confirmExported">
	<p>
		<span class="success0">
			<bean:message key="message.library.thesis.export.confirmed"/>
		</span>
	</p>
</logic:present>

<fr:form action="<%= "/theses/exportTheses.do?method=confirm&amp;" + context %>">
	<html:submit>
		<bean:message key="button.exportConfirm"/>
	</html:submit>
</fr:form>

<fr:view name="theses" schema="library.thesis.list.exported">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
</fr:view>
