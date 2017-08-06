package com.github.abdurahmanovart.notesrealm.manager;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Abdurakhmanov on 29.07.17
 */

public class RealmManager {

    private Realm mRealm;

    public RealmManager(Context context) {
        mRealm = Realm.getInstance(
                new RealmConfiguration.Builder(context)
                        .name("notesRealm.realm")
                        .build());
    }

    public Realm getRealm() {
        return mRealm;
    }
}
