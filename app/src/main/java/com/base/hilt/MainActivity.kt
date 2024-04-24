package com.base.hilt

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.ActivityMainBinding
import com.base.hilt.ui.login.LoginFragment
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.base.hilt.utils.Utils
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var dialog: Dialog? = null
    private lateinit var binding: ActivityMainBinding

    //private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var mPref: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // auth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().subscribeToTopic("learn2")
        DebugLog.e("onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        //   val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val fragment =
            navHostFragment.childFragmentManager.fragments.firstOrNull { it is LoginFragment }
        // Call revokeAccess() function if the fragment is not null

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*   val appBarConfiguration = AppBarConfiguration(
               setOf(
                   R.id.navigation_home, R.id.navigation_dashboard
               )
           )*/
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.layToolbar.setting.setOnClickListener {

            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.setting)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_Log_out -> {
                        // Facebook logout
                        val accessToken = AccessToken.getCurrentAccessToken()
                        if (accessToken != null) {
                            Log.i("accessToken", "2: $accessToken")
                            LoginManager.getInstance().logOut()
                        }
                        mPref.setValueBoolean(PrefKey.IS_USERlOGIN, false)
                        googleSignInClient.signOut().addOnCompleteListener {
                            if (fragment != null && fragment is LoginFragment) {
                                Log.i("revoke", "revokeAccess: ")
                              //  fragment.revokeAccess()
                            }
                            googleSignInClient.revokeAccess()
                            Toast.makeText(this, "logout click", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    /**
     * Show Progress dialog
     * @param t: true show progress bar
     *
     *  */
    fun displayProgress(t: Boolean) {
        // binding.loading = t
        if (t) {
            if (dialog == null) {
                dialog = Utils.progressDialog(this)
            }
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }

    /**
     * This method is used to hide the keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Toolbar manages items and visibility according to
     */
    fun toolBarManagement(toolbarModel: ToolbarModel?) {
        if (toolbarModel != null) {
            when {
                toolbarModel.isVisible -> {
                    binding.layToolbar.appBar.visibility = View.VISIBLE
                    binding.layToolbar.toolbarTitle.text = toolbarModel.title
                }

                else -> {
                    binding.layToolbar.appBar.visibility = View.GONE
                }
            }
            if (toolbarModel.isSetting) {
                binding.layToolbar.setting.visibility = View.VISIBLE
            } else {
                binding.layToolbar.setting.visibility = View.INVISIBLE
            }
            if (toolbarModel.isBottomNavVisible)
                binding.navView.visibility = View.VISIBLE
            else
                binding.navView.visibility = View.GONE
        }
    }


    /**
     * Override method for fragments attach
     */
    override fun attachBaseContext(base: Context) {
        DebugLog.e("attachBaseContext")
        // super.attachBaseContext(base);
        val languageCode = MyApp.applicationContext().mPref.getValueString(
            PrefKey.SELECTED_LANGUAGE,
            PrefKey.EN_CODE
        )
        useCustomConfig(base, languageCode)?.let { super.attachBaseContext(it) }
    }


    /**
     * Method called when language changes done
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
    }

    /**
     * After applied locale changes override method called
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val languageCode = mPref.getValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.EN_CODE)
        window.decorView.layoutDirection =
            if (languageCode == PrefKey.AR_CODE) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun useCustomConfig(context: Context, languageCode: String?): Context? {
        Locale.setDefault(Locale(languageCode!!))
        return if (Build.VERSION.SDK_INT >= 17) {
            val config = Configuration()
            config.setLocale(Locale(languageCode))
            context.createConfigurationContext(config)
        } else {
            val res: Resources = context.resources
            val config =
                Configuration(res.configuration)
            config.locale = Locale(languageCode)
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }

    /**
     *
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}