<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%--
<fr:view name="found" layout="tabular"/>
--%>

<div style="border: 1px solid black;">
    <fr:view name="bean" property="text" type="java.lang.String"/>
</div>

<textarea cols="80" rows="50"><fr:view name="text">
    <fr:layout>
        <fr:property name="escaped" value="true"/>
    </fr:layout>
</fr:view></textarea>
