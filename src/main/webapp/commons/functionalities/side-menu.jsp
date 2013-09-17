<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <div class="newnav">
        <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
                 type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext">
             <fr:layout name="side-menu">
                <fr:property name="moduleClasses" value="navheader"/>
                <fr:property name="selectedClasses" value="highlight5"/>
             </fr:layout>
        </fr:view>
    </div>
</logic:present>
