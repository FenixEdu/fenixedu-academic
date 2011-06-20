<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="link.manage.card.generation.header"/></em>
<h2><bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.edit.professionalCategory" /></h2>
<p><html:link page="/manageCardGeneration.do?method=showCategoryCodes">« Voltar</html:link></p>

<fr:edit id="professionalCategory"
		 name="professionalCategory"
		 schema="net.sourceforge.fenixedu.domain.Degree.card.generation.edit"
		 type="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory"
		 action="/manageCardGeneration.do?method=showCategoryCodes">
	<fr:schema bundle="CARD_GENERATION_RESOURCES" type="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory">
		<fr:slot name="name.content" bundle="CARD_GENERATION_RESOURCES"
				key="link.manage.card.generation.edit.professionalCategory" readOnly="true"/>
		<fr:slot name="identificationCardLabel" bundle="CARD_GENERATION_RESOURCES"
				key="label.professionalCategory.identificationCardLabel" required="true">
			<fr:property name="size" value="17" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop1"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>
