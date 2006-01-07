/*
 * Created on 16/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentExtraWork  extends IPersistentObject{
    public ExtraWork readExtraWorkByDay(Date day) throws Exception;
}
