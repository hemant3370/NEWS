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
