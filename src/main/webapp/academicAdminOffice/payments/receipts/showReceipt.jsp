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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipt" /></h2>

<logic:messagesPresent message="true" property="context">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="context" bundle="ACADEMIC_OFFICE_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
			<html:messages id="messages" message="true"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
</logic:messagesPresent>

<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="receipt" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipt" /></strong></p>
<fr:view name="receipt" schema="receipt.view-with-number-and-year">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",tdhl1" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.contributor" /></strong></p>
<logic:present name="receipt" property="contributorParty" >
	<fr:view name="receipt" property="contributorParty" schema="contributor.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		</fr:layout>
	</fr:view>
</logic:present>
<logic:notPresent name="receipt" property="contributorParty">
	<fr:view name="receipt" schema="receipt.view.contributorName">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		</fr:layout>
	</fr:view>
</logic:notPresent>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></strong></p>
	  	<fr:view name="receipt" property="entries" schema="entry.view">
		<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 mtop05 mbottom0 width700px" />
		<fr:property name="columnClasses" value="width8em acenter, width30em acenter,width8em acenter,width15em aright"/>
			<fr:property name="sortBy" value="whenRegistered=asc,externalId=asc"/>
		</fr:layout>
		</fr:view>

<table class="tstyle4 mtop0" style="width: 700px;">
	<tr>
		<td class="aright" colspan="4"><span style="background-color: #fdfbdd;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount"/>: <bean:define id="totalAmount" name="receipt" property="totalAmount" type="Money"/>&nbsp;<%= totalAmount.toPlainString() %>&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol"/></span></td>
	</tr>
</table>
<bean:define id="personId" name="receipt" property="person.externalId"/>

<table>
		<tr>
			<td>
				<html:form action='<%= "/receipts.do?personId=" + personId %>' target="_blank">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printReceipt" />
						<fr:edit id="receipt" name="receipt" visible="false" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.print"/></html:submit>
				</html:form>
			</td>

			<td>
				<logic:equal name="receipt" property="active" value="true">
				<html:form action="<%="/creditNotes.do?method=showCreditNotes&amp;personId=" + personId%>">
					<fr:edit id="receipt" name="receipt" visible="false" />
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.creditNotes"/>
					</html:submit>
				</html:form>
				</logic:equal>
			</td>

			<td>
				<html:form action="<%="/receipts.do?method=showReceipts&amp;personId=" + personId%>">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
					</html:submit>
				</html:form>	
			</td>
			<td>
			</td>
		</tr>
	</table>
