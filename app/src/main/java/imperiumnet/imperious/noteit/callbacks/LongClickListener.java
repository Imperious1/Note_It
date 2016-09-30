package imperiumnet.imperious.noteit.callbacks;

import android.support.annotation.Nullable;

/**
 * Created by Shadow on 6/28/2016.
 */
public interface LongClickListener {
    void delete(@Nullable String folderName);
    void moveToFolder(String folderName);
    void removeFromFolder();
    void restore();
}