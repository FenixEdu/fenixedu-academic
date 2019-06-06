package org.fenixedu.academic.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.faces.renderkit.RenderKitImpl;
import com.sun.faces.renderkit.ResponseStateManagerImpl;
import com.sun.faces.util.Base64;
import pt.ist.fenixWebFramework.RenderersConfigurationManager;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class AddSignatureToViewStateObjects  {


    private static class MyRenderKit extends RenderKitImpl {
        MyResponseStateManager myResponseStateManager;

        @Override
        public ResponseStateManager getResponseStateManager() {
            if (myResponseStateManager == null) {
                myResponseStateManager = new MyResponseStateManager();
            }
            return myResponseStateManager;
        }
    }

    private static class MyResponseStateManager extends ResponseStateManagerImpl {

        private static final String ALGORITHM = "HmacSHA256";
        private static final SecretKeySpec key = new SecretKeySpec(
                RenderersConfigurationManager.getConfiguration().viewStateSignatureKey().getBytes(StandardCharsets.UTF_8), ALGORITHM);

        private static byte[] sign(byte[] payload) {
            try {
                Mac mac = Mac.getInstance(ALGORITHM);
                mac.init(key);
                return mac.doFinal(payload);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException("Could not sign view state!", e);
            }
        }

        private static void validate(byte[] decodedForm, byte[] signature) {
            byte[] expected = sign(decodedForm);
            if (!Arrays.equals(expected, signature)) {
                throw invalidViewState();
            }
        }

        private static RuntimeException invalidViewState() {
            return new IllegalArgumentException("Invalid ViewState provided");
        }

        @Override
        public Object getTreeStructureToRestore(final FacesContext context, final String treeId) {

            Object structure = null;
            Object state = null;
            ByteArrayInputStream bis = null;
            GZIPInputStream gis = null;
            ObjectInputStream ois = null;
            boolean compress = this.isCompressStateSet(context);
            Map requestParamMap = context.getExternalContext().getRequestParameterMap();
            String viewString = (String)requestParamMap.get("com.sun.faces.VIEW");

            if (viewString == null ) {
                return null;
            } else {

                String[] parts = viewString.split("_", 2);
                if (parts.length != 2) {
                    throw invalidViewState();//TODO replace by correct exception
                }

                byte[] bytesSerializedObject = java.util.Base64.getDecoder().decode(parts[0]);
                byte[] bytesSignature = java.util.Base64.getDecoder().decode(parts[1]);

                validate(bytesSerializedObject, bytesSignature);

                try {
                    bis = new ByteArrayInputStream(bytesSerializedObject);
                    if (this.isCompressStateSet(context)) {
                        if (log.isDebugEnabled()) {
                            log.debug("Deflating state before restoring..");
                        }

                        gis = new GZIPInputStream(bis);
                        ois = new ObjectInputStream(gis);
                    } else {
                        ois = new ObjectInputStream(bis);
                    }

                    structure = ois.readObject();
                    state = ois.readObject();
                    Map requestMap = context.getExternalContext().getRequestMap();
                    requestMap.put("com.sun.faces.FACES_VIEW_STATE", state);
                    bis.close();
                    if (compress) {
                        gis.close();
                    }

                    ois.close();
                } catch (OptionalDataException e1) {
                    log.error(e1.getMessage(), e1);
                } catch (ClassNotFoundException e2) {
                    log.error(e2.getMessage(), e2);
                } catch (IOException e3) {
                    log.error(e3.getMessage(), e3);
                }

                return structure;
            }
        }

        @Override
        public void writeState(final FacesContext context, final StateManager.SerializedView view) throws IOException {

            String hiddenField = null;
            GZIPOutputStream zos = null;
            ObjectOutputStream oos = null;
            boolean compress = this.isCompressStateSet(context);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (compress) {
                if (log.isDebugEnabled()) {
                    log.debug("Compressing state before saving..");
                }

                zos = new GZIPOutputStream(bos);
                oos = new ObjectOutputStream(zos);
            } else {
                oos = new ObjectOutputStream(bos);
            }

            oos.writeObject(view.getStructure());
            oos.writeObject(view.getState());
            oos.close();
            if (compress) {
                zos.close();
            }

            bos.close();

            byte[] bytesOfObject = bos.toByteArray();
            String base64SerializedObject = new String(Base64.encode(bytesOfObject));
            String base64Signature = new String(Base64.encode(sign(bytesOfObject)));

            hiddenField = " <input type=\"hidden\" name=\"com.sun.faces.VIEW\" value=\"" + base64SerializedObject +"_"+
                    base64Signature + "\" />\n ";
            context.getResponseWriter().write(hiddenField);
        }
    }

    public void runTask() throws Exception {
        
        RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory("javax.faces.render.RenderKitFactory");
        RenderKitImpl html_basic = (RenderKitImpl) rkFactory.getRenderKit(FacesContext.getCurrentInstance(), "HTML_BASIC");
        final RenderKit renderKit = new MyRenderKit();

        Field rendererFamiliesField = RenderKitImpl.class.getDeclaredField("rendererFamilies");
        rendererFamiliesField.setAccessible(true);

        HashMap rendererFamiles = (HashMap) rendererFamiliesField.get(html_basic);
        rendererFamiliesField.set(renderKit, rendererFamiles);

        rkFactory.addRenderKit("HTML_BASIC", renderKit);
        JsonElement jsonElement = new Gson().toJsonTree(renderKit);
    }
}