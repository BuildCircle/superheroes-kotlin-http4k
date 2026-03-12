package buildcircle.characters

import dev.forkhandles.result4k.Result4k
import org.http4k.core.Status

typealias CharactersProvider = () -> Result4k<CharactersResponse, AWSCharactersProviderError>

data class AWSCharactersProviderError(val status: Status, val message: String)