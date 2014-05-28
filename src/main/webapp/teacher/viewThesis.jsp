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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="externalId" />

<html:xhtml />

<h2><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.evaluated.view" /></h2>

<ul>
	<li><html:link
		page="/thesisDocumentConfirmation.do?method=showThesisList">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			key="link.scientificCouncil.thesis.list.back" />
	</html:link></li>
	<logic:equal name="thesisPresentationState" property="name"
		value="DOCUMENTS_SUBMITTED">
		<li><html:link
			page="/thesisDocumentConfirmation.do?method=showConfirmationPage"
			paramId="thesisID" paramName="thesis" paramProperty="externalId">
			<bean:message bundle="APPLICATION_RESOURCES"
				key="link.thesis.confirm.documents" />
		</html:link></li>
	</logic:equal>
	<li><html:link
		page="<%= String.format("/thesisDocumentConfirmation.do?method=viewOperationsThesis&thesisID=%s",thesisId)%>">
		<bean:message key="link.thesis.operation"
			bundle="STUDENT_RESOURCES"/>
	</html:link></li>
</ul>

<logic:present name="showConfirmationPage">
	<div class="warning0" style="padding: 1em;"><strong><bean:message
		key="label.attention" bundle="APPLICATION_RESOURCES" />:</strong><br />
	<bean:message bundle="APPLICATION_RESOURCES"
		key="label.teacher.thesis.confirm.documents" />
	<div class="mtop1 forminline"><fr:form
		action="<%= String.format("/thesisDocumentConfirmation.do?method=confirmDocuments&amp;thesisID=%s", thesisId) %>">
		<html:submit>
			<bean:message bundle="APPLICATION_RESOURCES"
				key="button.thesis.confirm.documents" />
		</html:submit>
	</fr:form> <fr:form
		action="<%= String.format("/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s", thesisId) %>">
		<html:cancel>
			<bean:message bundle="APPLICATION_RESOURCES" key="button.cancel" />
		</html:cancel>
	</fr:form></div>
	</div>
</logic:present>

<logic:present name="documentsConfirmed">
	<div class="warning0" style="padding: 1em;"><strong><bean:message
		key="message.teacher.thesis.confirmed.documents"
		bundle="APPLICATION_RESOURCES" />:</strong><br />
	</div>
</logic:present>

<%-- Dissertation --%>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.evaluation.details" /></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thlight thright mtop05 mbottom05" />
		<fr:property name="columnClasses" value="width12em,," />
	</fr:layout>
</fr:view>


<%-- general process information --%>
<div class="infoop2 mtop1">
<p class="mvert0"><strong><bean:message
	bundle="APPLICATION_RESOURCES" key="label.comments" />:</strong> <logic:empty
	name="thesis" property="comment">
	<logic:empty name="thesis" property="rejectionComment">
		<bean:message bundle="APPLICATION_RESOURCES" key="message.no.comments" />
	</logic:empty>
</logic:empty> <bean:write name="thesis" property="comment" /> <bean:write
	name="thesis" property="rejectionComment" /></p>
</div>

<div class="infoop2 mtop1"><bean:define id="stateKey"
	type="java.lang.String">ThesisPresentationState.<bean:write
		name="thesisPresentationState" property="name" />.label</bean:define>
<p class="mvert0"><strong><bean:message
	bundle="APPLICATION_RESOURCES" key="<%= stateKey %>" /></strong></p>
</div>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.abstract" /></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages"
	value="true">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="label.thesis.abstract.empty" />
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages"
	value="true">
	<div
		style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
	<fr:view name="thesis" property="thesisAbstract">
		<fr:layout>
			<fr:property name="language" value="pt" />
			<fr:property name="showLanguageForced" value="true" />
		</fr:layout>
	</fr:view></div>

	<div
		style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
	<fr:view name="thesis" property="thesisAbstract">
		<fr:layout>
			<fr:property name="language" value="en" />
			<fr:property name="showLanguageForced" value="true" />
		</fr:layout>
	</fr:view></div>
</logic:equal>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.keywords" /></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages"
	value="true">
	<p><em><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="label.thesis.keywords.empty" /></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages"
	value="true">
	<p><fr:view name="thesis" property="keywords">
		<fr:layout>
			<fr:property name="language" value="pt" />
			<fr:property name="showLanguageForced" value="true" />
		</fr:layout>
	</fr:view></p>

	<p><fr:view name="thesis" property="keywords">
		<fr:layout>
			<fr:property name="language" value="en" />
			<fr:property name="showLanguageForced" value="true" />
		</fr:layout>
	</fr:view></p>
</logic:equal>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.evaluation.extendedAbstract" /></h3>

<logic:empty name="thesis" property="extendedAbstract">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="label.scientificCouncil.thesis.evaluation.noExtendedAbstract" />
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
	<fr:view name="thesis" property="extendedAbstract" layout="values"
		schema="coordinator.thesis.file" />
    (<fr:view name="thesis" property="extendedAbstract.size"
		layout="fileSize" />)
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.evaluation.dissertation" /></h3>

<logic:empty name="thesis" property="dissertation">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="label.scientificCouncil.thesis.evaluation.noDissertation" />
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
	<fr:view name="thesis" property="dissertation" layout="values"
		schema="coordinator.thesis.file" />
    (<fr:view name="thesis" property="dissertation.size"
		layout="fileSize" />)
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.evaluation.gradeAndDate" /></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thlight thright mtop05 mbottom05" />
		<fr:property name="columnClasses" value="width12em,," />
	</fr:layout>
</fr:view>

<%-- Orientation --%>
<h3 class="separator2 mtop2"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.review.section.orientation" /></h3>

<logic:empty name="thesis" property="orientator">
	<logic:empty name="thesis" property="coorientator">
		<p><em><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
			key="title.scientificCouncil.thesis.review.orientation.empty" /></em></p>
	</logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.teacher.thesis.section.orientation.orientator"/></h4>
	<fr:view name="thesis" property="orientator" layout="tabular"
		schema="thesis.jury.proposal.person.loginInfo">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thlight thright mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width12em,width35em," />
		</fr:layout>
	</fr:view>
	<logic:equal name="thesis"
		property="orientatorCreditsDistributionNeeded" value="true">
		<table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
			<tr>
				<th class="width12em"><bean:message
					bundle="SCIENTIFIC_COUNCIL_RESOURCES"
					key="label.scientificCouncil.thesis.edit.teacher.credits" />:</th>
				<td class="width35em"><logic:empty name="thesis"
					property="orientatorCreditsDistribution">-</logic:empty> <logic:notEmpty
					name="thesis" property="orientatorCreditsDistribution">
					<fr:view name="thesis" property="orientatorCreditsDistribution" /> %
                    </logic:notEmpty></td>
			</tr>
		</table>
	</logic:equal>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="coorientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.teacher.thesis.section.orientation.coorientator"/></h4>
	<fr:view name="thesis" property="coorientator" layout="tabular"
		schema="thesis.jury.proposal.person.loginInfo">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thlight thright mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width12em,width35em," />
		</fr:layout>
	</fr:view>
	<logic:equal name="thesis"
		property="coorientatorCreditsDistributionNeeded" value="true">
		<table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
			<tr>
				<th class="width12em"><bean:message
					bundle="SCIENTIFIC_COUNCIL_RESOURCES"
					key="label.scientificCouncil.thesis.edit.teacher.credits" />:</th>
				<td class="width35em"><logic:empty name="thesis"
					property="coorientatorCreditsDistribution">-</logic:empty> <logic:notEmpty
					name="thesis" property="coorientatorCreditsDistribution">
					<fr:view name="thesis" property="coorientatorCreditsDistribution" /> %
                    </logic:notEmpty></td>
			</tr>
		</table>
	</logic:equal>
</logic:notEmpty>

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.review.section.jury" /></h3>

<%-- Jury/President --%>
<h4 class="mtop2 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.review.section.jury.president" /></h4>

<logic:empty name="thesis" property="president">
	<p><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="title.scientificCouncil.thesis.review.president.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
	<fr:view name="thesis" property="president" layout="tabular"
		schema="thesis.jury.proposal.person.loginInfo">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thlight thright mtop05 mbottom05" />
			<fr:property name="columnClasses" value="width12em,width35em," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message
	bundle="SCIENTIFIC_COUNCIL_RESOURCES"
	key="title.scientificCouncil.thesis.review.section.vowels" /></h4>

<logic:empty name="thesis" property="vowels">
	<p><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES"
		key="title.scientificCouncil.thesis.review.vowels.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
	<logic:iterate id="vowel" name="thesis" property="vowels">
		<fr:view name="vowel" layout="tabular"
			schema="thesis.jury.proposal.person.loginInfo">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thlight thright mtop05 mbottom05" />
				<fr:property name="columnClasses" value="width12em,width35em," />
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
