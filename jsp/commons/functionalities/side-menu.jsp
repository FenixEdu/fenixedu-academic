<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <div class="newnav">
        <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
                 type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext">
             <fr:layout name="side-menu">
                <fr:property name="moduleClasses" value="navmod"/>
                <fr:property name="selectedClasses" value="highlight1"/>
             </fr:layout>
        </fr:view>
    </div>
</logic:present>
