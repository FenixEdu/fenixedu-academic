<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
