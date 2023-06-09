package ru.divarteam.atoll.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.utils.KotlinHolder
import ru.divarteam.atoll.utils.fromDefaultFormatToRuFormatString

@EpoxyModelClass(layout = R.layout.item_timeline)
abstract class EventTimelineEpoxyModel : EpoxyModelWithHolder<EventTimelineEpoxyModel.Holder>() {

    @EpoxyAttribute
    var state: TimelineItemState = TimelineItemState.COMMON

    @EpoxyAttribute
    var time = ""

    @EpoxyAttribute
    var text = ""

    override fun bind(holder: Holder) {
        holder.text.setText(text)
        holder.time.setText(time.fromDefaultFormatToRuFormatString())

        when (state) {
            TimelineItemState.FIRST -> {
                holder.title.visibility = View.VISIBLE
                holder.divider.visibility = View.VISIBLE
                holder.topLine.visibility = View.GONE
                holder.bottomLine.visibility = View.VISIBLE
            }

            TimelineItemState.COMMON -> {
                holder.title.visibility = View.GONE
                holder.divider.visibility = View.GONE
                holder.topLine.visibility = View.VISIBLE
                holder.bottomLine.visibility = View.VISIBLE
            }

            TimelineItemState.LAST -> {
                holder.title.visibility = View.GONE
                holder.divider.visibility = View.GONE
                holder.topLine.visibility = View.VISIBLE
                holder.bottomLine.visibility = View.GONE
            }
        }
    }

    inner class Holder : KotlinHolder() {
        val topLine by bind<View>(R.id.top_line)
        val bottomLine by bind<View>(R.id.bottom_line)
        val divider by bind<View>(R.id.divider)
        val title by bind<View>(R.id.timeline_title)
        val text by bind<TextView>(R.id.timeline_text)
        val time by bind<TextView>(R.id.timeline_time)
    }

    enum class TimelineItemState {
        FIRST, COMMON, LAST
    }
}