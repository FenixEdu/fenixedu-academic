<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message key="link.card.generation.search.people" /> Cartões</h2>

<bean:define id="url" type="java.lang.String">/searchPeople.do?method=search</bean:define>

<fr:edit name="searchParameters"
		 schema="card.generation.search.person.form"
		 type="net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson$SearchParameters"
		 action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>


<div class="mtop2">
	<logic:present name="searchPersonCollectionPager">
		<bean:define id="params">&amp;name=<logic:present name="searchParameters" property="name"><bean:write name="searchParameters" property="name"/></logic:present>&amp;email=<logic:present name="searchParameters" property="email"><bean:write name="searchParameters" property="email"/></logic:present>&amp;username=<logic:present name="searchParameters" property="username"><bean:write name="searchParameters" property="username"/></logic:present>&amp;documentIdNumber=<logic:present name="searchParameters" property="documentIdNumber"><bean:write name="searchParameters" property="documentIdNumber"/></logic:present></bean:define>
		<cp:collectionPages url="<%= "/identificationCardManager" + url + params %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="8"/>
		<fr:view name="people" schema="card.generation.search.person.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thtop tdcenter mtop05"/>
				<fr:property name="columnClasses" value="aleft,,,,,,"/>
				<fr:property name="link(view)" value="/searchPeople.do?method=viewPersonCards"/>
				<fr:property name="key(view)" value="label.view" />
				<fr:property name="param(view)" value="externalId/personId" />
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
				<fr:property name="order(view)" value="1" />
			</fr:layout>
		</fr:view>
	</logic:present>
</div>

