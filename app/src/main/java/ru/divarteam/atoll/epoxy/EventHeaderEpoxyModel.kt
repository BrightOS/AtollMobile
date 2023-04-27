package ru.divarteam.atoll.epoxy

import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divarteam.atoll.R
import ru.divarteam.atoll.ui.event.OnParticipateClickListener
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_event_header)
abstract class EventHeaderEpoxyModel : EpoxyModelWithHolder<EventHeaderEpoxyModel.Holder>() {

    @EpoxyAttribute
    var title = ""

    @EpoxyAttribute
    var description = ""

    @EpoxyAttribute
    lateinit var onParticipateClickListener: OnParticipateClickListener

    override fun bind(holder: Holder) {
        holder.title.setText(title)
        holder.description.setText(description)

        holder.participate.setOnClickListener {
            onParticipateClickListener.onParticipateClick()
        }
    }

    inner class Holder : KotlinHolder() {
        val title by bind<TextView>(R.id.event_title)
        val description by bind<TextView>(R.id.description)
        val participate by bind<Button>(R.id.participate)
    }
}