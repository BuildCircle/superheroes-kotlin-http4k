package buildcircle.characters

import org.http4k.core.Request
import org.http4k.core.Response
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.core.Body
import org.http4k.format.Jackson.auto
import org.http4k.core.Method.GET
import org.http4k.core.Status.Companion.OK

object GetCharacters : AwsApiAction<CharactersResponse> {
    val charactersLens = Body.auto<CharactersResponse>().toLens()

    override fun toRequest() = Request(GET, "/characters.json")

    override fun toResult(response: Response) = when (response.status) {
        OK -> Success(charactersLens(response))
        else -> Failure(AWSCharactersProviderError(response.status, response.bodyString()))
    }
}

