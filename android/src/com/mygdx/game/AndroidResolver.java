package com.mygdx.game;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.pay.PurchaseManagerConfig;

public class AndroidResolver extends PlatformResolver {

    private final static String GOOGLEKEY = "";
    static final int RC_REQUEST = 10001;    // (arbitrary) request code for the purchase flow

    public AndroidApplication androidApplication;
    public MyGdxGame game;

    public AndroidResolver(MyGdxGame game, AndroidApplication androidApplication) {
        super(game);
        this.game = game;
        this.androidApplication = androidApplication;

        PurchaseManagerConfig config = game.purchaseManagerConfig;
        config.addStoreParam(PurchaseManagerConfig.STORE_NAME_ANDROID_GOOGLE, GOOGLEKEY);

        initializeIAP(null, game.purchaseObserver, config);
    }
}