package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.HomeController;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping {

    private Map<String, Controller> mapping = new HashMap<>();

    void init() {
        mapping.put("/", new HomeController());
    }

    public Controller findHandler(String uriPath) {
        return mapping.get(uriPath);
    }
}
