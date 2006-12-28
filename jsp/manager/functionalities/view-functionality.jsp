<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.functionality" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         bread crumbs
     ======================  -->

<div>
    <html:link page="/toplevel/view.do">
        <bean:message key="link.toplevel.view" bundle="FUNCTIONALITY_RESOURCES"/>
    </html:link> //
    
    <logic:iterate id="crumb" name="crumbs">
        <html:link page="/module/view.do" paramId="module" paramName="crumb" paramProperty="idInternal">
            <fr:view name="crumb" property="name"/>
        </html:link> &gt;
    </logic:iterate>
    
    <fr:view name="functionality" property="name"/>
</div>

<!-- ======================
         information
     ======================  -->

<fr:view name="functionality" layout="tabular" 
         schema="functionalities.functionality.view.simple">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:view>

<!-- ======================
           links
     ======================  -->

<p class="mtop025">
<html:link page="/functionality/edit.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.edit" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>, 
<html:link page="/functionality/confirm.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.delete" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>
</p>

<!-- ======================
          availability
     ======================  -->

<fr:view name="functionality" layout="tabular" schema="functionalities.functionality.availability">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:view>

<p class="mtop025">
<html:link page="/functionality/manage.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.manage" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>, 
<html:link page="/functionality/exportStructure.do" paramId="functionality" paramName="functionality" paramProperty="idInternal">
    <bean:message key="link.functionality.export" bundle="FUNCTIONALITY_RESOURCES"/>
</html:link>
</p>