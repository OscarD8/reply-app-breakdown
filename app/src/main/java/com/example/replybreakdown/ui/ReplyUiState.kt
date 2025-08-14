package com.example.replybreakdown.ui

import com.example.replybreakdown.data.Email
import com.example.replybreakdown.data.MailboxType
import com.example.replybreakdown.data.local.LocalEmailsDataProvider

data class ReplyUiState (
    val mailboxes: Map<MailboxType, List<Email>> = emptyMap(),
    val currentMailbox: MailboxType = MailboxType.Inbox,
    val currentSelectedEmail: Email = LocalEmailsDataProvider.defaultEmail,
    val isShowingHomePage: Boolean = true
) {
    val currentMailboxEmails: List<Email> by lazy { mailboxes[currentMailbox]!! }
}