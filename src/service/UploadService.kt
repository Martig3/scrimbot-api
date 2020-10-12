package com.martige.service

import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import io.ktor.client.*
import io.ktor.client.request.*
import model.DathostServerInfo
import net.dv8tion.jda.api.JDA
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.MonthDay
import java.time.Year
import java.util.*
import java.util.concurrent.TimeUnit


class UploadService {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(UploadService::class.java)
        private var dropboxToken = System.getenv("dropbox_token")
        private var dropboxAppName = System.getenv("dropbox_app_name")
        private var discordTextChannelId: Long = System.getenv("discord_textchannel_id").toLong()
        private var config: DbxRequestConfig = DbxRequestConfig.newBuilder("dropbox/$dropboxAppName").build()
        val auth64String = "Basic " + Base64.getEncoder()
            .encodeToString(
                "${System.getenv("dathost_username")}:${System.getenv("dathost_password")}"
                    .toByteArray()
            )
        private lateinit var dropboxClient: DbxClientV2
        private val httpClient = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    suspend fun uploadDemo(filename: String, gameServerId: String, client: HttpClient, jda: JDA) {
        val demoFileUrl = "https://dathost.net/api/0.1/game-servers/$gameServerId/files/$filename.dem"
        val serverListUrl = "https://dathost.net/api/0.1/game-servers"
        val serverList: List<DathostServerInfo> = client.get(serverListUrl)
        val map = serverList
            .filter { it.id == gameServerId }
            .map { it.csgo_settings?.mapgroup_start_map }
            .firstOrNull() ?: "Unknown Map"
        val uploadPath =
            "/${Year.now()}-${MonthDay.now().month.value}-${MonthDay.now().dayOfMonth}_pug_${map}_$filename.dem"
        dropboxClient = DbxClientV2(config, dropboxToken)
        getGameServerFile(demoFileUrl).use { response ->
            response.body?.byteStream().use { `in` ->
                dropboxClient.files().uploadBuilder(uploadPath)
                    .uploadAndFinish(`in`)
                log.info("Uploaded $uploadPath successfully")
            }
        }
        val shareLink = dropboxClient.sharing().createSharedLinkWithSettings(uploadPath)
        val channel = jda.getTextChannelById(discordTextChannelId)
        channel?.sendMessage("New `.dem` replay files are available: \n$map - ${shareLink.url}")?.queue()
    }
    private fun getGameServerFile(demoFileUrl: String): Response {
        val getFileRequest = Request.Builder()
            .url(demoFileUrl)
            .get()
            .header("Authorization", auth64String)
            .build()
        return httpClient.newCall(getFileRequest).execute()
    }
}
