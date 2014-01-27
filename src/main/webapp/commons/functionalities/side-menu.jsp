<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div class="newnav">
    <fr:view name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>"
             type="org.fenixedu.bennu.portal.domain.MenuFunctionality">
         <fr:layout name="side-menu">
            <fr:property name="moduleClasses" value="navheader"/>
            <fr:property name="selectedClasses" value="highlight5"/>
         </fr:layout>
    </fr:view>
</div>
