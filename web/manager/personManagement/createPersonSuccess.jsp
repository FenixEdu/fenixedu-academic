<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- Paulo Zenida and Dinis Martins - 2006-03-02 -->
<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">

<p>Created Person</p>
<fr:view name="person" schema="person.create-validated">
	<fr:layout name="tabular">
		<fr:property name="classes" value="style1" />
		<fr:property name="columnClasses" value="listClasses," />
	</fr:layout>
</fr:view></div>