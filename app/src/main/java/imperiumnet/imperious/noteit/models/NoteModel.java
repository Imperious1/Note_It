package imperiumnet.imperious.noteit.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by blaze on 8/9/2016.
 */
@RealmClass
public class NoteModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String note;
    private String title;
    private String folder;
    private boolean synced;
    private Date creationDate;
    private boolean edited;
    private boolean trash;

    public boolean isTrash() {
        return trash;
    }

    public NoteModel setTrash(boolean trash) {
        this.trash = trash;
        return this;
    }

    public String getFolder() {
        return folder;
    }

    public NoteModel setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public int getId() {
        return id;
    }

    public NoteModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getNote() {
        return note;
    }

    public NoteModel setNote(String note) {
        this.note = note;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NoteModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isSynced() {
        return synced;
    }

    public NoteModel setSynced(boolean synced) {
        this.synced = synced;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public NoteModel setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public boolean isEdited() {
        return edited;
    }

    public NoteModel setEdited(boolean edited) {
        this.edited = edited;
        return this;
    }
}
