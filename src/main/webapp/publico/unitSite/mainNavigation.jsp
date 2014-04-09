<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<logic:present name="site">
   <fr:view name="site" layout="unit-side-menu">
        <fr:layout>
            <fr:property name="sectionUrl" value="/units/viewSite.do?method=section"/>
            <fr:property name="contextParam" value="unitID"/>
        </fr:layout>
    </fr:view>
</logic:present>
    
<div class="usitebannerlat">
	${site.sideBanner}
</div>
