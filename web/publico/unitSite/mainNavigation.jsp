<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
	<fr:view name="site" property="sideBanner" layout="html" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
</div>
