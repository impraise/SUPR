package com.impraise.suprdemo.scenes.helper

import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.domain.RoundCreationHelper
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.any
import org.mockito.BDDMockito.*

object GameTestHelper {

    fun members(): List<List<Member>> {
        val members = mutableListOf<List<Member>>()

        (1..5).forEach {
            val current = (1..4).map { Member(it.toString(), it.toString()) }
            members.add(current)
        }
        return members
    }

    fun alwaysTrueCondition(): RoundCreationHelper.Condition<Member> {
        return mock<RoundCreationHelper.Condition<Member>>().apply {
            given(this.satisfied(any())).willReturn(true)
        }
    }
}