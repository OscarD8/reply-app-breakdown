package com.example.replybreakdown.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.R
import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.local.LocalAccountsDataProvider
import com.example.replybreakdown.data.local.LocalEmailsDataProvider
import com.example.replybreakdown.ui.theme.ReplyTheme
import com.example.replybreakdown.ui.utils.ReplyContentType
import com.example.replybreakdown.ui.utils.ReplyNavigationType

@Composable
fun ReplyHomeScreen(
    replyUiState: ReplyUiState,
    navigationType: ReplyNavigationType,
    contentType: ReplyContentType,
    onTabChange: (MailboxType) -> Unit,
    onEmailClicked: (Email) -> Unit,
    onDetailsScreenBackPressed: () -> Unit,
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
            icon = Icons.AutoMirrored.Filled.Send,
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

    if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        val navigationDrawerContentDesc = stringResource(R.string.navigation_drawer)
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.width(dimensionResource(R.dimen.drawer_width)),
                    drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    ReplyNavigationDrawerContent(
                        currentTab = replyUiState.currentMailbox,
                        onTabChange = onTabChange,
                        navigationItems = navigationItemContentList,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(dimensionResource(R.dimen.drawer_padding_content))
                    )
                }
            },
            modifier = Modifier.testTag(navigationDrawerContentDesc)
        ) {
            ReplyAppContent(
                replyUiState = replyUiState,
                contentType = contentType,
                navigationType = navigationType,
                onTabChange = onTabChange,
                onEmailChange = onEmailClicked,
                navigationItems = navigationItemContentList,
                modifier = modifier
            )
        }
    } else {
        if (replyUiState.isShowingHomePage) {
            ReplyAppContent(
                replyUiState = replyUiState,
                contentType = contentType,
                navigationType = navigationType,
                onTabChange = onTabChange,
                onEmailChange = onEmailClicked,
                navigationItems = navigationItemContentList,
                modifier = modifier
            )
        } else {
            ReplyDetailsScreen(
                replyUiState = replyUiState,
                onBackIconPressed = onDetailsScreenBackPressed,
                modifier = modifier,
                isFullScreen = true
            )
        }
    }
}

@Composable
private fun ReplyAppContent(
    replyUiState: ReplyUiState,
    contentType: ReplyContentType,
    navigationType: ReplyNavigationType,
    navigationItems: List<NavigationItemContent>,
    onEmailChange: (Email) -> Unit,
    onTabChange: (MailboxType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val navigationRailContentDesc = stringResource(R.string.navigation_rail)
        AnimatedVisibility(visible = navigationType == ReplyNavigationType.NAVIGATION_RAIL) {
            ReplyNavigationRail(
                navigationItems = navigationItems,
                currentTab = replyUiState.currentMailbox,
                onTabChange = onTabChange,
                modifier = Modifier.testTag(navigationRailContentDesc)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            if (contentType == ReplyContentType.LIST_DETAIL) {
                ReplyListAndDetailContent(
                    replyUiState = replyUiState,
                    onEmailChange = onEmailChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensionResource(R.dimen.list_padding))
                )
            } else {
                ReplyListOnlyContent(
                    replyUiState = replyUiState,
                    onEmailChange = onEmailChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensionResource(R.dimen.list_padding))
                )
            }
            AnimatedVisibility(visible = navigationType == ReplyNavigationType.BOTTOM_NAVBAR) {
                val bottomNavBarContentDesc = stringResource(R.string.navigation_bottom)
                ReplyBottomNavBar(
                    navigationItems = navigationItems,
                    onTabChange = onTabChange,
                    currentTab = replyUiState.currentMailbox,
                    modifier = Modifier.testTag(bottomNavBarContentDesc)
                )
            }
        }
    }
}


@Composable
private fun ReplyBottomNavBar(
    navigationItems: List<NavigationItemContent>,
    currentTab: MailboxType,
    onTabChange: (MailboxType) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navigationItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentTab == navItem.mailboxType,
                onClick = { onTabChange(navItem.mailboxType) },
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
private fun ReplyNavigationRail(
    navigationItems: List<NavigationItemContent>,
    currentTab: MailboxType,
    onTabChange: (MailboxType) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        navigationItems.forEach { navItem ->
            NavigationRailItem(
                selected = navItem.mailboxType == currentTab,
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                onClick = { onTabChange(navItem.mailboxType) }
            )
        }
    }
}

@Composable
private fun ReplyNavigationDrawerContent(
    currentTab: MailboxType,
    onTabChange: (MailboxType) -> Unit,
    navigationItems: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ReplyNavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.drawer_padding_header))
        )
        navigationItems.forEach { navItem ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = navItem.text,
                    )
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                },
                onClick = { onTabChange(navItem.mailboxType) },
                selected = currentTab == navItem.mailboxType
            )
        }
    }
}

@Composable
private fun ReplyNavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyLogo(modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size)))
        ReplyProfileImage(
            drawableRes = LocalAccountsDataProvider.defaultAccount.avatar,
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

@Preview
@Composable
private fun PreviewPermDrawer() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    ReplyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReplyHomeScreen(
                replyUiState = ReplyUiState(mailboxes = mailboxes),
                navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER,
                contentType = ReplyContentType.LIST_DETAIL,
                onTabChange = {},
                modifier = Modifier.fillMaxSize(),
                onEmailClicked = {},
                onDetailsScreenBackPressed = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNavRail() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    ReplyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReplyHomeScreen(
                replyUiState = ReplyUiState(mailboxes = mailboxes),
                navigationType = ReplyNavigationType.NAVIGATION_RAIL,
                contentType = ReplyContentType.LIST_ONLY,
                onTabChange = {},
                modifier = Modifier.fillMaxSize(),
                onEmailClicked = {},
                onDetailsScreenBackPressed = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNavBar() {
    val mailboxes = LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
    ReplyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReplyHomeScreen(
                replyUiState = ReplyUiState(mailboxes = mailboxes),
                navigationType = ReplyNavigationType.BOTTOM_NAVBAR,
                contentType = ReplyContentType.LIST_ONLY,
                onTabChange = {},
                modifier = Modifier.fillMaxSize(),
                onEmailClicked = {},
                onDetailsScreenBackPressed = {}
            )
        }
    }
}