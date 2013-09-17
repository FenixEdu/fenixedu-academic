<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>
<h2 class="mtop15"><bean:message key="label.teachers" bundle="SITE_RESOURCES"/></h2>


<logic:iterate id="category" name="categories" type="net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory">
	<h2 class="greytxt mtop2">
		<fr:view name="category" property="name.content"/>
	</h2>

	<bean:define id="byCategory" value="true" toScope="request"/>
	<logic:iterate id="teacher" name="teachers" property="<%= category.getExternalId() %>" type="net.sourceforge.fenixedu.domain.Person">
		<fr:view name="teacher">
			<fr:layout name="person-presentation-card">
				<fr:property name="subLayout" value="values-as-list"/>
				<fr:property name="subSchema" value="present.research.member"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:iterate>