/*
 * Created on 9/Out/2003
 *
 *
 */
package Dominio;

import java.util.List;

/**
 * Authors : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 * (naat@mega.ist.utl.pt)
 *  
 */
public interface IMasterDegreeThesis extends IDomainObject {

    public abstract void setMasterDegreeProofVersions(List masterDegreeProofVersions);

    public abstract List getMasterDegreeProofVersions();

    public abstract void setMasterDegreeThesisDataVersions(List masterDegreeThesisDataVersions);

    public abstract List getMasterDegreeThesisDataVersions();

    public abstract void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);

    public abstract IStudentCurricularPlan getStudentCurricularPlan();
}