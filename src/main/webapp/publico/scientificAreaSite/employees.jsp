<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="label.employees" bundle="SITE_RESOURCES"/></h2>


<fr:view name="employees">
	<fr:layout name="person-presentation-card">
		<fr:property name="eachSchema" value="view.person"/>
		<fr:property name="eachLayout" value="values"/>
	</fr:layout>
</fr:view>



	
