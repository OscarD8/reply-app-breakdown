package com.example.replybreakdown.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.replybreakdown.R
import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.data.local.LocalAccountsDataProvider
import com.example.replybreakdown.data.local.LocalEmailsDataProvider
import com.example.replybreakdown.ui.theme.ReplyTheme
import com.example.replybreakdown.ui.utils.ReplyContentType
import com.example.replybreakdown.ui.utils.ReplyNavigationType

@Composable
fun ReplyHomeScreen(
    replyUiState: ReplyUiState,
    navType: ReplyNavigationType,
    contentType: ReplyContentType,
    onTabPressed: (MailboxType) -> Unit,
    onEmailCardPressed: (Email) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            mailboxType = MailboxType.Inbox,
            icon = Icons.Default.Inbox,
            text = stringResource(id = R.string.tab_inbox)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Sent,
            icon = Icons.Default.Send,
            text = stringResource(id = R.string.tab_sent)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Drafts,
            icon = Icons.Default.Drafts,
            text = stringResource(id = R.string.tab_drafts)
        ),
        NavigationItemContent(
            mailboxType = MailboxType.Spam,
            icon = Icons.Default.Report,
            text = stringResource(id = R.string.tab_spam)
        )
    )
    if (navType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.width(dimensionResource(R.dimen.drawer_width)),
                ) {
                    NavigationDrawerContent(
                        selectedDestination = replyUiState.currentMailbox,
                        navItemContentList = navigationItemContentList,
                        onTabPressed = onTabPressed,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                            .padding(dimensionResource(R.dimen.drawer_padding_content))
                    )
                }
            }
        ) {

        }
    }
}

@Composable
fun ReplyAppContent(
    replyUiState: ReplyUiState,
    navType: ReplyNavigationType,
    contentType: ReplyContentType,
    onEmailCardPressed: (Email) -> Unit,
    onTabPressed: (MailboxType) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier
) {
    Row(modifier = modifier) {
        AnimatedVisibility(visible = navType == ReplyNavigationType.NAVIGATION_RAIL) {
            ReplyNavigationRail(
                currentTab = replyUiState.currentMailbox,
                onTabPressed = onTabPressed,
                navItemContentList = navItemContentList
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (contentType == ReplyContentType.LIST_DETAIL) {
                // call ReplyScreen method for list detail
            } else {
                // call ReplyScreen method for list
            }
            AnimatedVisibility(visible = navType == ReplyNavigationType.BOTTOM_NAVBAR) {
                ReplyNavigationBottomBar(
                    currentTab = replyUiState.currentMailbox,
                    navItemContentList = navItemContentList,
                    onTabPressed = onTabPressed,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ReplyNavigationRail(
    currentTab: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        navItemContentList.forEach { navItem ->
            NavigationRailItem(
                selected = currentTab == navItem.mailboxType,
                onClick = {
                    onTabPressed(
                        navItem.mailboxType
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
private fun ReplyNavigationBottomBar(
    currentTab: MailboxType,
    onTabPressed: (MailboxType) -> Unit,
    navItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navItemContentList.forEach { navItem ->
            NavigationBarItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabPressed(navItem.mailboxType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationDrawerContent(
    selectedDestination: MailboxType,
    navItemContentList: List<NavigationItemContent>,
    onTabPressed: (MailboxType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        NavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.profile_image_padding))
        )
        navItemContentList.forEach { navItem ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(dimensionResource(R.dimen.drawer_padding_header))
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                selected = selectedDestination == navItem.mailboxType,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onTabPressed(navItem.mailboxType) }
            )
        }
    }
}

@Composable
fun NavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyLogo(
            modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size))
        )
        ReplyProfileImage(
            drawableResource = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(R.string.profile),
            modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
        )
    }
}

data class NavigationItemContent(
    val text: String,
    val icon: ImageVector,
    val mailboxType: MailboxType
)


@Preview (showBackground = true)
@Composable
fun PreviewNavDrawer() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    ReplyTheme {
        ReplyHomeScreen(
            replyUiState = ReplyUiState(
                mailboxes = mailboxes
            ),
            navType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER,
            contentType = ReplyContentType.LIST,
            onTabPressed = {mailboxType -> },
            onEmailCardPressed = {email -> },
            onDetailScreenBackPressed = {},

        )
    }
}