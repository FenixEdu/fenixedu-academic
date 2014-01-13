<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<html:xhtml />

<h2>Create Department</h2>
	<fr:edit id="department" name="bean" action="/manageAssociatedObjects.do?method=createDepartment">
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.ManageAssociatedObjects$AssociatedObjectsBean">
			<fr:slot name="active" key="active">
			</fr:slot>
			<fr:slot name="code" key="label.manager.code">
			</fr:slot>
			<fr:slot name="name" key="label.name">
			</fr:slot>
			<fr:slot name="realName" key="label.real.name">
			</fr:slot>
			<fr:slot name="realNameEn" key="label.real.name.en">
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle5 thleft thlight thmiddle mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
