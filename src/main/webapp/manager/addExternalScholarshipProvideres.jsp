<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="MANAGER">

<em>Entidades Externas para Bolsas</em>
<h2>Adicionar Entidades</h2>

		<fr:form action="/externalScholarshipProvider.do?method=add">
		
		<fr:edit id="bean" name="bean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.ExternalScholarshipProviderDA$ExternalScholarshipBean" >
				<fr:slot name="selected" layout="autoComplete" key="label.nif" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="rawSlotName" value="selected"/>
					<fr:property name="labelField" value="name"/>
					<fr:property name="indicatorShown" value="true"/>		
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.SearchPartyByNif"/>
					<fr:property name="args" value="slot=name"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="format" value="${name}"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
</logic:present>

