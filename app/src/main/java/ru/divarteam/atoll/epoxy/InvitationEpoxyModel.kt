package ru.divarteam.atoll.epoxy

import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.TeamResponse
import ru.divarteam.atoll.ui.invites.OnInvitationClickListener
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_invite)
abstract class InvitationEpoxyModel : EpoxyModelWithHolder<InvitationEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var teamResponse: TeamResponse

    @EpoxyAttribute
    lateinit var onInvitationClickListener: OnInvitationClickListener

    override fun bind(holder: Holder) {

        holder.teamName.setText(teamResponse.title)

        holder.accept.setOnClickListener {
            onInvitationClickListener.onAcceptClicked(teamResponse.teamId!!)
        }

        holder.deny.setOnClickListener {
            onInvitationClickListener.onDenyClicked(teamResponse.teamId!!)
        }
    }

    inner class Holder : KotlinHolder() {
        val teamName by bind<TextView>(R.id.team_name)
        val accept by bind<Button>(R.id.accept)
        val deny by bind<Button>(R.id.deny)
    }
}