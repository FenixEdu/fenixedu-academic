<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<em>
	<bean:message key="title.card.generation" bundle="CARD_GENERATION_RESOURCES"/>
</em>
<h2>
	<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.croosRefNewBatch" />
</h2>

<fr:edit id="crossReferenceBean"
		name="crossReferenceBean"
		schema="CardInfoUpload"
		action="/manageCardGeneration.do?method=crossReferenceNewBatch">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.ManageCardGenerationDA$CrossReferenceBean" bundle="CARD_GENERATION_RESOURCES">
		<fr:slot name="description" key="label.description" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
		<fr:slot name="inputStream" key="label.file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="fileNameSlot" value="filename"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/manageCardGeneration.do?method=firstPage"%>"/>
</fr:edit>
