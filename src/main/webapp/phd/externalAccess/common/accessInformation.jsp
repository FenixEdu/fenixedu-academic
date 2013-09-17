<%@ page language="java"%>
<%@ page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:slot name="email" required="true" validator="<%= EmailValidator.class.getName() %>">
	<fr:property name="size" value="40" />
</fr:slot>
<fr:slot name="password" required="true" layout="password">
	<fr:property name="size" value="40" />
</fr:slot>