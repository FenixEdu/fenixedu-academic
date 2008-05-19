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

<logic:present name="person">
	<fr:view name="person" schema="card.generation.search.person.list">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>

			<fr:property name="link(view)" value="/searchPeople.do?method=viewPersonCards"/>
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="param(view)" value="idInternal/personId" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(view)" value="1" />
		</fr:layout>
	</fr:view>

	<br/>
	<br/>

	<fr:view name="person" property="cardGenerationEntries" schema="card.generation.person.card.list">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>

			<fr:property name="link(view)" value="/searchPeople.do?method=viewPersonCard"/>
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="param(view)" value="idInternal/cardGenerationEntryId" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(view)" value="1" />
		</fr:layout>
	</fr:view>

	<br/>
	<br/>

	<logic:present name="cardGenerationEntry">
		<bean:write name="cardGenerationEntry" property="line"/>
	</logic:present>

</logic:present>
