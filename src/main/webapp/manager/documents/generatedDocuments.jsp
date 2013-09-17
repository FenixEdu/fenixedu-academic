<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="documents.management.title" bundle="MANAGER_RESOURCES" /></h2>

<logic:present role="MANAGER">

	<logic:messagesPresent message="true">
		<p><span class="error0"><!-- Error messages go here --> <html:messages id="message"
			message="true" bundle="MANAGER_RESOURCES">
			<bean:write name="message" />
		</html:messages> </span>
		<p>
	</logic:messagesPresent>

	<fr:form action="/generatedDocuments.do?method=search">
		<fr:edit name="searchBean" id="searchBean" schema="documents.search">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value=",,noborder" />
			</fr:layout>
		</fr:edit>
		<html:submit>
			<bean:message key="label.search" bundle="MANAGER_RESOURCES" />
		</html:submit>
	</fr:form>

	<logic:present name="documents">
		<logic:empty name="documents">
			<bean:message key="label.documents.empty" bundle="MANAGER_RESOURCES" />
		</logic:empty>

		<logic:notEmpty name="documents">
			<fr:view name="documents" schema="documents.list">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
					<fr:property name="columnClasses" value=",,nowrap,nowrap," />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:present>

</logic:present>