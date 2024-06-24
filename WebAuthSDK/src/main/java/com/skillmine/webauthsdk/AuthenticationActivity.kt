package com.skillmine.webauthsdk

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var baseUrl: String
    private lateinit var clientId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val webView: WebView = findViewById(R.id.webview_layout)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        baseUrl = intent.getStringExtra("baseUrl").toString()
        clientId = intent.getStringExtra("clientId").toString()

        webView.clearCache(true)
        webView.clearHistory()

        webView.setInitialScale(0)
        webView.isVerticalScrollBarEnabled = false
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.isScrollbarFadingEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        // Encode URL properly
        // Define your base URL, client ID, redirect URI, etc.
     //   val baseUrl = "https://nightly-accounts-api.complyment.com/authz-srv/authz"
       // val clientId = "236b91c8-b2f0-4891-a83c-f358a109a843"
        val redirectUri = "http://localhost:3000"

        // Build the query string
        val queryParams = buildString {
            append("client_id=").append(clientId)
            append("&response_type=token")
            append("&scope=openid%20profile%20user_info_all")
            append("&redirect_uri=").append(redirectUri)
            append("&groups_info=0")
            append("&response_mode=query")
        }

        // Combine base URL, paths, and query string
        val completeUrl = "$baseUrl?$queryParams"
        Log.d("WebView", "Initial URL: $completeUrl")
        // Load the initial URL
        webView.loadUrl(completeUrl)

        // Load the initial URL
        /*  webView.loadUrl(
              "https://nightly-accounts-api.complyment.com/authz-srv/authz?\n" +
                      "client_id=236b91c8-b2f0-4891-a83c-f358a109a843&response_type=token&scope=openid%20profile%20user_info_all&redirect_uri=\n" +
                      "http://localhost:3000&\n" +
                      "groups_info=0&response_mode=query"
          )
  */
        /*  webView.loadUrl(
              "https://nightly-accounts.complyment.com/htm/login.html?view_type=login&request_id=61ec4de7-1544-4a29-af62-c0d39e78f4fd&client_id=856a049c-aef4-4151-8db0-e79a2c99f8b1&flow_id=8c94d4f5-1201-4408-a636-e8c3bd76fb09"
          )*/


        // Use a custom WebViewClient to handle URL loading
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                if (url.startsWith("https://nightly-accounts.complyment.com/profile/personal-detail")) {
                    // Handle the redirect here, extract the token or authorization code if needed
                    // Example: val token = Uri.parse(url).getQueryParameter("token")
                    // progressBar.visibility = View.VISIBLE
                    return true
                }
                if (url.startsWith("http://localhost:3000")) {
                    val uri = Uri.parse(url)
                    accessToken = uri.getQueryParameter("access_token").toString()

                    accessToken.let {
                        Toast.makeText(
                            this@AuthenticationActivity,
                            "Access Token Received",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val resultIntent = Intent()
                    resultIntent.putExtra("access_token", accessToken)
                    setResult(RESULT_OK,resultIntent)
                    finish()


                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //   progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //   progressBar.visibility = View.GONE
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed() // Ignore SSL certificate errors
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                //   progressBar.visibility = View.GONE
                Log.e("WebAuth", "Error: ${error?.description}, Code: ${error?.errorCode}")
                super.onReceivedError(view, request, error)
            }
        }


        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = false
        webSettings.domStorageEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // Use default cache settings

    }
}