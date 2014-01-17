<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<html:xhtml />

<h2>Associate Person to Units</h2>
	<fr:edit id="office" name="bean" action="/manageAssociatedObjects.do?method=associatePersonUnit">
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.ManageAssociatedObjects$AssociatedObjectsBean">
			<fr:slot name="username" key="label.username"></fr:slot>
			<fr:slot name="accTypeEnum" key="label.username"></fr:slot>
			<fr:slot name="teacher" key="label.username"></fr:slot>
			<fr:slot name="start" key="label.username"></fr:slot>
			<fr:slot name="unit" layout="menu-select" key="label.username">
				<fr:property name="from" value="units" />
				<fr:property name="format" value="${partyName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle5 thleft thlight thmiddle mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
