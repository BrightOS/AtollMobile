package ru.divarteam.atoll.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.card.MaterialCardView
import ru.divarteam.atoll.R

@EpoxyModelClass(layout = R.layout.item_empty)
abstract class NoInvitesEpoxyModel : EpoxyModel<MaterialCardView>()