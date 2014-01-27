<%@ page language="java" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div id="navgeral">
    <fr:view name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>"
             type="org.fenixedu.bennu.portal.domain.MenuFunctionality">
        <fr:layout name="top-menu">
            <fr:property name="selectedClasses" value="navheader selected"/>
            <fr:property name="topLevelClasses" value="navheader"/>
        </fr:layout>
    </fr:view>
</div>
