package zw.co.icetech.villageconnect3;

import android.content.Context;

public interface ppaperInterface {
    void openPaper( String paperUrl);
    void downlaodPdf(Context context, String fileName, String fileExtension, String destinationDir, String url);
}
