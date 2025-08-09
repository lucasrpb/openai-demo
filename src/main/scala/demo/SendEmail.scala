package demo

import com.fasterxml.jackson.databind.JsonNode

import java.nio.file.{Files, Paths}
import java.util.Properties
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeMessage}

object SendEmail {

  val username = "lucasrpb.sec@gmail.com";
  val password = Files.readString(Paths.get("gmail.pwd")); // from Google App Passwords

  val props = new Properties();
  props.put("mail.smtp.auth", "true")
  props.put("mail.smtp.starttls.enable", "true")
  props.put("mail.smtp.host", "smtp.gmail.com")
  props.put("mail.smtp.port", "587")

  val session = Session.getInstance(props, new Authenticator {
    override def getPasswordAuthentication: PasswordAuthentication =
      new PasswordAuthentication(username, password)
  })

  def send(json: JsonNode): Unit = {
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(username))

      message.setRecipient(
        Message.RecipientType.TO,
        InternetAddress.parse(json.get("email").asText()).head
      )
      message.setSubject(json.get("subject").asText())
      message.setText(json.get("body").asText())

      Transport.send(message)
      println("Email sent successfully!")
    } catch {
      case ex => println(s"error on sending email: ${ex}")
    }
  }

}
