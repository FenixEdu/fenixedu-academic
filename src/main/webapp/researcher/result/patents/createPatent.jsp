<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- Titles -->
<em><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></em> <!-- tobundle -->
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.create.title"/></h2>

<!-- Author name -->
<p>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/>: <fr:view name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.name"/>
</p>

<%-- Warning/Error messages --%>
<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
	</html:messages>
</logic:messagesPresent>

<p class="mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.patentData"/></strong></p>
<fr:create id="createPatent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent" 
			schema="patent.create"
			action="/resultPatents/createPatent.do">
	<fr:hidden slot="participation" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person"/>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
        <fr:property name="columnClasses" value="listClasses,,tdclear tderror1"/>
    </fr:layout>
    <fr:destination name="exception" path="/resultPatents/prepareCreate.do"/>
    <fr:destination name="invalid" path="/resultPatents/prepareCreate.do"/>
    <fr:destination name="cancel" path="/resultPatents/management.do"/>
</fr:create>
<br/>