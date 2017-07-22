package app.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import app.MainApplication
import kotlinx.android.synthetic.main.activity_login.login_button
import kotlinx.android.synthetic.main.activity_login.progress
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class TinderLoginActivity : Activity(), TinderFacebookLoginFeature.ResultCallback {
    @Inject
    lateinit var tinderFacebookLoginFeature: TinderFacebookLoginFeature
    @Inject
    lateinit var tinderFacebookLoginCoordinator: TinderFacebookLoginCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inject()
        tinderFacebookLoginFeature.bind()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tinderFacebookLoginFeature.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        unbindFacebookLoginFeature()
        cancelOngoingTinderLogin()
        super.onDestroy()
    }

    override fun onSuccess(facebookId: String, facebookToken: String) {
        requestTinderLogin(facebookId, facebookToken)
    }

    private fun inject() = (application as MainApplication).applicationComponent
            .newTinderFacebookLoginComponent(
                    TinderFacebookLoginModule(this, login_button, progress, this))
            .inject(this)

    private fun unbindFacebookLoginFeature() = tinderFacebookLoginFeature.release(login_button)

    private fun requestTinderLogin(facebookId: String, facebookToken: String) {
        tinderFacebookLoginCoordinator.actionDoLogin(facebookId, facebookToken)
    }

    private fun cancelOngoingTinderLogin() = tinderFacebookLoginCoordinator.actionCancelLogin()

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, TinderLoginActivity::class.java)
    }
}
