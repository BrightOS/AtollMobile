package ru.divarteam.atoll.epoxy

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.TeamResponse
import ru.divarteam.atoll.ui.event.OnInviteClickListener
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_event_my_team)
abstract class EventMyTeamModel : EpoxyModelWithHolder<EventMyTeamModel.Holder>() {

    @EpoxyAttribute
    lateinit var teamResponse: TeamResponse

    @EpoxyAttribute
    var eventId: Int = 0

    @EpoxyAttribute
    var canInvite: Boolean = false

    @EpoxyAttribute
    lateinit var onInviteClickListener: OnInviteClickListener

    override fun bind(holder: Holder) {
        holder.teamName.setText(teamResponse.title)
        holder.invite.visibility = if (canInvite) View.VISIBLE else View.GONE
        holder.invite.setOnClickListener {
            onInviteClickListener.onInviteClick(eventId, teamResponse.teamId ?: 0)
        }
        holder.teamMembers.setText(teamResponse.users?.map { it.fullname }?.joinToString(", "))
    }

    inner class Holder : KotlinHolder() {
        val teamName by bind<TextView>(R.id.team_name)
        val divider by bind<View>(R.id.divider)
        val invite by bind<Button>(R.id.invite)
        val teamMembers by bind<TextView>(R.id.team_members)
    }
}