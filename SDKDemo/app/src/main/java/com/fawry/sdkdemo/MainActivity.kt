package com.fawry.sdkdemo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.mylibrary.FawrySdk
import com.app.mylibrary.interfaces.FawryPreLaunch
import com.app.mylibrary.interfaces.FawrySdkCallbacks
import com.app.mylibrary.models.*
import com.app.mylibrary.utils.FawryUtils
import com.fawry.sdk.CoreConfig
import com.fawry.sdk.presenter.MerchantPresenter
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    val merchantPresenter=MerchantPresenter()
    val chargeItems = ArrayList<PayableItem>()

    var baseUrl = "https://atfawry.fawrystaging.com/"
    var customerProfileId = "7117"


    var lang: FawrySdk.Languages = FawrySdk.Languages.ENGLISH
    val chargeItem3DSProduction = BillItems(
        itemId = "d5800a810fad4265a9bbd14fd0a7acdd",
        description = "book",
        quantity = "1",
        price = "3.00"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chargeItems.add(chargeItem3DSProduction)
        CoreConfig.apply {
            merchantCode="siYxylRjSPy1bcPYQhHgRw=="
            secretKey="0a38e95f-c2c2-4bfa-a0c5-f753cdf95256"
            url="https://atfawry.fawrystaging.com"
        }
        val tvHello:TextView=findViewById(R.id.tv_hello)

        tvHello.setOnClickListener {
//            initSDK()
            getMerchantData()
        }

    }

    private fun initSDK(){
        FawrySdk.launchAnonymousSDK(
            this,
            lang,
            baseUrl,
            FawryLaunchModel(
                launchMerchantModel = LaunchMerchantModel(
                    merchantCode = "siYxylRjSPy1bcPYQhHgRw==",
                    secretCode = "0a38e95f-c2c2-4bfa-a0c5-f753cdf95256",
                    merchantRefNum = FawryUtils.randomAlphaNumeric(10)
                ), launchCustomerModel = LaunchCustomerModel(
                    customerMobile = "01234567877",
                    customerEmail = "test@test.com",
                    customerProfileId = customerProfileId
                ),
                allow3DPayment = true,
                chargeItems = chargeItems,
                skipReceipt = false,
                skipLogin = true,
                payWithCardToken = true
            ), object : FawrySdkCallbacks {
                override fun onPreLaunch(onPreLaunch: FawryPreLaunch) {
                    onPreLaunch.onContinue()
                }

                override fun onInit() {

                }

                override fun onPaymentCompleted(msg: String, data: Any?) {
                    //called either failed or success payment
//                    Toast.makeText(this@MainActivity, "$data", Toast.LENGTH_SHORT).show()
                    Log.d("hitler", "${data}")
                }

                override fun onSuccess(msg: String, data: Any?) {
//                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    Log.d("hitler", "${data}")
                }

                override fun onFailure(error: String) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getMerchantData() {
        runBlocking {
            val response = merchantPresenter.getMerchantData()
            response.onSuccess {
                Log.d("Jerome", "merchant data: ${it}")
            }
            response.onError {
                Log.d("Jerome", "merchant msg: ${it}")
            }

        }
    }
}