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
    /** 輸出Log用的TAG */
    private static final String TAG = "Ch1702";

    /** Public Key */
    private static final String PUBLIC_KEY = "輸入應用程式的授權金鑰";

    /** 應用程式內的item ID */
 // 永久型產品
    static final String ITEM_GOLD = "gold";
 // 定期購買型產品
    static final String ITEM_SILVER = "silver";
 // 消耗型產品
    static final String ITEM_BRONZE = "bronze";

    /** 應用程式內購買需要輸入的代碼 */
    static final int RC_REQUEST = 10001;

    /** 產品的擁有旗標 */
    boolean mHasGold = false;
    boolean mHasSilver = false;
    boolean mHasBronze = false;

    /** 區域 */
    IabHelper mIabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1702_main);
        // 建立應用程式內的產品助手
        mIabHelper = new IabHelper(this, PUBLIC_KEY);

        // 設定登入生效
        mIabHelper.enableDebugLogging(true);

        // 建立item清單
        List<String> itemList = new ArrayList<String>();
        itemList.add("黃金產品�@500�~(可永久使用)");
        itemList.add("白銀產品�@300�~(需定期購買)");
        itemList.add("青銅產品�@100�~(消耗性使用)");

        // Spinner初始化
        final Spinner spIab = (Spinner) findViewById(R.id.spinnerIab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIab.setAdapter(adapter);

        // 開始應用程式內的助手Setup
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    // 檢查應用程式內的產品擁有狀況
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } else {
                    Log.d(TAG, "Setup失敗: " + result);
                }
            }
        });

        // 點擊購買按鈕
        findViewById(R.id.buyItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exeBuyItem(spIab.getSelectedItemPosition());
            }
        });
    }

    /**
     * 產品的購買處理
     * 
     * @param itemType
     */
    private void exeBuyItem(int itemType) {
        String payload = "";
        switch (itemType) {
        case 0:
            // 黃金產品
            mIabHelper.launchPurchaseFlow(this, ITEM_GOLD, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 1:
            // 白銀產品
            mIabHelper.launchPurchaseFlow(this, ITEM_SILVER,
                    IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 2:
            // 青銅產品
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
        // 捨棄應用程式內的產品助手
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    /**
     * 應用程式內的產品確認結束監聽器
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            if (result.isSuccess()) {
                // 確認是否擁有黃金產品(永久型)
                Purchase itemGold = inventory.getPurchase(ITEM_GOLD);
                if (itemGold != null) {
                    mHasGold = verifyDeveloperPayload(itemGold);
                    Log.d(TAG, "擁有黃金產品:" + mHasGold);
                }
                // 確認是否擁有白銀產品(定期購買型)
                Purchase itemSilver = inventory.getPurchase(ITEM_SILVER);
                if (itemSilver != null) {
                    mHasSilver = (itemSilver != null && verifyDeveloperPayload(itemSilver));
                    Log.d(TAG, "擁有白銀產品:" + mHasSilver);
                }
                // 確認是否擁有青銅產品(消耗型)
                Purchase itemBronze = inventory.getPurchase(ITEM_BRONZE);
                if (itemBronze != null) {
                    mHasBronze = (itemBronze != null && verifyDeveloperPayload(itemBronze));
                    Log.d(TAG, "擁有青銅產品:" + mHasSilver);
                    // 使用擁有的青銅產品
                    if (mHasBronze) {
                        mIabHelper.consumeAsync(
                                inventory.getPurchase(ITEM_BRONZE),
                                mConsumeFinishedListener);
                        return;
                    }
                } else {
                    Toast.makeText(Ch1702.this, "未擁用有任何青銅產品",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "產品清單搜尋失敗: " + result);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 將onActivityResult的結果賦予應用程式內的產品助手
        if (mIabHelper != null) {
            if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        // TODO 自訂的驗證實作處理

        return true;
    }

    /**
     * 應用程式內的產品購買完成監聽器
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isSuccess()) {
                // 檢查購買完成後的應用程式內產品的擁有狀況
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d(TAG, "無法正確購買應用程式內的item");
                    return;
                }
                if (purchase.getSku().equals(ITEM_GOLD)) {
                    // 擁有黃金產品
                    mHasGold = true;
                } else if (purchase.getSku().equals(ITEM_SILVER)) {
                    // 擁有白銀產品
                    mHasSilver = true;
                } else if (purchase.getSku().equals(ITEM_BRONZE)) {
                    // 擁有青銅產品
                    mHasBronze = true;
                }
            }
        }
    };

    /**
     * 消耗型產品使用時呼叫的監聽器
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast.makeText(Ch1702.this, "使用了青銅產品",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "產品使用失敗: " + result);
            }
        }
    };
}
