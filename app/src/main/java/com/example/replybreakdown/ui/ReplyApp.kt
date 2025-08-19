package com.example.replybreakdown.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.ui.utils.ReplyContentType
import com.example.replybreakdown.ui.utils.ReplyNavigationType

@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: ReplyViewModel = viewModel()
    val uiState: ReplyUiState by viewModel.uiState.collectAsState()
    val contentType: ReplyContentType
    val navigationType: ReplyNavigationType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = ReplyContentType.LIST
            navigationType = ReplyNavigationType.BOTTOM_NAVBAR
        }
        WindowWidthSizeClass.Medium -> {
            contentType = ReplyContentType.LIST
            navigationType = ReplyNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            contentType = ReplyContentType.LIST_DETAIL
            navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
        }
        else -> {
            contentType = ReplyContentType.LIST
            navigationType = ReplyNavigationType.BOTTOM_NAVBAR
        }
    }

    ReplyHomeScreen(
        replyUiState = uiState,
        navType = navigationType,
        contentType = contentType,
        onTabPressed = {
            mailbox: MailboxType ->
                viewModel.updateCurrentMailbox(mailbox)
                viewModel.resetHomeScreenState()
            },
        onEmailCardPressed = { email: Email -> viewModel.updateDetailsScreenState(email)},
        onDetailScreenBackPressed = { viewModel.resetHomeScreenState() },
        modifier = modifier
    )

}