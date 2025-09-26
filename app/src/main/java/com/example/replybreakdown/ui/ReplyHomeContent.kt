package com.example.replybreakdown.ui

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.replybreakdown.R
import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.data.local.LocalAccountsDataProvider
import com.example.replybreakdown.data.local.LocalEmailsDataProvider
import com.example.replybreakdown.ui.theme.ReplyTheme


@Composable
fun ReplyListAndDetailContent(
    replyUiState: ReplyUiState,
    onEmailChange: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    val emails = replyUiState.currentMailboxEmails
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LazyColumn(
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.email_list_item_vertical_spacing)
            ),
            modifier = modifier.weight(1f)
        ) {
            items(emails, key = {email -> email.id}) { email ->
                EmailListItem(
                    email = email,
                    selected = email == replyUiState.currentSelectedEmail,
                    onCardClicked = { onEmailChange(email) },
                )
            }
        }
        val activity = LocalContext.current as Activity
        ReplyDetailsScreen(
            replyUiState = replyUiState,
            onBackIconPressed = { activity.finish() },
            isFullScreen = replyUiState.isShowingHomePage,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ReplyListOnlyContent(
    replyUiState: ReplyUiState,
    onEmailChange: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    val emails = replyUiState.currentMailboxEmails
    LazyColumn(
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(dimensionResource(R.dimen.email_list_item_vertical_spacing))
    ) {
        item {
            ReplyHomeTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.topbar_padding_vertical))
            )
        }
        items(emails, key = {email -> email.id}) { email ->
            EmailListItem(
                email = email,
                selected = email == replyUiState.currentSelectedEmail,
                onCardClicked = { onEmailChange(email) }
            )
        }
    }
}

@Composable
private fun ReplyHomeTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ReplyLogo(modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size)))
        ReplyProfileImage(
            drawableRes = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(R.string.profile),
            modifier = Modifier.size(dimensionResource(R.dimen.topbar_logo_size))
        )
    }
}

@Composable
private fun EmailListItem(
    email: Email,
    selected: Boolean,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onCardClicked
    ) {
        Column(modifier = modifier
            .padding(dimensionResource(R.dimen.email_list_item_inner_padding))
        ) {
            EmailListItemHeader(
                email = email,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(email.subject),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.email_list_item_subject_body_spacing)
                )
            )
            Text(
                text = stringResource(email.body),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmailListItemHeader(
    email: Email,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyProfileImage(
            drawableRes = email.sender.avatar,
            description = stringResource(email.sender.firstName) + " "
                + stringResource(email.sender.lastName),
            modifier = Modifier.size(dimensionResource(R.dimen.email_header_profile_size))
        )
        Column {
            Text(
                text = stringResource(email.sender.firstName),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(email.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun ReplyLogo(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Image(
        painter = painterResource(R.drawable.logo),
        colorFilter = ColorFilter.tint(color),
        contentDescription = stringResource(R.string.logo)
    )
}

@Composable
fun ReplyProfileImage(
    @DrawableRes drawableRes: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = description,
            modifier = Modifier.clip(CircleShape)
        )
    }
}

@Preview
@Composable
fun PreviewEmailListItem(){
    ReplyTheme {
        EmailListItem(
            email = LocalEmailsDataProvider.allEmails[0],
            selected = true,
            onCardClicked = {}
        )
    }
}

@Preview ()
@Composable
fun PreviewListOnly() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    val uiState = ReplyUiState(
        mailboxes = mailboxes,
        currentSelectedEmail = mailboxes[MailboxType.Inbox]?.get(0)
            ?: LocalEmailsDataProvider.defaultEmail
    )

    ReplyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReplyListOnlyContent(
                replyUiState = uiState,
                onEmailChange = {},
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
            )
        }
    }
}

@Preview ()
@Composable
fun PreviewListAndDetail() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    val uiState = ReplyUiState(
        mailboxes = mailboxes,
        currentSelectedEmail = mailboxes[MailboxType.Inbox]?.get(0)
            ?: LocalEmailsDataProvider.defaultEmail
    )

    ReplyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReplyListAndDetailContent(
                replyUiState = uiState,
                onEmailChange = {},
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = dimensionResource(R.dimen.detail_card_outer_padding_horizontal))
            )
        }
    }
}