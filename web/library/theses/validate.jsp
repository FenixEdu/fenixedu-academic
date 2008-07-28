<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<bean:define id="thesisId" name="thesis" property="idInternal" />
<bean:define id="thesisState" name="thesis" property="libraryState.name" />
<bean:define id="person"
	name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>"
	property="person" />

<h2><bean:message key="title.library.thesis.verify" /></h2>

<ul>
	<li><html:link page="/theses/search.do?method=prepare">
		<bean:message key="link.back" />
	</html:link></li>
</ul>

<logic:present name="view">
	<logic:notEmpty name="thesis" property="lastLibraryOperation">
		<fr:view name="thesis" property="lastLibraryOperation" schema="library.thesis.state">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mvert05" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="thesis" property="lastLibraryOperation">
		<fr:view name="thesis" schema="library.thesis.state.null">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mvert05" />
			</fr:layout>
		</fr:view>
	</logic:empty>

	<logic:notEqual name="thesisState" value="ARCHIVE">
		<div class="mtop05 mbottom15"><html:form
			action="<%="/theses/validate.do?thesisID=" + thesisId%>">
			<html:hidden property="method" value="" />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
				onclick="this.form.method.value='prepareValidate';">
				<bean:message key="link.thesis.validate" />
			</html:submit>
			<logic:equal name="thesisState" value="NOT_DEALT">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
					onclick="this.form.method.value='preparePending';">
					<bean:message key="link.thesis.pending" />
				</html:submit>
			</logic:equal>
		</html:form></div>
	</logic:notEqual>

	<logic:equal name="thesisState" value="ARCHIVE">
		<div class="mtop05 mbottom15"><fr:form
			action="<%="/theses/validate.do?method=cancel&amp;thesisID=" + thesisId%>">
			<fr:create id="cancel" type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryCancelOperation"
				schema="library.thesis.cancel">
				<fr:hidden slot="thesisId" name="thesis" property="idInternal" />
				<fr:hidden slot="performerId" name="person" property="idInternal" />
			</fr:create>
			<html:submit>
				<bean:message key="link.thesis.cancel" />
			</html:submit>
		</fr:form></div>
	</logic:equal>
</logic:present>

<logic:present name="validate">
	<fr:create id="validate"
		type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryArchiveOperation"
		schema="library.thesis.validate"
		action="<%="/theses/validate.do?method=validate&amp;thesisID=" + thesisId%>">
		<fr:hidden slot="thesisId" name="thesis" property="idInternal" />
		<fr:hidden slot="performerId" name="person" property="idInternal" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thmiddle" />
		</fr:layout>
	</fr:create>
</logic:present>

<logic:present name="pending">
	<fr:create id="pending" type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryPendingOperation"
		schema="library.thesis.pending"
		action="<%="/theses/validate.do?method=pending&amp;thesisID=" + thesisId%>">
		<fr:hidden slot="thesisId" name="thesis" property="idInternal" />
		<fr:hidden slot="performerId" name="person" property="idInternal" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thtop" />
		</fr:layout>
	</fr:create>
</logic:present>

<h3 class="mtop2 mbottom05"><bean:message key="label.thesis.author" /></h3>
<fr:view name="thesis" schema="library.thesis.author">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 thtop tdtop" />
		<fr:property name="columnClasses" value="width12em,width35em," />
	</fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis" /></h3>
<fr:view name="thesis" schema="library.thesis.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 thtop tdtop" />
		<fr:property name="columnClasses" value="width12em,width35em," />
	</fr:layout>
</fr:view>
