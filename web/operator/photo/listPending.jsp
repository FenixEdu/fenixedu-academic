<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES" /></em>
<h2><bean:message key="label.operator.photo.title" bundle="MANAGER_RESOURCES" /></h2>

<p><strong>Fotografias pendentes para aprovação</strong></p>

<logic:empty name="pending">
    <p><em><bean:message key="label.operator.photo.no.pending" bundle="MANAGER_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="pending">
	<fr:edit id="result" name="pending" schema="operator.photo.pending.list"
		action="/pendingPhotos.do?method=accept" layout="tabular-editable">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
		</fr:layout>
	</fr:edit>
</logic:notEmpty>