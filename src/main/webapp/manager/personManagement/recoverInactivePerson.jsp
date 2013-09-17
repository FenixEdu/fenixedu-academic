<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="link.recover.inactive.person"/>
</h2>
<bean:message bundle="MANAGER_RESOURCES" key="message.activate.person"/>
<br/>
<br/>
<logic:notPresent name="findPersonFactory">
	<fr:create type="net.sourceforge.fenixedu.domain.Person$FindPersonFactory"
			schema="FindPersonFactory"
			action="/recoverInactivePerson.do?method=search">
	</fr:create>
</logic:notPresent>
<logic:present name="findPersonFactory">
	<fr:edit name="findPersonFactory"
			type="net.sourceforge.fenixedu.domain.Person$FindPersonFactory"
			schema="FindPersonFactory"
			action="/recoverInactivePerson.do?method=search">
	</fr:edit>
	<br/>
	<br/>
	<fr:view name="findPersonFactory" property="people" schema="FindPersonFactoryResult" layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
     		<fr:property name="columnClasses" value="listClasses"/>
			<fr:property name="link(activate)" value="/recoverInactivePerson.do?method=activate"/>
			<fr:property name="param(activate)" value="externalId/personID"/>
			<fr:property name="key(activate)" value="link.activate.person"/>
			<fr:property name="bundle(activate)" value="MANAGER_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>