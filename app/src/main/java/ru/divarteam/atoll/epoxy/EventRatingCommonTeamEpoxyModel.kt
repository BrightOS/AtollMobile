package ru.divarteam.atoll.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.RatingResponse
import ru.divarteam.atoll.network.response.TeamResponse
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_rating_common_team)
abstract class EventRatingCommonTeamModel : EpoxyModelWithHolder<EventRatingCommonTeamModel.Holder>() {

    @EpoxyAttribute
    lateinit var ratingResponse: RatingResponse

    @EpoxyAttribute
    lateinit var teamResponse: TeamResponse

    override fun bind(holder: Holder) {
        holder.place.setText("${ratingResponse.place}")
        holder.teamName.setText(teamResponse.title)
    }

    inner class Holder : KotlinHolder() {
        val place by bind<TextView>(R.id.place)
        val teamName by bind<TextView>(R.id.team_name)
    }
}