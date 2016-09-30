package imperiumnet.imperious.noteit.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by blaze on 8/9/2016.
 */
@RealmClass
public class FolderModel extends RealmObject {

    @PrimaryKey
    private String name;
    private Date creationDate;
    private String folder;

    public String getFolder() {
        return folder;
    }

    public FolderModel setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public String getName() {
        return name;
    }

    public FolderModel setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public FolderModel setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }
}
