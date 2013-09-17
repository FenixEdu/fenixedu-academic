<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:notEmpty name="metaDomainObjects">
<fr:view name="metaDomainObjects" schema="view.meta.domain.object">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>

			<fr:property name="key(edit)" value="edit.portal"/>
			<fr:property name="bundle(edit)" value="CONTENT_RESOURCES"/>
			<fr:property name="link(edit)" value="/portalManagement.do?method=prepareEditPortal"/>
			<fr:property name="param(edit)" value="associatedPortal.externalId/pid"/>
			<fr:property name="visibleIf(edit)" value="portalAvailable"/> 

			<fr:property name="bundle(create)" value="CONTENT_RESOURCES"/>
			<fr:property name="key(create)" value="create.portal"/>
			<fr:property name="link(create)" value="/portalManagement.do?method=prepareCreatePortal"/>
			<fr:property name="param(create)" value="externalId/oid" />
			<fr:property name="visibleIfNot(create)" value="portalAvailable"/>	

		</fr:layout>
</fr:view>

</logic:notEmpty>

<fr:create type="net.sourceforge.fenixedu.domain.MetaDomainObject" schema="create.meta.domain.object">
	<fr:layout>
		<fr:property name="classes" value="tstyle5" />
	</fr:layout>
</fr:create>