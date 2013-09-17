<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- Paulo Zenida and Dinis Martins - 2006-02-23 -->
<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">

<p>Create Person</p>
<fr:create id="create" action="/personManagement/createPerson.do?method=create"
	type="net.sourceforge.fenixedu.domain.Person"
	schema="person.create-validated">
	<fr:hidden slot="username" value="pXXXXX" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="style1" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:create></div>