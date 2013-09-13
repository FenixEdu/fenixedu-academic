<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.cron"/>
</h2>
<br/>
<br/>
<logic:present name="cronScriptState">
	
	<fr:view name="cronScriptState"
			schema="net.sourceforge.fenixedu.domain.system.CronScriptState.FullInformation"
			layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
   	 		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
	<br/>
	<br/>
		
	<bean:define id="path">/manager/cron.do?method=showScript&page=0&cronScriptStateID=<bean:write name="cronScriptState" property="externalId"/></bean:define>	
	<cp:collectionPages url="<%= path %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
	
	<br/>
	<br/>
	<logic:present name="cronScriptInvocationsPage">
		<fr:view name="cronScriptInvocationsPage"
				schema="net.sourceforge.fenixedu.domain.system.CronScriptInvocation"
				layout="tabular">
			<fr:layout>
		    	<fr:property name="classes" value="style1"/>
   	 			<fr:property name="columnClasses" value="listClasses"/>
				<fr:property name="link(view)" value="/cron.do?method=showScriptInvocationLog&amp;page=0"/>
				<fr:property name="param(view)" value="externalId/cronScriptInvocationID"/>
				<fr:property name="key(view)" value="link.view.log"/>
				<fr:property name="bundle(view)" value="MANAGER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>