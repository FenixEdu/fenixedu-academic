/*
 * Created on Nov 10, 2003
 *  
 */
package ServidorAplicacao.Servicos;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Luis Egidio
 * @author lmre@mega.ist.utl.pt Nuno Ochoa
 * @author nmgo@mega.ist.utl.pt
 * @author jpvl
 *  
 */
public class UtilsTestCase {

    /**
     * @param objects
     * @param values
     * @param args
     * @param property
     * @param infoType
     * @return
     */
    public static void readTestList(List objects, Object[] values, String property, Class infoType) {
        if (objects == null) {
            throw new IllegalArgumentException("Objects argument cannot be null!");
        }
        if (values == null) {
            throw new IllegalArgumentException("Values argument cannot be null!");
        }
        if (property == null) {
            throw new IllegalArgumentException("Property argument cannot be null!");
        }
        if (infoType == null) {
            throw new IllegalArgumentException("InfoType argument cannot be null!");
        }

        List expectedObjectList = Arrays.asList(values);
        List receivedList = new ArrayList();
        Assert.assertEquals("Size test:", expectedObjectList.size(), objects.size());
        boolean failed = false;
        for (int i = 0; i < objects.size(); i++) {
            Object object = objects.get(i);
            if (!Beans.isInstanceOf(object, infoType)) {
                Assert.fail("Object of type " + object.getClass().getName() + " not from type "
                        + infoType.getName());
            }
            Object value = null;
            try {
                value = PropertyUtils.getProperty(object, property);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                Assert.fail("Failed to get property " + property + " from "
                        + object.getClass().getName());
            }

            failed = failed || !expectedObjectList.contains(value);
            receivedList.add(value);

        }
        if (failed) {
            Assert.fail("Wrong values returned. Was expecting: " + expectedObjectList + " but received:"
                    + receivedList);
        }
    }
}