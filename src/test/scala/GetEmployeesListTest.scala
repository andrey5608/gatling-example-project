import io.gatling.core.Predef._
import io.gatling.core.body.Body
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.util.parsing.json.JSON

class GetEmployeesListTest extends Simulation {
  val jsonHeaders: String = scala.io.Source.fromFile("./src/test/resources/headers.json").mkString
  val jsonMap = JSON.parseFull(jsonHeaders).getOrElse(0).asInstanceOf[Map[String, String]]
  val innerMap = jsonMap.asInstanceOf[Map[String, String]]

  private val baseUrl = "https://server-url"
  private val contentType = "text/plain"
  private val endpoint = "/query-part"
  private val usersCount = 10

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(baseUrl)
    .inferHtmlResources()
    .acceptHeader("*/*")
    .contentTypeHeader(contentType)
    .maxConnectionsPerHostLikeChrome
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36")
    .disableCaching
  val headers_0 = Map("Authorization" -> "Bearer %s".format(innerMap("Bearer")),
    "Id-Token" -> innerMap("Id-Token"),
    "Origin" -> innerMap("Origin"),
    "Referrer" -> innerMap("Referrer"),
    "Content-Type" -> "application/json")
  val requestBody: Body = StringBody(
    "{put here json body}")
  val scn: ScenarioBuilder = scenario("RecordedSimulation")
    .repeat(120) {
      exec(http("/employee-spr/query")
        .post(endpoint)
        .body(requestBody)
        .headers(headers_0)
        .check(status.is(200)))
    }
  innerMap.keys //will give keys

  setUp(
    scn.inject(
      nothingFor(4),
      atOnceUsers(usersCount)
    ).protocols(httpProtocol))
}