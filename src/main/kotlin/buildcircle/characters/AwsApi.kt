package buildcircle.characters

import dev.forkhandles.result4k.Result4k
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters.SetBaseUriFrom

interface Action<R> {
    fun toRequest(): Request
    fun toResult(response: Response): R
}

interface AwsApiAction<R> : Action<Result4k<R, AWSCharactersProviderError>>

interface AwsApi {
    operator fun <R : Any> invoke(action: AwsApiAction<R>): Result4k<R, AWSCharactersProviderError>

    companion object
}

fun AwsApi.Companion.Http(client: HttpHandler) = object : AwsApi {
    private val http = SetBaseUriFrom(Uri.of("https://s3.eu-west-2.amazonaws.com/build-circle"))
        .then(client)

    override fun <R : Any> invoke(action: AwsApiAction<R>) =
        action.toResult(http(action.toRequest()))
}

fun AwsApi.getCharacters() = invoke(GetCharacters)