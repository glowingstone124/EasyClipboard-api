package org.glowingstone.easyclipboardapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
public class Contollers {
    String DATABASE = "jdbc://data/databse.db";
    @RequestMapping("/postclipboard")
    public String postdata(String data) {
        System.out.println("done");
        return Funcs.postHandler(data);
    }
    @RequestMapping("/getclipboard")
    public String getdata() {
        System.out.println("query done");
        return Funcs.getHandler();
    }
}
