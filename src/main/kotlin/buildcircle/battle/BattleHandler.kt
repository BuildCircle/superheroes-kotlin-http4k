package buildcircle.battle

import buildcircle.characters.CharacterResponse
import buildcircle.characters.CharactersProvider
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.fold
import dev.forkhandles.result4k.mapFailure
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.http4k.lens.Query

fun battleHandler(getCharacters: CharactersProvider): HttpHandler {
    val heroQuery = Query.required("hero")
    val villainQuery = Query.required("villain")

    return { request ->
        val heroName = heroQuery(request)
        val villainName = villainQuery(request)
        getCharacters()
            .mapFailure { Response(INTERNAL_SERVER_ERROR).body(it.message) }
            .flatMap { (characters) ->
                val hero = characters.find { it.name == heroName }
                val villain = characters.find { it.name == villainName }

                when {
                    hero == null -> Failure(Response(NOT_FOUND).body("Hero '$heroName' not found"))
                    villain == null -> Failure(Response(NOT_FOUND).body("Villain '$villainName' not found"))
                    else -> {
                        val winner = if (hero.score > villain.score) hero else villain
                        Success(winner)
                    }
                }
            }
            .fold(
                { Response(OK).body(Jackson.asFormatString(it))},
                { it })
    }
}