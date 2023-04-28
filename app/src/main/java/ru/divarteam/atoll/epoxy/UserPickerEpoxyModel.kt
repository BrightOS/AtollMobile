package ru.divarteam.atoll.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divarteam.atoll.R
import ru.divarteam.atoll.network.response.UserResponse
import ru.divarteam.atoll.ui.search.OnUserSelectedListener
import ru.divarteam.atoll.utils.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_user_picker)
abstract class UserPickerEpoxyModel : EpoxyModelWithHolder<UserPickerEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var userResponse: UserResponse

    @EpoxyAttribute
    lateinit var onUserSelectedListener: OnUserSelectedListener

    override fun bind(holder: Holder) {
        holder.userName.setText(userResponse.fullname ?: userResponse.mail)
        holder.about.visibility =
            if (userResponse.description == null)
                View.GONE
            else
                View.VISIBLE
        holder.about.setText(userResponse.description)

        holder.root.setOnClickListener {
            onUserSelectedListener.onUserSelect(userResponse)
        }
    }

    inner class Holder : KotlinHolder() {
        val root by bind<MaterialCardView>(R.id.root)
        val userName by bind<TextView>(R.id.user_name)
        val about by bind<TextView>(R.id.user_about)
    }
}