<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

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
		
	<bean:define id="path">/manager/cron.do?method=showScript&page=0&cronScriptStateID=<bean:write name="cronScriptState" property="idInternal"/></bean:define>	
	<cp:collectionPages url="<%= path %>" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
	
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
				<fr:property name="param(view)" value="idInternal/cronScriptInvocationID"/>
				<fr:property name="key(view)" value="link.view.log"/>
				<fr:property name="bundle(view)" value="MANAGER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>