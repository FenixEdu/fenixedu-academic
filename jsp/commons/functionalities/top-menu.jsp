<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <div id="navgeral">
        <fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>"
                 type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext">
             <fr:layout name="top-menu">
                <fr:property name="selectedClasses" value="active"/>
             </fr:layout>
        </fr:view>
    </div>
</logic:present>

<logic:notPresent name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
    <jsp:include page="/commons/commonGeneralNavigationBar.jsp"/>
</logic:notPresent>