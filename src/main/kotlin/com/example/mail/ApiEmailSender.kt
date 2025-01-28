package com.example.mail

import com.example.user.model.User
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendConfirmationMail(user: User, token: String) {
    val confirmationLink = "localhost:8080/auth/confirm?token=${token}" //path hardcoded
    val host = "live.smtp.mailtrap.io"
    val port = "587"
    val username = "api"
    val password = "97133bdfe690d10c59631fdd4b223e2e"
    val fromEmail = "hello@demomailtrap.com"
    val body = "Click the link below to confirm your Qwixx account registration: $confirmationLink"

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", host)
        put("mail.smtp.port", port)
        put("mail.smtp.ssl.trust", host)
    }

    val auth = object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    }

    val session = Session.getInstance(props, auth)

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(fromEmail))
            setRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(user.email))
            subject = "Confirm your registration at Qwixx"
            setText(body)
        }
        Transport.send(message)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun sendPasswordResetMail(user: User, resetToken: String) {
    val confirmationLink = "localhost:8080/auth/confirm-password-reset?token=${resetToken}" //path hardcoded
    val host = "live.smtp.mailtrap.io"
    val port = "587"
    val username = "api"
    val password = "97133bdfe690d10c59631fdd4b223e2e"
    val fromEmail = "hello@demomailtrap.com"
    val body = "Click the link below to reset password: $confirmationLink"

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", host)
        put("mail.smtp.port", port)
        put("mail.smtp.ssl.trust", host)
    }

    val auth = object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    }

    val session = Session.getInstance(props, auth)

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(fromEmail))
            setRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(user.email))
            subject = "Confirm password reset at Qwixx"
            setText(body)
        }
        Transport.send(message)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

