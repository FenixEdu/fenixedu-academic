<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
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
