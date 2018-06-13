package com.a8plus1.seen.Bean;

import android.net.Uri;
import android.os.Parcel;


public class MIFriend extends io.rong.imlib.model.UserInfo{
    public MIFriend(Parcel in) {
        super(in);
    }

    public MIFriend(String id, String name, Uri portraitUri) {
        super(id, name, portraitUri);
    }
}
