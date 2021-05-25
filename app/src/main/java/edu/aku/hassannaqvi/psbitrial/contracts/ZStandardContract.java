package edu.aku.hassannaqvi.psbitrial.contracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author hassan.naqvi.
 */

public class ZStandardContract {
    public static String CONTENT_AUTHORITY = "edu.aku.hassannaqvi.psbitrial";

    public static abstract class ZScoreTable implements BaseColumns {

        public static final String TABLE_NAME = "zstandards";

        public static final String _ID = "_id";
        public static final String COLUMN_SEX = "sex";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_L = "l";
        public static final String COLUMN_M = "m";
        public static final String COLUMN_S = "s";
        public static final String COLUMN_CAT = "cat";
        public static final String SERVER_URI = "zstandards.php";
        public static String PATH = "zstandards";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        //        public static final String REGION_DSS = "region";
        public static Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
                .buildUpon().appendPath(PATH).build();

        public static String getMovieKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}