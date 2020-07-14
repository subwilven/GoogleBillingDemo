package com.islam.googlebillingdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*


class MainActivity : AppCompatActivity(),PurchasesUpdatedListener {

    lateinit private var billingClient: BillingClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        billingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    querySkuDetails()
                    Toast.makeText(this@MainActivity,"Done",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the   startConnection() method.
                Toast.makeText(this@MainActivity,"Disconnected",Toast.LENGTH_SHORT).show()

            }
        })
    }
    fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("test25")
       // skuList.add("gas")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    for (skuDetails in skuDetailsList) {
                        val flowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .build()
                        billingClient.launchBillingFlow(this@MainActivity, flowParams)
                    }}
            }
        // Process the result.
    }

    override fun onPurchasesUpdated(p0: BillingResult?, p1: MutableList<Purchase>?) {

    }
}
