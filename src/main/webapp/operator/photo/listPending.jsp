<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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