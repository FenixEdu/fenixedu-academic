package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;

interface IServicoAssiduidade {
    void execute() throws NotExecuteException, NotAuthorizeException;
}