package app.k9mail.feature.account.setup.ui.common.item

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.k9mail.core.ui.compose.designsystem.molecule.ErrorView

@Composable
internal fun LazyItemScope.ErrorItem(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetry: () -> Unit = { },
) {
    ListItem(
        modifier = modifier,
    ) {
        ErrorView(
            title = title,
            message = message,
            onRetry = onRetry,
        )
    }
}
