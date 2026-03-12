package buildcircle

import buildcircle.battle.battleHandler
import buildcircle.characters.AwsApi
import buildcircle.characters.Http
import buildcircle.characters.getCharacters
import org.http4k.client.OkHttp
import org.http4k.client.OkHttp.invoke
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun appRouter(aws: AwsApi): HttpHandler = routes(
    "/battle" bind GET to battleHandler(aws::getCharacters)
)

fun main() {
    val aws: AwsApi = AwsApi.Http(OkHttp())

    val printingApp: HttpHandler = PrintRequest().then(appRouter(aws))

    val server = printingApp.asServer(Jetty(9000)).start()

    println("Server started on " + server.port())
}
