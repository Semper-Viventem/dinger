package data.tinder.recommendation

import data.database.CollectibleDaoDelegate
import domain.recommendation.DomainRecommendationInstagramPhoto

internal class RecommendationInstagramPhotoDaoDelegate(
        private val instagramDao: RecommendationUserInstagramPhotoDao,
        private val userInstagramDao: RecommendationUserInstagram_InstagramPhotoDao)
    : CollectibleDaoDelegate<String, DomainRecommendationInstagramPhoto>() {
    override fun selectByPrimaryKey(primaryKey: String) =
            instagramDao.selectInstagramPhotoByLink(primaryKey).firstOrNull()?.let {
                return@let DomainRecommendationInstagramPhoto(
                        link = it.link,
                        imageUrl = it.imageUrl,
                        thumbnailUrl = it.thumbnailUrl,
                        ts = it.ts)
            } ?: DomainRecommendationInstagramPhoto.NONE

    override fun insertResolved(source: DomainRecommendationInstagramPhoto) =
            instagramDao.insertInstagramPhoto(RecommendationUserInstagramPhotoEntity(
                    link = source.link,
                    imageUrl = source.imageUrl,
                    thumbnailUrl = source.thumbnailUrl,
                    ts = source.ts))

    fun insertResolvedForInstagramUsername(
            instagramUsername: String,
            instagramPhotos: Iterable<DomainRecommendationInstagramPhoto>) =
            instagramPhotos.forEach {
                insertResolved(it)
                userInstagramDao.insertInstagram_Photo(
                        RecommendationUserInstagramEntity_RecommendationUserInstagramPhotoEntity(
                                recommendationUserInstagramEntityUsername = instagramUsername,
                                recommendationUserInstagramPhotoEntityLink = it.link))
            }
}
