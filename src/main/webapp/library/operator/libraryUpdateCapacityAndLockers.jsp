<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<html:xhtml />

<h2><bean:message key="portal.library.update.capacity.and.lockers" bundle="PORTAL_RESOURCES" /></h2>

<fr:form action="/libraryOperator.do?method=selectLibrary">
	<fr:edit id="libraryInformation" name="libraryInformation">
		<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryInformation">
			<fr:slot name="library" key="label.library" layout="menu-select-postback" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.library.LibraryProvider" />
				<fr:property name="format" value="Biblioteca ${spaceBuilding.nameWithCampus}" />
				<fr:property name="destination" value="postback" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mbottom0" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="postback" path="/libraryOperator.do?method=selectLibraryToUpdate" />
	</fr:edit>
</fr:form>


<logic:present name="libraryInformation" property="library">
	<fr:form action="/libraryOperator.do?method=updateCapacityAndLockers">
		<fr:edit id="libraryUpdate" name="libraryInformation">
			<fr:schema bundle="LIBRARY_RESOURCES"
				type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryInformation">
				<fr:slot name="capacity" key="label.library.capacity">
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				</fr:slot>
				<fr:slot name="lockers" key="label.library.lockers">
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="postback" path="/libraryOperator.do?method=updateCapacityAndLockers" />
			<fr:destination name="invalid" path="/libraryOperator.do?method=handleInvalidCapacityOrLockers"/>
		</fr:edit>
		
		<html:submit>
			<bean:message key="button.confirm" bundle="LIBRARY_RESOURCES" />
		</html:submit>
	</fr:form>
</logic:present>