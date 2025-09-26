package com.example.replybreakdown.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.replybreakdown.ui.utils.ReplyContentType
import com.example.replybreakdown.ui.utils.ReplyNavigationType

@Composable
fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    viewModel: ReplyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val replyUiState by viewModel.uiState.collectAsState()
    val contentType: ReplyContentType
    val navigationType: ReplyNavigationType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = ReplyContentType.LIST_ONLY
            navigationType = ReplyNavigationType.BOTTOM_NAVBAR
        }
        WindowWidthSizeClass.Medium -> {
            contentType = ReplyContentType.LIST_ONLY
            navigationType = ReplyNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            contentType = ReplyContentType.LIST_DETAIL
            navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
        } else -> {
            contentType = ReplyContentType.LIST_ONLY
            navigationType = ReplyNavigationType.BOTTOM_NAVBAR
        }
    }

    ReplyHomeScreen(
        replyUiState = replyUiState,
        navigationType = navigationType,
        contentType = contentType,
        onTabChange = { mailbox ->
            viewModel.updateCurrentMailbox(mailbox)
            viewModel.resetHomeScreenStates()
        },
        onEmailClicked = { email -> viewModel.updateDetailsScreenStates(email)},
        onDetailsScreenBackPressed = { viewModel.resetHomeScreenStates() },
        modifier = modifier
    )
}