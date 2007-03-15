<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="idInternal" />

<html:xhtml />

<ul>
	<li>
		<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=listSubmitted&amp;thesisID=%s", thesisId) %>">
			<bean:message key="title.scientificCouncil.list.submitted" />
		</html:link>
	</li>
</ul>

<h2><bean:message key="title.scientificCouncil.review.proposal" /></h2>

<%-- Student information--%>
<h3><bean:message
	key="title.scientificCouncil.thesis.review.section.student" /></h3>

<fr:view name="thesis" property="student" layout="tabular"
	schema="thesis.review.student">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:view>

<%-- Orientation --%>
<h3><bean:message
	key="title.scientificCouncil.thesis.review.section.orientation" /></h3>

<%-- Orientator --%>
<h4><bean:message
	key="title.scientificCouncil.thesis.review.section.orientation.orientator" /></h4>

<logic:empty name="thesis" property="orientator">
	<p><bean:message
		key="title.scientificCouncil.thesis.review.orientator.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
	<logic:empty name="thesis" property="orientator.externalPerson">
		<fr:view name="thesis" property="orientator" layout="tabular"
			schema="thesis.review.person">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:empty>
	<logic:notEmpty name="thesis" property="orientator.externalPerson">
		<fr:view name="thesis" property="orientator" layout="tabular"
			schema="thesis.review.person.external">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:notEmpty>

<%-- Coorientator --%>
<h4><bean:message
	key="title.scientificCouncil.thesis.review.section.orientation.coorientator" /></h4>

<logic:empty name="thesis" property="coorientator">
	<p><bean:message
		key="title.scientificCouncil.thesis.review.coorientator.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
	<logic:empty name="thesis" property="coorientator.externalPerson">
		<fr:view name="thesis" property="coorientator" layout="tabular"
			schema="thesis.review.person">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:empty>
	<logic:notEmpty name="thesis" property="coorientator.externalPerson">
		<fr:view name="thesis" property="coorientator" layout="tabular"
			schema="thesis.review.person.external">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:notEmpty>

<%-- Dissertation --%>
<h3><bean:message
	key="title.scientificCouncil.thesis.review.section.dissertation" /></h3>

<logic:empty name="thesis" property="title">
	<p><bean:message
		key="label.scientificCouncil.thesis.review.information.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="title">
	<fr:view name="thesis" schema="thesis.review.information">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<%-- Jury --%>
<h3><bean:message
	key="title.scientificCouncil.thesis.review.section.jury" /></h3>

<%-- Jury/President --%>
<h4><bean:message
	key="title.scientificCouncil.thesis.review.section.jury.president" /></h4>

<logic:empty name="thesis" property="president">
	<p><bean:message
		key="title.scientificCouncil.thesis.review.president.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
	<logic:empty name="thesis" property="president.externalPerson">
		<fr:view name="thesis" property="president" layout="tabular"
			schema="thesis.review.person">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:empty>
	<logic:notEmpty name="thesis" property="president.externalPerson">
		<fr:view name="thesis" property="president" layout="tabular"
			schema="thesis.review.person.external">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4><bean:message
	key="title.scientificCouncil.thesis.review.section.vowels" /></h4>

<logic:empty name="thesis" property="vowels">
	<p><bean:message
		key="title.scientificCouncil.thesis.review.vowels.empty" /></p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
	<logic:iterate id="vowel" name="thesis" property="vowels">
		<logic:empty name="vowel" property="externalPerson">
			<fr:view name="vowel" layout="tabular" schema="thesis.review.person">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight mtop05" />
				</fr:layout>
			</fr:view>
		</logic:empty>
		<logic:notEmpty name="vowel" property="externalPerson">
			<fr:view name="vowel" layout="tabular"
				schema="thesis.review.person.external">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight mtop05" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>
