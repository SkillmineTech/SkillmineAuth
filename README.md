# Integrating `SkillmineAuthSDK` Library in Your Android App

## Introduction

The `SkillmineAuthSDK` Library enables seamless authentication integration in Android applications. It facilitates handling OAuth2 flows, allowing developers to easily manage user authentication and token retrieval within their apps.

## Prerequisites

Ensure you have the following before starting:

- Android Studio installed.
- An existing Android project.
- Minimum SDK version 21 or higher.
- Internet permission in your `AndroidManifest.xml` file:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />

# SkillmineAuth Library

## Installation
To use the AuthWeb library, add it to your project dependencies. Open your build.gradle file (Module: app) and add the following:

Sync your project to download and include the dependency.

```gradle
## auth_web_version = "1.0.5"
implementation "com.github.SkillmineTech:SkillmineAuthSDK:${auth_web_version}

```

## Authentication Flow
To initiate the authentication process, you can create an intent to open the Authentication Activity provided by the library. You can use an ActivityResultLauncher to handle the authentication process results.

Define authActivityResultLauncher in the Activity
Here, we want to open the Authentication Activity from LoginActivity.

```gradle
// Define the BASE_URL and CLIENT_ID
const val BASE_URL = "base_url"
const val CLIENT_ID = "client_id"
const val REDIRECT_URL = "redirect_url"
```
Initialize the authActivityResultLauncher:

```val authActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // Handle the result here
        val accessToken = data?.getStringExtra("access_token")
        // Use the access token as needed
    }
}
```
Open the Library’s Authentication Activity:
```loginButton.setOnClickListener {
    val intent =
                AuthenticationActivity.createIntent(this, BASE_URL, CLIENT_ID, REDIRECT_URL)
            authActivityResultLauncher.launch(intent)
}
```
## Conclusion
This guide has covered the basic steps to integrate and use the auth_webview_sdk Package in your Flutter app. For more advanced configurations and troubleshooting, contact Skillmine Technologies.