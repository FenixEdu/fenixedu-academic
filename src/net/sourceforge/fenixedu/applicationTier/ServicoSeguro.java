package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;

public abstract class ServicoSeguro implements IServicoAssiduidade {
    private ServicoAutorizacao _servicoAutorizacao;

    public ServicoSeguro(ServicoAutorizacao servicoAutorizacao) {
        _servicoAutorizacao = servicoAutorizacao;
    }

    public final void authorize() throws NotAuthorizeException {
        if (_servicoAutorizacao != null)
            _servicoAutorizacao.execute();
    }
}