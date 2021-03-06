package data.tinder.login

import domain.login.DomainAuthRequestParameters
import domain.login.DomainAuthedUser
import domain.login.LoginProvider
import io.reactivex.Single
import reporter.CrashReporter

internal class LoginProviderImpl(
        private val loginFacade: LoginFacade,
        private val crashReporter: CrashReporter) : LoginProvider {
    override fun login(parameters: DomainAuthRequestParameters): Single<DomainAuthedUser> =
            loginFacade.fetch(parameters).doOnError { crashReporter.report(it) }
}
