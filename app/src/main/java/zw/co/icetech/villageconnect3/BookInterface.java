package zw.co.icetech.villageconnect3;

import android.content.Context;

public interface BookInterface {
    void openBook(String book, String book_url);
    void downlaodPdf(Context context, String fileName, String fileExtension, String destinationDir, String url);
}
