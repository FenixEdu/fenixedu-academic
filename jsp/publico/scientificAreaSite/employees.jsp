<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="label.employees" bundle="SITE_RESOURCES"/></h2>


<fr:view name="employees">
	<fr:layout name="person-presentation-card">
		<fr:property name="eachSchema" value="view.person"/>
		<fr:property name="eachLayout" value="values"/>
	</fr:layout>
</fr:view>



	
