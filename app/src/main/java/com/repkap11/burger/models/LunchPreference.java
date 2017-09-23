package com.repkap11.burger.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by paul on 9/23/17.
 */

public class LunchPreference {
    public HashMap<String, String> users;

    public LunchPreference() {
    }

    public List<String> enumerateUsers() {
        if (users == null) {
            return new ArrayList<>(0);
        }
        ArrayList<String> resultList = new ArrayList(users.size());
        Iterator<Map.Entry<String, String>> it = users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            resultList.add(entry.getKey());
        }
        return resultList;
    }
}