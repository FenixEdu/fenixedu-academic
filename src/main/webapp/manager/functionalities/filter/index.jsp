<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="title.filter.test" bundle="FUNCTIONALITY_RESOURCES"/></h2>

<!-- ======================
         error message
     ======================  -->
     
<logic:messagesPresent property="selectBean" message="true">
    <html:messages id="beanMessage" property="selectBean" message="true" bundle="FUNCTIONALITY_RESOURCES">
        <bean:write name="beanMessage"/>
    </html:messages>
</logic:messagesPresent>

<!-- ======================
         input form
     ======================  -->

<fr:edit id="select-person" name="bean" layout="tabular" schema="functionalities.filter.select.bean"
         action="/filter/results.do">
         <fr:destination name="invalid" path="/filter/index.do"/>
         <fr:destination name="cancel" path="/filter/index.do"/>
</fr:edit>

<!-- ======================
         results table
     ======================  -->
     
<logic:present name="results">
    <fr:view name="results" layout="tabular" schema="functionalities.filter.result.bean">
        <fr:layout>
            <fr:property name="sortBy" value="functionality.publicPath"/>
        </fr:layout>
        <fr:destination name="viewModule" path="/functionality/view.do?functionality=${functionality.externalId}"/>
        <fr:destination name="testFilter" path="/test${publicPath}"/>
    </fr:view>
</logic:present>