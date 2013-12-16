<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:view name="metaDomainObjects" schema="view.meta.domain.object.portal">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>

			<fr:property name="key(edit)" value="edit.portal"/>
			<fr:property name="bundle(edit)" value="CONTENT_RESOURCES"/>
			<fr:property name="link(edit)" value="/portalManagement.do?method=prepareEditPortal"/>
			<fr:property name="param(edit)" value="externalId/pid"/>
		</fr:layout>
</fr:view>
