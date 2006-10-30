<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <h2><fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/></h2>
    
    <logic:notEmpty name="section" property="associatedItems">
    	<logic:iterate id="item" name="section" property="orderedItems">
    		<h3 class="mtop2"><fr:view name="item" property="name"/></h3>
    		
            <logic:notEmpty name="item" property="information">
    			<fr:view name="item" property="information">
    				<fr:layout>
    					<fr:property name="escaped" value="false" />
    					<fr:property name="newlineAware" value="false" />
    				</fr:layout>
    			</fr:view>
    
    		</logic:notEmpty>
            
    		<logic:notEmpty name="item" property="sortedVisibleFileItems">
                <fr:view name="item" property="sortedVisibleFileItems">
                    <fr:layout name="list">
                        <fr:property name="eachSchema" value="site.item.file.basic"/>
                        <fr:property name="eachLayout" value="values"/>
                        <fr:property name="style" value="<%= "list-style-image: url(" + request.getContextPath() + "/images/icon_file.gif);" %>"/>
                    </fr:layout>
                </fr:view>
    		</logic:notEmpty>
    	</logic:iterate>
    </logic:notEmpty>
</logic:present>