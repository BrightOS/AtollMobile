package ru.divarteam.atoll.epoxy

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.RatingResponse
import ru.divarteam.atoll.network.response.TeamResponse
import ru.divarteam.atoll.network.response.TimelineResponse
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_rating_top_team)
abstract class EventRatingTopTeamEpoxyModel :
    EpoxyModelWithHolder<EventRatingTopTeamEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var ratingResponse: RatingResponse

    @EpoxyAttribute
    lateinit var teamResponse: TeamResponse

    override fun bind(holder: Holder) {
        holder.ratingTitle.visibility = if (ratingResponse.place < 2) View.VISIBLE else View.GONE
        holder.divider.visibility = if (ratingResponse.place < 2) View.VISIBLE else View.GONE
        holder.place.setText("${ratingResponse.place}")
        holder.place.backgroundTintList = ColorStateList.valueOf(
            when (ratingResponse.place) {
                0, 1 -> ContextCompat.getColor(holder.place.context, R.color.yellow_700)
                2 -> ContextCompat.getColor(holder.place.context, R.color.grey_400)
                else -> ContextCompat.getColor(holder.place.context, R.color.brown_800)
            }
        )
        holder.teamName.setText(teamResponse.title)
        holder.teamMembers.setText(teamResponse.users?.map { it.fullname }?.joinToString("\n"))
    }

    inner class Holder : KotlinHolder() {
        val ratingTitle by bind<TextView>(R.id.rating_title)
        val place by bind<TextView>(R.id.place)
        val divider by bind<View>(R.id.divider)
        val teamName by bind<TextView>(R.id.team_name)
        val teamMembers by bind<TextView>(R.id.team_members)
    }
}