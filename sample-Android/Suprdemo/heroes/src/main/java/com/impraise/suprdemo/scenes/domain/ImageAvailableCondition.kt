package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.game.scenes.data.model.Member
import com.impraise.supr.game.scenes.domain.RoundCreationHelper

class ImageAvailableCondition : RoundCreationHelper.Condition<Member> {

    override fun satisfied(param: Member): Boolean {
        return param.avatarUrl.isNotEmpty()
                && !param.avatarUrl.contains("image_not_available")
    }
}