/*
 * Created on 28/Jan/2004
 */
package net.sourceforge.fenixedu.domain.util;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public abstract class TransformationUtils {

    public static List transformToIds(List domainList) {
	return (List) CollectionUtils.collect(domainList, new Transformer() {

	    public Object transform(Object input) {
		DomainObject domainObject = (DomainObject) input;
		return domainObject == null ? null : domainObject.getIdInternal();
	    }
	});
    }
}