/*
 * Created on 16/Nov/2003
 *
 */
package Dominio.teacher;

import Dominio.DomainObject;
import Dominio.ITeacher;
import Util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class ServiceProviderRegime extends DomainObject implements IServiceProviderRegime
{
    private ProviderRegimeType providerRegimeType;
    private Integer keyTeacher;
    private ITeacher teacher;
    
    /**
     * 
     */
    public ServiceProviderRegime()
    {
        super();
    }
    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher()
    {
        return keyTeacher;
    }

    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher)
    {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return Returns the providerRegimeType.
     */
    public ProviderRegimeType getProviderRegimeType()
    {
        return providerRegimeType;
    }

    /**
     * @param providerRegimeType The providerRegimeType to set.
     */
    public void setProviderRegimeType(ProviderRegimeType providerRegimeType)
    {
        this.providerRegimeType = providerRegimeType;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher()
    {
        return teacher;
    }

    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }
}
