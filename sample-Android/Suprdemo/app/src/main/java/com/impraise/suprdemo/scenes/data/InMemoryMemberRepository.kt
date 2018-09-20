package com.impraise.suprdemo.scenes.data

import com.impraise.supr.data.ResultList
import com.impraise.suprdemo.scenes.data.model.Member
import io.reactivex.Flowable
import java.util.*

/**
 * Created by guilhermebranco on 3/13/18.
 */
class InMemoryMemberRepository: MemberRepository {

    override fun all(): Flowable<ResultList<Member>> {
        val members = mutableListOf<Member>()
        for (index in 1..40) {
            val random = Random().nextInt(2)
            members.add(Member("$index Test teteteteteteteteteteete", images[random]))
        }
        return Flowable.just(ResultList.Success(members))
    }
}

val images = listOf(
        "https://upload.wikimedia.org/wikipedia/commons/3/3d/TonyRamosPorAndreaFarias.jpg",
        "https://www.famousbirthdays.com/faces/carrey-jim-image.jpg",
        "http://www.portaldotocantins.com/wp-content/uploads/2016/04/Faust%C3%A3o.jpg"
)