package com.impraise.suprdemo.scenes.domain

import com.impraise.supr.data.Result
import com.impraise.supr.domain.DomainLayerException
import com.impraise.supr.domain.ReactiveUseCase
import com.impraise.suprdemo.scenes.data.model.Member
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.domain.model.Round
import io.reactivex.Single
import java.util.*

/**
 * Created by guilhermebranco on 3/11/18.
 */
class CreateRoundUseCase(private val numberOfOptions: Int = 4): ReactiveUseCase<List<Member>, Result<Round>> {

    override fun get(param: List<Member>): Single<Result<Round>> {
        return if (param.size < numberOfOptions) Single.just(Result.Error(DomainLayerException(), null))
        else Single.just(Result.Success(createRound(param)))
    }

    private fun createRound(members: List<Member>): Round {
        val correct = Random().nextInt(0..numberOfOptions)
        val options = mutableListOf<Option>()
        for (index in 0 until numberOfOptions) {
            val member = members[index]
            if (index == correct) options.add(Option.Correct(member.name))
            else options.add(Option.Undefined(member.name))
        }
        return Round(members[correct].avatarUrl, options)
    }
}

fun Random.nextInt(range: IntRange): Int {
    return range.start + nextInt(range.last - range.start)
}

open class RoundCreationHelper {

    fun returnCorrectOptionOrFirstWithAvatar(members: List<Member>, numberOfOptions: Int): Int {
        var correct = randomIndex(numberOfOptions)
        return if (members[correct].avatarUrl.isEmpty()) members.indexOfFirst {
            it.avatarUrl.isNotEmpty()
        }
        else correct
    }

    open fun randomIndex(total: Int): Int {
        return Random().nextInt(0..total)
    }
}