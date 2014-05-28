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

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%@page import="net.sourceforge.fenixedu.domain.thesis.Thesis"%><html:xhtml />

<bean:define id="thesisId" name="thesis" property="externalId" />
<bean:define id="thesisState" name="thesis" property="libraryState.name" />
<bean:define id="person"
	name="LOGGED_USER_ATTRIBUTE"
	property="person" />

<h2><bean:message key="thesis.validation.title.details" /></h2>

<p>
    <html:link page="<%="/theses/search.do?method=update" +  request.getAttribute("searchArgs")  %>">
		<bean:message key="link.back" bundle="COMMON_RESOURCES" />
    </html:link>
</p>

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
		<div class="mtop05 mbottom15"><fr:form
			action="<%="/theses/validate.do?thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
			<html:hidden property="method" value="" />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
				onclick="this.form.method.value='prepareValidate';">
				<bean:message key="thesis.validation.button.validate" />
			</html:submit>
			<logic:equal name="thesisState" value="PENDING_ARCHIVE">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
					onclick="this.form.method.value='prepareEditPending';">
					<bean:message key="thesis.validation.button.editPendingComment" />
				</html:submit>
			</logic:equal>
			<logic:equal name="thesisState" value="NOT_DEALT">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
					onclick="this.form.method.value='preparePending';">
					<bean:message key="thesis.validation.button.pending" />
				</html:submit>
			</logic:equal>
            <logic:equal name="thesisState" value="ARCHIVE_CANCELED">
                <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
                    onclick="this.form.method.value='preparePending';">
                    <bean:message key="thesis.validation.button.pending" />
                </html:submit>
            </logic:equal>
		</fr:form></div>
	</logic:notEqual>

	<logic:equal name="thesisState" value="ARCHIVE">
		<div class="mtop05 mbottom15"><fr:form
			action="<%="/theses/validate.do?method=view&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
			<fr:create id="cancel" type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryCancelOperation"
				schema="library.thesis.cancel">
				<fr:hidden slot="thesisId" name="thesis" property="externalId" />
				<fr:hidden slot="performerId" name="person" property="externalId" />
			</fr:create>
			<html:submit>
				<bean:message key="thesis.validation.button.cancel" />
			</html:submit>
		</fr:form></div>
	</logic:equal>
</logic:present>

<logic:present name="validate">
	<fr:create id="validate"
		type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryArchiveOperation"
		schema="library.thesis.validate"
		action="<%="/theses/validate.do?method=view&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
		<fr:hidden slot="thesisId" name="thesis" property="externalId" />
		<fr:hidden slot="performerId" name="person" property="externalId" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thmiddle" />
		</fr:layout>
		<fr:destination name="invalid"
			path="<%="/theses/validate.do?method=prepareValidate&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>" />
	</fr:create>
</logic:present>

<logic:present name="pending">
	<fr:create id="pending" type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryPendingOperation"
		schema="library.thesis.pending"
		action="<%="/theses/validate.do?method=view&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
		<fr:hidden slot="thesisId" name="thesis" property="externalId" />
		<fr:hidden slot="performerId" name="person" property="externalId" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thtop" />
		</fr:layout>
		<fr:destination name="invalid"
			path="<%="/theses/validate.do?method=preparePending&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>" />
	</fr:create>
</logic:present>

<logic:present name="editPending">
    <fr:create id="edit" type="net.sourceforge.fenixedu.domain.thesis.ThesisLibraryPendingOperation"
        schema="library.thesis.pending"
        action="<%="/theses/validate.do?method=view&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
        <fr:hidden slot="thesisId" name="thesis" property="externalId" />
        <fr:hidden slot="performerId" name="person" property="externalId" />
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright mvert05 thtop" />
        </fr:layout>
        <fr:default slot="pendingComment" name="thesis" property="lastLibraryOperation.pendingComment" />
        <fr:destination name="invalid"
            path="<%="/theses/validate.do?method=prepareEditPending&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>" />
    </fr:create>
</logic:present>

<logic:notPresent name="history">
	<p><html:link page="<%="/theses/validate.do?method=history&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
        <bean:message key="thesis.validation.link.showHistory" />
	</html:link></p>
</logic:notPresent>

<logic:present name="history">
    <p><html:link page="<%="/theses/validate.do?method=view&amp;thesisID=" + thesisId + request.getAttribute("searchArgs") %>">
        <bean:message key="thesis.validation.link.hideHistory" />
    </html:link></p>

	<logic:empty name="history">
		<p><em><bean:message key="thesis.validation.message.feedback.emptyHistory" /></em></p>
	</logic:empty>
	<logic:notEmpty name="history">
		<fr:view name="history" schema="library.thesis.state">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thnowrap mtop1" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter,acenter,acenter," />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

<h3 class="mtop2 mbottom05"><bean:message key="label.thesis.Thesis.author" bundle="DOMAIN_RESOURCES" /></h3>
<fr:view name="thesis" schema="library.thesis.author">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 thtop tdtop" />
		<fr:property name="columnClasses" value="width12em,width35em," />
	</fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.Thesis" bundle="DOMAIN_RESOURCES" /></h3>
<fr:view name="thesis" schema="library.thesis.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 thtop tdtop" />
		<fr:property name="columnClasses" value="width12em,width35em," />
	</fr:layout>
</fr:view>
