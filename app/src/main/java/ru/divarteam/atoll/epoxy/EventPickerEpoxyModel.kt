package ru.divarteam.atoll.epoxy

import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.data.model.EventPickerModel
import ru.divarteam.atoll.ui.events.OnEventClickListener
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_event_picker)
abstract class EventPickerEpoxyModel : EpoxyModelWithHolder<EventPickerEpoxyModel.Holder>() {

    @EpoxyAttribute
    var dateTime = ""

    @EpoxyAttribute
    var title = ""

    @EpoxyAttribute
    var subtitle = ""

    @EpoxyAttribute
    var eventId = 0

    @EpoxyAttribute
    lateinit var onEventClickListener: OnEventClickListener

    override fun bind(holder: Holder) {
        holder.dateTime.text = dateTime
        holder.title.text = title
        holder.subtitle.text = subtitle

        holder.more.setOnClickListener {
            onEventClickListener.onEventClick(eventId)
        }
    }

    inner class Holder : KotlinHolder() {
        val dateTime by bind<TextView>(R.id.event_datetime)
        val title by bind<TextView>(R.id.event_title)
        val subtitle by bind<TextView>(R.id.event_subtitle)
        val more by bind<Button>(R.id.event_more)
    }
}