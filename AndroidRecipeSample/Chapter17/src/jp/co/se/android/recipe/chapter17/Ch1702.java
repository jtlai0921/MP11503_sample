package jp.co.se.android.recipe.chapter17;

import java.util.ArrayList;
import java.util.List;

import jp.co.se.android.recipe.chapter17.util.IabHelper;
import jp.co.se.android.recipe.chapter17.util.IabResult;
import jp.co.se.android.recipe.chapter17.util.Inventory;
import jp.co.se.android.recipe.chapter17.util.Purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Ch1702 extends Activity {
    /** ¿é¥XLog¥ÎªºTAG */
    private static final String TAG = "Ch1702";

    /** Public Key */
    private static final String PUBLIC_KEY = "¿é¤JÀ³¥Îµ{¦¡ªº±ÂÅvª÷Æ_";

    /** À³¥Îµ{¦¡¤ºªºitem ID */
 // ¥Ã¤[«¬²£«~
    static final String ITEM_GOLD = "gold";
 // ©w´ÁÁÊ¶R«¬²£«~
    static final String ITEM_SILVER = "silver";
 // ®ø¯Ó«¬²£«~
    static final String ITEM_BRONZE = "bronze";

    /** À³¥Îµ{¦¡¤ºÁÊ¶R»İ­n¿é¤Jªº¥N½X */
    static final int RC_REQUEST = 10001;

    /** ²£«~ªº¾Ö¦³ºX¼Ğ */
    boolean mHasGold = false;
    boolean mHasSilver = false;
    boolean mHasBronze = false;

    /** °Ï°ì */
    IabHelper mIabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1702_main);
        // «Ø¥ßÀ³¥Îµ{¦¡¤ºªº²£«~§U¤â
        mIabHelper = new IabHelper(this, PUBLIC_KEY);

        // ³]©wµn¤J¥Í®Ä
        mIabHelper.enableDebugLogging(true);

        // «Ø¥ßitem²M³æ
        List<String> itemList = new ArrayList<String>();
        itemList.add("¶Àª÷²£«~@500‰~(¥i¥Ã¤[¨Ï¥Î)");
        itemList.add("¥Õ»È²£«~@300‰~(»İ©w´ÁÁÊ¶R)");
        itemList.add("«C»É²£«~@100‰~(®ø¯Ó©Ê¨Ï¥Î)");

        // Spinnerªì©l¤Æ
        final Spinner spIab = (Spinner) findViewById(R.id.spinnerIab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIab.setAdapter(adapter);

        // ¶}©lÀ³¥Îµ{¦¡¤ºªº§U¤âSetup
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    // ÀË¬dÀ³¥Îµ{¦¡¤ºªº²£«~¾Ö¦³ª¬ªp
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } else {
                    Log.d(TAG, "Setup¥¢±Ñ: " + result);
                }
            }
        });

        // ÂIÀ»ÁÊ¶R«ö¶s
        findViewById(R.id.buyItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exeBuyItem(spIab.getSelectedItemPosition());
            }
        });
    }

    /**
     * ²£«~ªºÁÊ¶R³B²z
     * 
     * @param itemType
     */
    private void exeBuyItem(int itemType) {
        String payload = "";
        switch (itemType) {
        case 0:
            // ¶Àª÷²£«~
            mIabHelper.launchPurchaseFlow(this, ITEM_GOLD, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 1:
            // ¥Õ»È²£«~
            mIabHelper.launchPurchaseFlow(this, ITEM_SILVER,
                    IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 2:
            // «C»É²£«~
            mIabHelper.launchPurchaseFlow(this, ITEM_BRONZE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        default:
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // ±Ë±óÀ³¥Îµ{¦¡¤ºªº²£«~§U¤â
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    /**
     * À³¥Îµ{¦¡¤ºªº²£«~½T»{µ²§ôºÊÅ¥¾¹
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            if (result.isSuccess()) {
                // ½T»{¬O§_¾Ö¦³¶Àª÷²£«~(¥Ã¤[«¬)
                Purchase itemGold = inventory.getPurchase(ITEM_GOLD);
                if (itemGold != null) {
                    mHasGold = verifyDeveloperPayload(itemGold);
                    Log.d(TAG, "¾Ö¦³¶Àª÷²£«~:" + mHasGold);
                }
                // ½T»{¬O§_¾Ö¦³¥Õ»È²£«~(©w´ÁÁÊ¶R«¬)
                Purchase itemSilver = inventory.getPurchase(ITEM_SILVER);
                if (itemSilver != null) {
                    mHasSilver = (itemSilver != null && verifyDeveloperPayload(itemSilver));
                    Log.d(TAG, "¾Ö¦³¥Õ»È²£«~:" + mHasSilver);
                }
                // ½T»{¬O§_¾Ö¦³«C»É²£«~(®ø¯Ó«¬)
                Purchase itemBronze = inventory.getPurchase(ITEM_BRONZE);
                if (itemBronze != null) {
                    mHasBronze = (itemBronze != null && verifyDeveloperPayload(itemBronze));
                    Log.d(TAG, "¾Ö¦³«C»É²£«~:" + mHasSilver);
                    // ¨Ï¥Î¾Ö¦³ªº«C»É²£«~
                    if (mHasBronze) {
                        mIabHelper.consumeAsync(
                                inventory.getPurchase(ITEM_BRONZE),
                                mConsumeFinishedListener);
                        return;
                    }
                } else {
                    Toast.makeText(Ch1702.this, "¥¼¾Ö¥Î¦³¥ô¦ó«C»É²£«~",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "²£«~²M³æ·j´M¥¢±Ñ: " + result);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ±NonActivityResultªºµ²ªG½á¤©À³¥Îµ{¦¡¤ºªº²£«~§U¤â
        if (mIabHelper != null) {
            if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        // TODO ¦Û­qªºÅçÃÒ¹ê§@³B²z

        return true;
    }

    /**
     * À³¥Îµ{¦¡¤ºªº²£«~ÁÊ¶R§¹¦¨ºÊÅ¥¾¹
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isSuccess()) {
                // ÀË¬dÁÊ¶R§¹¦¨«áªºÀ³¥Îµ{¦¡¤º²£«~ªº¾Ö¦³ª¬ªp
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d(TAG, "µLªk¥¿½TÁÊ¶RÀ³¥Îµ{¦¡¤ºªºitem");
                    return;
                }
                if (purchase.getSku().equals(ITEM_GOLD)) {
                    // ¾Ö¦³¶Àª÷²£«~
                    mHasGold = true;
                } else if (purchase.getSku().equals(ITEM_SILVER)) {
                    // ¾Ö¦³¥Õ»È²£«~
                    mHasSilver = true;
                } else if (purchase.getSku().equals(ITEM_BRONZE)) {
                    // ¾Ö¦³«C»É²£«~
                    mHasBronze = true;
                }
            }
        }
    };

    /**
     * ®ø¯Ó«¬²£«~¨Ï¥Î®É©I¥sªººÊÅ¥¾¹
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast.makeText(Ch1702.this, "¨Ï¥Î¤F«C»É²£«~",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "²£«~¨Ï¥Î¥¢±Ñ: " + result);
            }
        }
    };
}
