package com.example.replybreakdown.ui

import androidx.lifecycle.ViewModel
import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.data.local.LocalEmailsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReplyViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ReplyUiState())
    val uiState: StateFlow<ReplyUiState> = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        val mailboxes: Map<MailboxType, List<Email>> =
            LocalEmailsDataProvider.allEmails.groupBy { it.mailbox }
        _uiState.value = ReplyUiState(
            mailboxes = mailboxes,
            currentSelectedEmail = mailboxes[MailboxType.Inbox]?.get(0)
                ?: LocalEmailsDataProvider.defaultEmail
        )
    }

    fun updateDetailsScreenState(email: Email) {
        _uiState.update { it.copy(
            currentSelectedEmail = email,
            isShowingHomePage = false
        ) }
    }

    fun resetHomeScreenState() {
        _uiState.update {
            it.copy(
                isShowingHomePage = true,
                currentSelectedEmail = it.mailboxes[it.currentMailbox]?.get(0)
                    ?: LocalEmailsDataProvider.defaultEmail
            )
        }
    }

    fun updateCurrentMailbox(mailbox: MailboxType) {
        _uiState.update { it.copy(
            currentMailbox = mailbox
        ) }
    }
}