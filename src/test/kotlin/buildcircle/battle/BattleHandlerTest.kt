package buildcircle.battle

import buildcircle.characters.CharacterResponse
import buildcircle.characters.CharactersResponse
import dev.forkhandles.result4k.Success
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BattleHandlerTest {

    @Test
    fun `returns the winner when hero has a higher score than villain`() {
        val hero = CharacterResponse(name = "Batman", score = 9.0, type = "hero")
        val villain = CharacterResponse(name = "Joker", score = 7.0, type = "villain")
        val provider = { Success(CharactersResponse(listOf(hero, villain))) }

        val handler = battleHandler(provider)
        val response = handler(Request(GET, "/battle?hero=Batman&villain=Joker"))

        assertEquals(OK, response.status)
        assert(response.bodyString().contains("Batman"))
    }
}