package zw.co.icetech.villageconnect3;

import android.content.Context;

public interface PrimaryBooksInterface {
    void openBook( String book_url);
    void downlaodPdf(Context context, String fileName, String fileExtension, String destinationDir, String url);
}

