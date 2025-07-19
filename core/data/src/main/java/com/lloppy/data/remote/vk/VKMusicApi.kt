package com.lloppy.data.remote.vk

import android.util.Log
import org.jsoup.Jsoup
import java.io.IOException

class VKMusicApi {

    companion object {
        private const val BASE_URL = "https://vk.com"
        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }

    data class VKAudio(
        val id: String,
        val ownerId: String,
        val title: String,
        val artist: String,
        val duration: Int,
        val url: String,
        val coverUrl: String? = null
    )

    /**
     * Получает список аудиозаписей пользователя
     * @param userId ID пользователя VK
     * @param section Раздел (по умолчанию "all")
     * @param offset Смещение (пагинация)
     */
    suspend fun getUserAudios(
        userId: String,
        section: String = "all",
        offset: Int = 0
    ): List<VKAudio> {
        return try {
            val url = "$BASE_URL/audios$userId?section=$section&offset=$offset"
            val doc = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .get()
            Log.w("FlashcardRepository", " _____________________ doc ${doc.title()}  ")
            Log.w("FlashcardRepository", " _____________________ doc ${doc.body()}  ")

            parseAudioList(doc)
        } catch (e: IOException) {
            emptyList()
        }
    }

    private fun parseAudioList(doc: org.jsoup.nodes.Document): List<VKAudio> {
        val audioList = mutableListOf<VKAudio>()

        // Ищем все элементы с аудиозаписями
        val audioElements = doc.select("div.audio_row")

        audioElements.forEach { element ->
            try {
                // Получаем данные из атрибута data-audio
                val audioData = element.attr("data-audio")
                if (audioData.isNotEmpty()) {
                    val jsonData = parseAudioData(audioData)

                    val fullId = element.attr("data-full-id") ?: ""
                    val (ownerId, id) = fullId.split("_").takeIf { it.size == 2 } ?: listOf("", "")

                    // Альтернативный способ получения данных, если data-audio не доступен
                    val titleElement = element.selectFirst(".audio_row__title_inner")
                    val title = titleElement?.text()?.trim() ?: jsonData.title

                    val artistElement = element.selectFirst(".audio_row__performers")
                    val artist = artistElement?.text()?.trim() ?: jsonData.artist

                    val durationText = element.selectFirst(".audio_row__duration")?.text() ?: "0:00"
                    val duration = jsonData.duration ?: parseDuration(durationText)

                    val audioUrl = titleElement?.attr("href")?.let {
                        if (it.startsWith("/")) "$BASE_URL$it" else it
                    } ?: ""

                    audioList.add(
                        VKAudio(
                            id = id,
                            ownerId = ownerId,
                            title = title,
                            artist = artist,
                            duration = duration,
                            url = audioUrl
                        )
                    )
                } else {
                    // Fallback parsing if data-audio is not available
                    val fullId = element.attr("data-full-id") ?: ""
                    val (ownerId, id) = fullId.split("_").takeIf { it.size == 2 } ?: listOf("", "")

                    val titleElement = element.selectFirst(".audio_row__title_inner")
                    val title = titleElement?.text()?.trim() ?: ""

                    val artistElement = element.selectFirst(".audio_row__performers")
                    val artist = artistElement?.text()?.trim() ?: ""

                    val durationText = element.selectFirst(".audio_row__duration")?.text() ?: "0:00"
                    val duration = parseDuration(durationText)

                    val audioUrl = titleElement?.attr("href")?.let {
                        if (it.startsWith("/")) "$BASE_URL$it" else it
                    } ?: ""

                    audioList.add(
                        VKAudio(
                            id = id,
                            ownerId = ownerId,
                            title = title,
                            artist = artist,
                            duration = duration,
                            url = audioUrl
                        )
                    )
                }
            } catch (e: Exception) {
                // Пропускаем некорректные записи
                e.printStackTrace()
            }
        }

        return audioList
    }

    private fun parseDuration(durationStr: String): Int {
        return try {
            val parts = durationStr.split(":")
            when (parts.size) {
                2 -> parts[0].toInt() * 60 + parts[1].toInt()
                3 -> parts[0].toInt() * 3600 + parts[1].toInt() * 60 + parts[2].toInt()
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }

    private data class AudioJsonData(
        val title: String,
        val artist: String,
        val duration: Int?
    )

    private fun parseAudioData(audioData: String): AudioJsonData {
        return try {
            // This is a simplified parser for the JSON-like data in data-audio attribute
            // The actual format is complex and might need more sophisticated parsing
            val parts = audioData.split(',')
            if (parts.size >= 5) {
                val title = parts[3].removeSurrounding("\"").replace("&amp;", "&").replace("&#39;", "'")
                val artist = parts[4].removeSurrounding("\"").replace("&amp;", "&").replace("&#39;", "'")
                val duration = parts.getOrNull(5)?.toIntOrNull()
                AudioJsonData(title, artist, duration)
            } else {
                AudioJsonData("", "", null)
            }
        } catch (e: Exception) {
            AudioJsonData("", "", null)
        }
    }
}