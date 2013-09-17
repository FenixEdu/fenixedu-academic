<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<html:xhtml />

<h2><bean:message key="portal.library.add.or.remove.operators" bundle="PORTAL_RESOURCES" /></h2>

<h3 class="mtop2"><bean:message key="label.find.operator" bundle="LIBRARY_RESOURCES" /></h3>

<fr:form action="/libraryOperator.do?method=searchOperator">
	<table>
		<tr><td>
		<fr:edit id="search.operator" name="higherCleranceGroupManagementBean">
			<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryHigherCleranceGroupManagementBean">
				<fr:slot name="searchUserId" key="label.search.operator" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit></td>
			
		<td><html:submit>
			<bean:message key="button.search" bundle="LIBRARY_RESOURCES" />
		</html:submit></td>
		</tr>
	</table>
</fr:form>


<logic:present name="higherCleranceGroupManagementBean" property="operator">
	<br>
	<fr:view name="higherCleranceGroupManagementBean">
		<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.domain.Person">
			<fr:slot name="operator" key="label.person.picture" layout="view-as-image">
				<fr:property name="classes" value="column3" />
				<fr:property name="useParent" value="true" />
				<fr:property name="moduleRelative" value="false" />
				<fr:property name="contextRelative" value="true" />
				<fr:property name="imageFormat" value="/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=${operator.istUsername}" />
			</fr:slot>
			<fr:slot name="operator.name" key="label.person.name" />
			<fr:slot name="operator.istUsername" key="label.person.istUsername" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
		</fr:layout>
	</fr:view>
	
	<logic:equal name="higherCleranceGroupManagementBean" property="belongsToGroup" value="true">
		<html:link action="/libraryOperator.do?method=removeOperator" paramId="istUsername" paramName="higherCleranceGroupManagementBean" paramProperty="operator.istUsername">
			<b><bean:message  key="link.remove.operator" bundle="LIBRARY_RESOURCES"/></b>
		</html:link>
	</logic:equal>
	
	<logic:equal name="higherCleranceGroupManagementBean" property="belongsToGroup" value="false">
		<html:link action="/libraryOperator.do?method=addOperator" paramId="istUsername" paramName="higherCleranceGroupManagementBean" paramProperty="operator.istUsername">
			<b><bean:message  key="link.add.operator" bundle="LIBRARY_RESOURCES"/></b>
		</html:link>
	</logic:equal>
	
</logic:present>


<logic:notEmpty name="higherCleranceGroupManagementBean" property="higherClearenceGroup.elements">
	<h3 class="mtop2"><bean:message key="label.operators" bundle="LIBRARY_RESOURCES" /> </h3>
	
	<fr:view name="higherCleranceGroupManagementBean" property="higherClearenceGroup.elements">
		<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.domain.Person">
			<fr:slot name="istUsername" key="label.person.istUsername" />
			<fr:slot name="name" key="label.person.name" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
			<fr:property name="sortBy" value="name, istUsername" />
	
			<fr:property name="link(view)" value="/libraryOperator.do?method=viewOperator" />
			<fr:property name="param(view)" value="istUsername" />
			<fr:property name="key(view)" value="link.view" />
			<fr:property name="bundle(view)" value="LIBRARY_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>