package com.lloppy.data.mapper

import com.lloppy.data.local.entity.FlashcardEntity
import com.lloppy.model.Flashcard

import java.util.Date

class FlashcardMapper {

    fun toDomain(entity: FlashcardEntity): Flashcard = Flashcard(
        id = entity.id,
        question = entity.question.ifEmpty { "No question available" },
        answer = entity.answer.ifEmpty { "No answer available" },
        lastReviewed = Date(entity.lastReviewed),
        nextReviewDue = Date(entity.nextReviewDue),
        category = entity.category,
        shouldShowAgain = entity.shouldShowAgain,
//            songTitle = parseSongTitle(entity.songId),
//            artist = parseArtist(entity.songId)
    )

    fun toEntity(domain: Flashcard): FlashcardEntity = FlashcardEntity(
        id = domain.id,
        question = domain.question,
        answer = domain.answer,
        lastReviewed = domain.lastReviewed.time,
        nextReviewDue = domain.nextReviewDue.time,
        category = domain.category,
        shouldShowAgain = domain.shouldShowAgain,
        //   songId = "${domain.artist}_${domain.songTitle}"
    )

    private fun parseSongTitle(songId: String?): String? {
        return songId?.split("_")?.getOrNull(1)
    }

    private fun parseArtist(songId: String?): String? {
        return songId?.split("_")?.getOrNull(0)
    }
}