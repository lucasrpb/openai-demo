package demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.ChatModel
import com.openai.models.responses.{ResponseCreateParams, ResponsePrompt}

import java.nio.file.{Files, Paths}
import scala.io.StdIn
import scala.jdk.CollectionConverters._

object Main {

  val client: OpenAIClient = OpenAIOkHttpClient.builder()
    .apiKey(Files.readString(Paths.get("openai.key")))
    .build()

  val mapper = new ObjectMapper()

  def main(args: Array[String]): Unit = {

    println("prompt: ")
    val msg = StdIn.readLine()

    val params = ResponseCreateParams.builder()
      .model(ChatModel.GPT_5)
      .prompt(
        ResponsePrompt.builder()
          .id("pmpt_6897a9bc9f9c8197882166c1308b7b79096dee35fe1defab")
          .build()
      )
      .input(msg)
      .build()

    // Call API
    val responses = client.responses().create(params).output().asScala
      .filter(_.message().isPresent)
      .flatMap(_.asMessage().content().asScala.filter(_._json().isPresent))
      .filter(_._json().get().asObject().isPresent)
      .map(_._json().get().asObject().get().get("text")).toSeq


    val json = mapper.readTree(responses.head.toString)

    SendEmail.send(json)

    println()
  }

}
