package ru.divarteam.atoll.ui.invites

interface OnInvitationClickListener {
    fun onAcceptClicked(teamId: Int)
    fun onDenyClicked(teamId: Int)
}