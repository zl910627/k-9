package app.k9mail.feature.account.setup.ui.common.mapper

import app.k9mail.autodiscovery.api.AutoDiscoveryResult
import app.k9mail.autodiscovery.api.ImapServerSettings
import app.k9mail.autodiscovery.api.SmtpServerSettings
import app.k9mail.core.common.net.toHostname
import app.k9mail.core.common.net.toPort
import app.k9mail.feature.account.setup.domain.entity.AutoDiscoveryAuthenticationType
import app.k9mail.feature.account.setup.domain.entity.AutoDiscoveryConnectionSecurity
import app.k9mail.feature.account.setup.domain.entity.IncomingProtocolType
import app.k9mail.feature.account.setup.domain.entity.toConnectionSecurity
import app.k9mail.feature.account.setup.domain.input.NumberInputField
import app.k9mail.feature.account.setup.domain.input.StringInputField
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract
import app.k9mail.feature.account.setup.ui.incoming.AccountIncomingConfigContract
import app.k9mail.feature.account.setup.ui.options.AccountOptionsContract
import app.k9mail.feature.account.setup.ui.outgoing.AccountOutgoingConfigContract
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class AccountAutoDiscoveryStateMapperKtTest {

    @Test
    fun `should map to default IncomingConfigState when empty`() {
        val incomingConfigState = EMPTY_STATE.toIncomingConfigState()

        assertThat(incomingConfigState).isEqualTo(AccountIncomingConfigContract.State())
    }

    @Test
    fun `should map to IncomingConfigState when no AutoDiscovery`() {
        val incomingConfigState = EMAIL_PASSWORD_STATE.toIncomingConfigState()

        assertThat(incomingConfigState).isEqualTo(
            AccountIncomingConfigContract.State(
                username = StringInputField(value = EMAIL_ADDRESS),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to IncomingConfigState when AutoDiscovery`() {
        val incomingConfigState = AUTO_DISCOVERY_STATE.toIncomingConfigState()

        assertThat(incomingConfigState).isEqualTo(
            AccountIncomingConfigContract.State(
                protocolType = IncomingProtocolType.IMAP,
                server = StringInputField(value = AUTO_DISCOVERY_HOSTNAME.value),
                security = AUTO_DISCOVERY_SECURITY.toConnectionSecurity(),
                port = NumberInputField(value = AUTO_DISCOVERY_PORT_IMAP.value.toLong()),
                username = StringInputField(value = AUTO_DISCOVERY_USERNAME),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to email username IncomingConfigState when AutoDiscovery empty username`() {
        val incomingConfigState = AUTO_DISCOVERY_STATE_USERNAME_EMPTY.toIncomingConfigState()

        assertThat(incomingConfigState).isEqualTo(
            AccountIncomingConfigContract.State(
                protocolType = IncomingProtocolType.IMAP,
                server = StringInputField(value = AUTO_DISCOVERY_HOSTNAME.value),
                security = AUTO_DISCOVERY_SECURITY.toConnectionSecurity(),
                port = NumberInputField(value = AUTO_DISCOVERY_PORT_IMAP.value.toLong()),
                username = StringInputField(value = EMAIL_ADDRESS),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to OutgoingConfigState when empty`() {
        val outgoingConfigState = EMPTY_STATE.toOutgoingConfigState()

        assertThat(outgoingConfigState).isEqualTo(AccountOutgoingConfigContract.State())
    }

    @Test
    fun `should map to OutgoingConfigState when no AutoDiscovery`() {
        val outgoingConfigState = EMAIL_PASSWORD_STATE.toOutgoingConfigState()

        assertThat(outgoingConfigState).isEqualTo(
            AccountOutgoingConfigContract.State(
                username = StringInputField(value = EMAIL_ADDRESS),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to OutgoingConfigState when AutoDiscovery`() {
        val outgoingConfigState = AUTO_DISCOVERY_STATE.toOutgoingConfigState()

        assertThat(outgoingConfigState).isEqualTo(
            AccountOutgoingConfigContract.State(
                server = StringInputField(value = AUTO_DISCOVERY_HOSTNAME.value),
                security = AUTO_DISCOVERY_SECURITY.toConnectionSecurity(),
                port = NumberInputField(value = AUTO_DISCOVERY_PORT_SMTP.value.toLong()),
                username = StringInputField(value = AUTO_DISCOVERY_USERNAME),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to email username OutgoingConfigState when AutoDiscovery empty username`() {
        val outgoingConfigState = AUTO_DISCOVERY_STATE_USERNAME_EMPTY.toOutgoingConfigState()

        assertThat(outgoingConfigState).isEqualTo(
            AccountOutgoingConfigContract.State(
                server = StringInputField(value = AUTO_DISCOVERY_HOSTNAME.value),
                security = AUTO_DISCOVERY_SECURITY.toConnectionSecurity(),
                port = NumberInputField(value = AUTO_DISCOVERY_PORT_SMTP.value.toLong()),
                username = StringInputField(value = EMAIL_ADDRESS),
                password = StringInputField(value = PASSWORD),
            ),
        )
    }

    @Test
    fun `should map to OptionsState when empty`() {
        val optionsState = EMPTY_STATE.toOptionsState()

        assertThat(optionsState).isEqualTo(AccountOptionsContract.State())
    }

    @Test
    fun `should map to OptionsState when email and password set`() {
        val optionsState = EMAIL_PASSWORD_STATE.toOptionsState()

        assertThat(optionsState).isEqualTo(
            AccountOptionsContract.State(
                accountName = StringInputField(value = EMAIL_ADDRESS),
            ),
        )
    }

    private companion object {
        const val EMAIL_ADDRESS = "test@example.com"
        const val PASSWORD = "password"
        const val SERVER_IMAP = "imap.example.com"
        const val SERVER_SMTP = "smtp.example.com"

        val AUTO_DISCOVERY_HOSTNAME = "incoming.example.com".toHostname()
        val AUTO_DISCOVERY_PORT_IMAP = 143.toPort()
        val AUTO_DISCOVERY_PORT_SMTP = 587.toPort()
        val AUTO_DISCOVERY_SECURITY = AutoDiscoveryConnectionSecurity.StartTLS
        val AUTO_DISCOVERY_AUTHENTICATION = AutoDiscoveryAuthenticationType.PasswordEncrypted
        const val AUTO_DISCOVERY_USERNAME = "username"

        val EMPTY_STATE = AccountAutoDiscoveryContract.State()

        val EMAIL_PASSWORD_STATE = AccountAutoDiscoveryContract.State(
            emailAddress = StringInputField(value = EMAIL_ADDRESS),
            password = StringInputField(value = PASSWORD),
        )

        val AUTO_DISCOVERY_STATE = EMAIL_PASSWORD_STATE.copy(
            autoDiscoverySettings = AutoDiscoveryResult.Settings(
                incomingServerSettings = ImapServerSettings(
                    hostname = AUTO_DISCOVERY_HOSTNAME,
                    port = AUTO_DISCOVERY_PORT_IMAP,
                    connectionSecurity = AUTO_DISCOVERY_SECURITY,
                    authenticationType = AUTO_DISCOVERY_AUTHENTICATION,
                    username = AUTO_DISCOVERY_USERNAME,
                ),
                outgoingServerSettings = SmtpServerSettings(
                    hostname = AUTO_DISCOVERY_HOSTNAME,
                    port = AUTO_DISCOVERY_PORT_SMTP,
                    connectionSecurity = AUTO_DISCOVERY_SECURITY,
                    authenticationType = AUTO_DISCOVERY_AUTHENTICATION,
                    username = AUTO_DISCOVERY_USERNAME,
                ),
                isTrusted = true,
            ),
        )

        val AUTO_DISCOVERY_STATE_USERNAME_EMPTY = AUTO_DISCOVERY_STATE.copy(
            autoDiscoverySettings = AUTO_DISCOVERY_STATE.autoDiscoverySettings?.copy(
                incomingServerSettings = (
                    AUTO_DISCOVERY_STATE.autoDiscoverySettings
                        ?.incomingServerSettings as ImapServerSettings
                    ).copy(
                    username = "",
                ),
                outgoingServerSettings = (
                    AUTO_DISCOVERY_STATE.autoDiscoverySettings
                        ?.outgoingServerSettings as SmtpServerSettings
                    ).copy(
                    username = "",
                ),
            ),
        )
    }
}
