package org.fenixedu.messaging.core.task;
	

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.MessageTemplate;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;


import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;


public class ImportMessageTemplates extends CustomTask {


    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.WRITE;
    }


    @Override public void runTask() throws Exception {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray json = (JsonArray) parser.parse(new FileReader("message-templates-dump.json"));


        Iterator<JsonElement> iterator = json.iterator();
        while(iterator.hasNext()){
            JsonObject jsonObject = (JsonObject) iterator.next();
            dothings(jsonObject);
        }


    }


    private void dothings(JsonObject object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String id = object.get("id").getAsString();
        JsonElement subject = object.get("subject");
        JsonObject body = (JsonObject) object.get("body");
        JsonObject html = (JsonObject) object.get("html");


        MessagingSystem system = MessagingSystem.getInstance();
        Method getTemplates = system.getClass().getMethod("getTemplateSet");
        getTemplates.setAccessible(true);


        Set<MessageTemplate> templates = (Set<MessageTemplate>) getTemplates.invoke(system);


        for (MessageTemplate template : templates) {
            if (template.getId().equals(id)){
                taskLog("Updating template with id: %s%n", id);
                FenixFramework.atomic(() -> {
                    template.setSubject(LocalizedString.fromJson(subject));
                    template.setTextBody(LocalizedString.fromJson(body));
                    template.setHtmlBody(LocalizedString.fromJson(html));
                });
            }
        }


    }
}