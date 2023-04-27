package ru.divarteam.atoll.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.RatingResponse
import ru.divarteam.atoll.network.response.TimelineResponse
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_rating_top_team)
abstract class EventRatingTopTeamEpoxyModel : EpoxyModelWithHolder<EventRatingTopTeamEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var ratingResponse: RatingResponse

    override fun bind(holder: Holder) {
        holder.place.setText("${ratingResponse.place}")
        // TODO: Добавить подробное отображение команды
    }

    inner class Holder : KotlinHolder() {
        val place by bind<TextView>(R.id.place)
        val teamName by bind<TextView>(R.id.team_name)
        val teamMembers by bind<TextView>(R.id.team_members)
    }
}