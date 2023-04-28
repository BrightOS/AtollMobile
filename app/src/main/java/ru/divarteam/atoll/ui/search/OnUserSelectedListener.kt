package ru.divarteam.atoll.ui.search

import ru.divarteam.atoll.network.response.UserResponse

interface OnUserSelectedListener {
    fun onUserSelect(userResponse: UserResponse)
}