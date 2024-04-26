package com.manoj.googlewalletdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.pay.Pay
import com.google.android.gms.pay.PayApiAvailabilityStatus
import com.google.android.gms.pay.PayClient
import com.manoj.googlewalletdemo.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var layout: ActivityMainBinding
    private lateinit var addToGoogleWalletButton: View

    private val addToGoogleWalletRequestCode = 1000

    private lateinit var walletClient: PayClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Instantiate the Pay client
        walletClient = Pay.getClient(this)

        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        fetchCanUseGoogleWalletApi()

        addToGoogleWalletButton = layout.addToGoogleWalletButton.root
        addToGoogleWalletButton.setOnClickListener {
            walletClient.savePasses(
                jsonObject,
                this,
                addToGoogleWalletRequestCode
            )
        }
    }

    // TODO: Create a method to check for the Google Wallet SDK and API
    private fun fetchCanUseGoogleWalletApi() {
        walletClient
            .getPayApiAvailabilityStatus(PayClient.RequestType.SAVE_PASSES)
            .addOnSuccessListener { status ->
                layout.passContainer.isVisible = status == PayApiAvailabilityStatus.AVAILABLE
            }
            .addOnFailureListener {
                // Hide the button and optionally show an error message
            }
    }

    // TODO: Handle the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addToGoogleWalletRequestCode) {
            when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }

                RESULT_CANCELED -> {
                    // Save canceled
                }

                PayClient.SavePassesResult.SAVE_ERROR -> data?.let { intentData ->
                    val errorMessage = intentData.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE)
                    Log.e("ErrorMessage----->>", "onActivityResult: $errorMessage")
                    Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
                    // Handle error. Consider informing the user.
                }

                PayClient.SavePassesResult.INTERNAL_ERROR -> data?.let {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }

                PayClient.SavePassesResult.API_UNAVAILABLE -> data?.let {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    // Handle unexpected (non-API) exception
                }
            }
        }
    }


    private val jsonObject = """{
    "iss": "Enter your issuer email",
    "aud": "google",
    "origins": [],
    "typ": "savetowallet",
    "payload": {
        "genericClasses": [
            {
                "id": "enter_your_issuer_id.unique_class_id",
                "classTemplateInfo": {
                    "cardTemplateOverride": {
                        "cardRowTemplateInfos": [
                            {
                                "twoItems": {
                                    "startItem": {
                                        "firstValue": {
                                            "fields": [
                                                {
                                                    "fieldPath": "object.textModulesData['profile']"
                                                }
                                            ]
                                        }
                                    },
                                    "endItem": {
                                        "firstValue": {
                                            "fields": [
                                                {
                                                    "fieldPath": "object.textModulesData['phone']"
                                                }
                                            ]
                                        }
                                    }
                                }
                            },
                            {
                                "oneItem": {
                                    "item": {
                                        "firstValue": {
                                            "fields": [
                                                {
                                                    "fieldPath": "object.textModulesData['desc']"
                                                }
                                            ]
                                        }
                                    }
                                }
                            }
                        ]
                    }
                }
            }
        ],
        "genericObjects": [
            {
                "id": "enter_your_issuer_id.same_unique_class_id",
                "id": "enter_your_issuer_id.same_unique_class_id",
                "genericType": "GENERIC_TYPE_UNSPECIFIED",
                "logo": {
                    "sourceUri": {
                        "uri": "https://utifo.com/Utifojava-Assests/223/1505/file_1505_1707394315436.jpg"
                    },
                    "contentDescription": {
                        "defaultValue": {
                            "language": "en",
                            "value": "test"
                        }
                    }
                },
                "cardTitle": {
                    "defaultValue": {
                        "language": "en",
                        "value": "Android"
                    }
                },
                "subheader": {
                    "defaultValue": {
                        "language": "en",
                        "value": "Name"
                    }
                },
                "header": {
                    "defaultValue": {
                        "language": "en",
                        "value": "Manoj Sharma"
                    }
                },
                "textModulesData": [
                    {
                        "id": "profile",
                        "header": "Profile",
                        "body": "Android"
                    },
                    {
                        "id": "phone",
                        "header": "Phone Number",
                        "body": "0987654321"
                    },
                    {
                        "id": "desc",
                        "header": "Description",
                        "body": "Life is death enjoy the journey"
                    }
                ],
             
                "hexBackgroundColor": "#00A3FF",
                "heroImage": {
                    "sourceUri": {
                        "uri": "https://mobilecidc.quikcard.com/public/assets/android/HeroImage3.jpg"
                    },
                    "contentDescription": {
                        "defaultValue": {
                            "language": "en",
                            "value": "HERO_IMAGE_DESCRIPTION"
                        }
                    }
                }
            }
        ]
    },
    "iat": 1680012899
}"""
}