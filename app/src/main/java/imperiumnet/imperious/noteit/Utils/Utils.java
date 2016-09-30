package imperiumnet.imperious.noteit.Utils;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import imperiumnet.imperious.noteit.models.FolderModel;
import imperiumnet.imperious.noteit.models.FolderModelSerializable;
import imperiumnet.imperious.noteit.models.NoteModel;
import imperiumnet.imperious.noteit.models.NoteModelSerializable;

/**
 * Created by blaze on 8/9/2016.
 */
public class Utils {

    public static String getDate() {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Calendar.getInstance().getTime());
    }

    public static NoteModelSerializable convertNM(NoteModel m) {
        return new NoteModelSerializable().setId(m.getId()).setTitle(m.getTitle()).setSynced(m.isSynced()).setEdited(m.isEdited()).setNote(m.getNote()).setCreationDate(m.getCreationDate()).setFolder(m.getFolder()).setTrash(m.isTrash());
    }

    public static NoteModel convertNMS(NoteModelSerializable m) {
        return new NoteModel().setId(m.getId()).setTitle(m.getTitle()).setSynced(m.isSynced()).setEdited(m.isEdited()).setNote(m.getNote()).setCreationDate(m.getCreationDate()).setFolder(m.getFolder()).setTrash(m.isTrash());
    }

    public static FolderModelSerializable convertFM(FolderModel m) {
        return new FolderModelSerializable().setName(m.getName()).setCreationDate(m.getCreationDate());
    }

    public static FolderModel convertFMS(FolderModelSerializable m) {
        return new FolderModel().setName(m.getName()).setCreationDate(m.getCreationDate());
    }

    public static boolean isEven(int number) {
        return (number % 2) == 0;
    }

    public static boolean isNote(Object object) {
        return object instanceof NoteModel;
    }


    public static Bundle getBundle(String key, String value) {
        Bundle b = new Bundle();
        b.putString(key, value);
        return b;
    }

    public static Bundle getBundle(String key, String value, String key2, String[] value2) {
        Bundle b = new Bundle();
        b.putString(key, value);
        b.putStringArray(key2, value2);
        return b;
    }

    public static Bundle getBundle(String key, String[] value) {
        Bundle b = new Bundle();
        b.putStringArray(key, value);
        return b;
    }

    public static Bundle getBundle(String key, String[] value, String key2, boolean value2) {
        Bundle b = new Bundle();
        b.putStringArray(key, value);
        b.putBoolean(key2, value2);
        return b;
    }

    public static Bundle getBundle(String key, boolean value) {
        Bundle b = new Bundle();
        b.putBoolean(key, value);
        return b;
    }

    public static Bundle getBundle(String key, ArrayList<String> value) {
        Bundle b = new Bundle();
        b.putStringArrayList(key, value);
        return b;
    }
}