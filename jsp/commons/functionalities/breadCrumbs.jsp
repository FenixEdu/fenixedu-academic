<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <div class="breadcrumbs">
        <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
                 type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext">
             <fr:layout name="bread-crumbs">
                <%-- <fr:property name="" value=""/> --%>
             </fr:layout>
        </fr:view>
    <div>
</logic:present>