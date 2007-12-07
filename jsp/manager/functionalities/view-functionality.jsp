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
         schema="functionalities.functionality.view">
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

<!-- ======================
          parameters
     ======================  -->
  
<br/>
<h3><bean:message key="title.functionality.parameters" bundle="FUNCTIONALITY_RESOURCES"/></h3>  
<bean:define id="id" name="functionality" property="idInternal"/>     
<bean:define id="removeURL" type="java.lang.String">/functionality/removeParameter.do?functionality=<bean:write name="functionality" property="idInternal"/></bean:define>
<fr:view name="functionality" property="parameters" schema="functionalities.functionality.parameter.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mvert05"/>
		<fr:property name="link(removeParameter)" value="<%= removeURL %>" />
		<fr:property name="param(removeParameter)" value="idInternal/functionalityParameter" />
		<fr:property name="key(removeParameter)" value="link.remove" />
	</fr:layout>
</fr:view>

<br/>
<fr:create id="createFunctionalityParameter" action="<%= "/functionality/view.do?functionality=" + id %>" type="net.sourceforge.fenixedu.domain.functionalities.FunctionalityParameter" 
	schema="functionalities.functionality.parameter.create">
	<fr:hidden slot="functionality" name="functionality"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright mtop05 mbottom025"/>
	</fr:layout>
</fr:create>
     
