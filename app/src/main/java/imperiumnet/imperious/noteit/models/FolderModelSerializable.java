package imperiumnet.imperious.noteit.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by blaze on 8/9/2016.
 */
public class FolderModelSerializable implements Serializable {
    private String name;
    private Date creationDate;
    private String folder;

    public String getFolder() {
        return folder;
    }

    public FolderModelSerializable setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public String getName() {
        return name;
    }

    public FolderModelSerializable setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public FolderModelSerializable setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }
}
