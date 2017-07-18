package data.network.tinder.recommendations

import com.squareup.moshi.Json

internal class DataRecommendationUserSpotifyThemeTrackArtist private constructor(
        @Json(name = "name")
        private val name: String,
        @Json(name = "id")
        private val id: String)