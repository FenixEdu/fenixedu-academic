package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;

public class ServicoAutorizacao implements IServicoAssiduidade {

    public void execute() throws NotAuthorizeException {
		// This look stupid but it avoids a irritating warnin that the exception is not thrown
		// Performance-wise the compiler removes the whole block because it is un-reachable.
		if (false) {
			throw new NotAuthorizeException();
		}
    }

}

