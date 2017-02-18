package com.hemant.myfeed.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by HemantSingh on 27/10/16.
 */

@RealmClass
public class StringObject extends RealmObject {
    public String string;

    public StringObject() {

    }

    public static StringObject init(Realm realm, String str) {
        StringObject stringObject = realm.createObject(StringObject.class);
        stringObject.string = str;
        return stringObject;

    }
}