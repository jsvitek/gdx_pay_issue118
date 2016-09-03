package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.StretchViewport;



public class MyGdxGame extends ApplicationAdapter {
    Stage stage;
    TextArea textArea;

	// ----- app stores -------------------------
	public static final int APPSTORE_UNDEFINED	= 0;
	public static final int APPSTORE_GOOGLE 	= 1;
	public static final int APPSTORE_OUYA 		= 2;
	public static final int APPSTORE_AMAZON 	= 3;
	public static final int APPSTORE_DESKTOP 	= 4;

	private int isAppStore = APPSTORE_UNDEFINED;




	static PlatformResolver m_platformResolver;
	public PurchaseManagerConfig purchaseManagerConfig;


    public MyGdxGame() {
        setAppStore(APPSTORE_GOOGLE);
        purchaseManagerConfig = new PurchaseManagerConfig();
        purchaseManagerConfig.addOffer(new Offer().setType(OfferType.ENTITLEMENT).setIdentifier("android.test.purchased"));
        purchaseManagerConfig.addOffer(new Offer().setType(OfferType.CONSUMABLE).setIdentifier("itemID"));
    }

	public PurchaseObserver purchaseObserver = new PurchaseObserver() {

		@Override
		public void handleRestore (Transaction[] transactions) {
			for (int i = 0; i < transactions.length; i++) {
				if (checkTransaction(transactions[i].getIdentifier()) == true) {
				}
			}
		}

		@Override
		public void handleRestoreError (Throwable e) {
			// getPlatformResolver().showToast("PurchaseObserver: handleRestoreError!");
			Gdx.app.log("ERROR", "PurchaseObserver: handleRestoreError!: " + e.getMessage());
			throw new GdxRuntimeException(e);
		}

		@Override
		public void handleInstall () {
			// getPlatformResolver().showToast("PurchaseObserver: installed successfully...");
			Gdx.app.log("handleInstall: ", "successfully..");
		}

		@Override
		public void handleInstallError (Throwable e) {
			// getPlatformResolver().showToast("PurchaseObserver: handleInstallError!");
			Gdx.app.log("ERROR", "PurchaseObserver: handleInstallError!: " + e.getMessage());
			throw new GdxRuntimeException(e);
		}

		@Override
		public void handlePurchase (Transaction transaction) {
			checkTransaction(transaction.getIdentifier());
		}

		@Override
		public void handlePurchaseError (Throwable e) {
			if (e.getMessage().equals("There has been a Problem with your Internet connection. Please try again later")) {

				// this check is needed because user-cancel is a handlePurchaseError too)
//	             getPlatformResolver().showToast("handlePurchaseError: " + e.getMessage());
			}

//			if (e.getCause().getMessage().equals("Unexpected getBuyIntent()"
//					+ " responseCode: ResponseCode{code=7, message='Failure"
//					+ " to purchase since item is already owned'} with response"
//					+ " data: Bundle[{RESPONSE_CODE=7}]")) {
//			}

            textArea.appendText("handlePurchaseError() called\n");//response code 7
//	        throw new GdxRuntimeException(e);
		}

		@Override
		public void handlePurchaseCanceled () {
            textArea.appendText("handlePurchaseCanceled() called\n");
		}
	};

	protected boolean checkTransaction (String ID) {
		boolean returnbool = false;
	    if ("android.test.purchased".equals(ID)) {
            textArea.appendText("Congratulations, you just purchased ENTITLEMENT\n");
	    	returnbool = true;
	    }
		return returnbool;
	}


	public PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}
	public static void setPlatformResolver (PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}

	public int getAppStore () {
		return isAppStore;
	}
	public void setAppStore (int isAppStore) {
		this.isAppStore = isAppStore;
	}


	@Override
	public void create () {
        setStage(new Stage(new StretchViewport(480, 800)));
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        textArea = new TextArea("PLEASE FOLLOW THESE EXACT STEPS:\n\n" +
                "1. purchase product \"ENTITLEMENT\" successfully\n" +
                "2. try to purchase product \"CONSUMABLE\" but cancel IAB purchase window (handlePurchaseCanceled is going to be called correctly)\n" +
                "3. try to purchase product \"ENTITLEMENT\" again - handlePurchaseError is called with response code 7 \"item already owned\"\n" +
                "4. try to purchase product \"CONSUMABLE\" but cancel IAB purchase window - handlePurchaseCancelled is not going to be called anymore\n" +
                "-------------------------------\n", skin);
        textArea.setPosition(0, 0);
        textArea.setSize(480, 600);
        textArea.setDisabled(true);

        Button.ButtonStyle style1 = new Button.ButtonStyle();
        style1.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("e_up.png"))));
        style1.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("e_down.png"))));

        Button button = new Button(style1);
        button.setPosition(stage.getWidth() / 2 - button.getWidth() / 2,  730);

        Button.ButtonStyle style2 = new Button.ButtonStyle();
        style2.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("c_up.png"))));
        style2.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("c_down.png"))));

        Button button2 = new Button(style2);
        button2.setPosition(stage.getWidth() / 2 - button.getWidth() / 2,  650);


        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getPlatformResolver().requestPurchase("android.test.purchased");
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getPlatformResolver().requestPurchase("itemID");
            }
        });

        stage.addActor(button);
        stage.addActor(button2);
        stage.addActor(textArea);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
	}

	@Override
	public void dispose () {
        stage.dispose();
	}

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
