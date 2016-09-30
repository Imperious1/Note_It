package imperiumnet.imperious.noteit.Singleton;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by blaze on 8/9/2016.
 */
public class SingleyBoo {
    private static SingleyBoo ourInstance = new SingleyBoo();
    private static Realm realm;
    public static Realm getRealm() {
        return realm;
    }

    public static SingleyBoo getInstance(Context context) {
        realm = Realm.getInstance(new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build());
        return ourInstance;
    }

    private SingleyBoo() {
    }
}
