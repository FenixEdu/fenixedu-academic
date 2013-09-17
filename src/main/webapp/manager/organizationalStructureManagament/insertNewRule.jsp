<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
</html:messages>

<html:form action="/rulesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertNewRule"/>

	<p><h2><bean:message bundle="MANAGER_RESOURCES" key="title.rules.management"/></h2></p>		
	<br/>
	<p><h3><bean:message bundle="MANAGER_RESOURCES" key="link.new.rule"/></h3></p>	
	<br/>
	
	<fr:create id="create" type="net.sourceforge.fenixedu.domain.organizationalStructure.ConnectionRule" schema="connectionRule.bean">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:create>
	
	<br/>
	<html:link module="/manager" page="/rulesManagement.do?method=listRules">
		<bean:message bundle="MANAGER_RESOURCES" key="label.return" />
	</html:link>
	
</html:form>