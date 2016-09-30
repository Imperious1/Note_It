package imperiumnet.imperious.noteit.Utils;

import java.util.ArrayList;
import java.util.Random;

import imperiumnet.imperious.noteit.Singleton.SingleyBoo;
import imperiumnet.imperious.noteit.models.FolderModel;
import imperiumnet.imperious.noteit.models.NoteModel;
import io.realm.RealmList;

/**
 * Created by blaze on 8/9/2016.
 */
public class RealmUtils {
    public static void addNote(NoteModel model) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().copyToRealm(model);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void updateNote(NoteModel model) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().copyToRealmOrUpdate(model);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void deleteNote(int id) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findAll().deleteLastFromRealm();
        SingleyBoo.getRealm().commitTransaction();
        organizeNotes();
    }

    public static void trashNote(int id) {
        SingleyBoo.getRealm().beginTransaction();
        if (SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findFirst().isTrash())
            SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findFirst().deleteFromRealm();
        else
            SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findFirst().setTrash(true).setFolder(null);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void clearTrash() {
        SingleyBoo.getRealm().beginTransaction();
        for(NoteModel m : RealmUtils.getNotes()) {
            if(m.isTrash()) {
                m.deleteFromRealm();
            }
        }
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void restoreNote(int id) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findFirst().setTrash(false);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void clearAllNotes() {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().delete(NoteModel.class);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static NoteModel getNote(int id) {
        return SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", id).findFirst();
    }

    private static boolean checkIfIdExist(int index) {
        return SingleyBoo.getRealm().where(NoteModel.class).findAll().where().equalTo("id", index).findFirst() != null;
    }

    public static void organizeNoteIds() {
        int record = 0;
        for (NoteModel n : SingleyBoo.getRealm().where(NoteModel.class).findAll()) {
            if (n.getId() == record) record++;
            else {
                if (!checkIfIdExist(record)) {
                    SingleyBoo.getRealm().beginTransaction();
                    n.setId(record++);
                    SingleyBoo.getRealm().commitTransaction();
                }
            }
        }
    }

    public static void organizeNotes() {
        scrambleNotes();
        int id = 1;
        for (NoteModel m : SingleyBoo.getRealm().where(NoteModel.class).findAll()) {
            SingleyBoo.getRealm().beginTransaction();
            m.setId(id);
            SingleyBoo.getRealm().commitTransaction();
            id++;
        }
    }

    private static void scrambleNotes() {
        Random r = new Random(System.currentTimeMillis());
        int id = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
        for (NoteModel m : SingleyBoo.getRealm().where(NoteModel.class).findAll()) {
            SingleyBoo.getRealm().beginTransaction();
            m.setId(id);
            SingleyBoo.getRealm().commitTransaction();
            id++;
        }
    }

    public static void addFolder(FolderModel model) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().copyToRealm(model);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static RealmList<NoteModel> searchNote(String query) {
        RealmList<NoteModel> list = new RealmList<>();
        for (NoteModel m : getNotes()) {
            if (m.getTitle().toLowerCase().contains(query))
                list.add(m);
        }
        return list;
    }

    public static void removeFromFolder(int position) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", position).findFirst().setFolder(null);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void addToFolder(NoteModel model, String folderName) {
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().where(NoteModel.class).equalTo("id", model.getId()).findFirst().setFolder(folderName);
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void clearAllFolders() {
        SingleyBoo.getRealm().beginTransaction();
        for (FolderModel m : getFolders()) {
            m.deleteFromRealm();
        }
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void deleteFolder(String folder) {
        deleteFolderContents(folder);
        SingleyBoo.getRealm().beginTransaction();
        SingleyBoo.getRealm().where(FolderModel.class).equalTo("name", folder).findFirst().deleteFromRealm();
        SingleyBoo.getRealm().commitTransaction();
    }

    public static void deleteFolderContents(String folder) {
        for (NoteModel m : SingleyBoo.getRealm().where(NoteModel.class).equalTo("folder", folder).findAll()) {
            SingleyBoo.getRealm().beginTransaction();
            m.deleteFromRealm();
            SingleyBoo.getRealm().commitTransaction();
        }
    }

    public static ArrayList<String> getFoldersString() {
        ArrayList<String> folderNames = new ArrayList<>();
        for (FolderModel m : SingleyBoo.getRealm().where(FolderModel.class).findAll()) {
            folderNames.add(m.getName());
        }
        return folderNames;
    }

    public static ArrayList<FolderModel> getFolders() {
        ArrayList<FolderModel> folderModels = new ArrayList<>();
        for (FolderModel m : SingleyBoo.getRealm().where(FolderModel.class).findAll()) {
            folderModels.add(m);
        }
        return folderModels;
    }

    public static ArrayList<NoteModel> getNotes() {
        ArrayList<NoteModel> noteModels = new ArrayList<>();
        for (NoteModel m : SingleyBoo.getRealm().where(NoteModel.class).findAll()) {
            noteModels.add(m);
        }
        return noteModels;
    }

    public static boolean checkIfFolderExists(String folderName) {
        return !SingleyBoo.getRealm().where(FolderModel.class).equalTo("name", folderName).findAll().isEmpty();
    }
}
