package com.mycheque.util.local;

/**
 * A useful set of regular expressions being used by the application.
 *
 * @author resxnvnce
 */
public interface RegExp {

    /**
     * The regular expression that generally, but not necessarily, matches a QR code in its raw form.
     */
    String QR_RAW = "^t=\\d{8}T\\d{4}&s=\\d+(?:\\.\\d{1,2})?&fn=\\d{16}&i=\\d{5,6}&fp=\\d{8,10}&n=1$";

    /**
     * The regular expression any valid username must match.
     */
    String USERNAME = "^[a-zA-Z0-9_-]{3,16}$";

    /**
     * The regular expression any valid password must match.
     */
    String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%&])[A-Za-z\\d@#$%&]{4,31}$";
}
