package org.fenixedu.academic.json.adapters;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@DefaultJsonAdapter(Person.class)
public class PersonJsonAdapter implements JsonViewer<Person> {
    @Override
    public JsonElement view(Person person, JsonBuilder ctx) {
        JsonObject object = new JsonObject();
        object.addProperty("id", person.getExternalId());
        object.addProperty("username", person.getUsername());
        object.addProperty("name", person.getName());
        object.addProperty("email", person.getDefaultEmailAddressValue());
        return object;
    }
}