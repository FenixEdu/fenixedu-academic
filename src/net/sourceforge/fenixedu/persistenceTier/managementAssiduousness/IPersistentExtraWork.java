/*
 * Created on 16/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentExtraWork  extends IPersistentObject{
    public IExtraWork readExtraWorkByDay(Date day) throws Exception;
}
