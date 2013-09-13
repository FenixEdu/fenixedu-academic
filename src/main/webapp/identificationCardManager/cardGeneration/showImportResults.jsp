<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="title.import.card.generation.report" bundle="CARD_GENERATION_RESOURCES"/></h2>

<h3><bean:message key="title.report.entries.not.corresponding.to.any.person" bundle="CARD_GENERATION_RESOURCES"/></h3>
<fr:view name="import.identification.card.report" property="unmatchedEntries" schema="import.identification.card.data.unmatched.entries.show">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>


<h3><bean:message key="title.persons.duplicated.by.identification.number" bundle="CARD_GENERATION_RESOURCES"/></h3>
<logic:empty name="import.identification.card.report" property="duplicatedPersonsById">
	<p><bean:message key="message.search.persons.by.identification.number.empty" bundle="CARD_GENERATION_RESOURCES"/></p>
</logic:empty>
<fr:view name="import.identification.card.report" property="duplicatedPersonsById" schema="import.identification.card.data.duplicated.persons.by.id.show">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>

<h3><bean:message key="title.persons.duplicated.by.name" bundle="CARD_GENERATION_RESOURCES"/></h3>
<logic:empty name="import.identification.card.report" property="duplicatedPersonsByName">
	<p><bean:message key="messsage.search.persons.by.name.empty" bundle="CARD_GENERATION_RESOURCES"/></p>
</logic:empty>
<logic:notEmpty name="import.identification.card.report" property="duplicatedPersonsByName">
<fr:view name="import.identification.card.report" property="duplicatedPersonsByName" schema="import.identification.card.data.duplicated.persons.by.name.show">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<h3><bean:message key="title.duplicated.lines" bundle="CARD_GENERATION_RESOURCES"/></h3>
<pre>
<fr:view name="import.identification.card.report" property="duplicatedLines">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
	</fr:layout>
</fr:view>
</pre>


<h3><bean:message key="title.matched.persons.by.identification.number" bundle="CARD_GENERATION_RESOURCES"/></h3>
<logic:empty name="import.identification.card.report" property="matchedPersonsById">
	<p><bean:message key="message.matched.persons.by.identification.number.not.found" bundle="CARD_GENERATION_RESOURCES"/></p>
</logic:empty>
<logic:notEmpty name="import.identification.card.report" property="matchedPersonsById">
<fr:view name="import.identification.card.report" property="matchedPersonsById" schema="import.identification.card.data.matched.persons">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>


<h3><bean:message key="title.machted.persons.by.name" bundle="CARD_GENERATION_RESOURCES"/></h3>
<logic:empty name="import.identification.card.report" property="matchedPersonsByName">
</logic:empty>
<logic:notEmpty name="import.identification.card.report" property="matchedPersonsByName">
<fr:view name="import.identification.card.report" property="matchedPersonsByName" schema="import.identification.card.data.matched.persons">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<p><bean:message key="label.number.record.sucessfully.imported" bundle="CARD_GENERATION_RESOURCES"/>: 
<fr:view name="import.identification.card.report" property="matches">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
	</fr:layout>
</fr:view>
</p>
