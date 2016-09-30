package imperiumnet.imperious.noteit.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Shadow on 6/29/2016.
 */
public class NoteModelSerializable implements Serializable {
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

    public NoteModelSerializable setTrash(boolean trash) {
        this.trash = trash;
        return this;
    }

    public String getFolder() {
        return folder;
    }

    public NoteModelSerializable setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public boolean isEdited() {
        return edited;
    }

    public NoteModelSerializable setEdited(boolean edited) {
        this.edited = edited;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public NoteModelSerializable setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public int getId() {
        return id;
    }

    public NoteModelSerializable setId(int id) {
        this.id = id;
        return this;
    }

    public String getNote() {
        return note;
    }

    public NoteModelSerializable setNote(String note) {
        this.note = note;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NoteModelSerializable setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isSynced() {
        return synced;
    }

    public NoteModelSerializable setSynced(boolean synced) {
        this.synced = synced;
        return this;
    }
}