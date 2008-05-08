<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
<html:xhtml/>

<h2>
	<bean:message key="link.card.generation.search.people" />
</h2>

<br/>

<bean:define id="url" type="java.lang.String">/searchPeople.do?method=search</bean:define>
<fr:edit name="searchParameters"
		 schema="card.generation.search.person.form"
		 type="net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson$SearchParameters"
		 action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>


<logic:present name="searchPersonCollectionPager">
	<br/>
	<br/>
	<bean:define id="params">&amp;name=<logic:present name="searchParameters" property="name"><bean:write name="searchParameters" property="name"/></logic:present>&amp;email=<logic:present name="searchParameters" property="email"><bean:write name="searchParameters" property="email"/></logic:present>&amp;username=<logic:present name="searchParameters" property="username"><bean:write name="searchParameters" property="username"/></logic:present>&amp;documentIdNumber=<logic:present name="searchParameters" property="documentIdNumber"><bean:write name="searchParameters" property="documentIdNumber"/></logic:present></bean:define>
	<cp:collectionPages url="<%= "/identificationCardManager" + url + params %>" 
		pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="8"/>
	<fr:view name="people" schema="person.name.id.dateOfBirth">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>
		</fr:layout>
	</fr:view>
</logic:present>