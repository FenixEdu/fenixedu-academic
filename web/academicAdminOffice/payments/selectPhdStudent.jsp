<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.scolarships.fct" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
<p class="mbottom05"><strong><bean:message key="label.scolarships" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<logic:notEmpty name="debts">
		<fr:view name="debts">
			<fr:layout name="tabular">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityExternalScholarshipExemption">
					<fr:slot name="description" key="label.net.sourceforge.fenixedu.domain.accounting.Event.description" />
				</fr:schema>				
				<fr:property name="linkFormat(liquidate)" value="/fctDebts.do?method=prepareLiquidation&exemptiontId=${externalId}" />
				<fr:property name="key(liquidate)" value="label.payments.liquidate" />
				<fr:property name="bundle(liquidate)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="classes" value="tstyle4 mtop05" />			
			</fr:layout>
		</fr:view>
</logic:notEmpty>

<logic:empty name="debts">
		<p>
			<em>
			<bean:message key="label.no.debts" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</em>
	</p>
</logic:empty>

</logic:present>

