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
    /** ��XLog�Ϊ�TAG */
    private static final String TAG = "Ch1702";

    /** Public Key */
    private static final String PUBLIC_KEY = "��J���ε{�������v���_";

    /** ���ε{������item ID */
 // �ä[�����~
    static final String ITEM_GOLD = "gold";
 // �w���ʶR�����~
    static final String ITEM_SILVER = "silver";
 // ���ӫ����~
    static final String ITEM_BRONZE = "bronze";

    /** ���ε{�����ʶR�ݭn��J���N�X */
    static final int RC_REQUEST = 10001;

    /** ���~���֦��X�� */
    boolean mHasGold = false;
    boolean mHasSilver = false;
    boolean mHasBronze = false;

    /** �ϰ� */
    IabHelper mIabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1702_main);
        // �إ����ε{���������~�U��
        mIabHelper = new IabHelper(this, PUBLIC_KEY);

        // �]�w�n�J�ͮ�
        mIabHelper.enableDebugLogging(true);

        // �إ�item�M��
        List<String> itemList = new ArrayList<String>();
        itemList.add("�������~�@500�~(�i�ä[�ϥ�)");
        itemList.add("�ջȲ��~�@300�~(�ݩw���ʶR)");
        itemList.add("�C�ɲ��~�@100�~(���өʨϥ�)");

        // Spinner��l��
        final Spinner spIab = (Spinner) findViewById(R.id.spinnerIab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIab.setAdapter(adapter);

        // �}�l���ε{�������U��Setup
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    // �ˬd���ε{���������~�֦����p
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } else {
                    Log.d(TAG, "Setup����: " + result);
                }
            }
        });

        // �I���ʶR���s
        findViewById(R.id.buyItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exeBuyItem(spIab.getSelectedItemPosition());
            }
        });
    }

    /**
     * ���~���ʶR�B�z
     * 
     * @param itemType
     */
    private void exeBuyItem(int itemType) {
        String payload = "";
        switch (itemType) {
        case 0:
            // �������~
            mIabHelper.launchPurchaseFlow(this, ITEM_GOLD, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 1:
            // �ջȲ��~
            mIabHelper.launchPurchaseFlow(this, ITEM_SILVER,
                    IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 2:
            // �C�ɲ��~
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
        // �˱����ε{���������~�U��
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    /**
     * ���ε{���������~�T�{������ť��
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            if (result.isSuccess()) {
                // �T�{�O�_�֦��������~(�ä[��)
                Purchase itemGold = inventory.getPurchase(ITEM_GOLD);
                if (itemGold != null) {
                    mHasGold = verifyDeveloperPayload(itemGold);
                    Log.d(TAG, "�֦��������~:" + mHasGold);
                }
                // �T�{�O�_�֦��ջȲ��~(�w���ʶR��)
                Purchase itemSilver = inventory.getPurchase(ITEM_SILVER);
                if (itemSilver != null) {
                    mHasSilver = (itemSilver != null && verifyDeveloperPayload(itemSilver));
                    Log.d(TAG, "�֦��ջȲ��~:" + mHasSilver);
                }
                // �T�{�O�_�֦��C�ɲ��~(���ӫ�)
                Purchase itemBronze = inventory.getPurchase(ITEM_BRONZE);
                if (itemBronze != null) {
                    mHasBronze = (itemBronze != null && verifyDeveloperPayload(itemBronze));
                    Log.d(TAG, "�֦��C�ɲ��~:" + mHasSilver);
                    // �ϥξ֦����C�ɲ��~
                    if (mHasBronze) {
                        mIabHelper.consumeAsync(
                                inventory.getPurchase(ITEM_BRONZE),
                                mConsumeFinishedListener);
                        return;
                    }
                } else {
                    Toast.makeText(Ch1702.this, "���֥Φ�����C�ɲ��~",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "���~�M��j�M����: " + result);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // �NonActivityResult�����G�ᤩ���ε{���������~�U��
        if (mIabHelper != null) {
            if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        // TODO �ۭq�����ҹ�@�B�z

        return true;
    }

    /**
     * ���ε{���������~�ʶR������ť��
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isSuccess()) {
                // �ˬd�ʶR�����᪺���ε{�������~���֦����p
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d(TAG, "�L�k���T�ʶR���ε{������item");
                    return;
                }
                if (purchase.getSku().equals(ITEM_GOLD)) {
                    // �֦��������~
                    mHasGold = true;
                } else if (purchase.getSku().equals(ITEM_SILVER)) {
                    // �֦��ջȲ��~
                    mHasSilver = true;
                } else if (purchase.getSku().equals(ITEM_BRONZE)) {
                    // �֦��C�ɲ��~
                    mHasBronze = true;
                }
            }
        }
    };

    /**
     * ���ӫ����~�ϥήɩI�s����ť��
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast.makeText(Ch1702.this, "�ϥΤF�C�ɲ��~",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "���~�ϥΥ���: " + result);
            }
        }
    };
}
