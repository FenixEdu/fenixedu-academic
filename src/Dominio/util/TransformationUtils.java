/*
 * Created on 28/Jan/2004
 */
package Dominio.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.IDomainObject;

/**
 * @author jpvl
 */
public abstract class TransformationUtils {

    public static List transformToIds(List domainList) {
        return (List) CollectionUtils.collect(domainList, new Transformer() {

            public Object transform(Object input) {
                IDomainObject domainObject = (IDomainObject) input;
                return domainObject == null ? null : domainObject.getIdInternal();
            }
        });
    }
}