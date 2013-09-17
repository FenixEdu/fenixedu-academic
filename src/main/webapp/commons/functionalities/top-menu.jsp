<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <div id="navgeral">
        <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
                 type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext">
             <fr:layout name="top-menu">
                <fr:property name="selectedClasses" value="navheader selected"/>
                <fr:property name="topLevelClasses" value="navheader"/>
             </fr:layout>
        </fr:view>
    </div>
</logic:present>

<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <jsp:include page="/commons/commonGeneralNavigationBar.jsp"/>
</logic:notPresent>