/*
 * Created on 2003/08/24
 *
 */
package DataBeans.util;

import org.apache.commons.lang.StringUtils;

import DataBeans.InfoObject;
import Dominio.IDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class DomainObject2DataBeanTransformer extends ObjectBeanTransformer {

    /**
     * @param toClass
     * @param fromClass
     */
    public DomainObject2DataBeanTransformer() {
        super(InfoObject.class, IDomainObject.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dataBean.util.ObjectBeanTransformer#getHashKey(java.lang.Object)
     */
    protected Object getHashKey(Object fromObject) {
        return fromObject.getClass().getName() + ((IDomainObject) fromObject).getIdInternal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see dataBean.util.ObjectBeanTransformer#destinationObjectConstructor(java.lang.Object)
     */
    protected Object destinationObjectConstructor(Object fromObject) {
        String classToCreate = InfoObject.class.getPackage().getName()
                + ".Info"
                + StringUtils.stripStart(fromObject.getClass().getName(), fromObject.getClass()
                        .getPackage().getName()
                        + ".");
        return objectConstructor(classToCreate);
    }

}