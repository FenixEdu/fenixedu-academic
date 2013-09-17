<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><fr:view name="unit" property="name"/></em>
<bean:define id="functionalityAction" value="scientificCouncilFiles" toScope="request"/>
<bean:define id="module" value="scientificCouncil" toScope="request"/>
