<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<h1>Validação de Email</h1>
	
	<logic:equal name="state" value="VALID">
		O seu email foi validado com sucesso!
	</logic:equal>			
	<logic:equal name="state" value="INVALID">
		Não foi possível validar o seu email. Verifique o link que lhe foi enviado. 
		Dispõe de <bean:write name="tries"/> tentativas.
	</logic:equal>
	<logic:equal name="state" value="REFUSED">
		O seu pedido de validação foi recusado. Execedeu o número de tentativas possíveis.
	</logic:equal>
